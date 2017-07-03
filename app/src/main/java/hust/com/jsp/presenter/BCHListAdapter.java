package hust.com.jsp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.bean.BCInfo;

/**
 * Created by lm on 2017/6/28.
 */

public class BCHListAdapter extends ArrayAdapter<BCInfo>{
    private int resourceID;
    public BCHListAdapter(Context context, int resourceID, List<BCInfo> bcInfoList){

        super(context,resourceID,bcInfoList);
        this.resourceID=resourceID;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BCInfo bcInfo=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.editTextName= (TextView) view.findViewById(R.id.editText1);
            viewHolder.editTextTime= (TextView) view.findViewById(R.id.editText2);
            viewHolder.editEndTime= (TextView) view.findViewById(R.id.editText3);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.editTextName.setText(bcInfo.getName());
        viewHolder.editTextTime.setText(bcInfo.getStartDisplayTime());
        viewHolder.editEndTime.setText(bcInfo.getEndDisplayTime());
        return view;
    }
    class ViewHolder{
        TextView editTextName;
        TextView editTextTime;
        TextView editEndTime;
    }
}
