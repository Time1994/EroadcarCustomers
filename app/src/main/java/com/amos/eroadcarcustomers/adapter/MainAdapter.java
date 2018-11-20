package com.amos.eroadcarcustomers.adapter;


import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.activity.MainActivity;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class MainAdapter extends BaseAdapter {
    private ArrayList<StoreBean> mData;
    private Context mContext;
    private LayoutInflater infater = null;

    public MainAdapter(Context context, ArrayList<StoreBean> data) {
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
            convertView = infater.inflate(R.layout.adapter_main, null);
            holder = new StoreHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StoreHolder) convertView.getTag();
        }
        StoreBean bean = mData.get(position);
        holder.name_tv.setText(bean.getPmg_points_name());
        holder.pingf_tv.setText(bean.getPmg_shop_score() + ".0分");
        holder.order_tv.setText("总订单:" + bean.getPmg_jobs_count() + "单");
        holder.address_tv.setText(bean.getPmg_points_addr());
        holder.baoyan_tv.setText(bean.getPmg_points_functions());
//        holder.weixiu_tv.setText("");
        ((MainActivity) mContext).setImage(bean.getPmg_shop_logo(), holder.store_iv,
                R.mipmap.ic_launcher);
        if (((MainActivity) mContext).latm != null && ((MainActivity) mContext).lngm != null) {
            LatLng start = new LatLng(((MainActivity) mContext).latm, ((MainActivity) mContext).lngm);
            LatLng end = new LatLng(Double.valueOf(bean.getPmg_points_cdy()),
                    Double.valueOf(bean.getPmg_points_cdx()));
            double distance = DistanceUtil.getDistance(start, end);
            //转换成公里
            if (distance < 1000) {
                temp = distance + "米"; //距离误差大小为500米以内
            } else {
                temp = String.format("%.2f", (distance) / 1000) + "km";
            }
            holder.distance_tv.setText(temp);
        } else {
            holder.distance_tv.setVisibility(View.GONE);
        }

        return convertView;
    }

    class StoreHolder {
        TextView name_tv, pingf_tv, order_tv, address_tv, baoyan_tv, weixiu_tv, distance_tv;
        ImageView store_iv;


        public StoreHolder(View view) {
            this.address_tv = (TextView) view.findViewById(R.id.address_tv);
            this.name_tv = (TextView) view.findViewById(R.id.name_tv);
            this.pingf_tv = (TextView) view.findViewById(R.id.pingf_tv);
            this.order_tv = (TextView) view.findViewById(R.id.order_tv);
            this.baoyan_tv = (TextView) view.findViewById(R.id.baoyan_tv);
            this.store_iv = (ImageView) view.findViewById(R.id.store_iv);
            this.distance_tv = (TextView) view.findViewById(R.id.distance_tv);
        }

    }

}
