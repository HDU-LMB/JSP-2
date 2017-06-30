package hust.com.jsp.bean;

/**
 * Created by hust on 2017/2/7.
 */

public class QWAction {
    private int startTime;
    private int endTime;

    public enum QWActionType{
        flight,land,action1,action2,action3
    }
    private QWActionType type;

    public enum QWActionLineType{
        bar,line
    }
    private QWActionLineType lineType;

    public enum QWActionPointType{
        leftArrow,rightArrow,circle,none
    }
    private QWActionPointType leftType;
    private QWActionPointType rightType;

    private int color;

    public int getStartTime(){
        return this.startTime;
    }

    public QWAction setStartTime(int time){
        this.startTime = time;
        return this;
    }

    public int getEndTime(){
        return  this.endTime;
    }

    public QWAction setEndTime(int time){
        this.endTime = time;
        return this;
    }

    public QWActionType getType(){
        return this.type;
    }

    public QWAction setType(QWActionType type){
        this.type = type;
        return this;
    }

    public QWActionLineType getLineType(){
        return this.lineType;
    }

    public QWAction setLineType(QWActionLineType lineType){
        this.lineType= lineType;
        return this;
    }

    public QWActionPointType getRightType(){
        return this.rightType;
    }

    public QWAction setRightPointType(QWActionPointType rightPointType){
        this.rightType = rightPointType;
        return this;
    }


    public QWActionPointType getLeftType(){
        return this.leftType;
    }

    public QWAction setLeftPointType(QWActionPointType leftPointType){
        this.leftType = leftPointType;
        return this;
    }

    public int getColor(){
        return this.color;
    }

    public QWAction setColor(int color){
        this.color= color;
        return this;
    }


}
