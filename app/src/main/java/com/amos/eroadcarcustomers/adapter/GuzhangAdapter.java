package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.activity.RoadsideActivity;
import com.amos.eroadcarcustomers.bean.GuzBean;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.utils.Constant;

import java.util.ArrayList;

public class GuzhangAdapter extends BaseAdapter {
    private ArrayList<GuzBean> mData;
    private Context mContext;
    private int maxImgCount;


    public GuzhangAdapter(Context context, ArrayList<GuzBean> data) {
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
                    .inflate(R.layout.adapter_guzhang, null);
        }

        final GuzBean bean = mData.get(position);

        CheckBox guz = (CheckBox) convertView.findViewById(R.id.guzhang_cb);

        guz.setText(bean.getGc_guzhang_desc());

        guz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RoadsideActivity) mContext).typeStrings.add(bean.getGc_guzhang_desc());
                } else {
                    for (int i = 0; i < ((RoadsideActivity) mContext).typeStrings.size(); i++) {
                        if (bean.getGc_type().equals(((RoadsideActivity) mContext).typeStrings.get(i))) {
                            ((RoadsideActivity) mContext).typeStrings.remove(i);
                            break;
                        }
                    }
                }
            }
        });

        return convertView;
    }
}
