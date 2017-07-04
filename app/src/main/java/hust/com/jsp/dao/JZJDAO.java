package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class JZJDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    public  JZJDAO(Context context){
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
    public List<JZJ> getAllJZJ(){
        return  toList(db.rawQuery("select * from jzj",null));

    }
    public JZJ getJZJ(int id){
        return  toInfo(db.rawQuery("select * from jzj where id = ?",new String[]{String.valueOf(id)}));
    }
    private JZJ toInfo(Cursor cursor){
        JZJ info=new JZJ();
        while(cursor.moveToNext()) {
            info.setID(cursor.getInt(cursor.getColumnIndex("id")));
            info.setDisplayName(cursor.getString(cursor.getColumnIndex("name")));
            info.setJzjType(cursor.getInt(cursor.getColumnIndex("type")));
        }
        return info;
    }
    private List<JZJ> toList(Cursor cursor){
        List<JZJ> list=new ArrayList<>();
        while(cursor.moveToNext()){
            JZJ info=new JZJ();
            info.setID(cursor.getInt(cursor.getColumnIndex("id")));
            info.setDisplayName(cursor.getString(cursor.getColumnIndex("name")));
            info.setJzjType(cursor.getInt(cursor.getColumnIndex("type")));
            list.add(info);
        }
        return list;
    }
}
