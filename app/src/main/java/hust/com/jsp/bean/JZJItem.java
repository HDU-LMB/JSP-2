package hust.com.jsp.bean;

import android.graphics.PointF;

import hust.com.jsp.utils.ImageCollection;

import java.util.List;

/**
 * Created by hust on 2016/12/26.
 */

public class JZJItem {

    public static final int testID1 = 0x001;

    private PointF position;
    private int imageID;
    private JZJStatus jzjStatus;
    private int id;
    public JZJItem(int id){
        this.id = id;
        this.position = new PointF(0.0f,0.0f);
        this.imageID = ImageCollection.baseJZJ01;
        this.jzjStatus = new JZJStatus();
    }

    public int getStatus(int jzjStatusID){
        return this.jzjStatus.getStatusValue(jzjStatusID);
    }

    public List<JZJStatusItem> getStatusList(){
        return this.jzjStatus.getStatusList();
    }

    public int getImageID(){
        return this.imageID;
    }

    public PointF getPosition() {
        return this.position;
    }
}
