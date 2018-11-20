package com.amos.eroadcarcustomers.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.CenterBean;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.view.RoundImageView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/29.
 */

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn;
    private TextView title_tv, name_tv, idnum_tv;
    private RoundImageView photo_iv;
    private ImageView man_iv, woman_iv;
    private CommonBean<CenterBean> centerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalcenter);

        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        idnum_tv = (TextView) findViewById(R.id.idnum_tv);
        photo_iv = (RoundImageView) findViewById(R.id.photo_iv);
        man_iv = (ImageView) findViewById(R.id.man_iv);
        woman_iv = (ImageView) findViewById(R.id.woman_iv);
        back_btn.setOnClickListener(this);
        title_tv.setText("个人中心");
        getPersonalCenter(application.getUserBean().getMm_id());
    }

    private void getPersonalCenter(String mm_id) {
        loadingDialog.setMessage("正在获取个人信息...");
        loadingDialog.dialogShow();
        String url = Constant.getPersonalCenter;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("mm_id", mm_id);
        GsonRequest<CommonBean<CenterBean>> requtst = new
                GsonRequest<CommonBean<CenterBean>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<CenterBean>> listener_code = new
            RequesListener<CommonBean<CenterBean>>() {
                @Override
                public void onResponse(CommonBean<CenterBean> arg0) {
                    // TODO Auto-generated method stub
                    centerBean = arg0;
                    mHandler.sendEmptyMessage(1);
                    loadingDialog.dismiss();
                }

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
                    loadingDialog.dismiss();
                }

            };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (centerBean != null && centerBean
                            .getState().equals
                                    ("success")) {
                        name_tv.setText(centerBean.getData().getMm_name());
                        idnum_tv.setText(centerBean.getData().getMm_cardid());
                        if (centerBean.getData().getMm_sex().equals("男")) {
                            man_iv.setImageResource(R.mipmap.icon_rediobutton_s);
                        } else if (centerBean
                                .getData().getMm_sex().equals("女")) {
                            woman_iv.setImageResource(R.mipmap.icon_rediobutton_s);
                        } else {
                            man_iv.setImageResource(R.mipmap.icon_radiobutton);
                            man_iv.setImageResource(R.mipmap.icon_radiobutton);
                        }
                    }
                    break;
                case -1:
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }
}
