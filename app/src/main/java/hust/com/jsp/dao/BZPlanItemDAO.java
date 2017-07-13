package hust.com.jsp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.db.JSPDBHelper;

/**
 * Created by GEAR on 2017/7/13.
 */

public class BZPlanItemDAO {
    private JSPDBHelper jspdbHelper;
    private SQLiteDatabase db;
    public  BZPlanItemDAO(Context context){
        jspdbHelper=new JSPDBHelper(context);
        db=jspdbHelper.getReadableDatabase();
    }
/*    public BZPlanItem getById(int id){
        return  toInfo(db.rawQuery("select * from bzplanitem where id=?",new String[]{String.valueOf(id)}));

    }*/
    public List<BZPlanItem> getListByBCAndJZJ(int bcid,int jzjid){
        return  toList(db.rawQuery("select * from bzplanitem where bcid=? and jzjid=? order by indexno",new String[]{String.valueOf(bcid),String.valueOf(jzjid)}));
    }

    public void addInfo(int index,BZPlanItem info){
        db.execSQL("insert into bzplanitem(" +
                        "bcid," +
                        "jzjid," +
                        "locationid," +
                        "indexno," +
                        "spendtime," +
                        "gas," +
                        "air," +
                        "elec," +
                        "fluid," +
                        "weapon," +
                        "guide," +
                        "cool," +
                        "oxygen) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        String.valueOf(info.getBcid()),
                        String.valueOf(info.getJzjid()),
                        String.valueOf(info.getLocationid()),
                        String.valueOf(index),
                        String.valueOf(info.getSpendTime()),
                        String.valueOf((info.isAddGas()?1:0)),
                        String.valueOf((info.isAddAir()?1:0)),
                        String.valueOf((info.isAddElectricity()?1:0)),
                        String.valueOf((info.isAddFluid()?1:0)),
                        String.valueOf((info.isAddWeapon()?1:0)),
                        String.valueOf((info.isAddGuide()?1:0)),
                        String.valueOf((info.isAddCool()?1:0)),
                        String.valueOf((info.isAddOxygen()?1:0)),
                });
    }

    public void addList(List<BZPlanItem> list){
            for(int i=0;i<list.size();i++){
                addInfo(i,list.get(i));
            }
//            BZPlanItem item=new BZPlanItem();
//        item.setAddOxygen(true);
//        item.setBcid(1);
//        item.setAddGuide(false);
//        item.setLocationid(1);
//        item.setJzjid(2);
//        item.setSpendTime(10);
//        item.setIndex(1);
//        addInfo(1,item);
    }
    public void addBZPlanList(List<BZPlan> list){
        db.beginTransaction();
        try{
            deleteBZPlan(list.get(0).getBzPlanItemList().get(0).getBcid());
            for(BZPlan info:list){
                List<BZPlanItem> bzPlanItemList=info.getBzPlanItemList();
                addList(bzPlanItemList);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }
    public void deleteBZPlan(int bcid){
        db.execSQL("delete from bzplanitem where bcid=?",new String[]{String.valueOf(bcid)});
    }
    private BZPlanItem toInfo(Cursor cursor){
        BZPlanItem info=new BZPlanItem();
        while(cursor.moveToNext()) {
            info.setBcid(cursor.getInt(cursor.getColumnIndex("bcid")));
            info.setJzjid(cursor.getInt(cursor.getColumnIndex("jzjid")));
            info.setLocationid(cursor.getInt(cursor.getColumnIndex("locationid")));
            info.setIndex(cursor.getInt(cursor.getColumnIndex("indexno")));
            info.setSpendTime(cursor.getLong(cursor.getColumnIndex("spendtime")));
            info.setAddGas(cursor.getInt(cursor.getColumnIndex("gas"))==1);
            info.setAddAir(cursor.getInt(cursor.getColumnIndex("air"))==1);
            info.setAddElectricity(cursor.getInt(cursor.getColumnIndex("ele"))==1);
            info.setAddFluid(cursor.getInt(cursor.getColumnIndex("fluid"))==1);
            info.setAddWeapon(cursor.getInt(cursor.getColumnIndex("weapon"))==1);
            info.setAddGuide(cursor.getInt(cursor.getColumnIndex("guide"))==1);
            info.setAddCool(cursor.getInt(cursor.getColumnIndex("cool"))==1);
            info.setAddOxygen(cursor.getInt(cursor.getColumnIndex("oxygen"))==1);
        }
        return info;
    }
    private List<BZPlanItem> toList(Cursor cursor){
        List<BZPlanItem> list=new ArrayList<>();
        while(cursor.moveToNext()){
            BZPlanItem info=new BZPlanItem();
            info.setBcid(cursor.getInt(cursor.getColumnIndex("bcid")));
            info.setJzjid(cursor.getInt(cursor.getColumnIndex("jzjid")));
            info.setLocationid(cursor.getInt(cursor.getColumnIndex("locationid")));
            info.setIndex(cursor.getInt(cursor.getColumnIndex("indexno")));
            info.setSpendTime(cursor.getLong(cursor.getColumnIndex("spendtime")));
            info.setAddGas(cursor.getInt(cursor.getColumnIndex("gas"))==1);
            info.setAddAir(cursor.getInt(cursor.getColumnIndex("air"))==1);
            info.setAddElectricity(cursor.getInt(cursor.getColumnIndex("ele"))==1);
            info.setAddFluid(cursor.getInt(cursor.getColumnIndex("fluid"))==1);
            info.setAddWeapon(cursor.getInt(cursor.getColumnIndex("weapon"))==1);
            info.setAddGuide(cursor.getInt(cursor.getColumnIndex("guide"))==1);
            info.setAddCool(cursor.getInt(cursor.getColumnIndex("cool"))==1);
            info.setAddOxygen(cursor.getInt(cursor.getColumnIndex("oxygen"))==1);
            list.add(info);
        }
        return list;
    }
}
