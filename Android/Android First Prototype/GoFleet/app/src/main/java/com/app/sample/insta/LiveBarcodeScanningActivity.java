package com.app.sample.insta;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.app.sample.insta.barcodedetection.BarcodeProcessor;
import com.app.sample.insta.barcodedetection.BarcodeResultFragment;
import com.app.sample.insta.camera.CameraSource;
import com.app.sample.insta.camera.CameraSourcePreview;
import com.app.sample.insta.camera.GraphicOverlay;
import com.app.sample.insta.camera.WorkflowModel;
import com.app.sample.insta.model.TicketFirestoreModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.common.base.Objects;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Demonstrates the barcode scanning workflow using camera preview. */
public class LiveBarcodeScanningActivity extends AppCompatActivity implements OnClickListener {

  private static final String TAG = "LiveBarcodeActivity";

  private CameraSource cameraSource;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private View settingsButton;
  private View flashButton;
  private Chip promptChip;
  private AnimatorSet promptChipAnimator;
  private WorkflowModel workflowModel;
  private WorkflowModel.WorkflowState currentWorkflowState;

  private FirebaseFirestore database;
  private DocumentReference documentReference;
  private FirebaseAuth firebaseAuth;
  private String Draw_date;
  private String rawValue;
  private Date drawDate;
  private Date Purchase_Date;
  private String[] str;
  private int Est_Winnings;
  private String Ticket_Type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_barcode);

    if (!Utils.allPermissionsGranted(this)) {
      Utils.requestRuntimePermissions(this);
    }

    preview = findViewById(R.id.camera_preview);
    graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    graphicOverlay.setOnClickListener(this);
    cameraSource = new CameraSource(graphicOverlay);

    promptChip = findViewById(R.id.bottom_prompt_chip);
    promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
    promptChipAnimator.setTarget(promptChip);

    findViewById(R.id.close_button).setOnClickListener(this);
    flashButton = findViewById(R.id.flash_button);
    flashButton.setOnClickListener(this);
    settingsButton = findViewById(R.id.settings_button);
    settingsButton.setOnClickListener(this);

    setUpWorkflowModel();

  }

  @Override
  protected void onResume() {
    super.onResume();

    workflowModel.markCameraFrozen();
    settingsButton.setEnabled(true);
    currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
    cameraSource.setFrameProcessor(new BarcodeProcessor(graphicOverlay, workflowModel));
    workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    BarcodeResultFragment.dismiss(getSupportFragmentManager());
  }

  @Override
  protected void onPause() {
    super.onPause();
    currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
    stopCameraPreview();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
      cameraSource = null;
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.close_button) {
      onBackPressed();

    } else if (id == R.id.flash_button) {
      if (flashButton.isSelected()) {
        flashButton.setSelected(false);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      } else {
        flashButton.setSelected(true);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      }

    } else if (id == R.id.settings_button) {
      // Sets as disabled to prevent the user from clicking on it too fast.
      settingsButton.setEnabled(false);
    }
  }

  private void startCameraPreview() {
    if (!workflowModel.isCameraLive() && cameraSource != null) {
      try {
        workflowModel.markCameraLive();
        preview.start(cameraSource);
      } catch (IOException e) {
        Log.e(TAG, "Failed to start camera preview!", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  private void stopCameraPreview() {
    if (workflowModel.isCameraLive()) {
      workflowModel.markCameraFrozen();
      flashButton.setSelected(false);
      preview.stop();
    }
  }

  private void setUpWorkflowModel() {
    workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

    // Observes the workflow state changes, if happens, update the overlay view indicators and
    // camera preview state.
    workflowModel.workflowState.observe(
        this,
        workflowState -> {
          if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
            return;
          }

          currentWorkflowState = workflowState;
          Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

          boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

          switch (workflowState) {
            case DETECTING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_point_at_a_barcode);
              startCameraPreview();
              break;
            case CONFIRMING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_move_camera_closer);
              startCameraPreview();
              break;
            case SEARCHING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_searching);
              stopCameraPreview();
              break;
            case DETECTED:
            case SEARCHED:
              promptChip.setVisibility(View.GONE);
              stopCameraPreview();
              break;
            default:
              promptChip.setVisibility(View.GONE);
              break;
          }

          boolean shouldPlayPromptChipEnteringAnimation =
              wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
          if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
            promptChipAnimator.start();
          }
        });
    
    workflowModel.detectedBarcode.observe(this, barcode -> {if (barcode != null) {
            /*ArrayList<BarcodeField> barcodeFieldList = new ArrayList<>();
            barcodeFieldList.add(new BarcodeField("Raw Value", barcode.getRawValue()));
            BarcodeResultFragment.show(getSupportFragmentManager(), barcodeFieldList);*/

            rawValue = barcode.getRawValue()
;
      /*TODO:add settings for after barcode been
         processed*/
      database = FirebaseFirestore.getInstance();

      if (rawValue != null)
      {
        documentReference = database.collection("tickets").document(rawValue);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            if (documentSnapshot.exists()) {
              // TODO: data conversions
              TicketFirestoreModel ticketFirestoreModel = documentSnapshot.toObject(TicketFirestoreModel.class);


              DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
              Draw_date = df.format(ticketFirestoreModel.getDraw_Date());
              drawDate = ticketFirestoreModel.getDraw_Date();
              Purchase_Date = ticketFirestoreModel.getPurchase_Date();
              Est_Winnings = ticketFirestoreModel.getEstWinnings();
              Ticket_Type = ticketFirestoreModel.getTicket_Type();

              String Draw_date = df.format(ticketFirestoreModel.getDraw_Date());
              String purchase_Date = df.format(ticketFirestoreModel.getPurchase_Date());

              // declaration and initialise String Array
              ArrayList<String> arrayList = ticketFirestoreModel.getBoards();
              str = new String[arrayList.size()];
              // ArrayList to Array Conversion
              for (int j = 0; j < arrayList.size(); j++) {

                // Assign each value to String array
                str[j] = arrayList.get(j);
                System.out.println(Arrays.toString(str));
              }

              setDocuments(rawValue, str, drawDate, Est_Winnings, Purchase_Date, Ticket_Type);

            /*                            String tType= ticketFirestoreModel.getTicket_Type();
                                        String ticketID = documentSnapshot.getId();

                                        ltPass = new LottoTicket(ticketID,tType,purchase_Date,Draw_date,str);*/

            }
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to retrieve document please, try again!",Toast.LENGTH_LONG).show();
            System.out.println("Failed to retrieve from firebase");
          }
        });
      }

          }
        });
  }

  public void setDocuments(String serialNo, String[] boards, Date drawDate, int Est_Winnings, Date purchaseDate, String ticketType)
  {
    String uID = getIntent().getStringExtra("uID");

    if (serialNo != null)
    {
      Map<String, Object> docData = new HashMap<>();
      docData.put("Boards", Arrays.asList(boards));
      docData.put("Draw_Date", new Timestamp(drawDate));
      docData.put("Est_Winnings", Est_Winnings);
      docData.put("Purchase_Date", new Timestamp(purchaseDate));
      docData.put("Ticket_Type", ticketType);
      database.collection("Users").document(uID).collection("tickets").document(serialNo).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          String saved = "New Ticket added!";
          showToast(saved);

          Intent intent = new Intent(LiveBarcodeScanningActivity.this, ActivityMain.class);
          startActivity(intent);

/*                    Intent intent = new Intent(ActivityCamera.this, ActivityTicket.class);
                    intent.putExtra("text", rawValue);
                    startActivity(intent);*/

        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          //TODO: exception
        }
      });
    }

  }
  private void showToast(String message)
  {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }
}
