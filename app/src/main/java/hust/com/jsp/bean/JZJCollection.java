package hust.com.jsp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/10.
 */

public class JZJCollection {
    private List<JZJ> jzjList;

    public JZJCollection(){
        jzjList = new ArrayList<>();
    }

    public void addJZJ(JZJ jzj){
        this.jzjList.add(jzj);
    }

    public void removeByName(String name){
        for (JZJ jzj :
                jzjList) {
            if (jzj.getDisplayName().equals(name)) {
                jzjList.remove(jzj);
                break;
            }
        }
    }

    public List<JZJ> filterByName(String filter){
        List<JZJ> ret = new ArrayList<>();
        for (JZJ jzj :
                jzjList) {
            if(jzj.getDisplayName().contains(filter)){
                ret.add(jzj);
            }
        }
        return  ret;
    }



}
