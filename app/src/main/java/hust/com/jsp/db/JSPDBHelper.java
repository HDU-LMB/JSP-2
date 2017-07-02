package hust.com.jsp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class JSPDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="JSP_DB.db";
    private static final int VERSION=1;
    private static final String CREATE_LOCATION="create table location(" +
            "id integer primary key autoincrement," +
            "name text," +
            "x real," +
            "y real," +
            "angle real)";
    public JSPDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION);
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A1","280","180","-200"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A2","480","180","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A3","680","180","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A4","880","180","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A5","980","180","-160"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B1","280","430","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B2","480","430","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B3","680","430","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B4","880","430","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B5","1080","430","0"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
