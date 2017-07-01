package hust.com.jsp.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import hust.com.jsp.R;
import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BLInfo;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.presenter.BCHListAdapter;
import hust.com.jsp.presenter.BCJZJHLAdapter;
import hust.com.jsp.presenter.BCJZJListAdapter;
import hust.com.jsp.view.BLJZJLayer;
import hust.com.jsp.view.HorizontalListView;

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
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("asd","create"+BCDetailActivity.this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bcdetail);
        jzjList=new ArrayList<JZJ>();
        bcjzjList=new ArrayList<JZJ>();
        bcInfoList=new ArrayList<BCInfo>();
        jzjList=getAllJZJ();
     //   initData();
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
            }
        });
        jzjHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JZJ info=jzjList.get(position);
                jzjID=info.getId();
                Log.v("bcdetail",String.valueOf(info.getId()));
                isseleted=true;

            }
        });
        jzjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JZJ info=bcjzjList.get(position);
                removeJZJfromBC(info);
                refreshBCJZJList(bcID);

            }
        });
        mapView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isseleted){
                    List<BLInfo> blInfoList=blMap.get(bcID);
                    BLInfo info=new BLInfo();
                    info.setBcid(bcID);
                    info.setJzjid(jzjID);
                    info.setPoint(mapView.getTouchPoint());
                    blInfoList.add(info);
                    blMap.put(bcID,blInfoList);
                    refreshBCJZJList(bcID);
                    refreshMap(bcID);
                    Log.v("bcdetail","add");
                    isseleted=false;
                }

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBCInfo();
            }
        });
    }
    private void saveBCInfo(){
        // TODO: 2017/6/29
        Log.v("bc","save");
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
    private void refreshMap(int bcID){
        mapView.clearLayer();

        List<BLInfo> blInfoList=blMap.get(bcID);
        for(BLInfo info:blInfoList){
            BLJZJLayer layer=new BLJZJLayer(mapView,getResources());
            layer.setJzjID(info.getJzjid());
            Log.v("bc",info.getPoint().toString()+" id  "+info.getJzjid());
            layer.setLocation(info.getPoint());
            mapView.addLayer(layer);
        }
        Log.v("bc",String.valueOf(mapView.getLayers().size()));
        mapView.refresh();
    }
    private void refreshBCJZJList(int bcID){
        bcjzjList.clear();
        List<BLInfo> blInfoList=blMap.get(bcID);
        for(BLInfo info:blInfoList){
            JZJ jzj=new JZJ(info.getJzjid(),"JZJ"+info.getJzjid(),"BY1");
            bcjzjList.add(jzj);
        }
        jzjListAdapter.notifyDataSetChanged();
    }
    private List<JZJ> getAllJZJ(){
        List<JZJ> jzjList=new ArrayList<JZJ>();
        jzjList.add(new JZJ(1,"JZJ-1","BY"));
        jzjList.add(new JZJ(2,"JZJ-2","BY1"));
        jzjList.add(new JZJ(3,"JZJ-3","BY"));
        jzjList.add(new JZJ(4,"JZJ-4","BY1"));
        jzjList.add(new JZJ(5,"JZJ-5","BY"));
        jzjList.add(new JZJ(6,"JZJ-6","BY1"));
        return  jzjList;
    }
    private void initData(){

        calendar.set(2017,6,28,12,0,0);
        bcInfoList.add(new BCInfo("BC1",calendar));
        calendar.set(2017,6,28,15,0,0);
        bcInfoList.add(new BCInfo("BC2",calendar));
        bcInfoList.add(new BCInfo("BC3",calendar));
        bcInfoList.add(new BCInfo("BC4",calendar));
        bcInfoList.add(new BCInfo("BC5",calendar));
        bcInfoList.add(new BCInfo("BC1",calendar));
        bcInfoList.add(new BCInfo("BC2",calendar));
        bcInfoList.add(new BCInfo("BC3",calendar));
        bcInfoList.add(new BCInfo("BC4",calendar));
        bcInfoList.add(new BCInfo("BC5",calendar));
    }
    private void addBCInfo()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View bcDialog = inflater.inflate(R.layout.new_bc_dialog, null);
        final EditText etBCName = (EditText) bcDialog.findViewById(R.id.et_bc_name);
        final TimePicker tpTime = (TimePicker) bcDialog.findViewById(R.id.tp_time);
        final DatePicker dpTime = (DatePicker) bcDialog.findViewById(R.id.dp_time);
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
                bcInfo.setTime(calendar.getTimeInMillis());
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
    class JZJaddChecked{

    }
}