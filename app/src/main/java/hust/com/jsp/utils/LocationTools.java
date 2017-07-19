package hust.com.jsp.utils;

import android.graphics.PointF;

import java.util.List;

import hust.com.jsp.bean.Location;

/**
 * Created by GEAR on 2017/7/5.
 */

public class LocationTools {
    public static Location getNearLoaction(PointF pointF,List<Location> list) {
        float[] floats={pointF.x,pointF.y};
        return  getNearLoaction(floats,list);
    }
    public static Location getNearLoaction(float x,float y,List<Location> list) {
        float[] floats={x,y};
        return  getNearLoaction(floats,list);
    }

    public static Location getNearLoaction(float[] floats,List<Location> list){
        Location location;
        double[] distance=new double[list.size()];
        int min_i=0;
        double min;
        for(int i=0;i<list.size();i++){
            Location info=list.get(i);
            distance[i]=Math.sqrt(Math.pow(info.getX()-floats[0],2)+Math.pow(info.getY()-floats[1],2));
        }
        min=distance[0];
        for(int i=0;i<distance.length;i++){
            if(distance[i]<min){
                min=distance[i];
                min_i=i;
            }
        }
        return list.get(min_i);
    }
    public static double getDistance(PointF loc1,PointF loc2){
        double distance=Math.sqrt(Math.pow(loc1.x-loc2.x,2)+Math.pow(loc1.y-loc2.y,2));
        return distance;
    }
    public static double getDistance(Location loc1,Location loc2){
        double distance=Math.sqrt(Math.pow(loc1.getX()-loc2.getX(),2)+Math.pow(loc1.getY()-loc2.getY(),2));
        return distance;
    }
}
