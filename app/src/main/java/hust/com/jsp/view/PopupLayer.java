package hust.com.jsp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.TextView;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.MapBaseLayer;

/**
 * Created by hust on 2017/1/4.
 */

public class PopupLayer extends MapBaseLayer {

    private PointF location;

    private RectF rect;

    private float bubbleHeight;
    private boolean isShow;

    public PopupLayer(MapView mapView){
        super(mapView);
        this.isShow = false;
        this.rect = new RectF(0.0f,0.0f,100.0f,200.0f);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        if(!isShow) return;
        float[] target = {location.x,location.y};
        currentMatrix.mapPoints(target);
        RectF targetRect = new RectF(rect);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        //paint.sets如图
        targetRect.offset(target[0]-(rect.width()/2.0f),target[1]-rect.height()-bubbleHeight);
        //canvas.drawRoundRect(targetRect,);

        canvas.drawRoundRect(targetRect,2*StatusLayer.MARGIN,2*StatusLayer.MARGIN,paint);
        targetRect = new RectF(rect.left+StatusLayer.MARGIN,rect.top+StatusLayer.MARGIN,rect.right-StatusLayer.MARGIN,rect.bottom-StatusLayer.MARGIN);
        targetRect.offset(target[0]-(targetRect.width()/2.0f)-StatusLayer.MARGIN,target[1]-targetRect.height()-bubbleHeight-2*StatusLayer.MARGIN);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(targetRect,StatusLayer.MARGIN,StatusLayer.MARGIN,paint);

    }

    public PopupLayer setLocation(PointF pointF){
        this.location = pointF;
        return this;
    }

    public PopupLayer setIsShow(boolean b) {
        this.isShow = b;
        return this;
    }
}
