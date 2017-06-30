package hust.com.jsp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017/4/12.
 */

public class DYDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "JSRecord_db";
    private static final int VERSION = 2;
    public static final String BC_ITEM_TABLE = "BCinfoDetail";
    public static final String BC_TABLE = "BCinfo";

    private static DYDBHelper instance;

    public DYDBHelper(Context context) {
        super(context, DATABASE, null, VERSION);
        instance =this;
//        this.onCreate(this.getWritableDatabase());
    }

    public DYDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE, factory, VERSION);
        instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table "+TEMPLATE_TABLE+"(id integer primary key, name TEXT, sequence integer, definition TEXT)");
        //db.execSQL("create table "+V1_DATA_TABLE+"(id integer primary key, tableID integer, sequence integer, data TEXT)");
        db.execSQL("create table "+ BC_ITEM_TABLE +" (id integer primary key, infoID integer , jzjid integer, actionlist TEXT, name TEXT, time integer,bctype TEXT)");
        db.execSQL("create table "+ BC_TABLE +" (id integer primary key, jzjlist TEXT, name TEXT, time integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + BC_ITEM_TABLE);
        db.execSQL("drop table " + BC_TABLE);
        db.execSQL("create table "+ BC_ITEM_TABLE +" (id integer primary key, infoID integer , jzjid integer, actionlist TEXT, name TEXT, time integer,bctype TEXT)");
        db.execSQL("create table "+ BC_TABLE +" (id integer primary key, jzjlist TEXT, name TEXT, time integer)");

    }

    public static DYDBHelper getInstance() {
        return instance;
    }
}
