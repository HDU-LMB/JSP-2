package hust.com.jsp.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import hust.com.jsp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hust on 2016/12/28.
 */

public class ImageCollection {

    public static ImageCollection collection;
    public static final int baseMap01 = 0x0001;
    public static final int baseJZJ01 = 0x0002;
    public static final int baseSTAT01 = 0x0003;

    private Map<Integer,Bitmap> imageCollect;

    public ImageCollection(Resources resources){
        this.imageCollect = new HashMap<>();
        try {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.map);
            this.imageCollect.put(baseMap01,bitmap);
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.mark);
            this.imageCollect.put(baseSTAT01,bitmap);
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.jzj);
            bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/12,bitmap.getHeight()/12,false);//JZJ图片缩小
            this.imageCollect.put(baseJZJ01,bitmap);

        }catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        ImageCollection.collection = this;
    }

    public Bitmap getBitmap(int id){
        return this.imageCollect.get(id);
    }
}
