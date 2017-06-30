package hust.com.jsp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by hust on 2017/2/14.
 */

public class RecordView  extends SurfaceView implements SurfaceHolder.Callback  {
    private SurfaceHolder holder;

    private float width;
    private float height;
    private float sepLeftTitle;
    private float sepColNorm;
    private float sepTopTitle;
    private float sepRowNorm;

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.holder = surfaceHolder;

    }

    public void refresh(){
        Canvas canvas = holder.lockCanvas();

    }

    public void reSizeMap(Canvas canvas){
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.sepLeftTitle = width/11.0f;
        this.sepColNorm = sepLeftTitle*2.0f;
        this.sepTopTitle = height/7.0f;
        this.sepRowNorm = this.sepTopTitle;
    }

    public void drawGrid(Canvas canvas){

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
