package hust.com.jsp.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by hust on 2016/12/28.
 */

public class JZJStatus {

    public static final int STATUS_1 = 0x001;
    public static final int STATUS_2 = 0x002;
    public static final int STATUS_3 = 0x003;
    public static final int STATUS_4 = 0x004;
    public static final int STATUS_5 = 0x005;

    private static List<JZJStatusItem> statusList;
    private Map<Integer,Integer> statusValues;

    public  JZJStatus(){
        this.statusValues = new Hashtable<>();
        for (JZJStatusItem jzjStautsItem :
                statusList) {
            this.statusValues.put(jzjStautsItem.getId(), 1);
        }
        //this.setStatusValue(STATUS_1,0);
        //this.setStatusValue(STATUS_3,0);
    }

    public static void initialize(){
        statusList = new ArrayList<>();
        JZJStatusItem jzjStatusItem;
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_1).setName("A").setColor(Color.rgb(49,183,231));//BLUE
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_2).setName("B").setColor(Color.rgb(0,255,255));//WHITE
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_3).setName("C").setColor(Color.rgb(255,158,0));//RED
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_4).setName("D").setColor(Color.GREEN);
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_5).setName("E").setColor(Color.rgb(158,252,230));//LTGRAY
        statusList.add(jzjStatusItem);
    }

    public List<JZJStatusItem> getStatusList(){
        return statusList;
    }

    public int getStatusValue(int statusID){
        return this.statusValues.get(statusID);
    }

    public JZJStatus setStatusValue(int statusID,int statusValue){
        this.statusValues.put(statusID,statusValue);
        return this;
    }

    public int getStatusValue(JZJStatusItem statusItem){
        return this.statusValues.get(statusItem.getId());
    }
}
