package com.amos.eroadcarcustomers.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.RoadsideBean;
import com.amos.eroadcarcustomers.utils.BdMapUtils;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RoadsideCancelActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn;
    private TextView title_tv;

    private TextView time_tv, address_tv;

    private MapView mapView;

    private RoadsideBean roadsideBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_cancel);
        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);

        time_tv = (TextView) findViewById(R.id.time_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);

        mapView = (MapView) findViewById(R.id.mapView);

//        ljif_code = getIntent().getStringExtra("ljif_code");

        title_tv.setText("一键路救");

        back_btn.setOnClickListener(this);

        roadsideBean = (RoadsideBean) getIntent().getSerializableExtra("BEAN");

        lngm = Double.valueOf(roadsideBean.getLjif_point_x());
        latm = Double.valueOf(roadsideBean.getLjif_point_y());

        time_tv.setText(roadsideBean.getLjif_cancel_time());
        address_tv.setText(roadsideBean.getLjif_address());


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


//        BdMapUtils.reverseGeoParse(lngm, latm, new OnGetGeoCoderResultListener() {
//            //获取正向解析结果时执行函数
//            @Override
//            public void onGetGeoCodeResult(GeoCodeResult arg0) {
//            }
//
//            //获取反向解析结果时执行函数
//            @Override
//            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    // 没有检测到结果
//
//                } else {////得到结果后处理方法
//                    address_tv.setText(result.getAddress());
//                }
//            }
//
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

        }
    }

}
