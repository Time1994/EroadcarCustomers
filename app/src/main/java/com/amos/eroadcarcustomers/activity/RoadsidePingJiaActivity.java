package com.amos.eroadcarcustomers.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.RoadsidePingAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.GuzBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RoadsidePingJiaActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn, sure_btn;
    private TextView title_tv, total_tv;
    private EditText pingjia_et;
    private GridView ping_gv;
    private RatingBar ratingBar;

    private int num = 100;
    private float rating = 1;

    private CommonBean<ArrayList<String>> guzBean;
    private CommonBean commonBean;
    public ArrayList<String> typeStrings = new ArrayList<>();

    private String ljif_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_pingjia);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);
        total_tv = (TextView) findViewById(R.id.total_tv);
        pingjia_et = (EditText) findViewById(R.id.pingjia_et);

        ping_gv = (GridView) findViewById(R.id.ping_gv);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        title_tv.setText("评论");
        total_tv.setText("0/100");

        sure_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        ljif_code = getIntent().getStringExtra("ljif_code");

        getCommentTabs();

        pingjia_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                if (s.length() > 0) {
                    sure_btn.setEnabled(true);
                } else {
                    sure_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = s.length();
                total_tv.setText(number + "/100");
                selectionStart = pingjia_et.getSelectionStart();
                selectionEnd = pingjia_et.getSelectionEnd();
                if (number == 100) {
                    ToastUtils.showShort("输入字数已达上限");
                }
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    pingjia_et.setText(s);
                    pingjia_et.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.sure_btn:
                if (typeStrings.size() == 0) {
                    ToastUtils.showShort("请选择服务");
                    return;
                }
                String s = "";

                for (int i = 0; i < typeStrings.size(); i++) {
                    s += typeStrings.get(i) + ",";
                }

                submitComments(s, pingjia_et.getText().toString());

                break;
        }
    }

    private void getCommentTabs() {
        loadingDialog.setMessage("正在获取信息...");
        loadingDialog.dialogShow();
        String url = Constant.GETCOMMENTTABS;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<String>>> requtst = new GsonRequest<CommonBean<ArrayList<String>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<ArrayList<String>>> listener_code = new
            RequesListener<CommonBean<ArrayList<String>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<String>> arg0) {
                    // TODO Auto-generated method stub
                    guzBean = arg0;
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

    private void submitComments(String c4w_context, String c4w_remark) {
        loadingDialog.setMessage("正在上传评价信息...");
        loadingDialog.dialogShow();
        String url = Constant.SUBMITCOMMENTS;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);

        map.put("c4w_mg_id", "");
        map.put("c4w_mg_name", "");
        map.put("c4w_order_code", ljif_code);
        map.put("c4w_context", c4w_context);
        map.put("c4w_level", rating + "");
        map.put("c4w_remark", c4w_remark);
        map.put("c4w_author", application.getUserBean().getMm_name());


        map.put("mm_id", application.getUserBean().getMm_id());
        map.put("mm_name", application.getUserBean().getMm_name());
        map.put("mm_cellphone", application.getUserBean().getMm_cellphone());
        map.put("mm_car_vin", application.getUserBean().getMm_car_vin());
        map.put("mm_car_lisence", application.getUserBean().getMm_car_lisence());
        GsonRequest<CommonBean> requtst = new GsonRequest<CommonBean>(Request.Method.POST,
                url, listener_submitComments);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean> listener_submitComments = new
            RequesListener<CommonBean>() {
                @Override
                public void onResponse(CommonBean arg0) {
                    // TODO Auto-generated method stub
                    commonBean = arg0;
                    mHandler.sendEmptyMessage(2);
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
                    if (guzBean.getState().equals("success")) {
                        ping_gv.setAdapter(new RoadsidePingAdapter(RoadsidePingJiaActivity.this, guzBean.getData()));
                    }
                    break;
                case 2:
                    if (commonBean.getState().equals("success")) {
                        onBackPressed();
                    }
                    ToastUtils.showShort(commonBean.getMsg());
                    break;
            }
        }
    };

}
