package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class BCDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    public  BCDAO(Context context){
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
    public BCInfo getById(int id){
        return  toInfo(db.rawQuery("select * from bcinfo where id=?",new String[]{String.valueOf(id)}));

    }
    public List<BCInfo> getAll(){
        return  toList(db.rawQuery("select * from bcinfo",null));
    }
    public void addInfo(BCInfo info){
        db.execSQL("insert into bcinfo(id,name,starttime,endtime) values(?,?,?,?)",
                new String[]{
                        String.valueOf(info.getId()),
                        String.valueOf(info.getName()),
                        String.valueOf(info.getLongStartTime()),
                        String.valueOf(info.getLongEndTime())
                });
    }
    public void deleteBC(int bcid){
        db.execSQL("delete from bcinfo where id=?",new String[]{String.valueOf(bcid)});
    }
    private BCInfo toInfo(Cursor cursor){
        BCInfo info=new BCInfo();
        while(cursor.moveToNext()) {
            info.setID(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setStartTime(cursor.getLong(cursor.getColumnIndex("starttime")));
            info.setEndTime(cursor.getLong(cursor.getColumnIndex("endtime")));
        }
        return info;
    }
    private List<BCInfo> toList(Cursor cursor){
        List<BCInfo> list=new ArrayList<>();
        while(cursor.moveToNext()){
            BCInfo info=new BCInfo();
            info.setID(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setStartTime(cursor.getLong(cursor.getColumnIndex("starttime")));
            info.setEndTime(cursor.getLong(cursor.getColumnIndex("endtime")));
            list.add(info);
        }
        return list;
    }
}
