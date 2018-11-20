package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.utils.Constant;

import java.util.ArrayList;

public class PersonalImgAdapter extends BaseAdapter {
    private ArrayList<HomeBean> mData;
    private Context mContext;
    private int maxImgCount;


    public PersonalImgAdapter(Context context, ArrayList<HomeBean> data) {
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
                    .inflate(R.layout.adapter_personal_img, null);
        }

        HomeBean bean = mData.get(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_iv);
        TextView name = (TextView) convertView.findViewById(R.id.name_tv);

        ((BaseActivity) mContext).setImage(Constant.HTTP + bean.getHm_img(), imageView, R.mipmap.icon_none_m);
        name.setText(bean.getHm_module());

        return convertView;
    }
}
