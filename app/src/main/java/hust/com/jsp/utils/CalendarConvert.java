package hust.com.jsp.utils;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by lm on 2017/6/24.
 */

public class CalendarConvert {
    private static  Calendar timer = Calendar.getInstance();
    public static long toLong(Calendar calendar){
        return calendar.getTimeInMillis();
    }
    public static long toLong(DatePicker date, TimePicker time){
        timer.set(date.getYear(),date.getMonth(),date.getDayOfMonth(),time.getHour(),time.getMinute(),0);
        Log.v("CalendarConvert",String.valueOf(timer.getTimeInMillis()));
        return timer.getTimeInMillis();
    }
}
