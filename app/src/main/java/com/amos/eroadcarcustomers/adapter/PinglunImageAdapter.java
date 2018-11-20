package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class PinglunImageAdapter extends BaseAdapter {
    private ArrayList<HomeBean> mData;
    private Context mContext;
    private int maxImgCount;


    public PinglunImageAdapter(Context context, ArrayList<HomeBean> data) {
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
                    .inflate(R.layout.adapter_pinglun_img, null);
        }

        HomeBean bean = mData.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_iv);

        if (bean.getHm_img() != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_none_m) //加载成功之前占位图
                    .error(R.mipmap.icon_none_m);
            String url = Constant.HTTP + bean.getHm_img();
            Glide.with(mContext).load(url).apply(options).into(imageView);
        }else {
            imageView.setVisibility(View.GONE);
        }
        //        ((BaseActivity) mContext).setImage(Constant.HTTP + bean.getHm_img(), imageView, R.mipmap.icon_none_m);
        return convertView;
    }
}
