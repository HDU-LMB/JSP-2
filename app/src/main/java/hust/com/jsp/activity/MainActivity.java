package hust.com.jsp.activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoItem;
import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Station;
import hust.com.jsp.db.DYDBHelper;
import hust.com.jsp.presenter.ZW_BCItemAdapter;
import hust.com.jsp.utils.ImageCollection;
import hust.com.jsp.utils.OnItemTouched;
import hust.com.jsp.view.ObjectLayer;
import hust.com.jsp.bean.JZJItem;
import hust.com.jsp.bean.JZJStatus;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hust.com.jsp.R;
import hust.com.jsp.view.ZWItemLayer;
import hust.com.jsp.view.ZWListLayer;
import hust.com.jsp.view.BZPlanPopupView;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private BZPlanPopupView bzPlanPopupView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private boolean isShow=false;
    private OnItemTouched onItemTouched;

    private ImageCollection imageCollection;
    private JZJItem jzjItem;

    private ZW_BCItemAdapter zw_bcItemAdapter;
    private Map<Integer,BZPlan> bcItemBZPlanMap = new TreeMap<>();//单波次下存储该波次下的所有bzplan


    private final float[] m_left_value = { 3.0f,  0.0f,  163.84772f,
                                               0.0f,  3.0f,  60.867737f,
                                               0.0f,  0.0f,  1.0f       };
    private final float[] m_right_value = {3.0f,  0.0f,  -1136.3689f,
                                               0.0f,  3.0f,  44.22734f,
                                               0.0f,  0.0f,  1.0f       };

    private Matrix matrix_left = new Matrix();
    private Matrix matrix_right = new Matrix();

//    @Override
//    protected void onResume(){
//        mapView.setVisibility(View.VISIBLE);
//        View fxPlanView = findViewById(R.id.FXPlanView);
//        fxPlanView.setVisibility(View.INVISIBLE);
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause(){
//        mapView.setVisibility(View.INVISIBLE);
//        super.onPause();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //matrix_left.setValues([3.0, 0.0, 350.5564][0.0, 3.0, 60.18579][0.0, 0.0, 1.0]);
        matrix_left.setValues(m_left_value);
        matrix_right.setValues(m_right_value);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imageCollection = new ImageCollection(this.getResources());
        JZJStatus.initialize();
        this.jzjItem = new JZJItem(JZJItem.testID1);
        mapView = (MapView) findViewById(R.id.MapView);

        {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.map);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }
            mapView.loadMap(bitmap);
        }

        onItemTouched = new OnItemTouched(this,mapView,R.drawable.popup);
        mapView.setMapViewListener(new MapViewListener(){

            @Override
            public void onMapLoadSuccess() {
                Bitmap bitmap = null;
                try{
                    bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    return;
                }
                ObjectLayer objectLayer = new ObjectLayer(mapView,jzjItem);//添加JZJ图层到mapView图层上

                objectLayer.setOnBitmapClickListener(onItemTouched);
                Log.i("JSPROJ","Listener added");
                objectLayer.setLocation(new PointF((float)400.0,(float)150.0));//添加JZJ的在HM坐标

//                initZWData();

                /*
                try{
                    bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mark);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    return;
                }

                BitmapLayer bitmapLayer1 = new BitmapLayer(mapView,bitmap);
                BitmapLayer bitmapLayer2 = new BitmapLayer(mapView,bitmap);
                BitmapLayer bitmapLayer3 = new BitmapLayer(mapView,bitmap);
                objectLayer.addBitmap(bitmapLayer1);
                objectLayer.addBitmap(bitmapLayer2);
                objectLayer.addBitmap(bitmapLayer3);
                */

                //mapView.addLayer(rectangleLayer);
                mapView.addLayer(objectLayer);//添加图层
               // mapView.addLayer(zwItemLayer);
//                zwListLayer.setLevel(0);
//                mapView.addLayer(zwListLayer);
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {

            }
        });


        Button button_right = (Button) findViewById(R.id.button_right);
        Button button_left = (Button) findViewById(R.id.button_left);

        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] p1 = {1.0f,0.0f};
                float[] p2 = {0.0f,0.0f};
                matrix_left.mapPoints(p1);
                matrix_left.mapPoints(p2);
                float x = p1[0]-p2[0];
                float y= p1[1]-p2[1];
                mapView.setCurrentZoom((float)Math.sqrt(x*x+y*y));
                mapView.getMatrix().set(matrix_left);
                mapView.refresh();
            }
        });

        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] p1 = {1.0f,0.0f};
                float[] p2 = {0.0f,0.0f};
                matrix_right.mapPoints(p1);
                matrix_right.mapPoints(p2);
                float x = p1[0]-p2[0];
                float y= p1[1]-p2[1];
                mapView.setCurrentZoom((float)Math.sqrt(x*x+y*y));
                mapView.getMatrix().set(matrix_right);
                mapView.refresh();
            }
        });


        DYDBHelper dydbHelper = new DYDBHelper(this);
        SQLiteDatabase db=dydbHelper.getReadableDatabase();
        BCInfo bcInfo=new BCInfo();
        bcInfo.readFromDB(db);
        for(int idx=0;idx<bcInfo.Count();idx++){//从数据库表中初始化数据，即读取波次1的所有bcitemplan
            BZPlan bzPlan=new BZPlan();
            List<BZPlanItem> bzPlanItemList=new ArrayList<>();
            bzPlan.setBzPlanItemList(bzPlanItemList);
            BCInfoItem item= bcInfo.get(idx);

            //读取数据库初始化数据
            if(idx==0) {
                JZJ jzj = new JZJ(JZJItem.testID1);
                BZPlanItem bzPlanItem = new BZPlanItem();
                Station station = new Station();
                station.setDisplayName("A1");
                bzPlanItem.setStation(station);
                bzPlanItem.setAddWeapon(true);
                bzPlanItem.setAddGas(true);
                bzPlanItem.setAddCool(true);
                bzPlan.addBZPlanItem(bzPlanItem);
                bzPlan.setJzj(jzj);
            }

            bcItemBZPlanMap.put(item.getID(),bzPlan);
        }

        zw_bcItemAdapter=new ZW_BCItemAdapter(this,bcInfo);
        final ListView zwBCItemlistView= (ListView) findViewById(R.id.zw_BCItemListView);
        zwBCItemlistView.setAdapter(zw_bcItemAdapter);
//        zwBCItemlistView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                BCInfoItem bcinfoitem= (BCInfoItem) zwBCItemlistView.getItemAtPosition(position);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        final ZWListLayer zwListLayer=new ZWListLayer(mapView);//加载zwListLayer图层
        mapView.addLayer(zwListLayer);

        zwBCItemlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BCInfoItem bcinfoItem= (BCInfoItem) zwBCItemlistView.getItemAtPosition(position);
                BZPlan bzPlan=bcItemBZPlanMap.get(bcinfoItem.getID());
                List<BZPlanItem> bzPlanItemList=bzPlan.getBzPlanItemList();
                JZJ jzj=bcinfoItem.getJzj();
                bzPlan.setJzj(jzj);

                //读取数据或者手动添加实现ZW选择
//                BZPlanItem bzPlanItem=new BZPlanItem();
//                Station station=new Station();
//                station.setDisplayName("A1");
//                bzPlanItem.setStation(station);
//                bzPlanItem.setAddWeapon(true);
//                bzPlanItem.setAddAir(true);
//                bzPlan.addBZPlanItem(bzPlanItem);


                zwListLayer.setBzPlan(bzPlan);
//                zwListLayer.isVisible=false;
                mapView.refresh();
//                ZWListLayer zwListLayer=initZWData();
//                if(isShow){
//                    zwListLayer.setIsShow(false);
//                    mapView.refresh();
//                    isShow = false;
//                    return;
//                }
//
//                zwListLayer.setIsShow(true);
//                mapView.send2Front(zwListLayer);
//                zwListLayer.setLevel(0);
//                mapView.refresh();
//                isShow = true;
                if(bzPlanPopupView!=null) bzPlanPopupView.dismiss();
                bzPlanPopupView=new BZPlanPopupView(MainActivity.this,bzPlan,mapView);
                bzPlanPopupView.setBackgroundDrawable(null);
                bzPlanPopupView.showAtLocation(MainActivity.this.findViewById(R.id.zw_BCItemListView), Gravity.LEFT|Gravity.TOP,312,246);
            }
        });
    }

    private ZWListLayer initZWData(BCInfoItem bcinfoitem)
    {
        BZPlanItem bzPlanItem=new BZPlanItem();
        JZJ jzj=bcinfoitem.getJzj();
//        jzj.setDisplayName("JZJ1");
//        Station station=new Station();
//        station.setDisplayName("A1");
//        bzPlanItem.setStation(station);
//        bzPlanItem.setAddAir(true);
//        bzPlanItem.setAddGas(true);
//        bzPlanItem.setAddCool(true);
//        bzPlanItem.setAddElectricity(true);
        //bzPlanItem.setAddFluid(true);
        bzPlanItem.setAddWeapon(true);
//        bzPlanItem.setAddOxygen(true);
        // bzPlanItem.setAddGuide(true);
        // BZPlan bzPlan=new BZPlan();
        // bzPlan.getBzPlanItemList().add(bzPlanItem);
        ZWItemLayer zwItemLayer=new ZWItemLayer(mapView,bzPlanItem,jzj);
//        zwItemLayer.setOffsetPoint(new PointF((float)200.0,(float)150.0));

        BZPlan bzPlan=new BZPlan();
        List<BZPlanItem> bzPlanItemList=new ArrayList<>();
        bzPlan.setBzPlanItemList(bzPlanItemList);
        bzPlan.addBZPlanItem(bzPlanItem);
//        bzPlan.addBZPlanItem(bzPlanItem);
//        bzPlan.addBZPlanItem(bzPlanItem);
        bzPlan.setJzj(jzj);
        ZWListLayer zwListLayer=new ZWListLayer(mapView);
        zwListLayer.setBzPlan(bzPlan);
//        zwListLayer.setLocation(new PointF(10,10));
//       zwListLayer.setIsShow(false);
        mapView.addLayer(zwListLayer);

        return zwListLayer;
    }

}
