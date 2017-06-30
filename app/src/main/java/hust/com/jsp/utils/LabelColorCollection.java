package hust.com.jsp.utils;

import android.graphics.Color;

/**
 * Created by Michael-Lee on 2017/6/29.
 */

public class LabelColorCollection {
    public static int[] getColors() {
        return colors;
    }

    public  static int getColor(int i){
        if(i<0 ||i>=colors.length)
            return colors[8];
        return colors[i];
    }

    private  static int colors[]=new int[]{
        Color.parseColor("#AEEEEE"),
                Color.parseColor("#B4EEB4"),
                Color.parseColor("#FFB90F"),
                Color.parseColor("#C6E2FF"),
                Color.parseColor("#87CEFF"),
                Color.parseColor("#48D1CC"),
                Color.parseColor("#00FF00"),
                Color.parseColor("#FFBBFF"),
                Color.LTGRAY
    };
}
