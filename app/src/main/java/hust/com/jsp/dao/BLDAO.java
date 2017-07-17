package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import hust.com.jsp.activity.MainActivity;
import hust.com.jsp.bean.BLInfo;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.bean.Station;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class BLDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    public  BLDAO(Context context){
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
    public List<BLInfo> getById(int bcid){
        return  toList(db.rawQuery("select * from blinfo where bcid=?",new String[]{String.valueOf(bcid)}));

    }
    public BLInfo getInfo(int bcid,int jzjid){
        return  toInfo(db.rawQuery("select * from blinfo where bcid = ? and jzjid=?",new String[]{String.valueOf(bcid
        ),String.valueOf(jzjid)}));
    }
    public void addInfo(BLInfo info){
        db.execSQL("insert into blinfo(bcid,jzjid,x,y,type) values(?,?,?,?,?)",
                new String[]{
                        String.valueOf(info.getBcid()),
                        String.valueOf(info.getJzjid()),
                        String.valueOf(info.getX()),
                        String.valueOf(info.getY()),
                        String.valueOf(info.getType())
        });
    }
    public void deleteByBCID(int bcid){
        db.execSQL("delete from blinfo where bcid=?",new String[]{String.valueOf(bcid)});
    }
    private BLInfo toInfo(Cursor cursor){
        BLInfo info=new BLInfo();
        while(cursor.moveToNext()) {
            info.setJzjid(cursor.getInt(cursor.getColumnIndex("jzjid")));
            info.setBcid(cursor.getInt(cursor.getColumnIndex("bcid")));
            info.setPoint(cursor.getFloat(cursor.getColumnIndex("x")), cursor.getFloat(cursor.getColumnIndex("y")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
        }
        return info;
    }
    private List<BLInfo> toList(Cursor cursor){
        List<BLInfo> list=new ArrayList<>();
        while(cursor.moveToNext()){
            BLInfo info=new BLInfo();
            info.setJzjid(cursor.getInt(cursor.getColumnIndex("jzjid")));
            info.setBcid(cursor.getInt(cursor.getColumnIndex("bcid")));
            info.setPoint(cursor.getFloat(cursor.getColumnIndex("x")),cursor.getFloat(cursor.getColumnIndex("y")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            list.add(info);
        }
        return list;
    }
}
