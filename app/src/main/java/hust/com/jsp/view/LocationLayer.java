package hust.com.jsp.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;

public class LocationLayer extends MapBaseLayer {

    private Bitmap bitmap;
    private BitmapLayer baseLayer;
    private Location location;
    private MapView mapView;
    private BitmapLayer.OnBitmapClickListener onBitmapClickListener;

    public LocationLayer(MapView mapView, Resources res,Location location){
        super(mapView);
        this.mapView=mapView;
        this.location=location;
        this.bitmap = BitmapFactory.decodeResource(res, R.drawable.location);
        Matrix matrix = new Matrix();
        matrix.setRotate(location.getAngle(), this.bitmap.getWidth() / 2, this.bitmap.getHeight() / 2);
        matrix.postScale(0.1f, 0.1f);
        Bitmap dstbmp = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(),
                matrix, true);

        this.baseLayer = new BitmapLayer(mapView,dstbmp);
        float[] point= mapView.convertScreenXY2MapXY(location.getX(),location.getY());
        PointF pointF=new PointF(point[0],point[1]);
        this.baseLayer.setLocation(pointF);
    }

    @Override
    public void onTouch(MotionEvent event) {
        this.baseLayer.onTouch(event);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {

        baseLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
    }


    public void setOnBitmapClickListener(BitmapLayer.OnBitmapClickListener onBitmapClickListener) {
        this.baseLayer.setOnBitmapClickListener(onBitmapClickListener);
    }

}
