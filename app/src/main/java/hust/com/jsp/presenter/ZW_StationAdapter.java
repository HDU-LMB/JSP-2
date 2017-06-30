package hust.com.jsp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hust.com.jsp.R;
import hust.com.jsp.bean.Station;

/**
 * Created by Michael-Lee on 2017/6/29.
 */

public class ZW_StationAdapter extends BaseAdapter {

    private Context context;
    private List<Station> stationList;

    public ZW_StationAdapter(Context context,List<Station> stationList){
        this.context=context;
        this.setStationList(stationList);
    }

    public void setStationList(List<Station> stationList){
        if(stationList==null)
            this.stationList=new ArrayList<>();
        else
            this.stationList=stationList;
    }

    public void add(Station station){
        stationList.add(station);
        notifyDataSetChanged();
    }
    public void remove(Station station){
        stationList.remove(station);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ZW_StationAdapter.ViewHolder vh;
        if(convertView == null){
            vh = new ZW_StationAdapter.ViewHolder();
            convertView =  LayoutInflater.from(context).inflate(R.layout.zw_station_listview,null);
            vh.mView = convertView;
            vh.tv_ZWName = (TextView) convertView.findViewById(R.id.zw_StationName);
            convertView.setTag(vh);
        }else{
            vh = (ZW_StationAdapter.ViewHolder) convertView.getTag();
        }

        Station station=(Station)this.getItem(position);
        vh.tv_ZWName.setText(station.getDisplayName());

        return convertView;
    }

    private class ViewHolder{
        public View mView;
        public TextView tv_ZWName;
    }
}
