package hust.com.jsp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import hust.com.jsp.bean.JZJStatusItem;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

/**
 * Created by hust on 2016/12/30.
 */

public class StatusLayer extends MapBaseLayer {

    private Rect rect;

    private PointF location;
    private JZJStatusItem status;

    public static final int MARGIN = 3;

    public StatusLayer(MapView mapView, JZJStatusItem status){
        super(mapView);
        this.rect = new Rect(0,0,25,25);//保障任务标签
        this.location = new PointF(300.0f,300.0f);
        this.status = status;
    }



    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        //Log.i("RECT","im drawing!");
        float[] target = {location.x,location.y};
        currentMatrix.mapPoints(target);
        Rect targetRect = new Rect(rect);
        targetRect.offset((int)target[0]-rect.width()/2,(int)target[1]-rect.height()/2);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        //canvas.drawRect(targetRect,paint);
        RectF rectF = new RectF(targetRect);
        canvas.drawRoundRect(rectF,2*MARGIN,2*MARGIN,paint);

        targetRect.set(targetRect.left+MARGIN,targetRect.top+MARGIN,targetRect.right-MARGIN,targetRect.bottom-MARGIN);
        paint.setColor(status.getColor());
        rectF = new RectF(targetRect);
        canvas.drawRoundRect(rectF,MARGIN,MARGIN,paint);

        Path path = new Path();
        path.moveTo(targetRect.left,targetRect.bottom);
        path.lineTo(targetRect.right,targetRect.bottom);
        paint.setColor(Color.BLACK);
        float fontSize = 18.0f;
        paint.setTextSize(fontSize);
        paint.setTextAlign(Paint.Align.CENTER);
        float y = (rectF.top+rectF.bottom)/2+7.0f;
        float x = (rectF.left+rectF.right)/2;
        canvas.drawText(status.getName(),x,y,paint);
    }

    public StatusLayer setLocation(PointF p) {
        this.location = p;
        return this;
    }

    public int getStatusID(){
        return this.status.getId();
    }
}
