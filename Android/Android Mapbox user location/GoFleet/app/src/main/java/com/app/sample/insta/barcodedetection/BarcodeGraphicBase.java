package com.app.sample.insta.barcodedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.app.sample.insta.R;
import com.app.sample.insta.camera.GraphicOverlay;
import com.app.sample.insta.settings.PreferenceUtils;

abstract class BarcodeGraphicBase extends GraphicOverlay.Graphic
{

  private final Paint boxPaint;
  private final Paint scrimPaint;
  private final Paint eraserPaint;

  final int boxCornerRadius;
  final Paint pathPaint;
  final RectF boxRect;

  BarcodeGraphicBase(GraphicOverlay overlay) {
    super(overlay);

    boxPaint = new Paint();
    boxPaint.setColor(ContextCompat.getColor(context, R.color.barcode_reticle_stroke));
    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(
        context.getResources().getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width));

    scrimPaint = new Paint();
    scrimPaint.setColor(ContextCompat.getColor(context, R.color.barcode_reticle_background));
    eraserPaint = new Paint();
    eraserPaint.setStrokeWidth(boxPaint.getStrokeWidth());
    eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    boxCornerRadius =
        context.getResources().getDimensionPixelOffset(R.dimen.barcode_reticle_corner_radius);

    pathPaint = new Paint();
    pathPaint.setColor(Color.WHITE);
    pathPaint.setStyle(Style.STROKE);
    pathPaint.setStrokeWidth(boxPaint.getStrokeWidth());
    pathPaint.setPathEffect(new CornerPathEffect(boxCornerRadius));

    boxRect = PreferenceUtils.getBarcodeReticleBox(overlay);
  }

  @Override
  protected void draw(Canvas canvas) {
    // Draws the dark background scrim and leaves the box area clear.
    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
    // As the stroke is always centered, so erase twice with FILL and STROKE respectively to clear
    // all area that the box rect would occupy.
    eraserPaint.setStyle(Style.FILL);
    canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, eraserPaint);
    eraserPaint.setStyle(Style.STROKE);
    canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, eraserPaint);

    // Draws the box.
    canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, boxPaint);
  }
}
