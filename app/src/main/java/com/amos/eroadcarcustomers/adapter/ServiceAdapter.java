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

public class ServiceAdapter extends BaseAdapter {
    private ArrayList<String> vector;
    private Context context;
    private LayoutInflater infater = null;
    private int clickTemp = -1;

    public ServiceAdapter(Context context, ArrayList<String> vector) {
        super();
        this.vector = vector;
        this.context = context;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_service_item, null);
        }
        TextView service_tv = (TextView) convertView
                .findViewById(R.id.service_tv);
        ImageView camera_iv = (ImageView) convertView.findViewById(R.id.camera_iv);
        service_tv.setText(vector.get(position));
//        for (int i = 0; i < vector.size(); i++) {
//        }

        //        if (clickTemp == position) {
        //            convertView.setBackgroundResource(R.drawable.btn_green);
        //            content_tv.setTextColor(context.getResources().getColor(
        //                    R.color.green_k));
        //        } else {
        //            convertView.setBackgroundResource(R.drawable.btn_gray);
        //            content_tv.setTextColor(context.getResources().getColor(
        //                    R.color.grayo));
        //        }
        //
        //        content_tv.setText(vector.get(position).getCpf_factory_name());

        return convertView;
    }

}
