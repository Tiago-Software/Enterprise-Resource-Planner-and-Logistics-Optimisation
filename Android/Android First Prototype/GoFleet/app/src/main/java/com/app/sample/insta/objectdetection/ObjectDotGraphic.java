package com.app.sample.insta.objectdetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;

import com.app.sample.insta.R;
import com.app.sample.insta.camera.GraphicOverlay;

/** A dot to indicate a detected object used by multiple objects detection mode. */
class ObjectDotGraphic extends GraphicOverlay.Graphic
{

  private final ObjectDotAnimator animator;
  private final Paint paint;
  private final PointF center;
  private final int dotRadius;
  private final int dotAlpha;

  ObjectDotGraphic(GraphicOverlay overlay, DetectedObject object, ObjectDotAnimator animator) {
    super(overlay);

    this.animator = animator;

    Rect box = object.getBoundingBox();
    center =
        new PointF(
            overlay.translateX((box.left + box.right) / 2f),
            overlay.translateY((box.top + box.bottom) / 2f));

    paint = new Paint();
    paint.setStyle(Style.FILL);
    paint.setColor(Color.WHITE);

    dotRadius = context.getResources().getDimensionPixelOffset(R.dimen.object_dot_radius);
    dotAlpha = paint.getAlpha();
  }

  @Override
  public void draw(Canvas canvas) {
    paint.setAlpha((int) (dotAlpha * animator.getAlphaScale()));
    canvas.drawCircle(center.x, center.y, dotRadius * animator.getRadiusScale(), paint);
  }
}
