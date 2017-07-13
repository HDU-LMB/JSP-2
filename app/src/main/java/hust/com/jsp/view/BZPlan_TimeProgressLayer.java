package hust.com.jsp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import java.util.List;

import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;

/**
 * Created by Michael-Lee on 2017/7/4.
 * 实时显示当前BZ计划的制定情况，甘特图的每一行代表一架FJ
 */

public class BZPlan_TimeProgressLayer extends MapBaseLayer {

    private Bitmap bitmap;
    private BitmapLayer baseLayer;
    private MapView mapView;
    private float width;//外围方框宽
    private float height;
    private PointF location;
    private boolean isShowTimeProgress;
    private List<BZPlan> bzPlanList;
    private float widthUnit;//方框单位时间下的长度
    private float heightLabel;//方框的高
    private float marginTop;//上下方框之间间隙
    private int time;

    public BZPlan_TimeProgressLayer(MapView mapView,List<BZPlan> bzPlanList){
        super(mapView);
        this.mapView=mapView;
        this.isShowTimeProgress=false;
        this.bzPlanList=bzPlanList;
        this.width=1600;
        this.height=800;
        this.widthUnit=4;
        this.heightLabel=20;
        this.marginTop=5;
        this.bitmap= Bitmap.createBitmap((int)width,(int)height,Bitmap.Config.ARGB_8888);//创建一个空白bitmap
        this.baseLayer=new BitmapLayer(mapView,bitmap);
        this.baseLayer.setLocation(new PointF(0.0f,630.0f));
    }

    public void setBzPlanList(List<BZPlan> bzPlanList) {
        this.bzPlanList = bzPlanList;
    }


    public void setShowTimeProgress(boolean showTimeProgress) {
        isShowTimeProgress = showTimeProgress;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF loc)
    {
        this.location = loc;
        this.baseLayer.setLocation(location);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void onTouch(MotionEvent event) {
        this.baseLayer.onTouch(event);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0.0f,location.y,this.width,location.y+this.height,paint);//清空
//        if(!isShow)  return;
//        if(bzPlan==null) return;;

        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
        canvas.drawRect(0.0f,location.y,this.width,location.y+this.height,paint);//画外方框

        if(bzPlanList==null || bzPlanList.size()==0) return;

        paint.setColor(Color.BLACK);
        float top0=20+location.y;
        float top;
        float left=50;
        float right;
        float bottom;
        canvas.drawLine(left,top0,left,top0+this.height,paint);//纵坐标
        canvas.drawLine(left,top0,left+this.width,top0,paint);//横坐标
        paint.setTextSize(14);
        paint.setStrokeWidth(1);
        for(int i=1;i<=50;i++){
            left=50+i*widthUnit*10;
            canvas.drawLine(left,top0,left,top0+4,paint);//横坐标刻度
            canvas.drawText(i*10+"",left-10,top0-3,paint);
        }
        top0+=8;
        for(int i=0;i<bzPlanList.size();i++){
            BZPlan bzPlan=bzPlanList.get(i);
            JZJ jzj=bzPlan.getJzj();
            top=top0+i*(heightLabel+marginTop);
            left=53;
            bottom=top+heightLabel;
            float textLeng = paint.measureText(jzj.getDisplayName());
            paint.setColor(Color.BLACK);
            canvas.drawText(jzj.getDisplayName(),(left-textLeng)-6,(top+bottom)/2+5,paint);//画JZJ名称

            if(!isShowTimeProgress) {
                for (int j = 0; j < bzPlan.getBzPlanItemList().size(); j++) {
                    BZPlanItem bzItem = bzPlan.getBzPlanItemList().get(j);

                    right = left + widthUnit * bzItem.getSpendTime();
                    canvas.drawRect(left, top, right, bottom, paint);//画每个ZW上任务花费的时间
                    canvas.drawText(bzItem.getStation().getDisplayName(), (left + right) / 2 - 10, (top + bottom) / 2 + 5, paint);//画ZW名称
                    left = right;
                }
            }else {
                for (int j = 0; j < bzPlan.getBzPlanItemList().size(); j++) {
                    BZPlanItem bzItem = bzPlan.getBzPlanItemList().get(j);

                    left = 53 + widthUnit * bzItem.getStartTime();
                    right = left + widthUnit * bzItem.getSpendTime();
                    canvas.drawRect(left, top, right, bottom, paint);//画每个ZW上任务花费的时间
                    canvas.drawText(bzItem.getStation().getDisplayName(), (left + right) / 2 - 10, (top + bottom) / 2 + 5, paint);//画ZW名称
//                left=right;
                }
                if(time>0) {
                    paint.setColor(Color.GREEN);
                    canvas.drawLine(53 + widthUnit * time, top0-8, 53 + widthUnit * time, top0 + height, paint);
                }
            }
        }

    }



}
