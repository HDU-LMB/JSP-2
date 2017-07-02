package hust.com.jsp.bean;

import android.graphics.PointF;

/**
 * Created by GEAR on 2017/7/2.
 */

public class Location {
    private int id;
    private String name;
    private float x;
    private float y;
    private float angle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    public PointF getPoint(){
        PointF pointF=new PointF();
        pointF.set(this.x,this.y);
        return pointF;
    }
}
