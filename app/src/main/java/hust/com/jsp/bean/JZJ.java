package hust.com.jsp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hust on 2017/1/11.
 */

public class JZJ {
    private int id;
    private String displayName;
    private int jzjType;
    private String jzjBeiyong;
    private Station nearstStation;
    public JZJ(){

    };
    public JZJ(int id,String name,String by,int jzjType){
        this.id=id;
        this.displayName=name;
        this.jzjBeiyong=by;
        this.jzjType=jzjType;
    }

    public int getJzjType() {
        return jzjType;
    }

    public String getJzjBeiyong() {
        return jzjBeiyong;
    }

    public void setJzjBeiyong(String jzjBeiyong) {
        this.jzjBeiyong = jzjBeiyong;
    }

    public void setJzjType(int jzjType) {
        this.jzjType = jzjType;
    }
    private static List<JZJ> jzjList = new ArrayList<>();

    public JZJ( int id){
        this.id = id;
        jzjList.add(this);
    }

    public JZJ setID(int id){
        this.id = id;
        return this;
    }

    public int getId(){
        return this.id;
    }

    public JZJ setDisplayName(String name){
        this.displayName = name;
        return this;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public static JZJ getJZJByID(int anInt) {
        for (JZJ jzj :
                jzjList) {
            if (jzj.id == anInt) return jzj;
        }
        return null;
    }
}