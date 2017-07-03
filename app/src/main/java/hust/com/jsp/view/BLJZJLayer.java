package hust.com.jsp.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;

/**
 * Created by hust on 2016/12/13.
 */

public class BLJZJLayer extends MapBaseLayer {

    private PointF location;
    private Bitmap bitmap;
    private BitmapLayer baseLayer;
    private JZJ jzj;
    private int jzjID;
    private final double radius = 30.0;
    private MapView mapView;
    private BitmapLayer.OnBitmapClickListener onBitmapClickListener;

    public BLJZJLayer(MapView mapView, Resources res, final JZJ jzj){
        super(mapView);
        this.mapView=mapView;
        switch (jzj.getJzjType()){
            case 1:
                this.bitmap = BitmapFactory.decodeResource(res, R.drawable.jzj);
                break;
            case 2:
                this.bitmap = BitmapFactory.decodeResource(res, R.drawable.jzj2);
                break;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.1f);
        Bitmap dstbmp = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(),
                matrix, true);
        this.baseLayer = new BitmapLayer(mapView,dstbmp);
      //  baseLayer.setAutoScale(true);
    }

    public void setLocation(PointF loc)
    {
        float[] point= mapView.convertScreenXY2MapXY(loc.x,loc.y);
        PointF pointF=new PointF(point[0],point[1]);
        this.location = pointF;
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

    public JZJ getJzj() {
        return jzj;
    }

    public void setJzj(JZJ jzj) {
        this.jzj = jzj;
    }

    public int getJzjID() {
        return jzjID;
    }

    public void setJzjID(int jzjID) {
        this.jzjID = jzjID;
    }
}
