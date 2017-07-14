package hust.com.jsp.bean;

import android.graphics.PointF;

import java.util.List;

/**
 * Created by hust on 2017/1/13.
 */

public class BZPlan {

    private int bcid;
    private JZJ jzj;
    private List<BZPlanItem> bzPlanItemList;
    private Station station;//BL的FJ初始ZW
    private long flightTime;//QF时间

    public int getBcid() {
        return bcid;
    }

    public void setBcid(int bcid) {
        this.bcid = bcid;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public long getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(long flightTime) {
        this.flightTime = flightTime;
    }



    public JZJ getJzj() {
        return jzj;
    }

    public void setJzj(JZJ jzj) {
        this.jzj = jzj;
    }

    public List<BZPlanItem> getBzPlanItemList() {
        return bzPlanItemList;
    }

    public void setBzPlanItemList(List<BZPlanItem> bzPlanItemList) {
        this.bzPlanItemList = bzPlanItemList;
    }

    public void addBZPlanItem(BZPlanItem item){
        item.setBcid(bcid);
        this.bzPlanItemList.add(item);
    }

    public void removeBZPlanItem(BZPlanItem item){
        this.bzPlanItemList.remove(item);
    }
}
