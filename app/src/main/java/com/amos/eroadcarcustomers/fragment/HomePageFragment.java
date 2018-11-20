package com.amos.eroadcarcustomers.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.DataInfoActivity;
import com.amos.eroadcarcustomers.activity.PersonalCenterActivity;
import com.amos.eroadcarcustomers.activity.StoreInfoActivity;
import com.amos.eroadcarcustomers.adapter.CityAdapter;
import com.amos.eroadcarcustomers.adapter.CountyStoreAdapter;
import com.amos.eroadcarcustomers.adapter.HomePageAdapter;
import com.amos.eroadcarcustomers.adapter.MainAdapter;
import com.amos.eroadcarcustomers.bean.CityBean;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.CountyStoreBean;
import com.amos.eroadcarcustomers.bean.StoreBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageFragment extends BaseFragment {

    private ListView main_lv, city_lv;
    private RelativeLayout city_rl, city_pop_rl, you_rl;
    private LinearLayout you_pop_ll;
    private ImageView city_iv, you_iv;
    private TextView you_tv, juli_tv, pingf_tv, city_tv, datainfo_tv;
    private CommonBean<ArrayList<StoreBean>> storeBean;
    private CommonBean<ArrayList<CityBean>> cityBean;
    private CommonBean<ArrayList<CountyStoreBean>> countyStoreBean;
    private HomePageAdapter mainAdapter;
    private CityAdapter cityAdapter;
    private CountyStoreAdapter countyStoreAdapter;
    private String orderId = "";
    private String city = "上海市";
    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true;// 是否首次定位
    private Double latm = 31.095979, lngm = 121.581199;
    private LatLng lls;

    @Override
    protected void initView() {
        main_lv = (ListView) mView.findViewById(R.id.main_lv);
        city_lv = (ListView) mView.findViewById(R.id.city_lv);
        city_rl = (RelativeLayout) mView.findViewById(R.id.city_rl);
        city_pop_rl = (RelativeLayout) mView.findViewById(R.id.city_pop_rl);
        you_rl = (RelativeLayout) mView.findViewById(R.id.you_rl);
        you_pop_ll = (LinearLayout) mView.findViewById(R.id.you_pop_ll);
        you_iv = (ImageView) mView.findViewById(R.id.you_iv);
        city_iv = (ImageView) mView.findViewById(R.id.city_iv);
        datainfo_tv = (TextView) mView.findViewById(R.id.datainfo_tv);
        you_tv = (TextView) mView.findViewById(R.id.you_tv);
        juli_tv = (TextView) mView.findViewById(R.id.juli_tv);
        pingf_tv = (TextView) mView.findViewById(R.id.pingf_tv);
        city_tv = (TextView) mView.findViewById(R.id.city_tv);
//        personalcenter_tv = (TextView) mView.findViewById(R.id.personalcenter_tv);
        city_rl.setOnClickListener(this);
        you_rl.setOnClickListener(this);
        city_pop_rl.setOnClickListener(this);
        you_pop_ll.setOnClickListener(this);

        juli_tv.setOnClickListener(this);
        pingf_tv.setOnClickListener(this);
        datainfo_tv.setVisibility(View.GONE);
//        datainfo_tv.setOnClickListener(this);
//        personalcenter_tv.setOnClickListener(this);

        main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mMainActivity, StoreInfoActivity.class);
                intent.putExtra("LATM", String.valueOf(latm));
                intent.putExtra("LNGM", String.valueOf(lngm));
                intent.putExtra("BEAN", storeBean.getData().get(position));
                startActivity(intent);
            }
        });
        city_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getShopsByCounty(cityBean.getData().get(i).getPmg_points_country());
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            // mCurrentMarker = BitmapDescriptorFactory
            // .fromResource(R.drawable.icon_geo);
            // MyLocationConfiguration config = new MyLocationConfiguration(
            // mCurrentMode, true, mCurrentMarker);
            // mBaiduMap.setMyLocationConfiguration();
            // // 当不需要定位图层时关闭定位图层
            // mBaiduMap.setMyLocationEnabled(false);

            if (location != null) {
                latm = location.getLatitude();// * 1e6;
                lngm = location.getLongitude();// * 1e6;
                System.out.println("当前坐标：" + "纬度——" + latm + "---经度——" + lngm);
                city = location.getCity();
                city_tv.setText(city);
                //打印出当前位置
                System.out.println("location.getAddrStr()=" + location.getAddrStr());
                //打印出当前城市
                System.out.println("location.getCity()=" + location.getCity());
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                lls = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(lls,
                        13);
                // points.add(ll);
                mHandler.sendEmptyMessage(1);
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 开启定位图层
        // 定位初始化
        mLocClient = new LocationClient(mMainActivity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        // 返回国测局经纬度坐标系 coor=gcj02
        // 返回百度墨卡托坐标系 coor=bd09
        // 返回百度经纬度坐标系 coor=bd09ll
        option.setScanSpan(0);
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setAddrType("all");
        // option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        getPoints();
    }

    private void getPoints() {
        mMainActivity.loadingDialog.setMessage("正在获取门店信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.getPoints;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<StoreBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<StoreBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<StoreBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<StoreBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<StoreBean>> arg0) {
                    // TODO Auto-generated method stub
                    storeBean = arg0;
                    mHandler.sendEmptyMessage(2);
                    mMainActivity.loadingDialog.dismiss();
                }

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
                    mMainActivity.loadingDialog.dismiss();
                }

            };

    private void getCountyByCity(String city) {
        mMainActivity.loadingDialog.setMessage("正在获取城市信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.getCountyByCity;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("city", city);
        GsonRequest<CommonBean<ArrayList<CityBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<CityBean>>>(Request.Method.POST,
                url, listener_getCountyByCity);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<CityBean>>> listener_getCountyByCity = new
            RequesListener<CommonBean<ArrayList<CityBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<CityBean>> arg0) {
                    // TODO Auto-generated method stub
                    cityBean = arg0;
                    mHandler.sendEmptyMessage(3);
                    mMainActivity.loadingDialog.dismiss();
                }

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
                    mMainActivity.loadingDialog.dismiss();
                }

            };

    private void getShopsByCounty(String county) {
        mMainActivity.loadingDialog.setMessage("正在获取门店信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.getShopsByCounty;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("county", county);
        GsonRequest<CommonBean<ArrayList<CountyStoreBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<CountyStoreBean>>>(Request.Method.POST,
                url, listener_getShopsByCounty);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<CountyStoreBean>>> listener_getShopsByCounty = new
            RequesListener<CommonBean<ArrayList<CountyStoreBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<CountyStoreBean>> arg0) {
                    // TODO Auto-generated method stub
                    countyStoreBean = arg0;
                    mHandler.sendEmptyMessage(4);
                    mMainActivity.loadingDialog.dismiss();
                }

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(-1);
                    mMainActivity.loadingDialog.dismiss();
                }

            };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getPoints();
                    break;
                case 2:
                    if (storeBean != null && storeBean.getData().size() != 0 && storeBean
                            .getState().equals("success")) {
                        mainAdapter = new HomePageAdapter(mMainActivity, storeBean.getData(),
                                latm, lngm);
                        main_lv.setAdapter(mainAdapter);
                        mainAdapter.notifyDataSetChanged();

                    } else {
                    }
                    break;
                case 3:
                    if (cityBean != null && cityBean.getData().size() != 0 && cityBean.getState().equals("success")) {
                        cityAdapter = new CityAdapter(mMainActivity, cityBean.getData());
                        city_lv.setAdapter(cityAdapter);
                    }
                    break;
                case 4:
                    if (countyStoreBean != null && countyStoreBean.getData().size() != 0 && countyStoreBean
                            .getState().equals("success")) {
                        countyStoreAdapter = new CountyStoreAdapter(mMainActivity, countyStoreBean.getData(), latm, lngm);
                        main_lv.setAdapter(countyStoreAdapter);
                        countyStoreAdapter.notifyDataSetChanged();
                        city_pop_rl.setVisibility(View.GONE);
                        city_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                    } else {
                    }
                    break;
                case -1:
                    break;
                default:
                    break;
            }
        }
    };


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.city_rl:
                //                int i = (int) getResources().getDimension(R.dimen.ninty);
                //
                //                if (null != popupwindowd && popupwindowd.isShowing()) {
                //                    popupwindowd.dismiss();
                //                } else {
                //                    initPopupWindow();
                //                    popupwindowd.showAtLocation(v, Gravity.TOP, 0, i);
                //                }
                if (city_pop_rl.isShown()) {
                    city_pop_rl.setVisibility(View.GONE);
                    city_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                } else {
                    getCountyByCity(city);
                    city_pop_rl.setVisibility(View.VISIBLE);
                    city_iv.setImageResource(R.mipmap.icon_sanjiao_up);
                    you_pop_ll.setVisibility(View.GONE);
                    you_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                }
                you_pop_ll.setVisibility(View.GONE);
                break;

            case R.id.you_rl:
                if (you_pop_ll.isShown()) {
                    you_pop_ll.setVisibility(View.GONE);
                    you_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                } else {
                    you_pop_ll.setVisibility(View.VISIBLE);
                    you_iv.setImageResource(R.mipmap.icon_sanjiao_up);

                    city_pop_rl.setVisibility(View.GONE);
                    city_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                }
                city_pop_rl.setVisibility(View.GONE);
                break;
            case R.id.city_pop_rl:
                city_pop_rl.setVisibility(View.GONE);
                break;
            case R.id.you_pop_ll:
                you_pop_ll.setVisibility(View.GONE);
                break;

            case R.id.juli_tv:
                you_pop_ll.setVisibility(View.GONE);
                you_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                you_tv.setText("距离优先");
                break;
            case R.id.pingf_tv:
                you_pop_ll.setVisibility(View.GONE);
                you_iv.setImageResource(R.mipmap.icon_sanjiao_down);
                you_tv.setText("评分优先");
                break;
//            case R.id.personalcenter_tv:
//                mMainActivity.intent(PersonalCenterActivity.class);
//                break;
//            case R.id.datainfo_tv:
//                mMainActivity.intent(DataInfoActivity.class);
//                break;

        }
    }


    private View convertView;
    private PopupWindow popupwindowd;


    // 选择时间
    public void initPopupWindow() {
        convertView = getLayoutInflater().inflate(R.layout.pop_city, null,
                false);
        popupwindowd = new PopupWindow(convertView,
                LinearLayout.LayoutParams.FILL_PARENT, getResources()
                .getDimensionPixelSize(R.dimen.hundred), true);
        //        popupwindowd.setAnimationStyle(R.style.AnimationPhoto);

        ExpandableListView city_lv = (ExpandableListView) convertView.findViewById(R.id.city_lv);


        popupwindowd.setBackgroundDrawable(new BitmapDrawable());
        popupwindowd.setFocusable(true);
        popupwindowd.setTouchable(true);
        popupwindowd.setOutsideTouchable(true);
        mMainActivity.backgroundAlpha(0.5f);
        popupwindowd.setOnDismissListener(mMainActivity.new poponDismissListener());
        popupwindowd.update();
    }

}
