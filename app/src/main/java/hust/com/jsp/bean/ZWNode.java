package hust.com.jsp.bean;

/**
 * Created by Michael-Lee on 2017/7/15.
 */

public class ZWNode{
    public JZJ jzj;
    public Station station;
    public Station initialStation;
    public ZWNode next;
    public float spendTime;//完成所任务花费时间
    public float actionStartTime;//开始任务的时间节点
    public float actionEndTime;//完成任务的时间节点


    public ZWNode(JZJ jzj){
        this.jzj=jzj;
        this.actionStartTime=0;
        this.actionEndTime=0;

    }
    public ZWNode(JZJ jzj,Station station){
        this.jzj=jzj;
        this.station=station;
        this.actionStartTime=0;
        this.actionEndTime=0;

    }
}
