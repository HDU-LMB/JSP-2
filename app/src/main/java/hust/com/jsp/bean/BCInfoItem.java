package hust.com.jsp.bean;

import android.content.ContentValues;

import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.JZJAction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */

public class BCInfoItem {
    private int id;

    public JZJ getJzj() {
        return jzj;
    }

    private JZJ jzj;

    public List<JZJAction> getJzjActionList() {
        return jzjActionList;
    }

    public void setJzjActionList(List<JZJAction> jzjActionList) {
        this.jzjActionList = jzjActionList;
    }

    private List<JZJAction> jzjActionList;
    private String name;
    private Calendar time;



    public String getTypeName() {
        return typeName;
    }

    private String typeName;

    public  BCInfoItem(JZJ jzj){
        this.jzj=jzj;
        this.jzjActionList= new ArrayList<>();
    }

    public void addAction(JZJAction action){
        this.jzjActionList.add(action);
    }

    public ContentValues ValueForDB(){
        ContentValues ret = new ContentValues();
        ret.put("id",id);
        ret.put("jzjid",jzj.getId());

        StringBuilder sb = new StringBuilder();
        for (JZJAction action :
                jzjActionList) {
            sb.append(action.Type);
            sb.append(";");
        }
        ret.put("actionlist",sb.toString());
        ret.put("name",name);
        ret.put("time",time.getTimeInMillis());
        ret.put("bctype",typeName);
        return ret;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public String getActionListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (JZJAction jzjAction :
                jzjActionList) {
            sb.append(jzjAction.getName());
            sb.append(" ");
        }
        return sb.toString();
    }

    public void setID(int anInt) {
        this.id = anInt;
    }

    public void setJZJ(int anInt){
        this.jzj = JZJ.getJZJByID(anInt);
    }

    public void setName(String string){
        this.name = string;
    }

    public void setTime(long aLong) {
        this.time = Calendar.getInstance();
        this.time.setTimeInMillis(aLong);
    }

    public void setType(String string) {
        this.typeName = string;
    }
}
