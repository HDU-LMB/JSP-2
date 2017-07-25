package hust.com.jsp.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.FXPlanItem;
import hust.com.jsp.bean.Location;
import hust.com.jsp.dao.BCDAO;
import hust.com.jsp.dao.FXPlanDAO;
import hust.com.jsp.dao.JZJDAO;
import hust.com.jsp.view.FXPlanView;
import hust.com.jsp.bean.JZJ;
import hust.com.jsp.bean.Station;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import hust.com.jsp.R;
public class Main2Activity extends AppCompatActivity {
    private JZJDAO jzjDAO;
    FXPlanView fxPlanView;
    private  Spinner sp;
    private Button btnExchange;
    private int displayType=0;
    private BCDAO bcDAO;
    private FXPlanDAO fxPlanDAO;
    private List<BCInfo> bcInfoList=new ArrayList<>();
    private List<FXPlanItem> fxPlanItemList=new ArrayList<>();
    private ArrayAdapter adapter;
    List<String> listDateItems=new ArrayList<>();//spinner数据源，存储日期(年-月-日)
//    @Override
//    protected void onResume(){
//        fxPlanView.setVisibility(View.VISIBLE);
//        View mapView = findViewById(R.id.MapView);
//        mapView.setVisibility(View.INVISIBLE);
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause(){
//        fxPlanView.setVisibility(View.INVISIBLE);
//        super.onPause();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        fxPlanView = (FXPlanView)findViewById(R.id.FXPlanView);
        //SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        //SurfaceHolder surfaceHolder
        jzjDAO=new JZJDAO(this);
        bcDAO=new BCDAO(this);
        fxPlanDAO=new FXPlanDAO(this);

        sp = (Spinner)findViewById(R.id.spinner2);
        adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,listDateItems);
        btnExchange= (Button) findViewById(R.id.btn_exchange);


        Button buttonLeft = (Button)findViewById(R.id.button3);
        Button buttonRight = (Button)findViewById(R.id.button4);
        testExample(adapter);
        sp.setAdapter(adapter);
        fxPlanView.setSelectedDate((String) (sp.getSelectedItem()));
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                fxPlanView.setSelectedDate((String) (sp.getSelectedItem()));
                fxPlanView.updateDateTime();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        refreshData();
        fxPlanView.refresh();


        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxPlanView.scaleView(1);
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxPlanView.scaleView(-1);
            }
        });

        fxPlanView.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int event=fxPlanView.getMotionEvent();
                if(event== MotionEvent.ACTION_MOVE) return;
                int rowNO=(int) (fxPlanView.getPointY()/fxPlanView.getSepRowNorm());
                if(fxPlanView.getItemMap().containsKey(rowNO))
                {
                    editFXJHinfo(fxPlanView.getItemMap().get(rowNO),adapter);
                }
            }
        });
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayType=displayType==0?1:0;
                fxPlanView.setDisplayType(displayType);
            }
        });
/*        fxPlanView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.v("fxplanview","long click");
                int rowNO=(int) (fxPlanView.getPointY()/fxPlanView.getSepRowNorm());
                if(fxPlanView.getItemMap().containsKey(rowNO))
                {
                    deleterFXJHInfo(fxPlanView.getItemMap().get(rowNO));
                    if(fxPlanView.getItemMap().size()==1)
                        removeDateItems(sp,adapter);
                }
                return true;
            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddFXJH);

        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                 addFXJHInfo(adapter);
            }
        });
        Button btnAddBC = (Button) findViewById(R.id.btn_addbc);
        btnAddBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBCInfo();
            }
        });

    }
    private void testExample(ArrayAdapter adapter){
        Station testStation1 = new Station();
        testStation1.setDisplayName("CR1").setID(123);

        Station testStation2 = new Station();
        testStation2.setDisplayName("CR2").setID(124);

        JZJ testjzj1 = new JZJ(321);
        testjzj1.setID(321).setDisplayName("101");

        JZJ testjzj2 = new JZJ(322);
        testjzj2.setID(322).setDisplayName("102");
        Calendar timer = Calendar.getInstance();
        FXPlanItem testItem1 = new FXPlanItem().setType(FXPlanItem.FXPlanType.both);
        timer.set(2016, 12, 28, 10, 10, 0);
        testItem1.setStartTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 11, 10, 0);
        testItem1.setChTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 12, 10, 0);
        testItem1.setFhTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 14, 45, 0);
        testItem1.setEndTime(timer.getTimeInMillis());
        testItem1.setGas(7).setStation(testStation1).setJzj(testjzj1).setPlanName("1101");

        FXPlanItem testItem2 = new FXPlanItem().setType(FXPlanItem.FXPlanType.land);
        timer.set(2016, 12, 28, 10, 40, 0);
        testItem2.setStartTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 11, 40, 0);
        testItem2.setChTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 12, 0, 0);
        testItem2.setFhTime(timer.getTimeInMillis());
        timer.set(2016, 12, 28, 13, 45, 0);
        testItem2.setEndTime(timer.getTimeInMillis());
        testItem2.setGas(5.0f).setStation(testStation2).setJzj(testjzj2).setPlanName("1102");
/*        fxPlanView.addItem(testItem1);
        fxPlanView.addItem(testItem2);*/
        addDateItems(testItem1,adapter);
        addDateItems(testItem2,adapter);
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
        tpTime.setIs24HourView(true);
        tpTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        tpEndTime.setIs24HourView(true);
        tpEndTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
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
                saveBCInfo(bcInfo);
                fxPlanView.addBCInfo(bcInfo);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    private void deleterFXJHInfo(final FXPlanItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除任务");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fxPlanView.removeItem(item);
                //addDateItems(item,dateMap);
                //fxPlanView.updateListDateItems();
                //sp.setSelection(fxPlanView.getListDateItems().size());
                fxPlanView.refresh();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     编辑修改FXJH信息
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editFXJHinfo(final FXPlanItem item,final ArrayAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View fxjhDialog = inflater.inflate(R.layout.edit_fxjh_info, null);
//        TextView renWuNO=(TextView) fxjhDialog.findViewById(R.id.RenWuNO);
//        TextView fjNO=(TextView)fxjhDialog.findViewById(R.id.jzjNO);
//        TextView fxyNO=(TextView)fxjhDialog.findViewById(R.id.fxyNO);
//        TextView gas=(TextView)fxjhDialog.findViewById(R.id.gas);
        final EditText renwuNO = (EditText) fxjhDialog.findViewById(R.id.renWuNO);
        final EditText jzjNOEditText = (EditText) fxjhDialog.findViewById(R.id.jzjNOEditText);
        final EditText fxyNOEditText = (EditText) fxjhDialog.findViewById(R.id.fxyNOEditText);
        final EditText gasEditText = (EditText) fxjhDialog.findViewById(R.id.gasEditText);
        final CheckBox flightCheckBox=(CheckBox) fxjhDialog.findViewById(R.id.flightCheckBox);
        final CheckBox landCheckBox=(CheckBox) fxjhDialog.findViewById(R.id.landCheckBox);
        final DatePicker flightDate=(DatePicker)fxjhDialog.findViewById(R.id.flightDate);
        final DatePicker landDate=(DatePicker)fxjhDialog.findViewById(R.id.landDate);
        final TimePicker flightTime=(TimePicker)fxjhDialog.findViewById(R.id.flightTime);
        final TimePicker landTime=(TimePicker)fxjhDialog.findViewById(R.id.landTime);
        final DatePicker chDate=(DatePicker)fxjhDialog.findViewById(R.id.chDate);
        final DatePicker fhDate=(DatePicker)fxjhDialog.findViewById(R.id.fhDate);
        final TimePicker chTime=(TimePicker)fxjhDialog.findViewById(R.id.chTime);
        final TimePicker fhTime=(TimePicker)fxjhDialog.findViewById(R.id.fhTime);
        //获取FXJH信息
        renwuNO.setText(item.getPlanName());
        jzjNOEditText.setText(item.getJzj().getDisplayName());
        fxyNOEditText.setText(item.getStation().getDisplayName());
        gasEditText.setText(item.getGas()+"");
        if(item.getType()== FXPlanItem.FXPlanType.flight) {
            flightCheckBox.setChecked(true);
            landCheckBox.setChecked(false);
        }
        else if(item.getType()== FXPlanItem.FXPlanType.land) {
            flightCheckBox.setChecked(false);
            landCheckBox.setChecked(true);
        }
        else if(item.getType()== FXPlanItem.FXPlanType.both){
            flightCheckBox.setChecked(true);
            landCheckBox.setChecked(true);
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getStartTime());
        flightDate.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        flightTime.setIs24HourView(true);
        flightTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        flightTime.setMinute(calendar.get(Calendar.MINUTE));
        calendar.setTimeInMillis(item.getEndTime());
        landDate.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        landTime.setIs24HourView(true);
        landTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        landTime.setMinute(calendar.get(Calendar.MINUTE));
        chTime.setIs24HourView(true);
        chTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        chTime.setMinute(calendar.get(Calendar.MINUTE));
        fhTime.setIs24HourView(true);
        fhTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        fhTime.setMinute(calendar.get(Calendar.MINUTE));
        calendar.setTimeInMillis(item.getChTime());
        chDate.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        chTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        chTime.setMinute(calendar.get(Calendar.MINUTE));
        calendar.setTimeInMillis(item.getFhTime());
        fhDate.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        fhTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        fhTime.setMinute(calendar.get(Calendar.MINUTE));
        builder.setView(fxjhDialog);
        builder.setTitle("Modify FXJH");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //检查数据
                if("".equals(renwuNO.getText().toString()) || "".equals(jzjNOEditText.getText().toString())|| "".equals(fxyNOEditText.getText().toString())|| "".equals(gasEditText.getText().toString())){
                    Toast.makeText(Main2Activity.this, "修改失败，请补充完整数据!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(flightCheckBox.isChecked()== false && landCheckBox.isChecked()== false) {
                    Toast.makeText(Main2Activity.this, "修改失败，请勾选复选框!", Toast.LENGTH_LONG).show();
                    return;
                }
                //修改FXJH信息
                item.setPlanName(renwuNO.getText().toString());
                item.getJzj().setDisplayName(jzjNOEditText.getText().toString());
                item.getStation().setDisplayName(fxyNOEditText.getText().toString());
                try {
                    item.setGas(Float.parseFloat(gasEditText.getText().toString()));
                }catch (NumberFormatException ne){
                    Toast.makeText(Main2Activity.this,"格式不正确，请输入数字!",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                if(flightCheckBox.isChecked()== true && landCheckBox.isChecked()== true)
                    item.setType(FXPlanItem.FXPlanType.both);
                else  if(flightCheckBox.isChecked()== true && landCheckBox.isChecked()== false)
                    item.setType(FXPlanItem.FXPlanType.flight);
                else  if(flightCheckBox.isChecked()== false && landCheckBox.isChecked()== true)
                    item.setType(FXPlanItem.FXPlanType.land);
                else {
                    Toast.makeText(Main2Activity.this, "请勾选复选框!", Toast.LENGTH_LONG).show();
                }
                calendar.set(flightDate.getYear(),flightDate.getMonth(),flightDate.getDayOfMonth(),flightTime.getHour(),flightTime.getMinute());
                item.setStartTime(calendar.getTimeInMillis());
                calendar.set(landDate.getYear(),landDate.getMonth(),landDate.getDayOfMonth(),landTime.getHour(),landTime.getMinute());
                item.setEndTime(calendar.getTimeInMillis());
                if(item.getStartTime()==item.getEndTime()) Toast.makeText(Main2Activity.this, "FJ起降时间不能相同，请重设时间!", Toast.LENGTH_LONG).show();
                calendar.set(chDate.getYear(),chDate.getMonth(),chDate.getDayOfMonth(),chTime.getHour(),chTime.getMinute());
                item.setChTime(calendar.getTimeInMillis());
                calendar.set(fhDate.getYear(),fhDate.getMonth(),fhDate.getDayOfMonth(),fhTime.getHour(),fhTime.getMinute());
                item.setFhTime(calendar.getTimeInMillis());
/*                if(item.getChTime()==item.getFhTime()) {
                    Toast.makeText(Main2Activity.this, "FJ出航返航时间不能相同，请重设时间!", Toast.LENGTH_LONG).show();
                    return;
                } */
                addDateItems(item,adapter);
                fxPlanDAO.deleteFXPlan(item.getFx_id());
                fxPlanDAO.addInfo(item);
               // fxPlanView.updateListDateItems();
                fxPlanView.refresh();
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(fxPlanView.getItemMap().size()==1){
                    removeDateItems(sp,adapter);
                }
                fxPlanView.removeItem(item);
                fxPlanDAO.deleteFXPlan(item.getFx_id());
                fxPlanView.refresh();
            }
        });
        builder.create().show();
//        Toast.makeText(Main2Activity.this, "添加成功!", Toast.LENGTH_LONG).show();
    }

    //添加新的FXJH信息
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addFXJHInfo(final ArrayAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View fxjhDialog = inflater.inflate(R.layout.edit_fxjh_info, null);
//        TextView renWuNO=(TextView) fxjhDialog.findViewById(R.id.RenWuNO);
//        TextView fjNO=(TextView)fxjhDialog.findViewById(R.id.jzjNO);
//        TextView fxyNO=(TextView)fxjhDialog.findViewById(R.id.fxyNO);
//        TextView gas=(TextView)fxjhDialog.findViewById(R.id.gas);
        final EditText renwuNO = (EditText) fxjhDialog.findViewById(R.id.renWuNO);
        final EditText jzjNOEditText = (EditText) fxjhDialog.findViewById(R.id.jzjNOEditText);
        final EditText fxyNOEditText = (EditText) fxjhDialog.findViewById(R.id.fxyNOEditText);
        final EditText gasEditText = (EditText) fxjhDialog.findViewById(R.id.gasEditText);
        final CheckBox flightCheckBox=(CheckBox) fxjhDialog.findViewById(R.id.flightCheckBox);
        final CheckBox landCheckBox=(CheckBox) fxjhDialog.findViewById(R.id.landCheckBox);
        final DatePicker flightDate=(DatePicker)fxjhDialog.findViewById(R.id.flightDate);
        final DatePicker landDate=(DatePicker)fxjhDialog.findViewById(R.id.landDate);
        final TimePicker flightTime=(TimePicker)fxjhDialog.findViewById(R.id.flightTime);
        final TimePicker landTime=(TimePicker)fxjhDialog.findViewById(R.id.landTime);
        final DatePicker chDate=(DatePicker)fxjhDialog.findViewById(R.id.chDate);
        final DatePicker fhDate=(DatePicker)fxjhDialog.findViewById(R.id.fhDate);
        final TimePicker chTime=(TimePicker)fxjhDialog.findViewById(R.id.chTime);
        final TimePicker fhTime=(TimePicker)fxjhDialog.findViewById(R.id.fhTime);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        landTime.setIs24HourView(true);
        flightTime.setIs24HourView(true);
        flightTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        flightTime.setMinute(calendar.get(Calendar.MINUTE));
        landTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        landTime.setMinute(calendar.get(Calendar.MINUTE));
        chTime.setIs24HourView(true);
        chTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        chTime.setMinute(calendar.get(Calendar.MINUTE));
        fhTime.setIs24HourView(true);
        fhTime.setHour(calendar.get(Calendar.HOUR_OF_DAY)+8);
        fhTime.setMinute(calendar.get(Calendar.MINUTE));
        flightCheckBox.setChecked(true);

        builder.setView(fxjhDialog);
        builder.setTitle("Modify FXJH");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //检查数据
                if("".equals(renwuNO.getText().toString()) || "".equals(jzjNOEditText.getText().toString())|| "".equals(fxyNOEditText.getText().toString())|| "".equals(gasEditText.getText().toString())){
                    Toast.makeText(Main2Activity.this, "添加失败，请补充完整数据!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flightCheckBox.isChecked()== false && landCheckBox.isChecked()== false) {
                    Toast.makeText(Main2Activity.this, "添加失败，请勾选复选框!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Float.parseFloat(gasEditText.getText().toString());
                }catch (NumberFormatException ne){
                    Toast.makeText(Main2Activity.this,"格式不正确，请输入数字!",Toast.LENGTH_SHORT).show();
                    return;
                }catch (Exception e){
                    Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flightDate.getYear()==landDate.getYear() && flightDate.getMonth()==landDate.getMonth() && flightDate.getDayOfMonth()==landDate.getDayOfMonth() && flightTime.getHour()==landTime.getHour() && flightTime.getMinute()==landTime.getMinute())
                    Toast.makeText(Main2Activity.this, "FJ起降时间不能相同，请重设时间!", Toast.LENGTH_SHORT).show();
                //新增FXJH信息
                FXPlanItem item=new FXPlanItem();
                Station station = new Station();
                String jzjNo=jzjNOEditText.getText().toString();
                String stationFXY=fxyNOEditText.getText().toString();
                station.setDisplayName(stationFXY).setID(1001);  //注意：这里id需要自增长赋值，且保证唯一
/*                JZJ jzj = new JZJ(2001);  //注意：这里参数需要自增长赋值，且保证唯一
                jzj.setDisplayName(jzjNo);*/
                JZJ jzj=jzjDAO.getJZJByName(jzjNo);
                if(jzj==null){
                    Toast.makeText(Main2Activity.this, "输入JZJ名字错误！", Toast.LENGTH_LONG).show();
                    return;
                }
                item.setPlanName(renwuNO.getText().toString());
                try {
                    item.setGas(Float.parseFloat(gasEditText.getText().toString()));
                }catch (NumberFormatException ne){
                    Toast.makeText(Main2Activity.this,"格式不正确，请输入数字!",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                if(flightCheckBox.isChecked()== true && landCheckBox.isChecked()== true)
                    item.setType(FXPlanItem.FXPlanType.both);
                else  if(flightCheckBox.isChecked()== true && landCheckBox.isChecked()== false)
                    item.setType(FXPlanItem.FXPlanType.flight);
                else  if(flightCheckBox.isChecked()== false && landCheckBox.isChecked()== true)
                    item.setType(FXPlanItem.FXPlanType.land);
                else {
                    Toast.makeText(Main2Activity.this, "请勾选复选框!", Toast.LENGTH_LONG).show();
                }
                calendar.set(flightDate.getYear(),flightDate.getMonth(),flightDate.getDayOfMonth(),flightTime.getHour(),flightTime.getMinute());
                item.setStartTime(calendar.getTimeInMillis());
                calendar.set(landDate.getYear(),landDate.getMonth(),landDate.getDayOfMonth(),landTime.getHour(),landTime.getMinute());
                item.setEndTime(calendar.getTimeInMillis());
                if(item.getStartTime()==item.getEndTime()) {
                    Toast.makeText(Main2Activity.this, "FJ起降时间不能相同，请重设时间!", Toast.LENGTH_LONG).show();
                    return;
                }
                calendar.set(chDate.getYear(),chDate.getMonth(),chDate.getDayOfMonth(),chTime.getHour(),chTime.getMinute());
                item.setChTime(calendar.getTimeInMillis());
                calendar.set(fhDate.getYear(),fhDate.getMonth(),fhDate.getDayOfMonth(),fhTime.getHour(),fhTime.getMinute());
                item.setFhTime(calendar.getTimeInMillis());
/*                if(item.getChTime()==item.getFhTime()) {
                    Toast.makeText(Main2Activity.this, "FJ出航返航时间不能相同，请重设时间!", Toast.LENGTH_LONG).show();
                    return;
                } */
                item.setFx_id(new Random().nextInt());
                item.setStation(station).setJzj(jzj);
                fxPlanView.addItem(item);
                fxPlanDAO.addInfo(item);
                addDateItems(item,adapter);

               // fxPlanView.updateListDateItems();
                //sp.setSelection(fxPlanView.getListDateItems().size());
/*                calendar.setTimeInMillis(item.getStartTime());
                String dateItem=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                String dateTime=dateItem+"-"+calendar.get(Calendar.HOUR_OF_DAY)+"-"+calendar.get(Calendar.MINUTE);

                fxPlanView.setSelectedDate(dateTime);
                fxPlanView.updateDateTime();*/
                fxPlanView.refresh();

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    //用map和list存储FJ的起降日期
    private void addDateItems(FXPlanItem item,final ArrayAdapter adapter) {
        Calendar timer=Calendar.getInstance();
        timer.setTimeInMillis(item.getStartTime());
        String dateItem=timer.get(Calendar.YEAR)+"-"+(timer.get(Calendar.MONTH)+1)+"-"+timer.get(Calendar.DAY_OF_MONTH);
        String dateTime=dateItem+"-"+timer.get(Calendar.HOUR_OF_DAY)+"-"+timer.get(Calendar.MINUTE);
        if(!fxPlanView.getDateMap().containsKey(dateItem)) {
            List<String> listDate=new ArrayList<>();
            listDate.add(dateTime);
            //listDateItems.add(dateItem);
            adapter.add(dateItem);
            fxPlanView.getDateMap().put(dateItem,listDate);
        }else if(fxPlanView.getDateMap().containsKey(dateItem)){
            if(!fxPlanView.getDateMap().get(dateItem).contains(dateTime))
                fxPlanView.getDateMap().get(dateItem).add(dateTime);
        }
        timer.setTimeInMillis(item.getEndTime());
        dateItem=timer.get(Calendar.YEAR)+"-"+(timer.get(Calendar.MONTH)+1)+"-"+timer.get(Calendar.DAY_OF_MONTH);
        dateTime=dateItem+"-"+timer.get(Calendar.HOUR_OF_DAY)+"-"+timer.get(Calendar.MINUTE);
        if(!fxPlanView.getDateMap().containsKey(dateItem)) {
            List<String> listDate=new ArrayList<>();
            listDate.add(dateTime);
            adapter.add(dateItem);
           // listDateItems.add(dateItem);
            fxPlanView.getDateMap().put(dateItem,listDate);
        }else if(fxPlanView.getDateMap().containsKey(dateItem)){
            if(!fxPlanView.getDateMap().get(dateItem).contains(dateTime))
                fxPlanView.getDateMap().get(dateItem).add(dateTime);
        }
        sp.setSelection(adapter.getPosition(dateItem));
        fxPlanView.setSelectedDate(dateItem);
        fxPlanView.updateDateTime();
    }

    private void removeDateItems(Spinner sp,final ArrayAdapter adapter) {
        Object selectedItem=sp.getSelectedItem();
        if(listDateItems.contains(selectedItem)) {
            adapter.remove(selectedItem);
            if(fxPlanView.getDateMap().containsKey(selectedItem))
                fxPlanView.getDateMap().remove(selectedItem);
           // adapter.notifyDataSetChanged();
        }
    }
    public void refreshData(){
        bcInfoList=getAllBCInfo();
        fxPlanItemList=getAllFXPlan();
        fxPlanView.setBcInfoList(bcInfoList);
        fxPlanView.setFXPlanList(fxPlanItemList);
        for(FXPlanItem info:fxPlanItemList){
            addDateItems(info,adapter);
        }

    }
    private List<FXPlanItem> getAllFXPlan(){
        return fxPlanDAO.getAll();
    }
    private List<BCInfo> getAllBCInfo(){
        return bcDAO.getAll();
    }
    private void saveBCInfo(BCInfo info){
        bcDAO.deleteBC(info.getId());
        bcDAO.addInfo(info);
    }
    private Station toStation(Location location){
        Station station=new Station();
        station.setID(location.getId());
        station.setAngle(location.getAngle());
        station.setDisplayName(location.getName());
        station.setLocation(location.getPoint());
        return station;
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
