package hust.com.jsp.bean;

import android.graphics.Point;

/**
 * Created by hust on 2017/1/13.
 */

public class Station {
    private int id;
    private String displayName;
    private Point location;

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

    public Station setLocation(Point loc){
        this.location = loc;
        return this;
    }
}
