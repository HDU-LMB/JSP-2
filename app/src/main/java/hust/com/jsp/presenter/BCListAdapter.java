package hust.com.jsp.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hust.com.jsp.R;

import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoSeries;

/**
 * Created by admin on 2017/5/12.
 */

public class BCListAdapter extends BaseAdapter {
    BCInfoSeries bcInfoSeries;
    Context mContext;
    int focus;

    public BCListAdapter(Context mContext, BCInfoSeries bcInfoSeries){
        this.mContext = mContext;
        this.bcInfoSeries = bcInfoSeries;
        this.focus = 0;
    }

    public int getFocus(){
        return this.focus;
    }

    @Override
    public int getCount() {
        return bcInfoSeries.Count()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position < bcInfoSeries.Count())
        {
            return bcInfoSeries.get(position);
        }
        else{
            return new BCInfo();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.layout_bclist_item,null);
            vh.mView = convertView;
            vh.tv_hc_name = (TextView) convertView.findViewById(R.id.textview_hc_name);
            vh.tv_hc_time = (TextView) convertView.findViewById(R.id.textview_hc_time_info);
            vh.tv_hc_jzjlist = (TextView) convertView.findViewById(R.id.textview_hc_jzjlist);
            vh.backGround = convertView.findViewById(R.id.bc_list_background);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        BCInfo bcInfo = (BCInfo) this.getItem(position);
        vh.tv_hc_name.setText(bcInfo.getName());
        vh.tv_hc_time.setText(bcInfo.getDisplayTime());
        vh.tv_hc_jzjlist.setText(bcInfo.getJZJString());
        if(focus == position) vh.backGround.setBackgroundColor(Color.LTGRAY);
        else vh.backGround.setBackgroundColor(Color.TRANSPARENT);

        return convertView;
    }

    public BCInfo getSelection() {
        return (BCInfo) this.getItem(focus);
    }

    public void setFocus(int aInt){
        this.focus = aInt;
    }

    public void createNewLine(BCInfo bcInfo) {
        this.bcInfoSeries.add(bcInfo);
    }

    private class ViewHolder{
        public View mView;
        public TextView tv_hc_name;
        public TextView tv_hc_time;
        public TextView tv_hc_jzjlist;
        public View backGround;
    }
}
