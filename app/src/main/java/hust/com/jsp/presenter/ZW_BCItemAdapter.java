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
import hust.com.jsp.bean.BCInfoItem;

/**
 * Created by Michael-Lee on 2017/6/28.
 */

public class ZW_BCItemAdapter extends BaseAdapter {

    private Context mContext;
    private BCInfo bcInfo;
    int focus;

    public ZW_BCItemAdapter(Context context, BCInfo bcInfo){
        this.mContext = context;
        this.setBcInfo(bcInfo);
        this.focus = 0;
    }

    public int getFocus(){
        return this.focus;
    }

    public void setFocus(int aInt){
        this.focus = aInt;
    }

    public void setBcInfo(BCInfo bcInfo){
        if(bcInfo == null) this.bcInfo = new BCInfo();
        else this.bcInfo = bcInfo;
    }
    @Override
    public int getCount() {
        return bcInfo.Count();
    }

    @Override
    public Object getItem(int position) {
        return bcInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZW_BCItemAdapter.ViewHolder vh;
        if(convertView == null){
            vh = new ZW_BCItemAdapter.ViewHolder();
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.zw_bc_item_listview,null);
            vh.mView = convertView;
            vh.tv_bc_jzjname = (TextView) convertView.findViewById(R.id.jzjName);
            vh.tv_bc_usageCondition = (TextView) convertView.findViewById(R.id.usageCondition);
            vh.tv_bc_actionlist = (TextView) convertView.findViewById(R.id.actionList);
           //vh.backGround = convertView.findViewById(R.id.bc_list_background);
            convertView.setTag(vh);
        }else{
            vh = (ZW_BCItemAdapter.ViewHolder) convertView.getTag();
        }

        BCInfoItem bcInfoItem= (BCInfoItem) this.getItem(position);
        vh.tv_bc_jzjname.setText(bcInfoItem.getJzj().getDisplayName());
        vh.tv_bc_usageCondition.setText(bcInfoItem.getTypeName());
        vh.tv_bc_actionlist.setText(bcInfoItem.getActionListDisplay());
//        if(focus == position) vh.backGround.setBackgroundColor(Color.LTGRAY);
//        else vh.backGround.setBackgroundColor(Color.TRANSPARENT);

        return convertView;
    }
    private class ViewHolder{
        public View mView;
        public TextView tv_bc_jzjname;
        public TextView tv_bc_usageCondition;
        public TextView tv_bc_actionlist;
        public View backGround;
    }
}
