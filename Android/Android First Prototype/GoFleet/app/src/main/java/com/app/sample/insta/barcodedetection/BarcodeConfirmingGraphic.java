package com.app.sample.insta.barcodedetection;

import android.graphics.Canvas;
import android.graphics.Path;

import com.app.sample.insta.camera.GraphicOverlay;
import com.app.sample.insta.settings.PreferenceUtils;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

/** Guides user to move camera closer to confirm the detected barcode. */
class BarcodeConfirmingGraphic extends BarcodeGraphicBase {

  private final FirebaseVisionBarcode barcode;

  BarcodeConfirmingGraphic(GraphicOverlay overlay, FirebaseVisionBarcode barcode) {
    super(overlay);
    this.barcode = barcode;
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);

    // Draws a highlighted path to indicate the current progress to meet size requirement.
    float sizeProgress = PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(overlay, barcode);
    Path path = new Path();
    if (sizeProgress > 0.95f) {
      // To have a completed path with all corners rounded.
      path.moveTo(boxRect.left, boxRect.top);
      path.lineTo(boxRect.right, boxRect.top);
      path.lineTo(boxRect.right, boxRect.bottom);
      path.lineTo(boxRect.left, boxRect.bottom);
      path.close();

    } else {
      path.moveTo(boxRect.left, boxRect.top + boxRect.height() * sizeProgress);
      path.lineTo(boxRect.left, boxRect.top);
      path.lineTo(boxRect.left + boxRect.width() * sizeProgress, boxRect.top);

      path.moveTo(boxRect.right, boxRect.bottom - boxRect.height() * sizeProgress);
      path.lineTo(boxRect.right, boxRect.bottom);
      path.lineTo(boxRect.right - boxRect.width() * sizeProgress, boxRect.bottom);
    }
    canvas.drawPath(path, pathPaint);
  }
}
