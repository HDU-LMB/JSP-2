package hust.com.jsp.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import hust.com.jsp.R;

public class ScheduleActivity extends AppCompatActivity {

    protected View fxPlanView;
    protected View mapView;
    protected View mapView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule_tabhost);

        TabHost tabHost_schedule = (TabHost) findViewById(R.id.tabhost_schedule);
        LocalActivityManager groupActivity =
                new LocalActivityManager(this,false);
        groupActivity.dispatchCreate(savedInstanceState);
        tabHost_schedule.setup(groupActivity);

        Intent intent3 = new Intent();
        intent3.setClass(getApplicationContext(), Main2Activity.class);
        TabHost.TabSpec tabSpec3 = tabHost_schedule.newTabSpec("FXJH").setIndicator("FXJH").setContent(intent3);
        tabHost_schedule.addTab(tabSpec3);

        final Intent intent1 = new Intent();
        intent1.setClass(getApplicationContext(), BCDetailActivity.class);
        TabHost.TabSpec tabSpec1 = tabHost_schedule.newTabSpec("编辑BC").setIndicator("编辑BC").setContent(intent1);
        tabHost_schedule.addTab(tabSpec1);

        Intent intent2 = new Intent();
        intent2.setClass(getApplicationContext(), MainActivity.class);
        TabHost.TabSpec tabSpec2 = tabHost_schedule.newTabSpec("编辑BZJH").setIndicator("编辑BZJH").setContent(intent2);
        tabHost_schedule.addTab(tabSpec2);


        tabHost_schedule.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabHost tabhost = (TabHost)findViewById(R.id.tabhost_schedule);
                View contentView = tabhost.getCurrentView();
                if(fxPlanView == null)
                    fxPlanView =  contentView.findViewById(R.id.FXPlanView);
                if (mapView == null)
                    mapView = contentView.findViewById(R.id.MapView);
                if (mapView1 == null)
                    mapView1 = contentView.findViewById(R.id.MapView1);

                switch (tabId) {
                    case "FXJH":
                        if(fxPlanView != null) fxPlanView.setVisibility(View.VISIBLE);
                        if(mapView != null) mapView.setVisibility(View.INVISIBLE);
                        if(mapView1 != null) mapView1.setVisibility(View.INVISIBLE);
                        break;
                    case "编辑BC":
                        if(fxPlanView != null) fxPlanView.setVisibility(View.INVISIBLE);
                        if(mapView != null) mapView.setVisibility(View.INVISIBLE);
                        if(mapView1 != null) mapView1.setVisibility(View.VISIBLE);
                        break;
                    case "编辑BZJH":
                        if(fxPlanView != null) fxPlanView.setVisibility(View.INVISIBLE);
                        if(mapView != null) mapView.setVisibility(View.VISIBLE);
                        if(mapView1 != null) mapView1.setVisibility(View.INVISIBLE);

                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TabHost tabhost = (TabHost)findViewById(R.id.tabhost_schedule);
        View contentView = tabhost.getCurrentView();
        if(fxPlanView == null)
            fxPlanView =  contentView.findViewById(R.id.FXPlanView);
        if (mapView == null)
            mapView = contentView.findViewById(R.id.MapView);
    }
}
