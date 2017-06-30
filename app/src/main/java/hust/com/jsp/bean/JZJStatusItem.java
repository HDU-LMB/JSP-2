package hust.com.jsp.bean;

/**
 * Created by hust on 2016/12/26.
 */

public class JZJStatusItem {
    private int id;
    private String name;

    private int color;

    public JZJStatusItem(int id){
        this.id = id;
    }

    public JZJStatusItem setName(String name){
        this.name = name;
        return this;
    }

    public JZJStatusItem setColor(int color){
        this.color = color;
        return this;
    }

    public int getColor(){
        return color;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }
}
