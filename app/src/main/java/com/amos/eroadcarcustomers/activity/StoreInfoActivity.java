package com.amos.eroadcarcustomers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.PingLunAdapter;
import com.amos.eroadcarcustomers.adapter.ServiceAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.PingLunBean;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.view.ListViewInScrollView;
import com.amos.eroadcarcustomers.view.MyGridView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/29.
 */

public class StoreInfoActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn, sure_btn;
    private TextView title_tv, name_tv, pingf_tv, order_tv, address_tv, date_tv, distance_tv,
            week_tv, pinglun_tv;
    private ImageView store_iv;
    private ImageButton tel_ib;
    private SmartRefreshLayout refreshLayout;
    private ScrollView scrollView_sl;
    private StoreBean storeBean;
    private String temp;
    private MyGridView service_gv;
    private CommonBean<ArrayList<String>> stringbean;
    private CommonBean<ArrayList<PingLunBean>> pinglunBean;
    private ListView pinglun_lv;
    private ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeinfo);
        back_btn = (Button) findViewById(R.id.back_btn);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);

        pinglun_lv = (ListView) findViewById(R.id.pinglun_lv);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        View headerView = getLayoutInflater().inflate(R.layout.include_store, null);

        pinglun_lv.addHeaderView(headerView);

        name_tv = (TextView) headerView.findViewById(R.id.name_tv);
        pingf_tv = (TextView) headerView.findViewById(R.id.pingf_tv);
        order_tv = (TextView) headerView.findViewById(R.id.order_tv);
        address_tv = (TextView) headerView.findViewById(R.id.address_tv);
        date_tv = (TextView) headerView.findViewById(R.id.date_tv);
        week_tv = (TextView) headerView.findViewById(R.id.week_tv);
        distance_tv = (TextView) headerView.findViewById(R.id.distance_tv);
        pinglun_tv = (TextView) headerView.findViewById(R.id.pinglun_tv);
        store_iv = (ImageView) headerView.findViewById(R.id.store_iv);
        tel_ib = (ImageButton) headerView.findViewById(R.id.tel_ib);
        service_gv = (MyGridView) headerView.findViewById(R.id.service_gv);

        //        name_tv = (TextView) findViewById(R.id.name_tv);
        //        pingf_tv = (TextView) findViewById(R.id.pingf_tv);
        //        order_tv = (TextView) findViewById(R.id.order_tv);
        //        address_tv = (TextView) findViewById(R.id.address_tv);
        //        date_tv = (TextView) findViewById(R.id.date_tv);
        //        week_tv = (TextView) findViewById(R.id.week_tv);
        //        distance_tv = (TextView) findViewById(R.id.distance_tv);
        //        pinglun_tv = (TextView) findViewById(R.id.pinglun_tv);
        //        store_iv = (ImageView) findViewById(R.id.store_iv);
        //        tel_ib = (ImageButton) findViewById(R.id.tel_ib);
        //        service_gv = (MyGridView) findViewById(R.id.service_gv);

        //        scrollView_sl = (ScrollView) findViewById(R.id.scrollView_sl);


        back_btn.setOnClickListener(this);
        tel_ib.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
        title_tv.setText("门店详情");


        Intent intent = getIntent();
        String latm = intent.getStringExtra("LATM");
        String lngm = intent.getStringExtra("LNGM");

        storeBean = (StoreBean) getIntent().getSerializableExtra("BEAN");
        address_tv.setText(storeBean.getPmg_points_addr());
        name_tv.setText(storeBean.getPmg_points_name());
        order_tv.setText("总订单:" + storeBean.getPmg_jobs_count() + "单");
        pingf_tv.setText(storeBean.getPmg_shop_score() + ".0分");
        date_tv.setText("周一到周五：" + storeBean.getPmg_business_time());
        week_tv.setText("周末：" + storeBean.getPmg_business_time());
        setImage(Constant.HTTP + storeBean.getPmg_shop_logo(), store_iv, R.mipmap.bg_qiye_bang);
        LatLng start = new LatLng(Double.valueOf(latm), Double.valueOf(lngm));
        LatLng end = new LatLng(Double.valueOf(storeBean.getPmg_points_cdy()),
                Double.valueOf(storeBean.getPmg_points_cdx()));
        double distance = DistanceUtil.getDistance(start, end);
        //转换成公里
        if (distance < 1000) {
            temp = distance + "米"; //距离误差大小为500米以内
        } else {
            temp = String.format("%.2f", (distance) / 1000) + "km";
        }
        distance_tv.setText(temp);

        getShopServices(storeBean.getPmg_points_code());
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getShopServices(storeBean.getPmg_points_code());
                serviceAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(/*,false*/);//传入false表示刷新失败
            }
        });
    }

    private void getShopServices(String shopcode) {
        loadingDialog.setMessage("正在获取门店服务...");
        loadingDialog.dialogShow();
        String url = Constant.getShopServices;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("shopcode", shopcode);
        GsonRequest<CommonBean<ArrayList<String>>> requtst = new
                GsonRequest<CommonBean<ArrayList<String>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<String>>> listener_code = new
            RequesListener<CommonBean<ArrayList<String>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<String>> arg0) {
                    // TODO Auto-generated method stub
                    stringbean = arg0;
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

    private void getCommentsByShop(String cmt_shop) {
        loadingDialog.setMessage("正在获取评论...");
        loadingDialog.dialogShow();
        String url = Constant.getCommentsByShop;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("cmt_shop", cmt_shop);
        GsonRequest<CommonBean<ArrayList<PingLunBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<PingLunBean>>>(Request.Method.POST,
                url, listener_getCommentsByShop);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<PingLunBean>>> listener_getCommentsByShop = new
            RequesListener<CommonBean<ArrayList<PingLunBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<PingLunBean>> arg0) {
                    // TODO Auto-generated method stub
                    pinglunBean = arg0;
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

    public void onResume() {
        super.onResume();
        getShopServices(storeBean.getPmg_points_code());
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (stringbean.getState().equals("success") && stringbean.getData().size() !=
                            0) {
                        serviceAdapter = new ServiceAdapter(StoreInfoActivity.this, stringbean
                                .getData());
                        service_gv.setAdapter(serviceAdapter);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            getCommentsByShop(storeBean.getPmg_points_code());
                        }
                    }, 200);
                    break;
                case 2:
                    if (pinglunBean.getState().equals("success")) {
                        pinglun_lv.setAdapter(new PingLunAdapter(StoreInfoActivity.this,
                                pinglunBean.getData()));
                        //                        setListViewHeightBasedOnChildren(pinglun_lv);
                        pinglun_tv.setText("网友点评（" + pinglunBean.getData().size() + "）");
                    } else {
                        pinglun_tv.setText("网友点评");
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
            case R.id.tel_ib:
                intentCall(storeBean.getPmg_points_tel());
                break;
            case R.id.sure_btn:
                Intent intent = new Intent(StoreInfoActivity.this, PingJiaActivity.class);
                intent.putExtra("SHOP", storeBean.getPmg_points_code());
                startActivity(intent);
                break;
        }
    }
}
