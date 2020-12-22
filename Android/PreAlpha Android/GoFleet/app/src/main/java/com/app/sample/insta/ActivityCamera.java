package com.app.sample.insta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.sample.insta.data.Tools;
import com.app.sample.insta.model.LottoTicket;
import com.app.sample.insta.model.TicketFirestoreModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ActivityCamera extends AppCompatActivity {

    private static final String TAG = "AndroidCameraApi";
 /*   private Button takePictureButton;*/
    private FloatingActionButton btnCaptureBarcode;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray(4);
    public static Bitmap bitmap;
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

    public static String LottoNum;


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private ImageView mImageView;
    private Button mTextButton;
    private Button mFaceButton;
    private Button mCloudButton;
    private Button mRunCustomModelButton;
    private Bitmap mSelectedImage;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;

    public static LottoTicket LT;

    //var barcode

    private String strText;
    private TextView ticketID;
    private TextView pDate;
    private TextView dDate;
    private TextView tType;
    private TextView noMatch;

    private LottoTicket ltPass; // to pass lottoticket object


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

      //  textureView = findViewById(R.id.textureView);
        textureView = new TextureView(this);

        textureView = findViewById(R.id.textureView);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);

       // assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
      //  assert takePictureButton != null;

        /*takePictureButton = new Button(this);*/
        btnCaptureBarcode = new FloatingActionButton(this);

       /* takePictureButton = (Button) findViewById(R.id.btn_takepicture);*/
        btnCaptureBarcode = (FloatingActionButton) findViewById(R.id.shoot);

        //TODO: Text Recognition.

        // assert takePictureButton != null;
        /*  takePictureButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });*/
        btnCaptureBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureBarcode();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //cameraDevice.close();
        startBackgroundThread();
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* cameraDevice = null;*/
        stopBackgroundThread();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        closeCamera(); //camera close
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NotNull CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error)
        {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(ActivityCamera.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void stopBackgroundThread() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBackgroundThread.quitSafely();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBackgroundThread.join();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBackgroundThread = null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBackgroundHandler = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //TODO: Text recognition
    protected void takePicture()
    {

        if (null == cameraDevice)
        {
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;

            if (characteristics != null)
            {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            int width = 0, height = 0;
            if (jpegSizes != null && 0 < jpegSizes.length)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            String dir = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();

            final File file = new File(getExternalFilesDir(dir) + "/pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener()
            {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try
                    {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                        /* Parse image to text scanner via bitmap */
                        mSelectedImage = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        runTextRecognition(mSelectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try
                    {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally
                    {
                        if (null != output)
                        {
                            output.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(ActivityCamera.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback()
            {
                @Override
                public void onConfigured(CameraCaptureSession session)
                {
                    try
                    {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session)
                {

                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void captureBarcode() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 720;
            int height = 1280;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            String dir = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();

            final File file = new File(getExternalFilesDir(dir) + "/pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader)
                {
                    Image image = null;

                    try
                    {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                        /* Parse image to text scanner via bitmap */
                        mSelectedImage = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        scanBarcodes(mSelectedImage);

                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    } finally {
                        if (image != null)
                        {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(ActivityCamera.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(ActivityCamera.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //assert map != null;

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityCamera.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;

        }
    }

    private void runTextRecognition(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exceptionmTextButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts)
    {
        String strText = "";

        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
        }
        else
            {

            // Full text of text recog
            strText = texts.getText();

            //Lotto number read
            Pattern p = Pattern.compile("[A-Z0-9]+:\\s\\d+\\s\\d+\\s\\d+\\s\\d+\\s\\d+\\s\\d+");

            Matcher m = p.matcher(strText);

            LottoNum = "";

            String LottoNumPrint = "";

            int i = 0;

                while(m.find())
                {
                    i++;
                }

            String[] Board = new String[i];

            i = 0;
            m = p.matcher(strText);

            while(m.find())
            {
                LottoNum = m.group();
                LottoNumPrint += LottoNum + " ";
                Board[i] = LottoNum;
                i++;
            }

         //   int g = i-1;

          //  String[] BoardRe = Arrays.copyOf(Board, g);

         //   BoardRe = Board;




            if(LottoNum == "")
            {
                LottoNum = "A00: 0 0 0 0 0 0";
            }

            LT = new LottoTicket();


            LT.setTBoards(Board);

            Intent intent = new Intent(ActivityCamera.this, ActivityTicket.class);
            intent.putExtra("text", LottoNumPrint);
            startActivity(intent);
        }
    }

    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    // barcode scanner
    private void scanBarcodes(Bitmap bitmap)
    {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        // [START set_detector_options]
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ITF).build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                         //serial no
                        for (FirebaseVisionBarcode barcode: barcodes)
                        {
                            rawValue = barcode.getRawValue();
                        }


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


                        // TODO: change system print line

     /*                   Intent intent = new Intent(ActivityCamera.this, ActivityTicket.class);
                        intent.putExtra("text", ltPass);
                        startActivity(intent);

*/
                        closeCamera(); //closes camera
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
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

                    Intent intent = new Intent(ActivityCamera.this, ActivityMain.class);
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
