package com.amos.eroadcarcustomers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.activity.MainActivity;
import com.amos.eroadcarcustomers.activity.MoreServiceActivity;
import com.amos.eroadcarcustomers.activity.RoadsideActivity;
import com.amos.eroadcarcustomers.activity.RoadsideCancelActivity;
import com.amos.eroadcarcustomers.activity.RoadsidePingJiaActivity;
import com.amos.eroadcarcustomers.activity.RoadsidePingJiadActivity;
import com.amos.eroadcarcustomers.activity.RoadsideTActivity;
import com.amos.eroadcarcustomers.adapter.RoadsideAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.bean.RoadsideBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RoadsideFragment extends BaseFragment {
    private RadioGroup top_rg;
    private ListView roadside_lv;
    private LinearLayout none_ll;
    private SmartRefreshLayout refreshLayout;
    private CommonBean<ArrayList<RoadsideBean>> bean;

    private  String type="全部";

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_roadside;
    }

    @Override
    protected void initView() {

        top_rg = (RadioGroup) mView.findViewById(R.id.top_rg);
        roadside_lv = (ListView) mView.findViewById(R.id.roadside_lv);
        none_ll = (LinearLayout) mView.findViewById(R.id.none_ll);
        refreshLayout=(SmartRefreshLayout)mView.findViewById(R.id.refreshLayout);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        top_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.all_rb) {
                    type = "全部";
                } else if (i == R.id.done_rb) {
                    type = "已完成";
                } else if (i == R.id.ping_rb) {
                    type = "已评价";
                } else if (i == R.id.cancel_rb) {
                    type = "已取消";
                }
                getLujiuOrders(type);
            }
        });
refreshLayout.setOnRefreshListener(new OnRefreshListener() {
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getLujiuOrders(type);
        new RoadsideAdapter(getActivity(), bean.getData()).notifyDataSetChanged();
        refreshLayout.finishRefresh(/*,false*/);//传入false表示刷新失败

    }
});
        roadside_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (bean.getData().get(i).getLjif_state().equals("未路救")) {
//                    Intent intent = new Intent(getActivity(), RoadsideTActivity.class);
//                    intent.putExtra("", "");
//                    startActivity(intent);
//                } else
                if (bean.getData().get(i).getLjif_state().equals("已取消")) {
                    Intent intent = new Intent(getActivity(), RoadsideCancelActivity.class);
                    intent.putExtra("ljif_code", bean.getData().get(i).getLjif_code());
                    intent.putExtra("BEAN", bean.getData().get(i));
                    startActivity(intent);
                } else if (bean.getData().get(i).getLjif_state().equals("已评价")) {
                    Intent intent = new Intent(getActivity(), RoadsidePingJiadActivity.class);
                    intent.putExtra("ljif_code", bean.getData().get(i).getLjif_code());
                    startActivity(intent);
                } else if (bean.getData().get(i).getLjif_state().equals("已完成")) {
                    Intent intent = new Intent(getActivity(), RoadsidePingJiaActivity.class);
                    intent.putExtra("ljif_code", bean.getData().get(i).getLjif_code());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), RoadsideTActivity.class);
                    intent.putExtra("ljif_code", bean.getData().get(i).getLjif_code());
                    intent.putExtra("ljif_state", bean.getData().get(i).getLjif_state());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getLujiuOrders(type);
    }

    private void getLujiuOrders(String ljif_state) {
        mMainActivity.loadingDialog.setMessage("正在获取订单信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.GETLUJIUORDERS;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("ljif_state", ljif_state);

        map.put("mm_id", mMainActivity.application.getUserBean().getMm_id());
        map.put("mm_name", mMainActivity.application.getUserBean().getMm_name());
        map.put("mm_cellphone", mMainActivity.application.getUserBean().getMm_cellphone());
        map.put("mm_car_vin", mMainActivity.application.getUserBean().getMm_car_vin());
        map.put("mm_car_lisence", mMainActivity.application.getUserBean().getMm_car_lisence());
        GsonRequest<CommonBean<ArrayList<RoadsideBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<RoadsideBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<ArrayList<RoadsideBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<RoadsideBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<RoadsideBean>> arg0) {
                    // TODO Auto-generated method stub
                    bean = arg0;
                    mHandler.sendEmptyMessage(1);
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
                    if (bean.getState().equals("success")) {
                        if (bean.getData() != null && bean.getData().size() != 0) {
                            none_ll.setVisibility(View.GONE);
                            roadside_lv.setVisibility(View.VISIBLE);

                            roadside_lv.setAdapter(new RoadsideAdapter(getActivity(), bean.getData()));
                        } else {
                            none_ll.setVisibility(View.VISIBLE);
                            roadside_lv.setVisibility(View.GONE);
                        }
                    }

//                    ToastUtils.showShort(bean.getMsg());

                    break;

                case -1:
                    break;
                default:
                    break;
            }
        }
    };
}
