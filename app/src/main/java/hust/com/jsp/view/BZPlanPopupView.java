package hust.com.jsp.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.TimeZoneFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
    private Button gasButton,fluidButton,coolButton,airButton,weaponButton,oxygenButton,electricityButton,guidButton;
    private Button addBZItem,deleteButton,cancelButton,saveBZItem;
    private TextView jzjName,zwName,selectZWName;
    private TextView aLabel,bLabel,cLabel,dLabel,eLabel,fLabel,gLabel,hLabel,iLabel;
    private TextView number1Label,number2Label,number3Label,number4Label,number5Label,number6Label,number7Label,number8Label,number9Label,number0Label;
    private Spinner spinner;
    private ListView zwListView;
    private BZPlan bzPlan;
    private BZPlanItem bzPlanItem;
    private JZJ jzj;
    private Station station;
    private boolean[] actions;
    private MapView mapView;
    List<Station> stationList;
    private ZW_StationAdapter zwStationAdapter;

    public BZPlanPopupView(Activity context, final BZPlan bzPlan, final MapView mapView){
        super(context);
        this.bzPlan=bzPlan;
        this.jzj=bzPlan.getJzj();
        this.mapView=mapView;
        this.stationList=new ArrayList<>();
        for(BZPlanItem item: bzPlan.getBzPlanItemList()){
            stationList.add(item.getStation());
        }
        this.zwStationAdapter=new ZW_StationAdapter(context,stationList);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView=inflater.inflate(R.layout.makebzplan,null);
        jzjName=(TextView)popView.findViewById(R.id.currentJZJName);
        selectZWName=(TextView)popView.findViewById(R.id.selectStationName);
        zwName=(TextView)popView.findViewById(R.id.currentZWName);
        gasButton= (Button) popView.findViewById(R.id.gasLabel);
        fluidButton= (Button) popView.findViewById(R.id.fluidLabel);
        coolButton= (Button) popView.findViewById(R.id.cooLabel);
        airButton= (Button) popView.findViewById(R.id.airLabel);
        weaponButton= (Button) popView.findViewById(R.id.weaponLabel);
        oxygenButton= (Button) popView.findViewById(R.id.oxygenLabel);
        electricityButton= (Button) popView.findViewById(R.id.electricityLabel);
        guidButton= (Button) popView.findViewById(R.id.guidLabel);
        addBZItem= (Button) popView.findViewById(R.id.addBZItem);
        deleteButton=(Button) popView.findViewById(R.id.deleteBZItem);
        saveBZItem= (Button) popView.findViewById(R.id.saveBZItem);
        cancelButton= (Button) popView.findViewById(R.id.cancelBZItem);
        zwListView= (ListView) popView.findViewById(R.id.zwNameListView);
        zwListView.setAdapter(zwStationAdapter);
        aLabel=(TextView)popView.findViewById(R.id.ALabel);
        bLabel=(TextView)popView.findViewById(R.id.BLabel);
        cLabel=(TextView)popView.findViewById(R.id.CLabel);
        dLabel=(TextView)popView.findViewById(R.id.DLabel);
        eLabel=(TextView)popView.findViewById(R.id.ELabel);
        fLabel=(TextView)popView.findViewById(R.id.FLabel);
        gLabel=(TextView)popView.findViewById(R.id.GLabel);
        hLabel=(TextView)popView.findViewById(R.id.HLabel);
        iLabel=(TextView)popView.findViewById(R.id.ILabel);
        spinner= (Spinner)popView.findViewById(R.id.zwStationSpinner);

        if(bzPlan.getBzPlanItemList().size()==0) {
            station = new Station();
            bzPlanItem = new BZPlanItem();
            actions = new boolean[]{false, false, false, false, false, false, false, false, false};
        }else {
            refreshBZItemInfo(0);
        }

        gasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[0]==false) {
                    actions[0]=true;

                    gasButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(0)));
                }else {
                    actions[0]=false;
                    gasButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        fluidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[3]==false) {
                    actions[3]=true;
                    fluidButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(3)));
                }else {
                    actions[3]=false;
                    fluidButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        coolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[6]==false) {
                    actions[6]=true;
                    coolButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(6)));
                }else {
                    actions[6]=false;
                    coolButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        airButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[1]==false) {
                    actions[1]=true;
                    airButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(1)));
                }else {
                    actions[1]=false;
                    airButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        weaponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[4]==false) {
                    actions[4]=true;
                    weaponButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(4)));
                }else {
                    actions[4]=false;
                    weaponButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        oxygenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[7]==false) {
                    actions[7]=true;
                    oxygenButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(7)));
                }else {
                    actions[7]=false;
                    oxygenButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        electricityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[2]==false) {
                    actions[2]=true;
                    electricityButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(2)));
                }else {
                    actions[2]=false;
                    electricityButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
            }
        });

        guidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actions[5]==false) {
                    actions[5]=true;
                    guidButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(5)));
                }else {
                    actions[5]=false;
                    guidButton.setBackground(new ColorDrawable(Color.LTGRAY));
                }
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

        //添加新ZWItem
        addBZItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                station=new Station();
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
                station.setDisplayName("A"+2);
                bzPlanItem.setStation(station);
                bzPlanItem.setAddGas(actions[0]);
                bzPlanItem.setAddAir(actions[1]);
                bzPlanItem.setAddElectricity(actions[2]);
                bzPlanItem.setAddFluid(actions[3]);
                bzPlanItem.setAddWeapon(actions[4]);
                bzPlanItem.setAddGuide(actions[5]);
                bzPlanItem.setAddCool(actions[6]);
                bzPlanItem.setAddOxygen(actions[7]);
                if(!stationList.contains(station))
                    zwStationAdapter.add(station);
                if(!bzPlan.getBzPlanItemList().contains(bzPlanItem))
                    bzPlan.addBZPlanItem(bzPlanItem);
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
            gasButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(0)));
        if(actions[1])
            airButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(1)));
        if(actions[2])
            electricityButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(2)));
        if(actions[3])
            fluidButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(3)));
        if(actions[4])
            weaponButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(4)));
        if(actions[5])
            guidButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(5)));
        if(actions[6])
            coolButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(6)));
        if(actions[7])
            oxygenButton.setBackground(new ColorDrawable(LabelColorCollection.getColor(7)));
    }

    private void  clearBZItemState(){
        gasButton.setBackground(new ColorDrawable(Color.LTGRAY));
        airButton.setBackground(new ColorDrawable(Color.LTGRAY));
        electricityButton.setBackground(new ColorDrawable(Color.LTGRAY));
        fluidButton.setBackground(new ColorDrawable(Color.LTGRAY));
        weaponButton.setBackground(new ColorDrawable(Color.LTGRAY));
        guidButton.setBackground(new ColorDrawable(Color.LTGRAY));
        coolButton.setBackground(new ColorDrawable(Color.LTGRAY));
        oxygenButton.setBackground(new ColorDrawable(Color.LTGRAY));
    }

}
