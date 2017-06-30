package hust.com.jsp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.bean.JZJ;

/**
 * Created by lm on 2017/6/28.
 */

public class BCJZJHLAdapter extends ArrayAdapter<JZJ>{
    private int resourceID;
    public BCJZJHLAdapter(Context context, int resourceID, List<JZJ> jzjList){
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
            viewHolder.imageViewJZJ= (ImageView) view.findViewById(R.id.jzj_image);
            viewHolder.editTextJZJ= (TextView) view.findViewById(R.id.jzj_name);
            viewHolder.editTextTYPE= (TextView) view.findViewById(R.id.jzj_type);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.imageViewJZJ.setImageResource(R.drawable.jzj);
        viewHolder.editTextJZJ.setText(jzj.getDisplayName());
        viewHolder.editTextTYPE.setText("TYPE");
        return view;
    }
    class ViewHolder{
        ImageView imageViewJZJ;
        TextView editTextJZJ;
        TextView editTextTYPE;
    }
}