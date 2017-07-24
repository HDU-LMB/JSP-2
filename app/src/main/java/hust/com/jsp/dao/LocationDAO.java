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
    public void addInfo(Location info){
        db.execSQL("insert into location(id,name,x,y,angle) values(?,?,?,?,?)",
                new String[]{
                        String.valueOf(info.getId()),
                        String.valueOf(info.getName()),
                        String.valueOf(info.getX()),
                        String.valueOf(info.getY()),
                        String.valueOf(info.getAngle())
                });
    }
    private void deleteInfo(int id){
        db.execSQL("delete from location where id=?",new String[]{String.valueOf(id)});
    }
    private void updateInfo(Location info){
        db.execSQL("update location set name =?,x=?,y=?,angle=? where id=?",
                new String[]{
                        String.valueOf(info.getName()),
                        String.valueOf(info.getX()),
                        String.valueOf(info.getY()),
                        String.valueOf(info.getAngle()),
                        String.valueOf(info.getId())});
    }
    public Location getInfoByName(String name){
        Cursor cursor=db.rawQuery("select * from location where name=?",new String[]{String.valueOf(name)});
        Location info=toInfo(cursor);
        return  info;
    }
    public Location getInfoByPonit(float x,float y){
        Cursor cursor=db.rawQuery("select * from location where x=? and y=?",new String[]{String.valueOf(x),String.valueOf(y)});
        Location info=toInfo(cursor);
        return  info;
    }
    private Location toInfo(Cursor cursor){
        Location info=new Location();
        while(cursor.moveToNext()) {
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setX(cursor.getFloat(cursor.getColumnIndex("x")));
            info.setY(cursor.getFloat(cursor.getColumnIndex("y")));
            info.setAngle(cursor.getFloat(cursor.getColumnIndex("angle")));
        }
        return info;
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
