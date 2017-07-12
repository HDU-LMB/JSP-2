package hust.com.jsp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.onlylemi.mapview.library.MapView;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Location;
import hust.com.jsp.bean.Station;
import hust.com.jsp.dao.LocationDAO;
import hust.com.jsp.presenter.ZW_StationAdapter;


/**
 * Created by Michael-Lee on 2017/6/29.
 * 说明：制定与修改BZJH，实现对飞机的ZW和任务的增删改操作
 */

public class BZPlanPopupView extends PopupWindow {

    View popView;
    private TextView gasLabel,fluidLabel,coolLabel,airLabel,weaponLabel,oxygenLabel,electricityLabel,guidLabel;
    private Button deleteButton,saveBZItem;
    private TextView jzjName,spendTime,zwName, deleteZWName;
    private TextView addBZItem,aLabel,bLabel,cLabel,dLabel,eLabel,fLabel,gLabel,hLabel, zLabel;
    private TextView number1Label,number2Label,number3Label,number4Label,number5Label,number6Label,number7Label,number8Label,number9Label,number0Label;
    private Drawable gasDrawable,fluidDrawable,coolDrawable,airDrawable,weaponDrawable,oxygenDrawable,electricDrawable,guidDrawable,grayDrawable;
    private boolean isAdd;//新否为添加新项
//    private Spinner spinnr;
//    private ListView zwStationListView;//所有zw
    private Station selectedStaion;
    private ListView zwListView;//当前包含的zw
    private BZPlan bzPlan;
    private BZPlanItem bzPlanItem;
    private JZJ jzj;
    private Station station;
    private boolean[] actions;
    private MapView mapView;
    stationValue putValue;
    List<Station> stationList;//当前JZJ（bcplanitem）所添加的zw
    private List<Station> stationTotalList;//HM上所有的ZW列表
    private ZW_StationAdapter zwStationAdapter;
    private ZW_StationAdapter zwStationTotalAdapter;
    private int position=0;
    private Context context;
    private LocationDAO locationDAO;
    private List<Location> locationList;
    public BZPlanPopupView(final Activity context, final BZPlan bzPlan, final MapView mapView){
        super(context);
        this.context=context;
        this.bzPlan=bzPlan;
        this.isAdd=false;
        this.jzj=bzPlan.getJzj();
        this.mapView=mapView;
        this.stationList=new ArrayList<>();
        this.stationTotalList=new ArrayList<>();
        this.selectedStaion=new Station();
        locationDAO=new LocationDAO(context);
        locationList=locationDAO.getAllLocation();
        for(BZPlanItem item: bzPlan.getBzPlanItemList()){
            stationList.add(item.getStation());
        }
        this.zwStationAdapter=new ZW_StationAdapter(context,stationList);
//        this.zwStationTotalAdapter=new ZW_StationAdapter(context,stationTotalList);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView=inflater.inflate(R.layout.make_bzplan,null);
        jzjName=(TextView)popView.findViewById(R.id.currentJZJName);
        spendTime=(TextView)popView.findViewById(R.id.spendTime);
//        selectZWName=(TextView)popView.findViewById(R.id.selectStationName);
        zwName=(TextView)popView.findViewById(R.id.currentZWName);
        gasLabel= (TextView) popView.findViewById(R.id.gasLabel);
        gasDrawable=gasLabel.getBackground();
        fluidLabel= (TextView) popView.findViewById(R.id.fluidLabel);
        fluidDrawable=fluidLabel.getBackground();
        coolLabel= (TextView) popView.findViewById(R.id.cooLabel);
        coolDrawable=coolLabel.getBackground();
        airLabel= (TextView) popView.findViewById(R.id.airLabel);
        airDrawable=airLabel.getBackground();
        weaponLabel= (TextView) popView.findViewById(R.id.weaponLabel);
        weaponDrawable=weaponLabel.getBackground();
        oxygenLabel= (TextView) popView.findViewById(R.id.oxygenLabel);
        oxygenDrawable=oxygenLabel.getBackground();
        electricityLabel= (TextView) popView.findViewById(R.id.electricityLabel);
        electricDrawable=electricityLabel.getBackground();
        guidLabel= (TextView) popView.findViewById(R.id.guidLabel);
        guidDrawable=guidLabel.getBackground();
        addBZItem= (TextView) popView.findViewById(R.id.addBZItem);
        grayDrawable=addBZItem.getBackground();
        deleteButton=(Button) popView.findViewById(R.id.deleteBZItem);
        saveBZItem= (Button) popView.findViewById(R.id.saveBZItem);
        zwListView= (ListView) popView.findViewById(R.id.zwNameListView);
//        zwStationListView=(ListView) popView.findViewById(R.id.staionTotalListView);
        zwListView.setAdapter(zwStationAdapter);
//        zwListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        zwStationListView.setAdapter(zwStationTotalAdapter);
        aLabel=(TextView)popView.findViewById(R.id.ALabel);
        bLabel=(TextView)popView.findViewById(R.id.BLabel);
        cLabel=(TextView)popView.findViewById(R.id.CLabel);
        dLabel=(TextView)popView.findViewById(R.id.DLabel);
        eLabel=(TextView)popView.findViewById(R.id.ELabel);
        fLabel=(TextView)popView.findViewById(R.id.FLabel);
        gLabel=(TextView)popView.findViewById(R.id.GLabel);
        hLabel=(TextView)popView.findViewById(R.id.HLabel);
        zLabel =(TextView)popView.findViewById(R.id.ZLabel);
        number0Label=(TextView)popView.findViewById(R.id.number0Label);
        number1Label=(TextView)popView.findViewById(R.id.number1Label);
        number2Label=(TextView)popView.findViewById(R.id.number2Label);
        number3Label=(TextView)popView.findViewById(R.id.number3Label);
        number4Label=(TextView)popView.findViewById(R.id.number4Label);
        number5Label=(TextView)popView.findViewById(R.id.number5Label);
        number6Label=(TextView)popView.findViewById(R.id.number6Label);
        number7Label=(TextView)popView.findViewById(R.id.number7Label);
        number8Label=(TextView)popView.findViewById(R.id.number8Label);
        number9Label=(TextView)popView.findViewById(R.id.number9Label);
        deleteZWName =(TextView)popView.findViewById(R.id.numberdeleteLabel);
        clearBZItemState();
//        spinner= (Spinner)popView.findViewById(R.id.zwStationSpinner);
        //从数据库表读取ZW信息，这里为测试数据
//        stationTotalList.add(new Station(1,new Point(100,100),"A1"));
//        stationTotalList.add(new Station(2,new Point(200,100),"A2"));
//        stationTotalList.add(new Station(3,new Point(300,100),"A3"));
//        stationTotalList.add(new Station(4,new Point(500,200),"B1"));
//        stationTotalList.add(new Station(1,new Point(600,150),"B2"));
//        stationTotalList.add(new Station(1,new Point(400,300),"C1"));
//        ZW_StationAdapter adapter=new ZW_StationAdapter(context,stationTotalList);
//        spinner.setAdapter(adapter);
        if(bzPlan.getBzPlanItemList().size()==0) {
            this.isAdd=true;
            station = new Station();
            bzPlanItem = new BZPlanItem();
            actions = new boolean[]{false, false, false, false, false, false, false, false, false};
        }else {
            refreshBZItemInfo(0);
        }


        putValue=new stationValue();
        putValue.prefix="";
        putValue.sequence="";

        gasLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[0]==false) {
                    actions[0]=true;
                    gasLabel.setBackground(gasDrawable);
                }else {
                    actions[0]=false;
                    gasLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        fluidLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[3]==false) {
                    actions[3]=true;
                    fluidLabel.setBackground(fluidDrawable);
                }else {
                    actions[3]=false;
                    fluidLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        coolLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[6]==false) {
                    actions[6]=true;
                    coolLabel.setBackground(coolDrawable);
                }else {
                    actions[6]=false;
                    coolLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        airLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[1]==false) {
                    actions[1]=true;
                    airLabel.setBackground(airDrawable);
                }else {
                    actions[1]=false;
                    airLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        weaponLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spendTime.setText(calculateSpendTime()+"");
                if(actions[4]==false) {
                    actions[4]=true;
                    weaponLabel.setBackground(weaponDrawable);
                }else {
                    actions[4]=false;
                    weaponLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        oxygenLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[7]==false) {
                    actions[7]=true;
                    oxygenLabel.setBackground(oxygenDrawable);
                }else {
                    actions[7]=false;
                    oxygenLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        electricityLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[2]==false) {
                    actions[2]=true;
                    electricityLabel.setBackground(electricDrawable);
                }else {
                    actions[2]=false;
                    electricityLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        guidLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(actions[5]==false) {
                    actions[5]=true;
                    guidLabel.setBackground(guidDrawable);
                }else {
                    actions[5]=false;
                    guidLabel.setBackground(grayDrawable);
                }
                spendTime.setText(calculateSpendTime()+"");
            }
        });

        aLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="A";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        bLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="B";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        cLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="C";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        dLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="D";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        eLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="E";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        fLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="F";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        gLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="G";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        hLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="H";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        zLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="Z";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number0Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"0";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number1Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"1";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number2Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"2";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number3Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"3";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number4Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"4";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number5Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"5";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number6Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"6";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number7Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"7";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number8Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"8";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        number9Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.sequence=putValue.sequence+"9";
                zwName.setText(putValue.prefix+putValue.sequence);
            }
        });
        deleteZWName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putValue.prefix="";
                putValue.sequence="";
                zwName.setText("");
            }
        });

        zwListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("ZWListView","item selected");
                if (((ListView)parent).getTag() != null){
                    ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((ListView)parent).setTag(view);
                view.setBackgroundColor(Color.LTGRAY);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        zwListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Station station= (Station) zwListView.getItemAtPosition(position);
                bzPlanItem=bzPlan.getBzPlanItemList().get(position);
                refreshBZItemInfo(position);
            }
        });
//        zwStationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedStaion=(Station) zwStationListView.getItemAtPosition(position);
//            }
//        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedStaion=(Station)spinner.getSelectedItem();
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });

        //添加新ZWItem
        addBZItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdd=true;
                zwName.setText("");
                station=new Station();
                bzPlanItem=new BZPlanItem();
                actions=new boolean[]{false,false,false,false,false,false,false,false,false};
                clearBZItemState();
                putValue=new stationValue();
                putValue.prefix="";
                putValue.sequence="";
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bzPlan.getBzPlanItemList().contains(bzPlanItem))
                    bzPlan.removeBZPlanItem(bzPlanItem);
                if(stationList.contains(station))
                    zwStationAdapter.remove(station);

                if(zwStationAdapter.getCount()!=0) {
                    refreshBZItemInfo(zwStationAdapter.getCount()-1);
                }else refreshBZItemInfo(-1);
                mapView.refresh();
            }
        });


        //保存信息
        saveBZItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(zwName.getText())){
                    Toast.makeText(context,"请选择ZW！",Toast.LENGTH_LONG);
                    return;
                }
                int flag=0;
                for(Location info:locationList){
                    if(info.getName().equals(zwName.getText().toString())){
                        flag=1;
                    }
                }
                if(flag==0){
                    Log.v("bzplanview","没有该站位，请重新选择！");
                    return;
                }
                station.setDisplayName(zwName.getText().toString());
                for(Location info:locationList){
                    if(info.getName().equals(zwName.getText().toString())){
                        station.setLocation(info.getPoint());
                        station.setAngle(info.getAngle());
                    }
                }
                bzPlanItem.setSpendTime(calculateSpendTime());
                bzPlanItem.setStation(station);
                bzPlanItem.setAddGas(actions[0]);
                bzPlanItem.setAddAir(actions[1]);
                bzPlanItem.setAddElectricity(actions[2]);
                bzPlanItem.setAddFluid(actions[3]);
                bzPlanItem.setAddWeapon(actions[4]);
                bzPlanItem.setAddGuide(actions[5]);
                bzPlanItem.setAddCool(actions[6]);
                bzPlanItem.setAddOxygen(actions[7]);

                if(isAdd) {
                    zwStationAdapter.add(station);
                    bzPlan.addBZPlanItem(bzPlanItem);
//                    zwStationAdapter.notifyDataSetChanged();
//                    position=stationList.size()-1;
//                    zwListView.setSelection(zwStationAdapter.getCount()-1);
                }else {//修改ZW
                    stationList.clear();
                    for(BZPlanItem item: bzPlan.getBzPlanItemList()){
                        stationList.add(item.getStation());
                    }
                    zwStationAdapter.notifyDataSetChanged();
//                    zwStationAdapter=new ZW_StationAdapter(context,stationList);
//                    zwListView.setAdapter(zwStationAdapter);
                }
                isAdd=false;
                refreshBZItemInfo(stationList.size()-1);
                mapView.refresh();
            }
        });

        this.setContentView(popView);
        this.setWidth(400);
        this.setHeight(400);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.Widget_AppCompat_Toolbar);
    }


    private void refreshBZItemInfo(int pos){
        if(pos<0){
            clearBZItemState();
            return;
        }

        bzPlanItem=bzPlan.getBzPlanItemList().get(pos);
        station=bzPlanItem.getStation();
        actions=bzPlanItem.getActions();
        jzjName.setText(jzj.getDisplayName());
//        putValue.prefix=station.getDisplayName().split("[A-Z]")[0];
//        putValue.sequence=station.getDisplayName().split("^[0-9]")[0];
        zwName.setText(station.getDisplayName());
        spendTime.setText(calculateSpendTime()+"");
        if(actions[0])
            gasLabel.setBackground(gasDrawable);
        if(actions[1])
            airLabel.setBackground(airDrawable);
        if(actions[2])
            electricityLabel.setBackground(electricDrawable);
        if(actions[3])
            fluidLabel.setBackground(fluidDrawable);
        if(actions[4])
            weaponLabel.setBackground(weaponDrawable);
        if(actions[5])
            guidLabel.setBackground(guidDrawable);
        if(actions[6])
            coolLabel.setBackground(coolDrawable);
        if(actions[7])
            oxygenLabel.setBackground(oxygenDrawable);
    }

    private void  clearBZItemState(){
        jzjName.setText("");
        gasLabel.setBackground(grayDrawable);
        airLabel.setBackground(grayDrawable);
        electricityLabel.setBackground(grayDrawable);
        fluidLabel.setBackground(grayDrawable);
        weaponLabel.setBackground(grayDrawable);
        guidLabel.setBackground(grayDrawable);
        coolLabel.setBackground(grayDrawable);
        oxygenLabel.setBackground(grayDrawable);
    }

    //计算完成ZW上的任务所花费的时间，这里暂假设每个任务之间为串联关系
    private float calculateSpendTime(){
        float time=0;
        if(actions[0])
            time+=10;
        if(actions[1])
            time+=10;
        if(actions[2])
            time+=10;
        if(actions[3])
            time+=10;
        if(actions[4])
            time+=10;
        if(actions[5])
            time+=10;
        if(actions[6])
            time+=10;
        if(actions[7])
            time+=10;

        return time;
    }

    class stationValue{
        String prefix;
        String sequence;
//        String orientation;
    }
}
//class numValue{
//    long value;
//}

