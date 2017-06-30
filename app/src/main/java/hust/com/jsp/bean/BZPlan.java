package hust.com.jsp.bean;

import java.util.List;

/**
 * Created by hust on 2017/1/13.
 */

public class BZPlan {
    private JZJ jzj;
    private List<BZPlanItem> bzPlanItemList;

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
        this.bzPlanItemList.add(item);
    }

    public void removeBZPlanItem(BZPlanItem item){
        this.bzPlanItemList.remove(item);
    }
}
