package hust.com.jsp.bean;

/**
 * Created by hust on 2017/1/13.
 */

public class FXPlanItem {
    private int fx_id;
    private String planName;
    private JZJ jzj;
    private Station station;
    private long startTime;
    private long endTime;
    private FXPlanType type;
    private float gas;
    private long chTime;
    private long fhTime;
    public int getFx_id() {
        return fx_id;
    }

    public long getChTime() {
        return chTime;
    }

    public void setChTime(long chTime) {
        this.chTime = chTime;
    }

    public long getFhTime() {
        return fhTime;
    }

    public void setFhTime(long fhTime) {
        this.fhTime = fhTime;
    }

    public void setFx_id(int fx_id) {
        this.fx_id = fx_id;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public FXPlanItem setPlanName(String name){
        this.planName = name;
        return this;
    }

    public FXPlanItem setStartTime(long time){
        this.startTime = time;
        return this;
    }

    public FXPlanItem setJzj(JZJ jzj){
        this.jzj = jzj;
        return this;
    }

    public FXPlanItem setEndTime(long time){
        this.endTime = time;
        return this;
    }

    public FXPlanItem setStation(Station station){
        this.station = station;
        return this;
    }

    public FXPlanItem setType(FXPlanType type){
        this.type = type;
        return this;
    }

    public FXPlanItem setGas(float gas){
        this.gas= gas;
        return this;
    }

    public FXPlanType getType(){
        return this.type;
    }

    public enum FXPlanType {
        land,flight,both
    }

    public String getPlanName(){return this.planName;}

    public float getGas(){
        return this.gas;
    }

    public JZJ getJzj(){
        return this.jzj;
    }

    public Station getStation(){
        return this.station;
    }

    public String getGasStatus(){
        return this.gas+"T";
    }

}
