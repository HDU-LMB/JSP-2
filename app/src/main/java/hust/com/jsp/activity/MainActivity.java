package hust.com.jsp.activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoItem;
import hust.com.jsp.bean.BLInfo;
import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.JZJAction;
import hust.com.jsp.bean.Station;
import hust.com.jsp.dao.BCDAO;
import hust.com.jsp.dao.BLDAO;
import hust.com.jsp.dao.JZJDAO;
import hust.com.jsp.db.DYDBHelper;
import hust.com.jsp.presenter.ZW_BCItemAdapter;
import hust.com.jsp.utils.ImageCollection;
import hust.com.jsp.utils.OnItemTouched;
import hust.com.jsp.view.BZPlan_TimeProgressLayer;
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
    private BCInfo bcInfo;//当前选中的bc
    private TextView currentBCName;
    private ZWListLayer zwListLayer;//mapView中最上面显示的ZW及任务（如加油、弹）的图层
    private ZW_BCItemAdapter zw_bcItemAdapter;
    private Spinner spBCList;//点击选择当前BC
    private List<String> bcListBZJH;//spBCList关联的数据源
    private List<BCInfo> bcInfoList;//某BL下所有的BC


    private Map<Integer,BZPlan> bcItemBZPlanMap = new TreeMap<>();//某BL下BC存储该波次下的所有bzplan
    private List<BZPlan> bzPlanList=new ArrayList<>();//某BL下所有所有的bzplan，用于绘画时间甘特图
    private Button showProgressButton;//点击显示或隐藏时间甘特图
    private BZPlan_TimeProgressLayer timeProgressLayer;//mapView中底部的绘画时间甘特图的图层




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imageCollection = new ImageCollection(this.getResources());
        JZJStatus.initialize();
        this.jzjItem = new JZJItem(JZJItem.testID1);

        initView();

        //MapView底部显示时间甘特图
        showTimeProgress();

        //最左边栏选中和显示当前BC的相关信息
        initBL_BC();

        //MapView顶部显示ZW和任务图
        showZWLayer();





    }

    private void initView(){


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

        ObjectLayer objectLayer = new ObjectLayer(mapView,jzjItem);//添加JZJ图层到mapView图层上
        objectLayer.setOnBitmapClickListener(onItemTouched);
        Log.i("JSPROJ","Listener added");
        objectLayer.setLocation(new PointF((float)400.0,(float)150.0));//添加JZJ的在HM坐标
        mapView.addLayer(objectLayer);//添加图层

        timeProgressLayer=new BZPlan_TimeProgressLayer(mapView,bzPlanList);
        timeProgressLayer.setLocation(new PointF(0,630));
        mapView.addLayer(timeProgressLayer);
        timeProgressLayer.isVisible=false;

        mapView.refresh();

        mapView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


            }
        });

        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onMapLoadSuccess() {

            }

            @Override
            public void onMapLoadFail() {

            }
        });







    }

    private void showTimeProgress(){
        showProgressButton= (Button) findViewById(R.id.button_TimeProgress);
        showProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeProgressLayer.isVisible) {
                    timeProgressLayer.isVisible = false;
                    mapView.refresh();
                }
                else {
                    timeProgressLayer.isVisible=true;
                    mapView.refresh();
                }

            }
        });
    }

    private void initBL_BC(){

        currentBCName= (TextView) findViewById(R.id.currentBCName);
        spBCList= (Spinner) findViewById(R.id.spBCList);
        bcListBZJH=new ArrayList<>();
        //初始化化已有的BC数据
        bcListBZJH.add("BC01");
        bcListBZJH.add("BC02");
        bcListBZJH.add("BC03");
        ArrayAdapter spAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,bcListBZJH);
        spBCList.setAdapter(spAdapter);

        //数据库表读取BC信息数据
        DYDBHelper dydbHelper = new DYDBHelper(this);
        SQLiteDatabase db=dydbHelper.getReadableDatabase();
        bcInfo=new BCInfo();
//        bcInfo.readFromDB(db);

        //测试数据
        bcInfo.setName((String)spBCList.getSelectedItem());
        JZJ jzj1=new JZJ(1);
        jzj1.setDisplayName("JZJ01");
        BCInfoItem bcInfoItem1 = new BCInfoItem(jzj1);
        bcInfoItem1.setID(1);
        bcInfoItem1.setJZJ(1);
        JZJAction action=new JZJAction();
        action.setName("A1-C1 QF");
        bcInfoItem1.addAction(action);
        bcInfoItem1.setName("波次1");
        bcInfoItem1.setTime(Long.parseLong("1485598200510"));
        bcInfoItem1.setType("可用");
        JZJ jzj2=new JZJ(2);
        jzj2.setDisplayName("JZJ02");
        BCInfoItem bcInfoItem2 = new BCInfoItem(jzj2);
        bcInfoItem2.setID(2);
        bcInfoItem2.setJZJ(2);
        JZJAction action2=new JZJAction();
        action2.setName("A1-C3 JL");
        bcInfoItem2.addAction(action2);
        bcInfoItem2.setName("波次1");
        bcInfoItem2.setTime(Long.parseLong("1485598200510"));
        bcInfoItem2.setType("备用");
        bcInfo.addBCInfoItem(bcInfoItem1);
        bcInfo.addBCInfoItem(bcInfoItem2);

        bcInfoList=new ArrayList<>();
        BCInfo bcInfo2=new BCInfo();
        bcInfo2.setName("BC02");
        BCInfo bcInfo3=new BCInfo();
        bcInfo3.setName("BC03");
        bcInfoList.add(bcInfo);
        bcInfoList.add(bcInfo2);
        bcInfoList.add(bcInfo3);

        for(int idx=0;idx<bcInfo.Count();idx++){//从数据库表中初始化数据，即读取波次1的所有bcitemplan
            BZPlan bzPlan=new BZPlan();
            List<BZPlanItem> bzPlanItemList=new ArrayList<>();
            bzPlan.setBzPlanItemList(bzPlanItemList);
            BCInfoItem bcItem= bcInfo.get(idx);
            JZJ jzj = bcItem.getJzj();
            bzPlan.setJzj(jzj);
            //读取数据库初始化数据，这里是测试数据
            if(idx==0) {
                BZPlanItem bzPlanItem = new BZPlanItem();
                Station station = new Station();
                station.setDisplayName("A1");
                bzPlanItem.setStation(station);
                bzPlanItem.setSpendTime(30);
                bzPlanItem.setAddWeapon(true);
                bzPlanItem.setAddGas(true);
                bzPlanItem.setAddCool(true);
                bzPlan.addBZPlanItem(bzPlanItem);
            }

            bcItemBZPlanMap.put(bcItem.getID(),bzPlan);
            bzPlanList.add(bzPlan);
        }
    }

    private void showZWLayer(){
        zw_bcItemAdapter=new ZW_BCItemAdapter(this,bcInfo);
        final ListView zwBCItemlistView= (ListView) findViewById(R.id.zw_BCItemListView);
        zwBCItemlistView.setAdapter(zw_bcItemAdapter);
        spBCList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentBCName.setText((String)spBCList.getSelectedItem());
                bcInfo=bcInfoList.get(position);
                zw_bcItemAdapter=new ZW_BCItemAdapter(MainActivity.this,bcInfo);
                zwBCItemlistView.setAdapter(zw_bcItemAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //mapView中显示ZW及任务的图层
        zwListLayer=new ZWListLayer(mapView);//加载zwListLayer图层
        mapView.addLayer(zwListLayer);
        zwBCItemlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BCInfoItem bcinfoItem= (BCInfoItem) zwBCItemlistView.getItemAtPosition(position);
                BZPlan bzPlan=bcItemBZPlanMap.get(bcinfoItem.getID());
                List<BZPlanItem> bzPlanItemList=bzPlan.getBzPlanItemList();
                JZJ jzj=bcinfoItem.getJzj();
                bzPlan.setJzj(jzj);
                zwListLayer.setBzPlan(bzPlan);

                mapView.refresh();

                if(bzPlanPopupView!=null) bzPlanPopupView.dismiss();
                bzPlanPopupView=new BZPlanPopupView(MainActivity.this,bzPlan,mapView);
                bzPlanPopupView.setBackgroundDrawable(null);
                bzPlanPopupView.setFocusable(true);//这两个属性设置用于点击弹出框外部，窗体消失
                bzPlanPopupView.setOutsideTouchable(true);
                bzPlanPopupView.showAtLocation(MainActivity.this.findViewById(R.id.zw_BCItemListView), Gravity.LEFT|Gravity.TOP,312,246);
            }
        });
    }

}
