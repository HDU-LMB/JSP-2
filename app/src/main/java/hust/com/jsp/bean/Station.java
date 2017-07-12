package hust.com.jsp.bean;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by hust on 2017/1/13.
 */

public class Station {
    private int id;
    private String displayName;
    private PointF location;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private float angle;

    public Station(){}

    public Station(int id,PointF loc,String displayName){
        this.id=id;
        this.location=loc;
        this.displayName=displayName;
    }
    public Station setID(int id){
        this.id = id;
        return this;
    }

    public Station setDisplayName(String name){
        this.displayName = name;
        return this;
    }

    public String getDisplayName(){
        return  this.displayName;
    }

    public Station setLocation(PointF loc){
        this.location = loc;
        return this;
    }

}
