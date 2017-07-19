package hust.com.jsp.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;
import com.onlylemi.mapview.library.layer.BitmapLayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import hust.com.jsp.R;
import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BLInfo;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.bean.Station;
import hust.com.jsp.dao.BCDAO;
import hust.com.jsp.dao.BLDAO;
import hust.com.jsp.dao.JZJDAO;
import hust.com.jsp.dao.LocationDAO;
import hust.com.jsp.db.JSPDBHelper;
import hust.com.jsp.presenter.BCHListAdapter;
import hust.com.jsp.presenter.BCJZJHLAdapter;
import hust.com.jsp.presenter.BCJZJListAdapter;
import hust.com.jsp.utils.LocationTools;
import hust.com.jsp.view.BLJZJLayer;
import hust.com.jsp.view.HorizontalListView;
import hust.com.jsp.view.LocationLayer;

/**
 * Created by lm on 2017/6/27.
 */

public class BCDetailActivity extends AppCompatActivity {
    private MapView mapView;
    private BCJZJListAdapter jzjListAdapter;
    private List<JZJ> jzjList;
    private List<JZJ> bcjzjList;
    private BCHListAdapter bchListAdapter;
    private List<BCInfo> bcInfoList;
    private BCJZJHLAdapter jzjhlAdapter;
    private int bcID=0;
    private int jzjID=0;
    private boolean isseleted=false;
    private Map<Integer,List<BLInfo>> blMap=new TreeMap<>();
    private List<Location> locationList;
    private Map<Integer,BLJZJLayer> layerMap;
    private int clickType=0;
    Calendar calendar = Calendar.getInstance();
    private LocationDAO locationDAO;
    private JZJDAO jzjDAO;
    private BLDAO blDAO;
    private BCDAO bcDAO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("asd","create"+BCDetailActivity.this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bcdetail);
        locationDAO=new LocationDAO(this);
        jzjDAO=new JZJDAO(this);
        blDAO=new BLDAO(this);
        bcDAO=new BCDAO(this);
        locationList=locationDAO.getAllLocation();
        jzjList=new ArrayList<JZJ>();
        bcjzjList=new ArrayList<JZJ>();
        bcInfoList=new ArrayList<BCInfo>();
        jzjList=getAllJZJ();
        layerMap=new TreeMap<>();
        initData();
        initView();
    }
    private void initView(){
        mapView = (MapView) findViewById(R.id.MapView1);

        {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.map);
                Log.v("asd","create "+bitmap);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }
            mapView.loadMap(bitmap);
        }
        for(Location info:locationList){
            LocationLayer layer=new LocationLayer(mapView,getResources(),info);
            Log.v("bc","location:"+info.getPoint().toString());
            mapView.addLayer(layer);

        }
        for(final JZJ jzj:jzjList){
            final BLJZJLayer layer=new BLJZJLayer(mapView,getResources(),jzj);
            layer.setOnBitmapClickListener(new BitmapLayer.OnBitmapClickListener() {
                @Override
                public void onBitmapClick(BitmapLayer layer1) {
                    layer.isVisible=true;
                    List<BLInfo> blInfoList=blMap.get(bcID);
                    Iterator<BLInfo> iterator = blInfoList.iterator();
                    while(iterator.hasNext()){
                        BLInfo info = iterator.next();
                        if(info.getJzjid()==jzj.getId()) {
                            clickCir();
                            Log.v("bc layer", jzj.getId() + "    type:" + clickType);
                            info.setType(info.getType()+1);
                            //     info.setType(clickType);
                            if (info.getType() == 3) {
                                iterator.remove();
                                //  mapView.removeLayer(layer);
                                // Log.v("bc layer",mapView.getLayers().size()+"");
                                layer.isVisible = false;
                                mapView.refresh();
                            }
                            Log.v("clickJZJ",info.getJzjid()+":"+layer.isVisible+"  ");
                        }

                    }
                    refreshBCJZJList(bcID);
/*                    for(BLInfo info:blInfoList){
                        if(info.getJzjid()==jzj.getId()) {
                            clickCir();
                            Log.v("bc layer",jzj.getId()+"    type:"+clickType);
                            info.setType(clickType);
                            if(info.getType()==3){
                                blInfoList.remove(info);
                              //  mapView.removeLayer(layer);
                               // Log.v("bc layer",mapView.getLayers().size()+"");
                                layer.isVisible=false;
                                mapView.refresh();
                            }
                        }
                    }
                    refreshBCJZJList(bcID);*/
                }
            });
            layerMap.put(jzj.getId(),layer);
        }
        mapView.refresh();
        jzjListAdapter=new BCJZJListAdapter(this, R.layout.bc_jzj_item,bcjzjList);
        ListView jzjListView= (ListView) findViewById(R.id.bc_jzj_listview);
        jzjListView.setAdapter(jzjListAdapter);
        bchListAdapter=new BCHListAdapter(this, R.layout.bc_item,bcInfoList);
        HorizontalListView bcHListView= (HorizontalListView) findViewById(R.id.bc_hlistview);
        bcHListView.setAdapter(bchListAdapter);
        jzjhlAdapter=new BCJZJHLAdapter(this, R.layout.bc_jzj_loc_item,jzjList);
        HorizontalListView jzjHListView= (HorizontalListView) findViewById(R.id.jzj_hlistview);
        jzjHListView.setAdapter(jzjhlAdapter);
        FloatingActionButton fabAdd= (FloatingActionButton) findViewById(R.id.fab_add);
        Button btnSave= (Button) findViewById(R.id.save_button);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBCInfo();
            }
        });
        bcHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BCInfo bcInfo=bcInfoList.get(position);
                bcID=bcInfo.getId();
                Log.v("bcdetail",String.valueOf(bcInfo.getId()));
                refreshBCJZJList(bcID);
                refreshMap(bcID);
                if (((HorizontalListView)parent).getTag() != null){
                    ((View)((HorizontalListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((HorizontalListView)parent).setTag(view);
                view.setBackgroundColor(Color.LTGRAY);
            }
        });
        bcHListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BCDetailActivity.this);
                builder.setTitle("删除BC");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// TODO: 2017/7/17
                        BCInfo info=bcInfoList.get(position);
                        int id=info.getId();
                        deleteBC(id);
                        deleteBL(id);
                        blMap.remove(id);
                        bcInfoList.remove(info);
                        bchListAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                return true;
            }
        });
        jzjHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JZJ info=jzjList.get(position);
                jzjID=info.getId();
                Log.v("bcdetail",String.valueOf(info.getId()));
                isseleted=true;
                if (((HorizontalListView)parent).getTag() != null){
                    ((View)((HorizontalListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((HorizontalListView)parent).setTag(view);
                view.setBackgroundColor(Color.LTGRAY);

            }
        });
/*        jzjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JZJ info=bcjzjList.get(position);
                removeJZJfromBC(info);
                refreshBCJZJList(bcID);

            }
        });*/
        mapView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isseleted){
                    boolean containInfo=false;
                    BLInfo info=new BLInfo();
                    List<BLInfo> blInfoList=blMap.get(bcID);
                    for(BLInfo infoFromList:blInfoList){
                        if(infoFromList.getJzjid()==jzjID){
                            info=infoFromList;
                            containInfo=true;
                        }
                    }
                    Location location= LocationTools.getNearLoaction(mapView.getTouchPoint(),locationList);
                    info.setPoint(location.getPoint());
                    info.setType(0);
                    if(containInfo){

                    }
                    else{
                        info.setBcid(bcID);
                        info.setJzjid(jzjID);
                        blInfoList.add(info);
                    }
                    blMap.put(bcID,blInfoList);
                    refreshBCJZJList(bcID);
                    refreshMap(bcID);
                    isseleted=false;
                    clickType=0;

                }

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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBCInfo();
                saveBLInfo();
            }
        });


    }
    private void deleteBL(int bcID){
        blDAO.deleteByBCID(bcID);
    }
    private void deleteBC(int bcID){
        bcDAO.deleteBC(bcID);
    }
    private void saveBCInfo(){
        // TODO: 2017/6/29
        bcDAO.deleteBC(bcID);
        for(BCInfo info:bcInfoList){
            if(info.getId()==bcID){
                bcDAO.addInfo(info);
            }
        }
    }
    private void saveBLInfo(){
        blDAO.deleteByBCID(bcID);
        List<BLInfo> blInfoList=blMap.get(bcID);
        for(BLInfo info:blInfoList){
            blDAO.addInfo(info);
        }
    }
    private void removeJZJfromBC(JZJ jzj){
        List<BLInfo> blInfoList=blMap.get(bcID);
        for(int i=0;i<blInfoList.size();i++){
            if(blInfoList.get(i).getJzjid()==jzj.getId()){
                Log.v("bc","map id"+String.valueOf(blInfoList.get(i).getJzjid()));
                blInfoList.remove(i);
                i--;

            }
        }
        Log.v("bc","jzj id"+String.valueOf(jzj.getId()));
    }
    private void refreshMap(final int bcID){
        mapView.clearLayer();
        List<BLInfo> blInfoList=blMap.get(bcID);
        for(final BLInfo info:blInfoList){
            BLJZJLayer layer=layerMap.get(info.getJzjid());
            layer.isVisible=true;
            layer.setLocation(info.getPoint());
            Location location=locationDAO.getInfoByPonit(info.getX(),info.getY());
            layer.setAngle(location.getAngle());
            mapView.addLayer(layer);
            Log.v("refreshMap",info.getJzjid()+":"+layer.isVisible);
        }

        for(Location info:locationList){
            LocationLayer layer=new LocationLayer(mapView,getResources(),info);
            Log.v("bc","location:"+info.getPoint().toString());
            mapView.addLayer(layer);
        }
        Log.v("bc",String.valueOf(mapView.getLayers().size()));
        mapView.refresh();
    }
    private void refreshBCJZJList(int bcID){
        bcjzjList.clear();
        List<BLInfo> blInfoList=blMap.get(bcID);
        for(BLInfo info:blInfoList){
            JZJ jzj=jzjDAO.getJZJ(info.getJzjid());
            switch (info.getType()){
                case 1:
                    jzj.setJzjBeiyong("主用");
                    bcjzjList.add(jzj);
                    break;
                case 2:
                    jzj.setJzjBeiyong("备用");
                    bcjzjList.add(jzj);
                    break;
            }

        }
        jzjListAdapter.notifyDataSetChanged();
    }
    private List<JZJ> getAllJZJ(){
        List<JZJ> jzjList=new ArrayList<JZJ>();
        jzjList=jzjDAO.getAllJZJ();
        return  jzjList;
    }
    private void initData(){

        bcInfoList=bcDAO.getAll();
        for(BCInfo info:bcInfoList){
            List<BLInfo> blInfoList=blDAO.getById(info.getId());
            blMap.put(info.getId(),blInfoList);
        }
        bcID=bcInfoList.get(0).getId();

    }
    private void addBCInfo()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View bcDialog = inflater.inflate(R.layout.new_bc_dialog, null);
        final EditText etBCName = (EditText) bcDialog.findViewById(R.id.et_bc_name);
        final TimePicker tpTime = (TimePicker) bcDialog.findViewById(R.id.tp_time);
        final DatePicker dpTime = (DatePicker) bcDialog.findViewById(R.id.dp_time);
        final TimePicker tpEndTime = (TimePicker) bcDialog.findViewById(R.id.tp_end_time);
        final DatePicker dpEndTime = (DatePicker) bcDialog.findViewById(R.id.dp_end_time);
        final Calendar calendar = Calendar.getInstance();
        builder.setView(bcDialog);
        builder.setTitle("ADD BC");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                BCInfo bcInfo=new BCInfo();
                String bcName=etBCName.getText().toString();
                bcInfo.setName(bcName);
                bcInfo.setID(new Random().nextInt());
                calendar.set(dpTime.getYear(),dpTime.getMonth(),dpTime.getDayOfMonth(),tpTime.getHour(),tpTime.getMinute());
                bcInfo.setStartTime(calendar.getTimeInMillis());
                calendar.set(dpEndTime.getYear(),dpEndTime.getMonth(),dpEndTime.getDayOfMonth(),tpEndTime.getHour(),tpEndTime.getMinute());
                bcInfo.setEndTime(calendar.getTimeInMillis());
                bcInfoList.add(bcInfo);
                bchListAdapter.notifyDataSetChanged();
                List<BLInfo> blInfoList=new ArrayList<BLInfo>();
                blMap.put(bcInfo.getId(),blInfoList);
                bcID=bcInfo.getId();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    private void clickCir(){
        if(clickType==3){
            clickType=0;
        }
        else{
            clickType++;
        }
    }
}
