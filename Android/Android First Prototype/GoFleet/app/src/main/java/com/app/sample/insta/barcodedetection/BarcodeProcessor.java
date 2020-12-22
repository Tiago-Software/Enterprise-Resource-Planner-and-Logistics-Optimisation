package com.app.sample.insta.barcodedetection;

import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.MainThread;

import com.app.sample.insta.camera.CameraReticleAnimator;
import com.app.sample.insta.camera.FrameProcessorBase;
import com.app.sample.insta.camera.GraphicOverlay;
import com.app.sample.insta.camera.WorkflowModel;
import com.app.sample.insta.settings.PreferenceUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.List;

/** A processor to run the barcode detector. */
public class BarcodeProcessor extends FrameProcessorBase<List<FirebaseVisionBarcode>>
{

  private static final String TAG = "BarcodeProcessor";

  FirebaseVisionBarcodeDetectorOptions options =
          new FirebaseVisionBarcodeDetectorOptions.Builder()
                  .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ITF).build();

  private final FirebaseVisionBarcodeDetector detector =
      FirebaseVision.getInstance().getVisionBarcodeDetector(options);
  private final WorkflowModel workflowModel;
  private final CameraReticleAnimator cameraReticleAnimator;

  public BarcodeProcessor(GraphicOverlay graphicOverlay, WorkflowModel workflowModel) {
    this.workflowModel = workflowModel;
    this.cameraReticleAnimator = new CameraReticleAnimator(graphicOverlay);
  }

  @Override
  protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
    return detector.detectInImage(image);
  }

  @MainThread
  @Override
  protected void onSuccess(FirebaseVisionImage image, List<FirebaseVisionBarcode> results, GraphicOverlay graphicOverlay)
  {
    if (!workflowModel.isCameraLive())
    {
      return;
    }

    Log.d(TAG, "Barcode result size: " + results.size());

    // Picks the barcode, if exists, that covers the center of graphic overlay.
    FirebaseVisionBarcode barcodeInCenter = null;
    for (FirebaseVisionBarcode barcode : results) {
      RectF box = graphicOverlay.translateRect(barcode.getBoundingBox());
      if (box.contains(graphicOverlay.getWidth() / 2f, graphicOverlay.getHeight() / 2f)) {
        barcodeInCenter = barcode;
        break;
      }
    }

    graphicOverlay.clear();
    if (barcodeInCenter == null) {
      cameraReticleAnimator.start();
      graphicOverlay.add(new BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator));
      workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);

    } else {
      cameraReticleAnimator.cancel();
      float sizeProgress =
          PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(graphicOverlay, barcodeInCenter);
      if (sizeProgress < 1) {
        // Barcode in the camera view is too small, so prompt user to move camera closer.
        graphicOverlay.add(new BarcodeConfirmingGraphic(graphicOverlay, barcodeInCenter));
        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.CONFIRMING);

      } else {
        // Barcode size in the camera view is sufficient.
        if (PreferenceUtils.shouldDelayLoadingBarcodeResult(graphicOverlay.getContext())) {
          ValueAnimator loadingAnimator = createLoadingAnimator(graphicOverlay, barcodeInCenter);
          loadingAnimator.start();
          graphicOverlay.add(new BarcodeLoadingGraphic(graphicOverlay, loadingAnimator));
          workflowModel.setWorkflowState(WorkflowModel.WorkflowState.SEARCHING);

        } else {
          workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTED);
          workflowModel.detectedBarcode.setValue(barcodeInCenter);
        }
      }
    }
    graphicOverlay.invalidate();
  }

  private ValueAnimator createLoadingAnimator(
      GraphicOverlay graphicOverlay, FirebaseVisionBarcode barcode) {
    float endProgress = 1.1f;
    ValueAnimator loadingAnimator = ValueAnimator.ofFloat(0f, endProgress);
    loadingAnimator.setDuration(2000);
    loadingAnimator.addUpdateListener(
        animation -> {
          if (Float.compare((float) loadingAnimator.getAnimatedValue(), endProgress) >= 0) {
            graphicOverlay.clear();
            workflowModel.setWorkflowState(WorkflowModel.WorkflowState.SEARCHED);
            workflowModel.detectedBarcode.setValue(barcode);
          } else {
            graphicOverlay.invalidate();
          }
        });
    return loadingAnimator;
  }

  @Override
  protected void onFailure(Exception e) {
    Log.e(TAG, "Barcode detection failed!", e);
  }

  @Override
  public void stop() {
    try {
      detector.close();
    } catch (IOException e) {
      Log.e(TAG, "Failed to close barcode detector!", e);
    }
  }
}
