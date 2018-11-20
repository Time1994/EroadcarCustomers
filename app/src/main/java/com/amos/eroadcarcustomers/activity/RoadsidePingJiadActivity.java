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
import com.amos.eroadcarcustomers.adapter.RoadsidePingdAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.RoadsideBean;
import com.amos.eroadcarcustomers.bean.RoadsidePingjiaBean;
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

public class RoadsidePingJiadActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn;
    private TextView title_tv;

    private TextView content_tv;

    private GridView ping_gv;

    private RatingBar ratingBar;

    private CommonBean<RoadsidePingjiaBean> roadsideBean;

    private String ljif_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_pingjiad);

        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);

        content_tv = (TextView) findViewById(R.id.content_tv);

        ping_gv = (GridView) findViewById(R.id.ping_gv);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        title_tv.setText("评论");

        back_btn.setOnClickListener(this);

        ljif_code = getIntent().getStringExtra("ljif_code");

        getCommentsByOrder();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.sure_btn:

                break;
        }
    }

    private void getCommentsByOrder() {
        loadingDialog.setMessage("正在获取订单信息...");
        loadingDialog.dialogShow();
        String url = Constant.GETCOMMENTSBYORDER;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);

        map.put("c4w_order_code", ljif_code);
        GsonRequest<CommonBean<RoadsidePingjiaBean>> requtst = new GsonRequest<CommonBean<RoadsidePingjiaBean>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<RoadsidePingjiaBean>> listener_code = new
            RequesListener<CommonBean<RoadsidePingjiaBean>>() {
                @Override
                public void onResponse(CommonBean<RoadsidePingjiaBean> arg0) {
                    // TODO Auto-generated method stub
                    roadsideBean = arg0;
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
                    if (roadsideBean.getState().equals("success")) {
                        content_tv.setText(roadsideBean.getData().getC4w_remark());
                        ratingBar.setRating(Float.valueOf(roadsideBean.getData().getC4w_level()));

                        String[] s = roadsideBean.getData().getC4w_context().split(",");
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < s.length - 1; i++) {
                            list.add(s[i]);
                        }

                        ping_gv.setAdapter(new RoadsidePingdAdapter(RoadsidePingJiadActivity.this, list));
                    }
                    break;
            }
        }
    };

}
