package hust.com.jsp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hust.com.jsp.activity.Main2Activity;
import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.FXPlan;
import hust.com.jsp.bean.FXPlanItem;
import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Station;
import hust.com.jsp.utils.CalendarConvert;

/**
 * Created by hust on 2017/1/22.
 */

public class FXPlanView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Canvas canvas;
    private int displayType=0;
    private int numOfDisHour=10;
    private int numOfDisRow=13;
    private long startViewTime;
    private long endViewTime;
    private int motionEvent=0;
    private float width;
    private float height;
    private float sepLeftTitle;
    private float sepColNorm;
    private float sepTopTitle;
    private float sepRowNorm;
    private float sepColDivide;
    private float sepPerTime;
    private int move=0;
    private float pointY;
    private float[] oldPoint={0,0};
    public float getPointY() {
        return pointY;
    }
    public int getMotionEvent(){return motionEvent;}
    private Map<Integer,FXPlanItem> itemMap=new TreeMap<>();
    public  Map<Integer, FXPlanItem> getItemMap() {
        return itemMap;
    }
    public float getSepRowNorm() {
        return sepRowNorm;
    }
    private FXPlanItem testItem;
    private List<BCInfo> bcInfoList=new ArrayList<>();
    private List<BCLineLayer> bcLayerList=new ArrayList<>();
    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Map<String, List<String>> getDateMap() {
        return dateMap;
    }

    Map<String,List<String>> dateMap=new TreeMap<>();
    private String selectedDate=new String();

    private List<FXPlanItem> itemList;

    public FXPlanView(Context context) {
        this(context, null);
    }

    public FXPlanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FXPlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        itemList = new ArrayList<>();
    }

    public void reMapSize(Canvas canvas){
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.sepLeftTitle = width/10.0f;//第一列宽
        this.sepColNorm = (width-sepLeftTitle)/numOfDisHour;//列宽：小时刻度
        this.sepColDivide = sepColNorm /4.0f;//列中分钟刻度
        this.sepTopTitle = height/numOfDisRow;//行高
        this.sepRowNorm = this.sepTopTitle;
        this.sepPerTime = sepColNorm/(3600000);//单位时间下的长度分辨率
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("TEST", "cavas created");
        this.holder = holder;
        updateDateTime();
    }

    public void updateDateTime() {
        Calendar timer = Calendar.getInstance();
        if (selectedDate==null || selectedDate.equals("") || dateMap.get(selectedDate).size()<2)
            return ;
        String[] d=dateMap.get(selectedDate).get(dateMap.get(selectedDate).size()-2).split("-");
        int year=Integer.parseInt(d[0]);
        int month=Integer.parseInt(d[1]);
        int day=Integer.parseInt(d[2]);
        int hour=Integer.parseInt(d[3]);
        timer.set(year,month-1,day,hour,0,0);
        this.startViewTime = timer.getTimeInMillis();
        timer.add(Calendar.HOUR_OF_DAY,numOfDisHour);
        this.endViewTime = timer.getTimeInMillis();
        refresh();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void refresh() {
        if (holder != null) {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                Log.i("TEST","Draw cavas");
                canvas.drawColor(Color.WHITE);
                reMapSize(canvas);
                drawGrid();
                int i = 0;
                itemMap.clear();//清空列表
                //listDateItems.clear();
                for (FXPlanItem item : itemList) {
                   if(selectedDate(item,selectedDate)) {
                        drawFXPlan(item, i++);
                       itemMap.put(i+move,item);//重新按顺序加载
                //        addDateItems(item, listDateItems);
                    }
                }
                for(BCLineLayer layer : bcLayerList){
                    drawBCLine(layer);
                }
                //drawFXPlan(testItem,0);
                //drawFXPlan(testItem,1);
                holder.unlockCanvasAndPost(canvas);
            }
            else Log.i("TEST","No cavas");
        }
    }

    public FXPlanView addItem(FXPlanItem item){
        this.itemList.add(item);
        return this;
    }
    public void removeItem(FXPlanItem item){
        itemList.remove(item);
    }
    public void scaleView(int mutli){
        numOfDisRow+=mutli;
        numOfDisHour+=mutli;
        refresh();
    }
    public FXPlanView rollTime(int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.startViewTime);
        calendar.roll(Calendar.HOUR_OF_DAY,hours);
        this.startViewTime = calendar.getTimeInMillis();
        Log.v("start hour",calendar.get(Calendar.HOUR_OF_DAY)+"");
        calendar.add(Calendar.HOUR_OF_DAY,numOfDisHour);
        this.endViewTime = calendar.getTimeInMillis();
        Log.v("end hour",calendar.get(Calendar.HOUR_OF_DAY)+"");
        refresh();
        return this;
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

        for(;sepr<width-0.01f;sepr+=this.sepColNorm){//画小时格线
            canvas.drawLine(sepr,0.0f,sepr,height,paint);
            if(hour>23) canvas.drawText(hour-24+":00",sepr+sepColNorm/2.0f,this.sepTopTitle/4.0f+this.sepTopTitle/8.0f,paint);
            else
                canvas.drawText(""+hour+":00",sepr+sepColNorm/2.0f,this.sepTopTitle/4.0f+this.sepTopTitle/8.0f,paint);
            hour++;

            paint.setColor(Color.LTGRAY);
            float sepDr = sepr+this.sepColDivide;


            int min = 0;
            String o ;
            for(;sepDr<(sepr+this.sepColNorm-0.01f);sepDr+=this.sepColDivide){//画分钟线
                paint.setColor(Color.LTGRAY);
                canvas.drawLine(sepDr,this.sepTopTitle/2.0f,sepDr,height,paint);
                paint.setColor(Color.BLACK);

                if(min==0){
                    o="00";
                }
                else{
                    o = ""+min;
                }
                canvas.drawText(o,sepDr-this.sepColDivide/2.0f,this.sepTopTitle*0.85f,paint);
                min+=15;
            }
            o = ""+min;
            canvas.drawText(o,sepDr-this.sepColDivide/2.0f,this.sepTopTitle*0.85f,paint);
        }
        canvas.drawLine(this.sepLeftTitle,this.sepTopTitle/2.0f,width,this.sepTopTitle/2.0f,paint);

        float sepddr = this.sepTopTitle;

        for(;sepddr<height;sepddr+=this.sepTopTitle) {//画行格线
            canvas.drawLine(0.0f,sepddr,width,sepddr,paint);
        }
    }
    private void drawBCLine(BCLineLayer layer){
        BCInfo info=layer.getBcInfo();
        if(info.getLongStartTime()>this.endViewTime) return;
        if(info.getLongEndTime()<this.startViewTime) return;
        float x1 = this.sepPerTime*(info.getLongStartTime()-this.startViewTime)+this.sepLeftTitle;
        float x2=this.sepPerTime*(info.getLongEndTime()-this.startViewTime)+this.sepLeftTitle;
        layer.draw(canvas,x1,x2,this.sepTopTitle,this.height);
    }
    private void drawFXPlan(FXPlanItem fxPlanItem,int yOffset){
        //  long viewExpand = this.endViewTime-this.startViewTime;
        Paint paint = new Paint();
        paint.setStrokeWidth(4.0f);
        paint.setColor(Color.BLACK);
        paint.setTextSize(sepRowNorm/4.0f);
        if(yOffset+move<0 ){
            return;
        };
        long itemLeftExpand = fxPlanItem.getStartTime()-this.startViewTime;
        long itemSelfExpand = fxPlanItem.getEndTime()-fxPlanItem.getStartTime();
        Log.i("itemSelfExpand", ""+itemSelfExpand);

        float relativeX = this.sepPerTime*(itemLeftExpand)+this.sepLeftTitle;
        float itemHeight = this.sepRowNorm/3.0f*2.0f;
        float relativeY = this.sepTopTitle+this.sepRowNorm*(yOffset+move);
        float itemExpand = this.sepPerTime*itemSelfExpand;

        float leng3 = paint.measureText(fxPlanItem.getJzj().getDisplayName()+"  "+fxPlanItem.getJzj().getJzjType());//画JZJ编号
        canvas.drawText(fxPlanItem.getJzj().getDisplayName()+"  "+fxPlanItem.getJzj().getJzjType(),(this.sepLeftTitle-leng3)/3.0f,relativeY+this.sepRowNorm*0.5f+sepRowNorm/8.0f,paint);


        if(fxPlanItem.getEndTime()<(this.startViewTime))
            return;
        if(fxPlanItem.getStartTime()>(this.endViewTime))
                return;

        // fxPlanItem.getStartTime();
        if(displayType==0){
            canvas.drawLine(relativeX,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand,relativeY+this.sepRowNorm/2.0f,paint);//画起飞到降落时间段横线
            canvas.drawLine(relativeX+itemHeight/2.0f,relativeY+(sepRowNorm-itemHeight)/2.0f,relativeX+itemHeight/2.0f,relativeY+(sepRowNorm+itemHeight)/2.0f,paint);//画竖分隔线


            float leng1 = paint.measureText(fxPlanItem.getPlanName());//画任务编号
            canvas.drawText(fxPlanItem.getPlanName(),relativeX+itemHeight/2.0f-leng1-2.0f,relativeY+sepRowNorm*0.625f-itemHeight/4.0f,paint);
            float leng2 = paint.measureText(fxPlanItem.getStation().getDisplayName());//画FXY代号
            canvas.drawText(fxPlanItem.getStation().getDisplayName(),relativeX+itemHeight/2.0f-leng2-2.0f,relativeY+sepRowNorm*0.625f+itemHeight/4.0f,paint);
            //float leng3 = paint.measureText(testItem.getGasStatus());
            canvas.drawText(fxPlanItem.getGasStatus(),relativeX+itemHeight/2.0f+2.0f,relativeY+sepRowNorm*0.625f+itemHeight/4.0f,paint);//画载油量

            Calendar timer = Calendar.getInstance();
            timer.setTimeInMillis(fxPlanItem.getStartTime());
            String qfTime=timer.get(Calendar.HOUR_OF_DAY)+":"+timer.get(Calendar.MINUTE);
            timer.setTimeInMillis(fxPlanItem.getEndTime());
            String jlTime=timer.get(Calendar.HOUR_OF_DAY)+":"+timer.get(Calendar.MINUTE);
            canvas.drawText(qfTime+"-"+jlTime,relativeX+itemHeight/2.0f+2.0f,relativeY+sepRowNorm*0.625f-itemHeight/4.0f,paint);//画QF-JL时间

            if(fxPlanItem.getType()== FXPlanItem.FXPlanType.land){//在本舰JL
                canvas.drawLine(relativeX+itemExpand,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand+itemHeight/2.0f,relativeY+(sepRowNorm+itemHeight/2)/2.0f,paint);
            }
            else if(fxPlanItem.getType()== FXPlanItem.FXPlanType.flight){//QF
                canvas.drawLine(relativeX+itemExpand,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand+itemHeight/2.0f,relativeY+(sepRowNorm-itemHeight/2)/2.0f,paint);
            }
            else if(fxPlanItem.getType()== FXPlanItem.FXPlanType.both){//两者
                canvas.drawLine(relativeX+itemExpand,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand+itemHeight/2.0f,relativeY+(sepRowNorm+itemHeight/2)/2.0f,paint);
                canvas.drawLine(relativeX+itemExpand,relativeY+this.sepRowNorm/2.0f,relativeX+itemExpand+itemHeight/2.0f,relativeY+(sepRowNorm-itemHeight/2)/2.0f,paint);
            }

        }
        else{
            float time1=(fxPlanItem.getChTime()-fxPlanItem.getStartTime())*sepPerTime;
            float time2=(fxPlanItem.getFhTime()-fxPlanItem.getChTime())*sepPerTime;
            float time3=(fxPlanItem.getEndTime()-fxPlanItem.getFhTime())*sepPerTime;
            canvas.drawLine(relativeX,relativeY+this.sepRowNorm/6*5,relativeX+time1,relativeY+this.sepRowNorm/6*1,paint);
            canvas.drawLine(relativeX+time1,relativeY+this.sepRowNorm/6*1,relativeX+time1+time2,relativeY+this.sepRowNorm/6*1,paint);
            canvas.drawLine(relativeX+time1+time2,relativeY+this.sepRowNorm/6*1,relativeX+time1+time2+time3,relativeY+this.sepRowNorm/6*5,paint);

        }
        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);//清除覆盖编号区域，重新画任务编号
        canvas.drawRect(0.0f,relativeY+1.0f,this.sepLeftTitle-1.0f,relativeY+sepRowNorm-1.0f,paint1);
        canvas.drawText(fxPlanItem.getJzj().getDisplayName()+"  "+fxPlanItem.getJzj().getJzjType(),(this.sepLeftTitle-leng3)/2.0f,relativeY+this.sepRowNorm*0.5f+sepRowNorm/8.0f,paint);


    }
    private int mScrolling=0;
    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldPoint[0]=event.getX();
                oldPoint[1]=event.getY();
                pointY=event.getY();
                motionEvent=MotionEvent.ACTION_DOWN;
                Log.v("fx","press");
                mScrolling=0;
                break;
            case MotionEvent.ACTION_MOVE:
                int num=numOfFXItem(itemList,selectedDate);
                if(num>numOfDisRow-1){
                    motionEvent=MotionEvent.ACTION_MOVE;
                    if(event.getY()-oldPoint[1]>sepRowNorm){
                        move++;
                        oldPoint[1]=event.getY();
                    }
                    else if(event.getY()-oldPoint[1]<-sepRowNorm){
                        move--;
                        oldPoint[1]=event.getY();
                    }
                    if(move+num<numOfDisRow-1){
                        move=numOfDisRow-1-num;
                    }
                    else if(move>0){
                        move=0;
                    }
                    refresh();
                }
                if(event.getX()-oldPoint[0]>sepColNorm){
                    rollTime(-1);
                    oldPoint[0]=event.getX();
                }
                else if(event.getX()-oldPoint[0]<-sepColNorm){
                    rollTime(1);
                    oldPoint[0]=event.getX();
                }
                mScrolling=1;
                Log.v("fx","up"+String.valueOf(move));
                break;
            case MotionEvent.ACTION_UP:
                for(BCLineLayer layer:bcLayerList){
                    layer.onTouch(event);
                }
                mScrolling=(mScrolling==0)?3:4;
        }
        Log.v("scroll",mScrolling+"");
        if(mScrolling!=4){

            return super.onTouchEvent(event);
        }
        else{
            return true;
        }
    }

    //根据筛选的日期显示相应计划
    private boolean selectedDate(FXPlanItem item,String date){
        boolean result=true;
        if (date==null || date.equals(""))
            return false;
        String[] d=date.split("-");
        int year=Integer.parseInt(d[0]);
        int month=Integer.parseInt(d[1]);
        int day=Integer.parseInt(d[2]);
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month-1,day,0,0,0);
        long timeZero=calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY,24);
        long time24=calendar.getTimeInMillis();
        if(item.getEndTime()<timeZero || item.getStartTime()>time24)
            result=false;
        return  result;
    }
    private int numOfFXItem(List<FXPlanItem> fxPlanList,String date){
        int num=0;
        for (FXPlanItem item : fxPlanList) {
            if(selectedDate(item,selectedDate)) {
                num++;
            }
        }
        return num;
    }
    public void setDisplayType(int type){
        displayType=type;
        refresh();
    }

    public List<BCInfo> getBcInfoList() {
        return bcInfoList;
    }

    public void setBcInfoList(List<BCInfo> bcInfoList) {
        this.bcInfoList = bcInfoList;
        for(BCInfo info:bcInfoList){
            BCLineLayer layer=new BCLineLayer();
            layer.setBcInfo(info);
            bcLayerList.add(layer);
        }
    }
    public void addBCInfo(BCInfo info){
        this.bcInfoList.add(info);
        BCLineLayer layer=new BCLineLayer();
        layer.setBcInfo(info);
        bcLayerList.add(layer);
    }
}
