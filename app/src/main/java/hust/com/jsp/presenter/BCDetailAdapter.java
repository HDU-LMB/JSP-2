package hust.com.jsp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hust.com.jsp.R;
import hust.com.jsp.bean.BCInfo;
import hust.com.jsp.bean.BCInfoItem;

/**
 * Created by admin on 2017/5/13.
 */

public class BCDetailAdapter extends BaseAdapter {

    private Context mContext;
    private BCInfo bcInfo;

    public BCDetailAdapter(Context context, BCInfo bcInfo){
        this.mContext = context;
        this.setBcInfo(bcInfo);
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
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.layout_bclist_item,null);
            vh.mView = convertView;
            vh.tv_bc_jzjname = (TextView) convertView.findViewById(R.id.textView_bcdetial_jzjname);
            vh.tv_bc_id = (TextView) convertView.findViewById(R.id.textView_bcdetail_id);
            vh.tv_bc_actionlist = (TextView) convertView.findViewById(R.id.textView_bcdetail_actionlist);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        BCInfoItem bcInfoItem = bcInfo.get(position);
        vh.tv_bc_jzjname.setText(bcInfoItem.getName());
        vh.tv_bc_id.setText(bcInfoItem.getID());
        vh.tv_bc_actionlist.setText(bcInfoItem.getActionListDisplay());
        return convertView;
    }

    public void resetInfo(BCInfo selection) {
        this.bcInfo = selection;
    }


    private class ViewHolder{
        public View mView;
        public TextView tv_bc_jzjname;
        public TextView tv_bc_id;
        public TextView tv_bc_actionlist;
        public View backGround;
    }
}