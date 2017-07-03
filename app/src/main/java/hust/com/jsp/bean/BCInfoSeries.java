package hust.com.jsp.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import hust.com.jsp.db.DYDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/12.
 */

public class BCInfoSeries {
    List<BCInfo> bcInfoList;

    public int Count() {
        return this.bcInfoList.size();
    }

    public BCInfo get(int position) {
        return bcInfoList.get(position);
    }

    public void writeIntoDB(){
        DYDBHelper db = DYDBHelper.getInstance();

    }
/*
    public void readFromDB(){
        DYDBHelper dbHelper = DYDBHelper.getInstance();
        this.bcInfoList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DYDBHelper.BC_TABLE,new String[]{"id","jzjlist","name","time"},"",new String[]{},"","","");
        while(cursor.moveToNext()){
            BCInfo bcInfo = new BCInfo();
            bcInfo.setID(cursor.getInt(0));
            bcInfo.setName(cursor.getString(2));
            bcInfo.setTime(cursor.getLong(3));
            bcInfo.readFromDB(db);
            bcInfoList.add(bcInfo);
        }
        db.close();
    }*/

    public void add(BCInfo bcInfo) {
        this.bcInfoList.add(bcInfo);
    }
}
