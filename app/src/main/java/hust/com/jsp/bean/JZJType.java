package hust.com.jsp.bean;

import android.graphics.Point;

import java.util.List;

/**
 * Created by hust on 2017/1/11.
 */

public class JZJType {
    private int typeID;
    private String displayName;
    private List<Point> outline;

    public boolean isEqualTo(JZJType jzjType){
        return this.typeID == jzjType.getTypeID();
    }

    public int getTypeID(){
        return this.typeID;
    }

}
