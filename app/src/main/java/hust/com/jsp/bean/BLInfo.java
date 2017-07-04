package hust.com.jsp.bean;

import android.graphics.PointF;

/**
 * Created by GEAR on 2017/6/28.
 */

public class BLInfo {
    PointF point=new PointF();
    int bcid;
    int jzjid;
    int type;
    JZJ jzj;

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        float x=point.x;
        float y=point.y;
        this.point.x=x;
        this.point.y=y;
    }
    public float getX(){
        return this.point.x;
    }
    public float getY(){
        return this.point.y;
    }
    public void setPoint(float x,float y){
        this.point.x=x;
        this.point.y=y;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public JZJ getJZJ(){
        jzj=new JZJ(jzjid,"jzj1","beiyong",1);

        return jzj;
    }
    public void setJZJ(JZJ jzj){
        this.jzj=jzj;
    }
    public int getBcid() {
        return bcid;
    }

    public void setBcid(int bcid) {
        this.bcid = bcid;
    }

    public int getJzjid() {
        return jzjid;
    }

    public void setJzjid(int jzjid) {
        this.jzjid = jzjid;
    }
    public int typerCir(){
        if(this.type==3){
            this.type=0;
        }
        else{
            this.type++;
        }
        return this.type;
    }
}
