package com.amos.eroadcarcustomers.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.utils.Tool;

import java.util.ArrayList;

public class LujiuImageAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private Context mContext;
    private int maxImgCount;


    public LujiuImageAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        mData = data;
        mContext = context;
        if (mData.size() > 6) {
            mData.remove(data.size() - 1);
            ToastUtils.showLong("图片已达上限");
        }
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
                    .inflate(R.layout.adapter_lujiuimage_item, null);
        }

        String imagebean = mData.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imagebean, options);
        bitmap = Tool.rotaingImageView(0, bitmap);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.camera_iv);
        imageView.setImageBitmap(bitmap);
        return convertView;
    }
}
