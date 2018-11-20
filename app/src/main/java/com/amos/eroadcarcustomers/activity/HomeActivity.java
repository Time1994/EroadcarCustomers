package com.amos.eroadcarcustomers.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.SectionsPagerAdapter;
import com.amos.eroadcarcustomers.fragment.BuyFragment;
import com.amos.eroadcarcustomers.fragment.HomeFragment;
import com.amos.eroadcarcustomers.fragment.HomePageFragment;
import com.amos.eroadcarcustomers.fragment.PersonalCenterFragment;
import com.amos.eroadcarcustomers.fragment.RentFragment;
import com.amos.eroadcarcustomers.fragment.RoadsideFragment;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.a.v;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationBar bottomNavigationBar;
    private BadgeItem badgeItem;
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;

    private HomePageFragment homePageFragment;

    private String city = "上海市";
    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true;// 是否首次定位
    private LatLng lls;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        // 开启定位图层
        // 定位初始化
        mLocClient = new LocationClient(HomeActivity.this);
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
        try {

            if (application.getUserBean().getMm_name().equals("")) {
                intent(DataInfoActivity.class);
            }
        }catch (Exception e ){
            System.out.println("报错："+e);
        }

    }

    private void initView() {
        initBottomNavigationBar();
        initViewPager();
    }

    private void initBottomNavigationBar()

    {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //        bottomNavigationBar.setTabSelectedListener(this);
//        badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("6");
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.white);
        bottomNavigationBar.
                addItem(new BottomNavigationItem(R.mipmap.icon_logo, "首页")
                        .setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.icon_logo_s))
                        .setActiveColorResource(R.color.green).setInActiveColorResource(R.color
                                .blacko))
                .addItem(new BottomNavigationItem(R.mipmap.icon_buy_s, "我要买车")
                        .setInactiveIcon
                                (ContextCompat.getDrawable(this, R.mipmap.icon_buy
                                )).setActiveColorResource(R.color.green).setInActiveColorResource
                                (R.color
                                        .blacko))
                .addItem(new BottomNavigationItem(R.mipmap.icon_order_s, "订单").setInactiveIcon
                        (ContextCompat.getDrawable(this, R.mipmap.icon_order))
                        .setActiveColorResource(R.color.green).setInActiveColorResource(R.color
                                .blacko))
                .addItem(new BottomNavigationItem(R.mipmap.icon_main_s, "我的").setInactiveIcon
                        (ContextCompat.getDrawable(this, R.mipmap.icon_main))
                        .setActiveColorResource(R.color.green).setInActiveColorResource(R.color
                                .blacko).setBadgeItem(badgeItem))
                .initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener
                () {//这里也可以使用SimpleOnTabSelectedListener
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            @Override
            public void onTabSelected(int position) {//未选中 -> 选中

                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中
            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中
            }
        });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new BuyFragment());
        fragments.add(new RoadsideFragment());
        fragments.add(new PersonalCenterFragment());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showShort(R.string.exit_app);
            exitTime = System.currentTimeMillis();
        } else {
            application.exit();
        }
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
                city = location.getCity();
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                lls = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(lls,
                        13);
                // points.add(ll);
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

    }
}
