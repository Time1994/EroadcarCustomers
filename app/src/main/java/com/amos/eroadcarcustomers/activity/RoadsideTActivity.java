package com.amos.eroadcarcustomers.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.GuzhangAdapter;
import com.amos.eroadcarcustomers.adapter.LujiuImageAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.GuzBean;
import com.amos.eroadcarcustomers.bean.RoadsideBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.utils.Tool;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RoadsideTActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn, other_btn, sure_btn;
    private TextView title_tv, note_tv;

    private MapView mapView;

    private CommonBean<RoadsideBean> guzBean, roadsideBean, jishiBean;
    private CommonBean arriveBean;
    private String ljif_code, ljif_state, note = "";

    private int totalTime = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadsidet);
        other_btn = (Button) findViewById(R.id.other_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);

        note_tv = (TextView) findViewById(R.id.note_tv);

        mapView = (MapView) findViewById(R.id.mapView);

        ljif_code = getIntent().getStringExtra("ljif_code");
        ljif_state = getIntent().getStringExtra("ljif_state");

        title_tv.setText("一键路救");
        other_btn.setText("取消");

        back_btn.setOnClickListener(this);
        other_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);

        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }


        mapView.showScaleControl(false);
        mapView.showZoomControls(false);

        LatLng lls = new LatLng(latm,
                lngm);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(lls,
                13);
        BaiduMap mbaiduMap = mapView.getMap();
        mbaiduMap.animateMapStatus(u);


        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_map_loc);
        BitmapDescriptor bitmapm = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_map_car);
        OverlayOptions option = new MarkerOptions()
                .position(lls)
                .icon(bitmap);
        OverlayOptions optionm = new MarkerOptions()
                .position(lls)
                .icon(bitmapm);
        Marker mMarker = (Marker) mbaiduMap.addOverlay(option);
        Marker mMarkerm = (Marker) mbaiduMap.addOverlay(optionm);

        setInterface();

//        if (!ljif_state.equals("已到达")) {
//            mHandler.postDelayed(runnable, 1000);
//        }
        if (!ljif_state.equals("已到达")) {
//            mHandler.postDelayed(runnable, 1000);

            getDaoJiShiTime();
        }

        mHandler.postDelayed(runnableState, 500);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            totalTime++;
            String s = "";
            int day = 0, hour = 0, mins = 0, sec = 0;
            if (totalTime < 60) {
                sec = totalTime;
                s = sec + "秒";
            } else if (totalTime >= 60 && totalTime <= 3600) {
                mins = totalTime / 60;
                sec = totalTime - mins * 60;
                s = mins + "分" + sec + "秒";
            } else if (totalTime > 3600 && totalTime <= 86400) {
                hour = totalTime / 3600;
                mins = (totalTime - (hour * 3600)) / 60;
                sec = totalTime - (hour * 3600) - (mins * 60);
                s = hour + "时" + mins + "分" + sec + "秒";
            } else {
                try {
                    day = totalTime / 86400;
                    hour = (totalTime - day * 86400) / 3600;
                    mins = (totalTime - (day * 86400) - (hour * 3600)) / 60;
                    sec = totalTime - (day * 86400) - (hour * 3600) - (mins * 60);
                    s = day + "天" + hour + "时" + mins + "分" + sec + "秒";
                } catch (Exception e) {
                }
            }
            note_tv.setText(note + "已等待：" + s);
            mHandler.postDelayed(this, 1000);
        }
    };


    Runnable runnableState = new Runnable() {
        @Override
        public void run() {
            getLujiuStateByCode();

            mHandler.postDelayed(this, 10000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(runnableState);
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.other_btn:

                showDialogMessage(RoadsideTActivity.this, "确认取消路救？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelLujiu();
                    }
                });

                break;

            case R.id.sure_btn:
                showDialogMessage(RoadsideTActivity.this, "确认维修师傅已到达？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        workerArriveTime();
                    }
                });
                break;

        }
    }

    private void getLujiuStateByCode() {
//        loadingDialog.setMessage("正在取消路救信息...");
//        loadingDialog.dialogShow();
        String url = Constant.GETLUJIUSTATEBYCODE;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("ljif_code", ljif_code);
        GsonRequest<CommonBean<RoadsideBean>> requtst = new GsonRequest<CommonBean<RoadsideBean>>(Request.Method.POST,
                url, getLujiuStateByCode);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<RoadsideBean>> getLujiuStateByCode = new
            RequesListener<CommonBean<RoadsideBean>>() {
                @Override
                public void onResponse(CommonBean<RoadsideBean> arg0) {
                    // TODO Auto-generated method stub
                    roadsideBean = arg0;
                    mHandler.sendEmptyMessage(3);
//                    loadingDialog.dismiss();
                }

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
//                    loadingDialog.dismiss();
                }

            };


    //倒计时
    private void getDaoJiShiTime() {
        loadingDialog.setMessage("正在加载数据...");
        loadingDialog.dialogShow();
        String url = Constant.getDaoJiShiTime;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("ljif_code", ljif_code);
        GsonRequest<CommonBean<RoadsideBean>> requtst = new GsonRequest<CommonBean<RoadsideBean>>(Request.Method.POST,
                url, listener_getDaoJiShiTime);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<RoadsideBean>> listener_getDaoJiShiTime = new
            RequesListener<CommonBean<RoadsideBean>>() {
                @Override
                public void onResponse(CommonBean<RoadsideBean> arg0) {
                    // TODO Auto-generated method stub
                    jishiBean = arg0;
                    mHandler.sendEmptyMessage(4);
                    loadingDialog.dismiss();
                }


                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
                    loadingDialog.dismiss();
                }

            };

    private void cancelLujiu() {
        loadingDialog.setMessage("正在取消路救信息...");
        loadingDialog.dialogShow();
        String url = Constant.CANCELLUJIU;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("ljif_code", ljif_code);
        GsonRequest<CommonBean<RoadsideBean>> requtst = new GsonRequest<CommonBean<RoadsideBean>>(Request.Method.POST,
                url, listener_cancel);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<RoadsideBean>> listener_cancel = new
            RequesListener<CommonBean<RoadsideBean>>() {
                @Override
                public void onResponse(CommonBean<RoadsideBean> arg0) {
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

    private void workerArriveTime() {
        loadingDialog.setMessage("正在上传信息...");
        loadingDialog.dialogShow();
        String url = Constant.WORKERARRIVETIME;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("ljif_code", ljif_code);
        GsonRequest<CommonBean> requtst = new GsonRequest<CommonBean>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean> listener_code = new
            RequesListener<CommonBean>() {
                @Override
                public void onResponse(CommonBean arg0) {
                    // TODO Auto-generated method stub
                    arriveBean = arg0;
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
                        Intent intent = new Intent(RoadsideTActivity.this, RoadsideCancelActivity.class);
                        intent.putExtra("BEAN", guzBean.getData());
                        startActivity(intent);
                        finish();
                    }
                    ToastUtils.showShort(guzBean.getMsg());
                    break;

                case 2:
                    if (arriveBean.getState().equals("success")) {
                        dialogMessage.dismiss();
                        sure_btn.setVisibility(View.GONE);
                        note_tv.setVisibility(View.GONE);
                        other_btn.setText("");

                        mHandler.removeCallbacks(runnable);
                    }
                    ToastUtils.showShort(arriveBean.getMsg());
                    break;
                case 3:
                    if (roadsideBean.getState().equals("success")) {
                        ljif_state = roadsideBean.getData().getLjif_state();

                        setInterface();
                    }
                    break;
                case 4:

                    if (jishiBean.getState().equals("success")) {
                        totalTime = getTimeJishi(jishiBean.getData().getCreate(), jishiBean.getData().getCurrent());
                    }

                    if (!ljif_state.equals("已到达")) {
                        mHandler.postDelayed(runnable, 1000);
                    }

                    break;
            }
        }
    };

    private int getTimeJishi(String creat, String current) {
        int t = 0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(current);
            Date d2 = df.parse(creat);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别

            t = (int) diff / 1000;
//            long days = diff / (1000 * 60 * 60 * 24);
//            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//            long s = (diff - days * (1000 * 60 * 60 * 24 )- hours * (1000 * 60 * 60)) / (1000 * 60)- minutes * (1000 * 60)) / 1000;
//            System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    private void setInterface() {
        if (ljif_state.equals("未路救")) {
            note = "正在为您处理，";
        } else if (ljif_state.equals("已指派")) {
            note = "处理成功！维修师傅正在飞速向您赶来，";

            sure_btn.setVisibility(View.VISIBLE);
        } else if (ljif_state.equals("已完成")) {
            ToastUtils.showLong("车辆已修好\n" +
                    "可以正常使用～");
            mHandler.removeCallbacks(runnableState);

            Intent intent = new Intent(RoadsideTActivity.this, RoadsidePingJiaActivity.class);
            intent.putExtra("ljif_code", ljif_code);
            startActivity(intent);
            finish();
        } else if (ljif_state.equals("已到达")) {
            sure_btn.setVisibility(View.GONE);
            note_tv.setVisibility(View.GONE);
            other_btn.setText("");
        }
    }
}
