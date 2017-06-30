package hust.com.jsp.bean;

import android.graphics.PointF;

/**
 * Created by GEAR on 2017/6/28.
 */

public class BLInfo {
    PointF point=new PointF();
    int bcid;
    int jzjid;


    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        float x=point.x;
        float y=point.y;
        this.point.x=x;
        this.point.y=y;
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
}
