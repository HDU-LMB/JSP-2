package com.onlylemi.mapview.library.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;

/**
 * BitmapLayer
 *
 * @author: onlylemi
 */
public class BitmapLayer extends MapBaseLayer {

    private PointF location;
    private Bitmap bitmap;
    private Paint paint;

    private boolean autoScale = false;

    private OnBitmapClickListener onBitmapClickListener;

    private Matrix matrix;
    private float zoom;
    private float rotate;

    public BitmapLayer(MapView mapView, Bitmap bitmap) {
        this(mapView, bitmap, null);
    }

    public BitmapLayer(MapView mapView, Bitmap bitmap, PointF location) {
        super(mapView);
        this.location = location;
        this.bitmap = bitmap;

        paint = new Paint();
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (onBitmapClickListener != null) {
            float[] values = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
            StringBuilder sb = new StringBuilder();
            matrix.getValues(values);
            for (float v :
                    values) {
                sb.append(v);
                sb.append("f,");
            }
            float[] goal = mapView.convertScreenXY2MapXY(event.getX(), event.getY());
            Log.i("BitmapLayer", "goal: " + goal[0] + ", " + goal[1]+" Matrix:"+sb.toString());
            if (goal[0] > location.x - bitmap.getWidth() / 2 &&
                    goal[0] < location.x + bitmap.getWidth() / 2 &&
                    goal[1] > location.y - bitmap.getHeight() / 2 &&
                    goal[1] < location.y + bitmap.getHeight() / 2) {
                onBitmapClickListener.onBitmapClick(this);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        this.matrix = currentMatrix;
        this.zoom = currentZoom;
        this.rotate = currentRotateDegrees;
        if (isVisible && bitmap != null) {
            canvas.save();
            float[] goal = {location.x, location.y};
            if (!autoScale) {
                currentMatrix.mapPoints(goal);
            } else {
                canvas.setMatrix(currentMatrix);
            }
            canvas.drawBitmap(bitmap, goal[0] - bitmap.getWidth() / 2,
                    goal[1] - bitmap.getHeight() / 2, paint);
            canvas.restore();
        }
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public void setOnBitmapClickListener(OnBitmapClickListener onBitmapClickListener) {
        this.onBitmapClickListener = onBitmapClickListener;
    }

    public interface OnBitmapClickListener {
        void onBitmapClick(BitmapLayer layer);
    }
}
