package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.bean.Location;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class LocationDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    public  LocationDAO(Context context){
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
    public List<Location> getAllLocation(){
        Cursor cursor=db.rawQuery("select * from location",null);
        List<Location> list=toList(cursor);
        return  list;

    }
    private List<Location> toList(Cursor cursor){
        List<Location> list=new ArrayList<>();
        while(cursor.moveToNext()){
            Location info=new Location();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setX(cursor.getFloat(cursor.getColumnIndex("x")));
            info.setY(cursor.getFloat(cursor.getColumnIndex("y")));
            info.setAngle(cursor.getFloat(cursor.getColumnIndex("angle")));
            list.add(info);
        }
        return list;
    }
}
