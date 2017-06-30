package hust.com.jsp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

import hust.com.jsp.bean.QWAction;
import hust.com.jsp.bean.QWPlanItem;

/**
 * Created by hust on 2017/2/7.
 */

public class QWPlanView extends SurfaceView implements SurfaceHolder.Callback  {

    private SurfaceHolder holder;
    private Canvas canvas;

    private long startViewTime;
    private long endViewTime;

    private float width;
    private float height;
    private float sepLeftTitle;
    private float sepColNorm;
    private float sepTopTitle;
    private float sepRowNorm;
    private float sepColDivide;
    private float sepPerTime;

    public QWPlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void reMapSize(Canvas canvas){
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.sepLeftTitle = width/11.0f;
        this.sepColNorm = sepLeftTitle*2.0f;
        this.sepColDivide = sepColNorm /6.0f;
        this.sepTopTitle = height/7.0f;
        this.sepRowNorm = this.sepTopTitle;
        this.sepPerTime = sepColNorm*5.0f/(endViewTime-startViewTime);
    }

    private void drawGrid() {
        Paint paint = new Paint();


        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        float sepr = this.sepLeftTitle;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.startViewTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        paint.setTextSize(this.sepTopTitle/4.0f);
        paint.setTextAlign(Paint.Align.CENTER);

        for(;sepr<width-0.01f;sepr+=this.sepColNorm){
            canvas.drawLine(sepr,0.0f,sepr,height,paint);
            canvas.drawText(""+hour+":00",sepr+sepColNorm/2.0f,this.sepTopTitle/4.0f+this.sepTopTitle/8.0f,paint);
            hour++;

            paint.setColor(Color.LTGRAY);
            float sepDr = sepr+this.sepColDivide;

            int r = 0;
            String o ;
            for(;sepDr<(sepr+this.sepColNorm-0.01f);sepDr+=this.sepColDivide){
                paint.setColor(Color.LTGRAY);
                canvas.drawLine(sepDr,this.sepTopTitle/2.0f,sepDr,height,paint);
                paint.setColor(Color.BLACK);

                if(r==0){
                    o="00";
                }
                else{
                    o = ""+r;
                }
                canvas.drawText(o,sepDr-this.sepColDivide/2.0f,this.sepTopTitle*0.875f,paint);
                r+=10;
            }
            o = ""+r;
            canvas.drawText(o,sepDr-this.sepColDivide/2.0f,this.sepTopTitle*0.875f,paint);
        }
        canvas.drawLine(this.sepLeftTitle,this.sepTopTitle/2.0f,width,this.sepTopTitle/2.0f,paint);

        float sepddr = this.sepTopTitle;

        for(;sepddr<height;sepddr+=this.sepTopTitle) {
            canvas.drawLine(0.0f,sepddr,width,sepddr,paint);
        }
    }

    private void drawQWPlan(QWPlanItem qwPlanItem, int yOffset){
        long viewExpand = this.endViewTime-this.startViewTime;

        for (QWAction action :
                qwPlanItem.actionList) {



            long itemLeftExpand = action.getStartTime()-this.startViewTime;
            long itemSelfExpand = action.getEndTime()-action.getStartTime();
            float relativeX = this.sepPerTime*(itemLeftExpand)+this.sepLeftTitle;
            float itemHeight = this.sepRowNorm/3.0f*2.0f;
            float relativeY = this.sepTopTitle+this.sepRowNorm*yOffset;
            float itemExpand = this.sepPerTime*itemSelfExpand;

            float relativeCenterY = relativeY+this.sepRowNorm/2.0f;

            float lineSep = this.sepRowNorm/3.0f;
            float barHeight = this.sepRowNorm/4.0f;

            Paint paint = new Paint();
            paint.setStrokeWidth(4.0f);
            paint.setColor(action.getColor());

            float leftEnd = 0.0f;
            float rightEnd =0.0f;

            switch(action.getType()) {
                case flight:
                    break;
                case land:
                    break;
                case action1:
                    break;
            }

            switch (action.getLeftType()){
                case leftArrow:
                    canvas.drawLine(relativeX,relativeCenterY,relativeX+lineSep,relativeCenterY-lineSep/2.0f,paint);
                    canvas.drawLine(relativeX,relativeCenterY,relativeX+lineSep,relativeCenterY+lineSep/2.0f,paint);
                    break;
                case rightArrow:
                    canvas.drawLine(relativeX,relativeCenterY-lineSep/2.0f,relativeX+lineSep,relativeCenterY,paint);
                    canvas.drawLine(relativeX,relativeCenterY+lineSep/2.0f,relativeX+lineSep,relativeCenterY,paint);
                    leftEnd = lineSep;
                    break;
                case circle:
                    canvas.drawCircle(relativeX+lineSep/2.0f,relativeCenterY-lineSep/2.0f,lineSep/2.0f,paint);
                    leftEnd = lineSep;
                    break;
                case none:
                    break;
            }

            switch (action.getRightType()){
                case leftArrow:
                    canvas.drawLine(relativeX+itemExpand-lineSep,relativeCenterY,relativeX+itemExpand,relativeCenterY+lineSep/2.0f,paint);
                    canvas.drawLine(relativeX+itemExpand-lineSep,relativeCenterY,relativeX+itemExpand,relativeCenterY+lineSep/2.0f,paint);
                    rightEnd = lineSep;
                    break;
                case rightArrow:
                    canvas.drawLine(relativeX+itemExpand-lineSep,relativeCenterY-lineSep/2.0f,relativeX+itemExpand,relativeCenterY,paint);
                    canvas.drawLine(relativeX+itemExpand-lineSep,relativeCenterY+lineSep/2.0f,relativeX+itemExpand,relativeCenterY,paint);
                    break;
                case circle:
                    canvas.drawCircle(relativeX+itemExpand-lineSep/2.0f,relativeCenterY-lineSep/2.0f,lineSep/2.0f,paint);
                    rightEnd = lineSep;
                    break;
                case none:
                    break;
            }

            switch (action.getLineType()){
                case line:
                    canvas.drawLine(relativeX+leftEnd,relativeCenterY-lineSep/2.0f,relativeX+itemExpand-rightEnd,relativeCenterY-lineSep/2.0f,paint);
                    break;
                case bar:
                    canvas.drawRect(relativeX+leftEnd,relativeCenterY-lineSep/2.0f-barHeight/2.0f,relativeX+itemExpand-rightEnd,relativeCenterY-lineSep/2.0f+barHeight/2.0f,paint);
                    break;
            }

        }



/*
        if(fxPlanItem.getEndTime()<(this.startViewTime)) return;
        if(fxPlanItem.getStartTime()>(this.endViewTime)) return;

        long itemLeftExpand = fxPlanItem.getStartTime()-this.startViewTime;
        long itemSelfExpand = fxPlanItem.getEndTime()-fxPlanItem.getStartTime();
        Log.i("itemSelfExpand", ""+itemSelfExpand);


        Paint paint = new Paint();
        paint.setStrokeWidth(4.0f);
        paint.setColor(Color.BLACK);

        fxPlanItem.getStartTime();

        canvas.drawLine(relativeX,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand-itemHeight/2.0f,relativeY+this.sepRowNorm/2.0f,paint);
        canvas.drawLine(relativeX+itemHeight/2.0f,relativeY+(sepRowNorm-itemHeight)/2.0f,relativeX+itemHeight/2.0f,relativeY+(sepRowNorm+itemHeight)/2.0f,paint);

        paint.setTextSize(sepRowNorm/4.0f);
        float leng1 = paint.measureText(testItem.getJzj().getDisplayName());
        canvas.drawText(testItem.getJzj().getDisplayName(),relativeX+itemHeight/2.0f-leng1-2.0f,relativeY+sepRowNorm*0.625f-itemHeight/4.0f,paint);
        float leng2 = paint.measureText(testItem.getStation().getDisplayName());
        canvas.drawText(testItem.getStation().getDisplayName(),relativeX+itemHeight/2.0f-leng2-2.0f,relativeY+sepRowNorm*0.625f+itemHeight/4.0f,paint);
        float leng3 = paint.measureText(testItem.getGasStatus());
        canvas.drawText(testItem.getGasStatus(),relativeX+itemHeight/2.0f+2.0f,relativeY+sepRowNorm*0.625f+itemHeight/4.0f,paint);


        if(fxPlanItem.getType()== FXPlanItem.FXPlanType.land){
            canvas.drawLine(relativeX+itemExpand,relativeY+(sepRowNorm+itemHeight)/2.0f,relativeX+itemExpand-itemHeight/2.0f,relativeY+this.sepRowNorm/2.0f,paint);
        }
        else if(fxPlanItem.getType()== FXPlanItem.FXPlanType.flight){
            canvas.drawLine(relativeX+itemExpand,relativeY+(sepRowNorm-itemHeight)/2.0f,relativeX+itemExpand-itemHeight/2.0f,relativeY+this.sepRowNorm/2.0f,paint);
        }
        else if(fxPlanItem.getType()== FXPlanItem.FXPlanType.both){
            canvas.drawLine(relativeX+itemExpand,relativeY+(sepRowNorm-itemHeight)/2.0f,relativeX+itemExpand-itemHeight/2.0f,relativeY+this.sepRowNorm/2.0f,paint);
            canvas.drawLine(relativeX+itemExpand,relativeY+(sepRowNorm+itemHeight)/2.0f,relativeX+itemExpand-itemHeight/2.0f,relativeY+this.sepRowNorm/2.0f,paint);
        }
        */
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
