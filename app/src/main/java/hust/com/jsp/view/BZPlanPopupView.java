package hust.com.jsp.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.TimeZoneFormat;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onlylemi.mapview.library.MapView;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.activity.MainActivity;
import hust.com.jsp.bean.BZPlan;
import hust.com.jsp.bean.BZPlanItem;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Station;
import hust.com.jsp.presenter.ZW_StationAdapter;
import hust.com.jsp.utils.LabelColorCollection;


/**
 * Created by Michael-Lee on 2017/6/29.
 * 说明：制定与修改BZJH，实现对飞机的ZW和任务的增删改操作
 */

public class BZPlanPopupView extends PopupWindow {

    View popView;
    private TextView gasLabel,fluidLabel,coolLabel,airLabel,weaponLabel,oxygenLabel,electricityLabel,guidLabel;
    private Button deleteButton,cancelButton,saveBZItem;
    private TextView jzjName,zwName,selectZWName;
    private TextView addBZItem,aLabel,bLabel,cLabel,dLabel,eLabel,fLabel,gLabel,hLabel,iLabel;
    private TextView number1Label,number2Label,number3Label,number4Label,number5Label,number6Label,number7Label,number8Label,number9Label,number0Label;
    private Drawable gasDrawable,fluidDrawable,coolDrawable,airDrawable,weaponDrawable,oxygenDrawable,electricDrawable,guidDrawable,grayDrawable;

    private Spinner spinnr;
    private ListView zwStationListView;//所有zw
    private Station selectedStaion;
    private ListView zwListView;//当前包含的zw
    private BZPlan bzPlan;
    private BZPlanItem bzPlanItem;
    private JZJ jzj;
    private Station station;
    private boolean[] actions;
    private MapView mapView;
    List<Station> stationList;//当前JZJ（bcplanitem）所添加的zw
    private List<Station> stationTotalList;//HM上所有的ZW列表
    private ZW_StationAdapter zwStationAdapter;
    private ZW_StationAdapter zwStationTotalAdapter;

    public BZPlanPopupView(final Activity context, final BZPlan bzPlan, final MapView mapView){
        super(context);
        this.bzPlan=bzPlan;
        this.jzj=bzPlan.getJzj();
        this.mapView=mapView;
        this.stationList=new ArrayList<>();
        this.stationTotalList=new ArrayList<>();
        this.selectedStaion=new Station();
        for(BZPlanItem item: bzPlan.getBzPlanItemList()){
            stationList.add(item.getStation());
        }
        this.zwStationAdapter=new ZW_StationAdapter(context,stationList);
        this.zwStationTotalAdapter=new ZW_StationAdapter(context,stationTotalList);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView=inflater.inflate(R.layout.makebzplan,null);
        jzjName=(TextView)popView.findViewById(R.id.currentJZJName);
        selectZWName=(TextView)popView.findViewById(R.id.selectStationName);
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
        cancelButton= (Button) popView.findViewById(R.id.cancelBZItem);
        zwListView= (ListView) popView.findViewById(R.id.zwNameListView);
        zwStationListView=(ListView) popView.findViewById(R.id.staionTotalListView);
        zwListView.setAdapter(zwStationAdapter);
//        zwListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        zwStationListView.setAdapter(zwStationTotalAdapter);
        aLabel=(TextView)popView.findViewById(R.id.ALabel);
        bLabel=(TextView)popView.findViewById(R.id.BLabel);
        cLabel=(TextView)popView.findViewById(R.id.CLabel);
        dLabel=(TextView)popView.findViewById(R.id.DLabel);
        eLabel=(TextView)popView.findViewById(R.id.ELabel);
        fLabel=(TextView)popView.findViewById(R.id.FLabel);
        gLabel=(TextView)popView.findViewById(R.id.GLabel);
        hLabel=(TextView)popView.findViewById(R.id.HLabel);
        iLabel=(TextView)popView.findViewById(R.id.ILabel);
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
        clearBZItemState();
//        spinner= (Spinner)popView.findViewById(R.id.zwStationSpinner);
        //从数据库表读取ZW信息，这里为测试数据
        stationTotalList.add(new Station(1,new Point(100,100),"A1"));
        stationTotalList.add(new Station(2,new Point(200,100),"A2"));
        stationTotalList.add(new Station(3,new Point(300,100),"A3"));
        stationTotalList.add(new Station(4,new Point(500,200),"B1"));
        stationTotalList.add(new Station(1,new Point(600,150),"B2"));
        stationTotalList.add(new Station(1,new Point(400,300),"C1"));
        ZW_StationAdapter adapter=new ZW_StationAdapter(context,stationTotalList);
//        spinner.setAdapter(adapter);
        if(bzPlan.getBzPlanItemList().size()==0) {
            station = new Station();
            bzPlanItem = new BZPlanItem();
            actions = new boolean[]{false, false, false, false, false, false, false, false, false};
        }else {
            refreshBZItemInfo(0);
        }


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
            }
        });

        weaponLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[4]==false) {
                    actions[4]=true;
                    weaponLabel.setBackground(weaponDrawable);
                }else {
                    actions[4]=false;
                    weaponLabel.setBackground(grayDrawable);
                }
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
            }
        });

        aLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        zwStationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStaion=(Station) zwStationListView.getItemAtPosition(position);
            }
        });
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
                bzPlanItem=new BZPlanItem();
                actions=new boolean[]{false,false,false,false,false,false,false,false,false};
                clearBZItemState();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bzPlan.getBzPlanItemList().contains(bzPlanItem))
                    bzPlan.removeBZPlanItem(bzPlanItem);
                if(stationList.contains(station))
                    zwStationAdapter.remove(station);

                if(stationList.size()!=0) {
                    zwListView.setSelection(stationList.size() - 1);
//                    if (zwListView.isSelected())
                    refreshBZItemInfo((int) zwListView.getSelectedItemId());
                }
                mapView.refresh();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                dismiss();//销毁弹出框
            }
        });

        //保存信息
        saveBZItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStaion.getDisplayName()==null || "".equals(selectedStaion.getDisplayName())){
                    Toast.makeText(context,"请选择ZW！",Toast.LENGTH_LONG).show();
                    return;
                }
                station=selectedStaion;
                bzPlanItem.setStation(station);
                bzPlanItem.setAddGas(actions[0]);
                bzPlanItem.setAddAir(actions[1]);
                bzPlanItem.setAddElectricity(actions[2]);
                bzPlanItem.setAddFluid(actions[3]);
                bzPlanItem.setAddWeapon(actions[4]);
                bzPlanItem.setAddGuide(actions[5]);
                bzPlanItem.setAddCool(actions[6]);
                bzPlanItem.setAddOxygen(actions[7]);

                if(!bzPlan.getBzPlanItemList().contains(bzPlanItem)) {
                    bzPlan.addBZPlanItem(bzPlanItem);
                    zwListView.setSelection(stationList.size()-1);
                    if(!stationList.contains(station))
                        zwStationAdapter.add(station);
                }else {
                    int pos=(int)zwListView.getSelectedItemId();
                    zwStationAdapter.remove(pos+1);
                    zwStationAdapter.add(pos+1,station);
                }
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
        clearBZItemState();
        bzPlanItem=bzPlan.getBzPlanItemList().get(pos);
        station=bzPlanItem.getStation();
        actions=bzPlanItem.getActions();
        jzjName.setText(jzj.getDisplayName());
        zwName.setText(station.getDisplayName());
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
        gasLabel.setBackground(grayDrawable);
        airLabel.setBackground(grayDrawable);
        electricityLabel.setBackground(grayDrawable);
        fluidLabel.setBackground(grayDrawable);
        weaponLabel.setBackground(grayDrawable);
        guidLabel.setBackground(grayDrawable);
        coolLabel.setBackground(grayDrawable);
        oxygenLabel.setBackground(grayDrawable);
    }

}
