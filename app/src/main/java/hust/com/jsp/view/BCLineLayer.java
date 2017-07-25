package hust.com.jsp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import hust.com.jsp.bean.BCInfo;

/**
 * Created by lm on 2017/7/25.
 */

public class BCLineLayer {
    private BCInfo bcInfo;
    public boolean isVisible = true;
    private OnClickListener onClickListener;
    private float x1,x2,y1,y2;
    public void onTouch(MotionEvent event) {
        if(event.getX()>x1&&event.getX()<x2){
            Log.v("line","click");
        }
        else {
            Log.v("line","out");
        }
    }
    public void draw(Canvas canvas,float x1,float x2,float y1,float y2) {
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        Paint paint=new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawLine(x1,y1,x1,y2,paint);
        canvas.drawText(bcInfo.getName(),x1,y2,paint);
        paint.setColor(Color.RED);
        canvas.drawLine(x2,y1,x2,y2,paint);
        canvas.drawText(bcInfo.getName(),x2,y2,paint);
    }

    public BCInfo getBcInfo() {
        return bcInfo;
    }

    public void setBcInfo(BCInfo bcInfo) {
        this.bcInfo = bcInfo;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick();
    }
}
