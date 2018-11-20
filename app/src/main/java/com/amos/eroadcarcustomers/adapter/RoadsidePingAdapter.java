package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.RoadsideActivity;
import com.amos.eroadcarcustomers.activity.RoadsidePingJiaActivity;
import com.amos.eroadcarcustomers.bean.GuzBean;

import java.util.ArrayList;

public class RoadsidePingAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private Context mContext;
    private int maxImgCount;


    public RoadsidePingAdapter(Context context, ArrayList<String> data) {
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
                    .inflate(R.layout.adapter_roadside_ping, null);
        }

        final String bean = mData.get(position);

        CheckBox guz = (CheckBox) convertView.findViewById(R.id.guzhang_cb);

        guz.setText(bean);

        guz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RoadsidePingJiaActivity) mContext).typeStrings.add(bean);
                } else {
                    for (int i = 0; i < ((RoadsidePingJiaActivity) mContext).typeStrings.size(); i++) {
                        if (bean.equals(((RoadsidePingJiaActivity) mContext).typeStrings.get(i))) {
                            ((RoadsidePingJiaActivity) mContext).typeStrings.remove(i);
                            break;
                        }
                    }
                }
            }
        });

        return convertView;
    }
}
