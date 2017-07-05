package hust.com.jsp.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import hust.com.jsp.utils.LabelColorCollection;

/**
 * Created by hust on 2016/12/28.
 */

public class JZJStatus {

    public static final int STATUS_1 = 0x001;
    public static final int STATUS_2 = 0x002;
    public static final int STATUS_3 = 0x003;
    public static final int STATUS_4 = 0x004;
    public static final int STATUS_5 = 0x005;
    public static final int STATUS_6 = 0x006;
    public static final int STATUS_7 = 0x007;
    public static final int STATUS_8 = 0x008;

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
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_1).setName("油").setColor(LabelColorCollection.getColor(0));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_2).setName("气").setColor(LabelColorCollection.getColor(1));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_3).setName("电").setColor(LabelColorCollection.getColor(2));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_4).setName("液").setColor(LabelColorCollection.getColor(3));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_5).setName("弹").setColor(LabelColorCollection.getColor(4));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_6).setName("导").setColor(LabelColorCollection.getColor(5));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_7).setName("冷").setColor(LabelColorCollection.getColor(6));
        statusList.add(jzjStatusItem);
        jzjStatusItem = new JZJStatusItem(JZJStatus.STATUS_8).setName("氧").setColor(LabelColorCollection.getColor(7));
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
