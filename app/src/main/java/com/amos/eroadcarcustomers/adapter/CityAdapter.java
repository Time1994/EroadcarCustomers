package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.MainActivity;
import com.amos.eroadcarcustomers.bean.CityBean;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {
    private ArrayList<CityBean> mData;
    private Context mContext;
    private LayoutInflater infater = null;

    public CityAdapter(Context context, ArrayList<CityBean> data) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mData = data;
        infater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        StoreHolder holder = null;
        String temp;
        Double latm, lngm;
        if (convertView == null || convertView.getTag() == null) {
            convertView = infater.inflate(R.layout.adapter_cityitem, null);
            holder = new StoreHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StoreHolder) convertView.getTag();
        }
        CityBean bean = mData.get(position);
       holder.city_tv.setText(bean.getPmg_points_country()+"("+bean.getShopcount()+")");
        return convertView;
    }

    class StoreHolder {
        TextView city_tv;


        public StoreHolder(View view) {
            this.city_tv = (TextView) view.findViewById(R.id.city_tv);
        }

    }

}
