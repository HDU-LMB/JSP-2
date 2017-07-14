package hust.com.jsp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.bean.BZPlan;

/**
 * Created by Michael-Lee on 2017/6/27.
 */

public class ZWListLayer extends MapBaseLayer {

    private Bitmap bitmap;
    private BitmapLayer baseLayer;
    private float width;//外围方框宽
    private float height;
    private BZPlan bzPlan;
    private List<ZWItemLayer> zwItemLayerList;
    private float offsetX;
    private float margin=6;
    private boolean isSho;

    public ZWListLayer(MapView mapView){
        super(mapView);
        this.height=131;
        this.width=mapView.getMapWidth()*2;
        offsetX=108;
//        this.isShow = false;
        zwItemLayerList=new ArrayList<>();
        Log.i("Test", "ZWListLayer: mapView.width="+mapView.getMapWidth());
        this.bitmap= Bitmap.createBitmap((int)width,(int)height,Bitmap.Config.ARGB_8888);//创建一个空白bitmap
        this.baseLayer=new BitmapLayer(mapView,bitmap);
        this.baseLayer.setAutoScale(true);
        this.baseLayer.setLocation(new PointF(0.0f,0.0f));
    }

    public void setBzPlan(BZPlan bzPlan) {
        this.bzPlan = bzPlan;
    }

    public void setLocation(PointF location) {
        baseLayer.setLocation(location);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0.0f,0.0f,this.width,this.height,paint);//清空
//        if(!isShow)  return;
        if(bzPlan==null) return;;

        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
        canvas.drawRect(0.0f,0.0f,this.width,this.height,paint);//画外方框

        zwItemLayerList.clear();
        for(int i=0;i<bzPlan.getBzPlanItemList().size();i++){
            ZWItemLayer zwItemLayer=new ZWItemLayer(mapView,bzPlan.getBzPlanItemList().get(i),bzPlan.getJzj());
            zwItemLayerList.add(zwItemLayer);
        }
        int i=0;
        for(ZWItemLayer zwLayer : zwItemLayerList){
            PointF p=new PointF(i++*offsetX+margin,margin);
            zwLayer.setOffsetPoint(p);
            zwLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        }
//        isShow=false;
    }

//    public ZWListLayer setIsShow(boolean b){
//        this.isShow=b;
//        return this;
//    }
}
