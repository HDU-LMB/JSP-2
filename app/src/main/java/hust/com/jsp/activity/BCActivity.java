package hust.com.jsp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoSeries;
import hust.com.jsp.db.DYDBHelper;
import hust.com.jsp.R;
import hust.com.jsp.presenter.BCDetailAdapter;
import hust.com.jsp.presenter.BCListAdapter;

/**
 * Created by admin on 2017/5/10.
 */

public class BCActivity extends AppCompatActivity {
    BCListAdapter bcListAdapter;
    BCDetailAdapter bcDetailAdapter;

    EditText edit_bc_filter;
    EditText edit_bc_name;
    EditText edit_bc_time;
    EditText edit_bc_type;
    Button button_bc_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule_input);

        edit_bc_filter = (EditText) findViewById(R.id.editText_bcfilter);
        edit_bc_name = (EditText) findViewById(R.id.editText_bcname);
        edit_bc_time = (EditText) findViewById(R.id.editText_bctime);
        edit_bc_type = (EditText) findViewById(R.id.editText_bctype);
        button_bc_save = (Button) findViewById(R.id.button_bc_save);

        DYDBHelper dydbHelper = new DYDBHelper(this);
        BCInfoSeries bcInfoSeries = new BCInfoSeries();
        bcInfoSeries.readFromDB();
        bcListAdapter = new BCListAdapter(this,bcInfoSeries);

        ListView list_bcList = (ListView)findViewById(R.id.zw_BCItemListView);
        list_bcList.setAdapter(bcListAdapter);
        list_bcList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bcListAdapter.setFocus(position);
                //bcDetailAdapter.resetInfo(bcListAdapter.getSelection());
                setData(bcListAdapter.getSelection());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bcListAdapter.setFocus(0);
            }
        });

        button_bc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bcListAdapter.getFocus() == bcListAdapter.getCount()){
                    BCInfo bcInfo = new BCInfo();
                    bcInfo.setName(edit_bc_name.getText().toString());
                    bcListAdapter.createNewLine(bcInfo);
                }
            }
        });

        BCInfo bcInfo  = bcListAdapter.getSelection();
        ListView BC_DetailView = (ListView)findViewById(R.id.list_bc_detail);
        bcDetailAdapter = new BCDetailAdapter(this,bcInfo);
        BC_DetailView.setAdapter(bcDetailAdapter);

    }

    public void setData(BCInfo bcInfo){
        bcDetailAdapter.resetInfo(bcInfo);
        bcDetailAdapter.setBcInfo(bcInfo);
        edit_bc_name.setText(bcInfo.getName());
        edit_bc_time.setText(bcInfo.getDisplayTime());
        edit_bc_type.setText(bcInfo.getType());
        bcDetailAdapter.notifyDataSetChanged();
        bcListAdapter.notifyDataSetChanged();
    }

}
