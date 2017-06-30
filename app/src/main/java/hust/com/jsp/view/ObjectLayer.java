package hust.com.jsp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import hust.com.jsp.utils.ImageCollection;
import hust.com.jsp.bean.JZJItem;
import hust.com.jsp.bean.JZJStatusItem;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hust on 2016/12/13.
 */

public class ObjectLayer extends MapBaseLayer {

    private PointF location;
    private Bitmap bitmap;

    private List<Boolean> FLAGS;

    private BitmapLayer baseLayer;

    private List<StatusLayer> layers;

    //private Map<BitmapLayer,Boolean> layerStatus;

    private JZJItem jzjitem;

    private final double radius = 30.0;
    private BitmapLayer.OnBitmapClickListener onBitmapClickListener;

    public ObjectLayer(MapView mapView, JZJItem jzjItem){
        super(mapView);
        this.layers = new ArrayList();
        //this.layerStatus = new Hashtable();
        Log.i("XIXI","read bitmap:"+jzjItem.getImageID());
        this.jzjitem = jzjItem;
        this.bitmap = ImageCollection.collection.getBitmap(jzjItem.getImageID());
        baseLayer = new BitmapLayer(mapView,this.bitmap);//底层图
        baseLayer.setAutoScale(true);
        baseLayer.setLocation(jzjItem.getPosition());

        for (JZJStatusItem statusItem : jzjItem.getStatusList()) {
            StatusLayer statusLayer = new StatusLayer(mapView,statusItem);
            layers.add(statusLayer);
        }

        /*
        for (JZJStatusItem item :
                this.jzjStatus.getStatusList()) {
            ImageCollection.collection.getBitmap(item.getId());
            this.layers.add(bitmaplayer);
        }*/
    }

    public void setLocation(PointF loc)
    {
        this.location = loc;
        this.baseLayer.setLocation(loc);
    }

    @Override
    public void onTouch(MotionEvent event) {
        this.baseLayer.onTouch(event);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        /*
        if(baseLayer == null) return;
        baseLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        if(layers == null) return;
        reLocateBitmaps(currentZoom,currentRotateDegrees);
        for (BitmapLayer bitmaplayer:layers
             ) {
            if(!layerStatus.getDataType(bitmaplayer)) continue;
            //Log.d("DRW_BITMAP","bitmap layer drawed at" + bitmaplayer.getLocation().toString());
            bitmaplayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        }
        */
        if(baseLayer == null) return;
        baseLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        if(layers == null) return;
        this.reLocateBitmaps(currentZoom,currentRotateDegrees);
        for (StatusLayer statusLayer : layers) {
            if(jzjitem.getStatus(statusLayer.getStatusID())<1)continue;
            statusLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        }
    }

    public ObjectLayer addBitmap(StatusLayer layer) {
        this.layers.add(layer);
        //this.layerStatus.put(layer,true);
        return  this;
    }

    public void setOnBitmapClickListener(BitmapLayer.OnBitmapClickListener onBitmapClickListener) {
        this.baseLayer.setOnBitmapClickListener(onBitmapClickListener);
    }
    
    private void reLocateBitmaps(float currentZoom,float currentRotateDegrees){
        int layerCount = layers.size();
        double rotateRad = Math.toRadians(currentRotateDegrees);
        if(layerCount >0){
            double divedCircle = 2.0*Math.PI/layerCount;
            for (int i = 0;i<layerCount;i++ ) {
                double tX = Math.sin(divedCircle * i + rotateRad) * radius/currentZoom + this.location.x;
                double tY = Math.cos(divedCircle * i + rotateRad) * radius/currentZoom + this.location.y;
                PointF p = new PointF((float) tX,(float) tY);
                layers.get(i).setLocation( p);
            }
        }
    }
}
