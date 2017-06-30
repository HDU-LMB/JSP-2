package hust.com.jsp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;

/**
 * Created by lm on 2017/6/28.
 */

public class BCJZJListAdapter extends ArrayAdapter<JZJ>{
    private int resourceID;
    public BCJZJListAdapter(Context context, int resourceID, List<JZJ> jzjList){

        super(context,resourceID,jzjList);
        this.resourceID=resourceID;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JZJ jzj=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.editTextJZJ= (TextView) view.findViewById(R.id.editText1);
            viewHolder.editTextTYPE= (TextView) view.findViewById(R.id.editText2);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.editTextJZJ.setText(jzj.getDisplayName());
        viewHolder.editTextTYPE.setText(jzj.getJzjBeiyong());
        return view;
    }

    class ViewHolder{
        TextView editTextJZJ;
        TextView editTextTYPE;

    }
}
