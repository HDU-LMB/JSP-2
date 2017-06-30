package hust.com.jsp.utils;

import android.app.Activity;
import android.graphics.PointF;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hust.com.jsp.view.PopupLayer;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.BitmapLayer;

/**
 * Created by hust on 2016/12/22.
 */

public class OnItemTouched implements BitmapLayer.OnBitmapClickListener
{
    private MapView mapView;
    private RelativeLayout infoView;
    private TextView popup;
    private Activity activity;
    private int resoureces;
    private Boolean isShow;
    private PopupLayer popupLayer;
    public OnItemTouched(Activity mainAct, MapView mapView, int resources){
        this.mapView = mapView;
        this.activity = mainAct;
        this.resoureces = resources;
        //popup = new TextView(mainAct);
        //popup.setBackgroundResource(R.drawable.popup);
        this.infoView = new RelativeLayout(mainAct);
        this.isShow = false;

        popupLayer= new PopupLayer(mapView);
        popupLayer.setIsShow(false);
        mapView.addLayer(popupLayer);
    }

    @Override
    public void onBitmapClick(BitmapLayer layer){

        PointF position = layer.getLocation();
        popupLayer.setLocation(position);
        float[] screenPos = mapView.convertMapXY2ScreenXY(position.x,position.y);
        int screenX = (int)screenPos[0];
        int screenY = (int)screenPos[1];

        Log.i("JSPROJ","Touched at "+screenPos[0]+","+screenPos[1]);
        if(isShow){
            popupLayer.setIsShow(false);
            mapView.refresh();
            /*
            infoView.removeAllViews();
            ((ViewGroup)infoView.getParent()).removeView(infoView);*/
            this.isShow = false;
            return;
        }

        //mapView.addLayer(popupLayer);
        popupLayer.setIsShow(true);
        mapView.send2Front(popupLayer);
        popupLayer.setLevel(0);
        mapView.refresh();

        /*
        popup = new TextView(this.activity);
        popup.setBackgroundResource(resoureces);
        int h = popup.getMeasuredHeight();
        int w = popup.getMeasuredWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenX+h,screenY+w,0,0);
        this.infoView.addView(popup,params);

        RelativeLayout.LayoutParams relLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        activity.addContentView(this.infoView, relLayoutParams);
        */
        this.isShow = true;
        final Toast touched = Toast.makeText(activity, "Touched at "+screenX+","+screenY, Toast.LENGTH_SHORT);

    }
}
