package hust.com.jsp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GEAR on 2017/7/2.
 */

public class JSPDBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DB_NAME="JSP_DB.db";
    private static final int VERSION=1;

    public JSPDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        creatTable();
        addData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void creatTable(){
        String CREATE_LOCATION="create table location(" +
                "id integer primary key autoincrement," +
                "name text," +
                "x real," +
                "y real," +
                "angle real)";
       String CREATE_JZJ="create table jzj(" +
                "id integer primary key autoincrement," +
                "name text," +
                "type integer)";
        String CREATE_BLINFO="create table blinfo(" +
                "bcid integer," +
                "jzjid integer," +
                "x real," +
                "y real," +
                "type integer)";
        String CREATE_BCINFO="create table bcinfo(" +
                "id integer," +
                "name text," +
                "starttime real," +
                "endtime real)";
        db.execSQL(CREATE_LOCATION);
        db.execSQL(CREATE_JZJ);
        db.execSQL(CREATE_BLINFO);
        db.execSQL(CREATE_BCINFO);
    }
    private void addData(){
        //添加站位
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A1","160","60","-200"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A2","260","60","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A3","360","60","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A4","460","60","-180"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"A5","560","60","-160"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B1","160","220","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B2","275","220","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B3","390","220","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B4","500","220","0"});
        db.execSQL("insert into location(name,x,y,angle) values(?,?,?,?)",new String[]{"B5","600","220","0"});
        //添加JZJ
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-1","1"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-2","1"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-3","1"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-4","1"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-5","2"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-6","2"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-7","2"});
        db.execSQL("insert into jzj(name,type) values(?,?)",new String[]{"JZJ-8","2"});
        //添加bc
        db.execSQL("insert into bcinfo(id,name,starttime,endtime) values(?,?,?,?)",new String[]{"1","BC-1","1336226501635","1336226501635"});
        //添加bl
        db.execSQL("insert into blinfo(bcid,jzjid,x,y,type) values(?,?,?,?,?)",new String[]{"1","1","160","60","1"});
        db.execSQL("insert into blinfo(bcid,jzjid,x,y,type) values(?,?,?,?,?)",new String[]{"1","2","160","220","1"});

    }
}
