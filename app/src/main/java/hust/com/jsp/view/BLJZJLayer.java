package hust.com.jsp.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;

/**
 * Created by hust on 2016/12/13.
 */

public class BLJZJLayer extends MapBaseLayer {

    private PointF location;
    private Bitmap bitmap;
    private BitmapLayer baseLayer;
    private JZJ jzj;
    private int jzjID;
    private final double radius = 30.0;
    private MapView mapView;
    private BitmapLayer.OnBitmapClickListener onBitmapClickListener;
    private float progress=Float.MAX_VALUE;
    public BLJZJLayer(MapView mapView, Resources res, final JZJ jzj){
        super(mapView);
        this.mapView=mapView;
        this.jzj=jzj;
        switch (jzj.getJzjType()){
            case 1:
                this.bitmap = BitmapFactory.decodeResource(res, R.drawable.jzj);
                break;
            case 2:
                this.bitmap = BitmapFactory.decodeResource(res, R.drawable.jzj2);
                break;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.1f);
        Bitmap dstbmp = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(),
                matrix, true);
        this.baseLayer = new BitmapLayer(mapView,dstbmp);
        //  baseLayer.setAutoScale(true);
    }
    public void setAngle(float angle){
        Matrix matrix = new Matrix();
        matrix.setRotate(angle, this.bitmap.getWidth() / 2, this.bitmap.getHeight() / 2);
        matrix.postScale(0.1f, 0.1f);
        Bitmap dstbmp = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(),
                matrix, true);
        this.baseLayer.setBitmap(dstbmp);
    }
    public void setLocation(PointF loc)
    {
/*        float[] point= mapView.convertScreenXY2MapXY(loc.x,loc.y);
        PointF pointF=new PointF(point[0],point[1]);
        this.location = pointF;*/
        this.location=loc;
        this.baseLayer.setLocation(loc);
    }

    @Override
    public void onTouch(MotionEvent event) {
        this.baseLayer.onTouch(event);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {

        baseLayer.draw(canvas,currentMatrix,currentZoom,currentRotateDegrees);
        if(isVisible){
            canvas.save();
            canvas.setMatrix(currentMatrix);
            drawTextWithRect(canvas,jzj.getDisplayName(),location.x,location.y-5);
            if(progress<=1){
                drawTextWithRect(canvas,String.format("%.2f",progress),location.x,location.y+10);
            }
            else if(progress==2){
                drawTextWithRect(canvas,"等待中",location.x,location.y+10);
            }
            else if(progress==4){
                drawTextWithRect(canvas,"转运中",location.x,location.y+10);
            }
            else if(progress==5){
                drawTextWithRect(canvas,"初始",location.x,location.y+10);
            }
            canvas.restore();
        }
    }
    private void drawTextWithRect(Canvas canvas,String str,float x,float y){
        Paint paint=new Paint();
/*        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x-25,y-10,x+15,y+2,paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x-25,y-10,x+15,y+2,paint);*/
        paint.setTextSize(10);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(str,x-20,y,paint);
    }

    public void setOnBitmapClickListener(BitmapLayer.OnBitmapClickListener onBitmapClickListener) {
        this.baseLayer.setOnBitmapClickListener(onBitmapClickListener);
    }

    public JZJ getJzj() {
        return jzj;
    }

    public void setJzj(JZJ jzj) {
        this.jzj = jzj;
    }

    public int getJzjID() {
        return jzjID;
    }

    public void setJzjID(int jzjID) {
        this.jzjID = jzjID;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
