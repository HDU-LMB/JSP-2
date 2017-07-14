package hust.com.jsp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.Spinner;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoItem;
import hust.com.jsp.bean.BLInfo;
import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.BZPlan_TimeSchemaOrder;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.bean.Station;
import hust.com.jsp.dao.BCDAO;
import hust.com.jsp.dao.BLDAO;
import hust.com.jsp.dao.BZPlanItemDAO;
import hust.com.jsp.dao.JZJDAO;
import hust.com.jsp.dao.LocationDAO;
import hust.com.jsp.presenter.ZW_BCItemAdapter;
import hust.com.jsp.utils.ImageCollection;
import hust.com.jsp.utils.LocationTools;
import hust.com.jsp.utils.OnItemTouched;
import hust.com.jsp.view.BLJZJLayer;
import hust.com.jsp.view.BZPlan_TimeProgressLayer;
import hust.com.jsp.view.LocationLayer;
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
//    private TextView currentBCName;
    private ZWListLayer zwListLayer;//mapView中最上面显示的ZW及任务（如加油、弹）的图层
    private ZW_BCItemAdapter zw_bcItemAdapter;
    private Spinner spBCList;//点击选择当前BC
    private List<String> bcList_SpinnerSource;//spBCList关联的数据源
    private List<BCInfo> bcInfoList;//某BC下所有的BCInfo
    private Map<Integer,BZPlan> bcItemBZPlanMap ;//某Bc下存储该波次下的所有bzplan
    private Map<BCInfo,Map<Integer,BZPlan>> bcList_ItemMap=new TreeMap<>();//存储所有的BC
    private List<BZPlan> bzPlanList;//某BC下所有所有的bzplan，用于绘画时间甘特图
    private List<BZPlan> bzPlanTimeList;//bz方案的时刻表
    private List<BZPlan> bzPlanTranList;
    private Map<BCInfo,List<BZPlan>> bzListMap=new TreeMap<>();//存储所有BC的bzPlanList
    private Button calculateTimeProgress;
    private Button btnSave;
    private Button showProgressButton;//点击显示或隐藏时间甘特图
    private BZPlan_TimeProgressLayer timeProgressLayer;//mapView中底部的绘画时间甘特图的图层
    private Button scrollUpTimeProgressLayer;
    private Button scrollDownTimeProgressLayer;
    private SeekBar seekBarTimeProgress;//进度条

    private Map<Integer,List<BLInfo>> blMap=new TreeMap<>();
    private static List<Location> locationList;
    private List<JZJ> bcjzjList;
    private List<JZJ> jzjList;
    private JZJDAO jzjDAO;
    private BLDAO blDAO;
    private BCDAO bcDAO;
    private BZPlanItemDAO bzPlanDAO;
    private LocationDAO locationDAO;
    private Map<Integer,BLJZJLayer> layerMap;//key-jzj.ID，value-jzjLayer

    public static MainActivity getInstance() {
        return instance;
    }

    private static MainActivity instance;

    public MainActivity(){
        instance=this;
    }

    public static List<Location> getLocationList() {
        return locationList;
    }

    public void refreshData(){
        this.jzjItem = new JZJItem(JZJItem.testID1);

        jzjDAO=new JZJDAO(this);
        blDAO=new BLDAO(this);
        bcDAO=new BCDAO(this);
        bzPlanDAO=new BZPlanItemDAO(this);
        locationDAO=new LocationDAO(this);
        bcjzjList=new ArrayList<JZJ>();
        bcInfoList=new ArrayList<BCInfo>();
        bcList_SpinnerSource =new ArrayList<>();
        locationList=locationDAO.getAllLocation();
        bcItemBZPlanMap = new TreeMap<>();
        bzPlanList=new ArrayList<>();
        bzPlanTimeList =new ArrayList<>();
        layerMap=new TreeMap<>();
        jzjList=getAllJZJ();
        bcList_ItemMap=new TreeMap<>();//存储所有的BC
        bzListMap=new TreeMap<>();//存储所有BC的bzPlanList
        blMap=new TreeMap<>();

        bcInfoList=bcDAO.getAll();//获取所有BC
        for(BCInfo info:bcInfoList){
            bcInfo=info;
            List<BLInfo> blInfoList=blDAO.getById(info.getId());
            blMap.put(info.getId(),blInfoList);//根据bcid获取相关BL
            bcList_SpinnerSource.add(info.getName());

            bcjzjList.clear();
            //根据BL获取JZJ
            for(BLInfo blInfo:blInfoList){

                JZJ jzj=jzjDAO.getJZJ(blInfo.getJzjid());
                jzj.setCurrentStation(blInfo.getStation());
                BCInfoItem bcInfoItem=new BCInfoItem(jzj);
                bcInfoItem.setStation(blInfo.getStation());
                switch (blInfo.getType()){
                    case 1:
                        jzj.setJzjBeiyong("非备用");
                        bcjzjList.add(jzj);
                        break;
                    case 2:
                        jzj.setJzjBeiyong("备用");
                        bcjzjList.add(jzj);
                        break;
                }
                bcInfoItem.setType(jzj.getJzjBeiyong());
                bcInfo.addBCInfoItem(bcInfoItem);
                if(jzj.getId()!=0){
                    bcjzjList.add(jzj);
                }
            }
        }
        for(BCInfo bcInfo:bcInfoList){
            bcItemBZPlanMap=new TreeMap<>();
            bzPlanList=new ArrayList<>();
            for(int idx=0;idx<bcInfo.Count();idx++) {//从数据库表中初始化数据，即读取波次1的所有bcitemplan
                BCInfoItem bcItem = bcInfo.get(idx);
                bcItem.setID(idx);
                JZJ jzj = bcItem.getJzj();

                BZPlan bzPlan = new BZPlan();
                List<BZPlanItem> bzPlanItemList =bzPlanDAO.getListByBCAndJZJ(bcInfo.getId(),jzj.getId());
                bzPlan.setBzPlanItemList(bzPlanItemList);
                bzPlan.setJzj(jzj);
                bzPlan.setStation(bcItem.getStation());
                bzPlan.setBcid(bcInfo.getId());

                bcItemBZPlanMap.put(bcItem.getID(), bzPlan);
                bzPlanList.add(bzPlan);
            }

            bcList_ItemMap.put(bcInfo,bcItemBZPlanMap);
            bzListMap.put(bcInfo,bzPlanList);
        }

        for(final JZJ jzj:jzjList){
            BLJZJLayer layer=new BLJZJLayer(mapView,getResources(),jzj);

            layerMap.put(jzj.getId(),layer);
        }
        ArrayAdapter spAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, bcList_SpinnerSource);
        spBCList.setAdapter(spAdapter);

    }

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
        locationDAO=new LocationDAO(this);
        jzjDAO=new JZJDAO(this);
        bzPlanDAO=new BZPlanItemDAO(this);
        blDAO=new BLDAO(this);
        bcDAO=new BCDAO(this);
        bcjzjList=new ArrayList<JZJ>();
        bcInfoList=new ArrayList<BCInfo>();
        bcList_SpinnerSource =new ArrayList<>();
        locationList=locationDAO.getAllLocation();
        bcItemBZPlanMap = new TreeMap<>();
        bzPlanList=new ArrayList<>();
        bzPlanTimeList =new ArrayList<>();
        layerMap=new TreeMap<>();
        jzjList=getAllJZJ();
        bcList_ItemMap=new TreeMap<>();//存储所有的BC

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
        timeProgressLayer.setLocation(new PointF(0,560));
        mapView.addLayer(timeProgressLayer);
        timeProgressLayer.isVisible=true;

        mapView.getMatrix().setTranslate(0,-55);
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

        bcInfoList=bcDAO.getAll();//获取所有BC
        for(BCInfo info:bcInfoList){
            bcInfo=info;
            List<BLInfo> blInfoList=blDAO.getById(info.getId());
            blMap.put(info.getId(),blInfoList);//根据bcid获取相关BL
            bcList_SpinnerSource.add(info.getName());

            bcjzjList.clear();
            //根据BL获取JZJ
            for(BLInfo blInfo:blInfoList){

                JZJ jzj=jzjDAO.getJZJ(blInfo.getJzjid());
                jzj.setCurrentStation(blInfo.getStation());
                BCInfoItem bcInfoItem=new BCInfoItem(jzj);
                bcInfoItem.setStation(blInfo.getStation());
                switch (blInfo.getType()){
                    case 1:
                        jzj.setJzjBeiyong("非备用");
                        bcjzjList.add(jzj);
                        break;
                    case 2:
                        jzj.setJzjBeiyong("备用");
                        bcjzjList.add(jzj);
                        break;
                }
                bcInfoItem.setType(jzj.getJzjBeiyong());
                bcInfo.addBCInfoItem(bcInfoItem);
                if(jzj.getId()!=0){
                    bcjzjList.add(jzj);
                }
            }
        }
        for(BCInfo bcInfo:bcInfoList){
            bcItemBZPlanMap=new TreeMap<>();
            bzPlanList=new ArrayList<>();
            for(int idx=0;idx<bcInfo.Count();idx++) {//从数据库表中初始化数据，即读取波次1的所有bcitemplan
                BCInfoItem bcItem = bcInfo.get(idx);
                bcItem.setID(idx);
                JZJ jzj = bcItem.getJzj();

                BZPlan bzPlan = new BZPlan();
                List<BZPlanItem> bzPlanItemList =bzPlanDAO.getListByBCAndJZJ(bcInfo.getId(),jzj.getId());
                bzPlan.setBzPlanItemList(bzPlanItemList);
                bzPlan.setJzj(jzj);
                bzPlan.setStation(bcItem.getStation());
                bzPlan.setBcid(bcInfo.getId());

                bcItemBZPlanMap.put(bcItem.getID(), bzPlan);
                bzPlanList.add(bzPlan);
            }

            bcList_ItemMap.put(bcInfo,bcItemBZPlanMap);
            bzListMap.put(bcInfo,bzPlanList);
        }

        for(final JZJ jzj:jzjList){
            BLJZJLayer layer=new BLJZJLayer(mapView,getResources(),jzj);

            layerMap.put(jzj.getId(),layer);
        }
        btnSave= (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBZPlanItem();
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

        seekBarTimeProgress=(SeekBar) findViewById(R.id.timeSeekBar);
        seekBarTimeProgress.setMax(375);
        seekBarTimeProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v("Timeprogress","T="+progress);
                refreshMapRefTime(progress,bzPlanTranList);
                timeProgressLayer.setTime(progress);
                mapView.refresh();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        calculateTimeProgress=(Button) findViewById(R.id.button_CalculateTimeProgress);
        calculateTimeProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BZPlan_TimeSchemaOrder timeSchemaOrder=new BZPlan_TimeSchemaOrder(bzPlanList);
                timeSchemaOrder.initSchemaItem();
                bzPlanTimeList=timeSchemaOrder.getSchemaTimeProgress();
                bzPlanTranList=addTranNode();
                timeProgressLayer.setBzPlanList(bzPlanTranList);
                timeProgressLayer.setShowTimeProgress(true);
                mapView.refresh();
            }
        });

        scrollUpTimeProgressLayer=(Button) findViewById(R.id.button_ScrollUpTimeProgress);
        scrollUpTimeProgressLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PointF loc=timeProgressLayer.getLocation();
                if((loc.y-50)>140) {
                    loc.y -= 50;
                    timeProgressLayer.setLocation(loc);
                    mapView.refresh();
                }
            }
        });
        scrollDownTimeProgressLayer=(Button) findViewById(R.id.button_ScrollDownTimeProgress);
        scrollDownTimeProgressLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PointF loc=timeProgressLayer.getLocation();
                if((560-loc.y)>=50) {
                    loc.y += 50;
                    timeProgressLayer.setLocation(loc);
                    mapView.refresh();
                }
            }
        });
    }

    /**
     * 复制List<BZPlan> ，用于添加转运节点
     * @param listP 原始list
     * @return
     */
    private List<BZPlan> copyList(List<BZPlan> listP){
        List<BZPlan> list=new ArrayList<>();
        for(int i=0;i<listP.size();i++){
            BZPlan bzPlanP=listP.get(i);
            BZPlan bzPlan=new BZPlan();
            bzPlan.setBcid(bzPlanP.getBcid());
            bzPlan.setJzj(bzPlanP.getJzj());
            bzPlan.setStation(bzPlanP.getStation());
            List<BZPlanItem> bzPlanItemListP=bzPlanP.getBzPlanItemList();
            List<BZPlanItem> bzPlanItemList=new ArrayList<>();
            bzPlanItemList.addAll(bzPlanItemListP);
            bzPlan.setBzPlanItemList(bzPlanItemList);
            list.add(bzPlan);
        }
        return list;
    }
    /**
     * 添加转运节点
     */
    private List<BZPlan> addTranNode(){
        List<BZPlan> bzPlanList=copyList(bzPlanTimeList);
        for(int i=0;i<bzPlanList.size();i++){
            BZPlan bzPlan=bzPlanList.get(i);
            List<BZPlanItem> bzPlanItemList=bzPlan.getBzPlanItemList();
            if(bzPlanItemList.size()==0){
                continue;
            }
            for(int j=0;j<bzPlanItemList.size();j+=2){
                if(j==bzPlanItemList.size()-1){
                    break;
                }
                BZPlanItem bzPlanItem1=bzPlanItemList.get(j);
                BZPlanItem bzPlanItem2=bzPlanItemList.get(j+1);
                BZPlanItem tranNode=new BZPlanItem();
                tranNode.setStation(bzPlanItem1.getStation());
                tranNode.setIndex(j+1);
                tranNode.setStartTime(bzPlanItem1.getEndTime());
                double distance= LocationTools.getDistance(bzPlanItem1.getStation().getLocation(),bzPlanItem2.getStation().getLocation());
                float tranTime= (float) (distance*0.03+2);
                tranNode.setSpendTime(tranTime);
                tranNode.setEndTime(bzPlanItem1.getEndTime()+tranTime);
                for(int k=j+1;k<bzPlanItemList.size();k++){
                    bzPlanItemList.get(k).setStartTime(bzPlanItemList.get(k).getStartTime()+tranTime);
                    bzPlanItemList.get(k).setEndTime(bzPlanItemList.get(k).getEndTime()+tranTime);
                }
   /*             bzPlanItem2.setStartTime(bzPlanItem2.getStartTime()+tranTime);
                bzPlanItem2.setEndTime(bzPlanItem2.getStartTime()+bzPlanItem2.getSpendTime());*/
                bzPlanItemList.add(j+1,tranNode);
            }
        }
        return bzPlanList;
    }
    /**
     * 根据拖动条更新Mapview
     * @param time 进度条时间
     * @param bzList 输入bz计划
     */
    private void refreshMapRefTime(float time , List<BZPlan> bzList){
        if(bzList==null){
            return;
        }
        for(BZPlan bzPlan:bzList){
            List<BZPlanItem> bzPlanItemList=bzPlan.getBzPlanItemList();
            if(bzPlanItemList.size()==0){
                continue;
            }
            int flag=0;
            Station station= new Station();
            float timeper=Float.MAX_VALUE;
            BLJZJLayer layer=layerMap.get(bzPlan.getJzj().getId());
            for(int i=0;i<bzPlanItemList.size();i++){
                BZPlanItem bzPlanItem=bzPlanItemList.get(i);
                if(bzPlanItem.getStartTime()<=time&&bzPlanItem.getEndTime()>time){
                    if(i%2==0){
                        flag=1;
                        station=bzPlanItem.getStation();
                        timeper=(time-bzPlanItem.getStartTime())/bzPlanItem.getSpendTime();

                    }
                    else {
                        flag=4;
                        station=bzPlanItem.getStation();
                    }
                    break;
                }
                else if(time>bzPlanItemList.get(bzPlanItemList.size()-1).getEndTime()){
                    flag=3;
                }
                else if(i<bzPlanItemList.size()-1){
                    if(bzPlanItem.getEndTime()<time&&bzPlanItemList.get(i+1).getStartTime()>time){
                        if(i%2==0){
                            flag=2;
                            station=bzPlanItem.getStation();
                        }
                        else {
                            flag=2;
                            station=bzPlanItemList.get(i-1).getStation();
                        }

                    }
                }

            }
            switch (flag){
                case 1:     //进行保障
                    layer.isVisible=true;
                    layer.setProgress(timeper);
                    layer.setLocation(station.getLocation());
                    layer.setAngle(station.getAngle());
                    break;
                case 2:     //等待
                    layer.isVisible=true;
                    layer.setProgress(2);
                    layer.setLocation(station.getLocation());
                    layer.setAngle(station.getAngle());
                    break;
                case 3:     //起飞
                    layer.isVisible=false;
                    break;
                case 4:     //转运
                    layer.isVisible=true;
                    layer.setProgress(4);
                    layer.setLocation(station.getLocation());
                    layer.setAngle(station.getAngle());
                    break;
            }

        }
    }

    /**
     * 保存BZ计划
     */
    private void saveBZPlanItem(){
        Log.v("bzplan","save");
        bzPlanDAO.addBZPlanList(bzPlanList);
    }


    private void initBL_BC(){

        spBCList= (Spinner) findViewById(R.id.spBCList);
        ArrayAdapter spAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, bcList_SpinnerSource);
        spBCList.setAdapter(spAdapter);

    }

    private void showZWLayer(){
        zw_bcItemAdapter=new ZW_BCItemAdapter(this,bcInfo);
        final ListView zwBCItemlistView= (ListView) findViewById(R.id.zw_BCItemListView);
        zwBCItemlistView.setAdapter(zw_bcItemAdapter);
        spBCList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                currentBCName.setText((String)spBCList.getSelectedItem());

                bcInfo=bcInfoList.get(position);
                bzPlanList=bzListMap.get(bcInfo);
                timeProgressLayer.setBzPlanList(bzPlanList);
                timeProgressLayer.setShowTimeProgress(false);
                bcItemBZPlanMap=bcList_ItemMap.get(bcInfo);
                zw_bcItemAdapter=new ZW_BCItemAdapter(MainActivity.this,bcInfo);
                zwBCItemlistView.setAdapter(zw_bcItemAdapter);
                refreshMap(bcInfo.getId());
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
                if (((ListView)parent).getTag() != null){
                    ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((ListView)parent).setTag(view);
                view.setBackgroundColor(Color.LTGRAY);

                BCInfoItem bcinfoItem= (BCInfoItem) zwBCItemlistView.getItemAtPosition(position);

                BZPlan bzPlan=bcItemBZPlanMap.get(bcinfoItem.getID());
                List<BZPlanItem> bzPlanItemList=bzPlan.getBzPlanItemList();
                JZJ jzj=bcinfoItem.getJzj();
                bzPlan.setJzj(jzj);
                zwListLayer.setBzPlan(bzPlan);

                timeProgressLayer.setBzPlanList(bzPlanList);
                timeProgressLayer.setShowTimeProgress(false);
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

    private void refreshMap(final int bcID){
        mapView.clearLayer();

        List<BLInfo> blInfoList=blMap.get(bcID);
        for(final BLInfo info:blInfoList){
            BLJZJLayer layer=layerMap.get(info.getJzjid());
            Log.v("bc",info.getPoint().toString()+" id  "+info.getJzjid());
            layer.setLocation(info.getPoint());
            Location location=locationDAO.getInfoByPonit(info.getX(),info.getY());
            layer.setAngle(location.getAngle());
            mapView.addLayer(layer);
        }

        for(Location info:locationList){
            LocationLayer layer=new LocationLayer(mapView,getResources(),info);
            Log.v("bc","location:"+info.getPoint().toString());
            mapView.addLayer(layer);
        }

        mapView.addLayer(zwListLayer);
        mapView.addLayer(timeProgressLayer);

        mapView.refresh();
    }

    private List<JZJ> getAllJZJ(){
        List<JZJ> jzjList=new ArrayList<JZJ>();
        jzjList=jzjDAO.getAllJZJ();
        return  jzjList;
    }
}
