package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.RoadsideActivity;
import com.amos.eroadcarcustomers.activity.RoadsidePingJiaActivity;
import com.amos.eroadcarcustomers.bean.RoadsideBean;

import java.util.ArrayList;

public class RoadsideAdapter extends BaseAdapter {
    private ArrayList<RoadsideBean> mData;
    private Context mContext;
    private int maxImgCount;


    public RoadsideAdapter(Context context, ArrayList<RoadsideBean> data) {
        // TODO Auto-generated constructor stub
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        if (convertView == null || convertView.getTag() == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.adapter_roadside_item, null);
        }

        TextView state_tv = (TextView) convertView.findViewById(R.id.state_tv);
        TextView guzhang_tv = (TextView) convertView.findViewById(R.id.guzhang_tv);
        TextView time_tv = (TextView) convertView.findViewById(R.id.time_tv);
        TextView address_tv = (TextView) convertView.findViewById(R.id.address_tv);


        final RoadsideBean bean = mData.get(position);


        state_tv.setText(bean.getLjif_state());
        guzhang_tv.setText("文字说明：" + bean.getLjif_remark() + "\n" + "故障原因：" + bean.getLjif_type());
        time_tv.setText(bean.getLjif_create_time());
        address_tv.setText(bean.getLjif_address());


        return convertView;
    }
}
