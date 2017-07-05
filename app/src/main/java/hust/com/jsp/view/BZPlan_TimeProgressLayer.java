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

import hust.com.jsp.bean.BCInfo;
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
    private List<BZPlan> bzPlanList;

    private float widthUnit;//方框单位时间下的长度
    private float heightLabel;//方框的高
    private float marginTop;//上下方框之间间隙

    public BZPlan_TimeProgressLayer(MapView mapView,List<BZPlan> bzPlanList){
        super(mapView);
        this.mapView=mapView;
        this.bzPlanList=bzPlanList;
        this.width=1600;
        this.height=260;
        this.widthUnit=3;
        this.heightLabel=20;
        this.marginTop=5;
        this.bitmap= Bitmap.createBitmap((int)width,(int)height,Bitmap.Config.ARGB_8888);//创建一个空白bitmap
        this.baseLayer=new BitmapLayer(mapView,bitmap);
        this.baseLayer.setLocation(new PointF(0.0f,630.0f));
    }

    public void setLocation(PointF loc)
    {
        this.location = loc;
        this.baseLayer.setLocation(location);
    }
    @Override
    public void onTouch(MotionEvent event) {

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
        float top=5+location.y;
        float left=5;
        float right;
        float bottom;
        for(int i=0;i<bzPlanList.size();i++){
            BZPlan bzPlan=bzPlanList.get(i);
            JZJ jzj=bzPlan.getJzj();
            top+=i*(heightLabel+marginTop);
            left=50;
            bottom=top+heightLabel;
            float textLeng = paint.measureText(jzj.getDisplayName());
            canvas.drawText(jzj.getDisplayName(),(left-textLeng)-2,(top+bottom)/2+5,paint);//画JZJ名称

            for(int j=0;j<bzPlan.getBzPlanItemList().size();j++){
                BZPlanItem bzItem=bzPlan.getBzPlanItemList().get(j);

                right=left+widthUnit*bzItem.getSpendTime();
                canvas.drawRect(left,top,right,bottom,paint);//画每个ZW上任务花费的时间
                left=right;
            }
        }

    }


}
