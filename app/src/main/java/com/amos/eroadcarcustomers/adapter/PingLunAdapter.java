package com.amos.eroadcarcustomers.adapter;


import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.MainActivity;
import com.amos.eroadcarcustomers.activity.StoreInfoActivity;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.bean.PingLunBean;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.view.MyGridView;
import com.example.xlhratingbar_lib.XLHRatingBar;

public class PingLunAdapter extends BaseAdapter {
    private ArrayList<PingLunBean> mData;
    private Context mContext;
    private LayoutInflater infater = null;
    private float rating = 1;

    public PingLunAdapter(Context context, ArrayList<PingLunBean> data) {
        // TODO Auto-generated constructor stub
        mData = data;
        mContext = context;
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
        PingLunHolder pingLunHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = infater.inflate(R.layout.adapter_pinglun, null);
            pingLunHolder = new PingLunAdapter.PingLunHolder(convertView);
            convertView.setTag(pingLunHolder);
        } else {
            pingLunHolder = (PingLunHolder) convertView.getTag();
        }
        PingLunBean bean = mData.get(position);
        pingLunHolder.name_tv.setText(bean.getCmt_author());
        pingLunHolder.pinglun_tv.setText(bean.getCmt_context());
        pingLunHolder.time_tv.setText(bean.getCmt_time());
//        ((StoreInfoActivity) mContext).setImage(Constant.HTTP + bean.getCmt_img1(), pingLunHolder
//                        .user_iv,
//                R.mipmap.icon_photo);
        pingLunHolder.ratingBar.setRating(Float.valueOf(bean.getCmt_level()));
        ArrayList<HomeBean> list = new ArrayList<>();
        HomeBean bean1 = new HomeBean();
        bean1.setHm_img(bean.getCmt_img1());
        list.add(bean1);

        HomeBean bean2 = new HomeBean();
        bean2.setHm_img(bean.getCmt_img2());
        list.add(bean2);

        HomeBean bean3 = new HomeBean();
        bean3.setHm_img(bean.getCmt_img3());
        list.add(bean3);
        pingLunHolder.img_gv.setAdapter(new PinglunImageAdapter(mContext,list));
        return convertView;

    }

    class PingLunHolder {
        TextView name_tv, time_tv, pinglun_tv;
        ImageView user_iv;
        RatingBar ratingBar;
        MyGridView img_gv;


        public PingLunHolder(View view) {
            this.time_tv = (TextView) view.findViewById(R.id.time_tv);
            this.name_tv = (TextView) view.findViewById(R.id.name_tv);
            this.pinglun_tv = (TextView) view.findViewById(R.id.pinglun_tv);
            this.user_iv = (ImageView) view.findViewById(R.id.user_iv);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            this.img_gv = (MyGridView) view.findViewById(R.id.img_gv);
        }

    }


}
