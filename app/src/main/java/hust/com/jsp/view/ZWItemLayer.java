package hust.com.jsp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;

/**
 * Created by Michael-Lee on 2017/6/26.
 * 功能：绘画展示FJ保障站位及任务情况
 */

public class ZWItemLayer extends MapBaseLayer{

    private RectF labelRect;
  //  private RectF baseRct;
    private BZPlanItem bzPlanItem;
//    private Bitmap bitmap;
//    private BitmapLayer baseLayer;
    private float width;//外围方框宽
    private float height;
    private int labelWidth;//标签方框宽与高
    private final int MARGIN = 3;//圆角矩形的圆角半径
    private JZJ jzj;


    public void setOffsetPoint(PointF offsetPoint) {
        this.offsetPoint = offsetPoint;
    }

    private PointF offsetPoint;
//    public void setLocation(PointF location) {
//        baseLayer.setLocation(location);
//    }



    public ZWItemLayer(MapView mapView, BZPlanItem bzItem, JZJ jzj){
        super(mapView);
        this.labelWidth=25;
        int x0=2*MARGIN;
        int y0=labelWidth+MARGIN;
        this.labelRect=new RectF(x0,y0,x0+labelWidth,y0+labelWidth);
        this.width=98;
        this.height=118;
       // this.baseRect=new RectF(0,0,width,height);
        this.bzPlanItem=bzItem;
        this.jzj=jzj;
        offsetPoint =new PointF(0,0);
//        this.bitmap= Bitmap.createBitmap((int)width+1 ,(int)height+1,Bitmap.Config.ARGB_8888);//创建一个空白bitmap
//        this.baseLayer=new BitmapLayer(mapView,bitmap);
//        this.baseLayer.setAutoScale(true);
//        this.baseLayer.setLocation(new PointF(500,300));
    }


    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
//        if(baseLayer == null) return;
//        baseLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
//        float[] pointXY = {baseLayer.getLocation().x,baseLayer.getLocation().y};
//        currentMatrix.mapPoints(pointXY);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(16.0f);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(jzj.getDisplayName(), offsetPoint.x,labelWidth/2+6+offsetPoint.y,paint);//画FJ名称
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(bzPlanItem.getSpendTime()+"", offsetPoint.x+width/2+10,labelWidth/2+6+offsetPoint.y,paint);
        paint.setTextAlign(Paint.Align.RIGHT);

        if(bzPlanItem.getStation()==null) return;

        canvas.drawText(bzPlanItem.getStation().getDisplayName(), offsetPoint.x+width,labelWidth/2+6+offsetPoint.y,paint);//画ZW名称

        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
        canvas.drawRect(offsetPoint.x,0+offsetPoint.y,offsetPoint.x+width,height+offsetPoint.y,paint);//画外方框
        canvas.drawLine(offsetPoint.x,25+offsetPoint.y, offsetPoint.x+width,25+offsetPoint.y,paint);

        String[] label=bzPlanItem.getLabels();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                float left= offsetPoint.x+labelRect.left+j*(labelWidth+2*MARGIN);
                float top=labelRect.top+i*(labelWidth+2*MARGIN)+offsetPoint.y;
                float right=left+labelWidth;
                float bottom=top+labelWidth;

                if(bzPlanItem.getActions()[i*3+j])//根据任务情况是否填充颜色
                    paint.setColor(bzPlanItem.getColors()[i*3+j]);
                else
                    paint.setColor(Color.LTGRAY);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(left,top,right,bottom,2*MARGIN,2*MARGIN,paint);//画标签方框

                float fontSize = 14.0f;
                paint.setTextSize(fontSize);
                paint.setColor(Color.BLACK);
                paint.setTextAlign(Paint.Align.CENTER);
                float y = (top+bottom)/2+4.0f;
                float x = (left+right)/2-1.0f;
                canvas.drawText(label[i*3+j],x,y,paint);//画标签名字
            }
        }

    }
}
