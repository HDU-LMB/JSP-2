package hust.com.jsp.bean;

/**
 * Created by admin on 2017/1/12.
 */

public class JZJAction {
    public int Type;
    enum JZJActionType{type1,type2,type3};

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public String getName(){
        return this.name;
    }
}
