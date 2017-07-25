package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.bean.FXPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Station;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/25.
 */

public class FXPlanDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    private Context context;
    public  FXPlanDAO(Context context){
        this.context=context;
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
    public FXPlanItem getById(int id){
        return  toInfo(db.rawQuery("select * from fxplan where fxid=?",new String[]{String.valueOf(id)}));

    }
    public List<FXPlanItem> getByJZJId(int jzjid){
        return  toList(db.rawQuery("select * from fxplan where jzjid=?",new String[]{String.valueOf(jzjid)}));
    }
    public List<FXPlanItem> getBetweenTime(float x1,float x2){
        return  toList(db.rawQuery("select * from fxplan where starttime between ? and ?",new String[]{String.valueOf(x1),String.valueOf(x2)}));
    }
    public List<FXPlanItem> getAll(){
        return  toList(db.rawQuery("select * from fxplan",null));
    }
    public void addInfo(FXPlanItem info){
        db.execSQL("insert into fxplan(fxid,jzjid,planname,fxyname,starttime,endtime,chtime,fhtime,gas,type) values(?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        String.valueOf(info.getFx_id()),
                        String.valueOf(info.getJzj().getId()),
                        String.valueOf(info.getPlanName()),
                        String.valueOf(info.getStation().getDisplayName()),
                        String.valueOf(info.getStartTime()),
                        String.valueOf(info.getEndTime()),
                        String.valueOf(info.getChTime()),
                        String.valueOf(info.getFhTime()),
                        String.valueOf(info.getGas()),
                        String.valueOf(info.getIntType())
                });
    }
    public void deleteFXPlan(int fxid){
        db.execSQL("delete from fxplan where fxid=?",new String[]{String.valueOf(fxid)});
    }
    private FXPlanItem toInfo(Cursor cursor){
        if(cursor.getCount()==0){
            return null;
        }
        FXPlanItem info=new FXPlanItem();
        while(cursor.moveToNext()) {
            JZJDAO jzjdao=new JZJDAO(context);
            info.setFx_id(cursor.getInt(cursor.getColumnIndex("fxid")));
            info.setJzj(jzjdao.getJZJ(cursor.getInt(cursor.getColumnIndex("jzjid"))));
            info.setPlanName(cursor.getString(cursor.getColumnIndex("planname")));
            info.setStationName(cursor.getString(cursor.getColumnIndex("fxyname")));
            info.setStartTime(cursor.getLong(cursor.getColumnIndex("starttime")));
            info.setEndTime(cursor.getLong(cursor.getColumnIndex("endtime")));
            info.setChTime(cursor.getLong(cursor.getColumnIndex("chtime")));
            info.setFhTime(cursor.getLong(cursor.getColumnIndex("fhtime")));
            info.setGas(cursor.getFloat(cursor.getColumnIndex("gas")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
        }
        return info;
    }
    private List<FXPlanItem> toList(Cursor cursor){
        List<FXPlanItem> list=new ArrayList<>();
        while(cursor.moveToNext()){
            FXPlanItem info=new FXPlanItem();
            JZJDAO jzjdao=new JZJDAO(context);
            info.setFx_id(cursor.getInt(cursor.getColumnIndex("fxid")));
            info.setJzj(jzjdao.getJZJ(cursor.getInt(cursor.getColumnIndex("jzjid"))));
            info.setPlanName(cursor.getString(cursor.getColumnIndex("planname")));
            info.setStationName(cursor.getString(cursor.getColumnIndex("fxyname")));
            info.setStartTime(cursor.getLong(cursor.getColumnIndex("starttime")));
            info.setEndTime(cursor.getLong(cursor.getColumnIndex("endtime")));
            info.setChTime(cursor.getLong(cursor.getColumnIndex("chtime")));
            info.setFhTime(cursor.getLong(cursor.getColumnIndex("fhtime")));
            info.setGas(cursor.getFloat(cursor.getColumnIndex("gas")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            list.add(info);
        }
        return list;
    }
}