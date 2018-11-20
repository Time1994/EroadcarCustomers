package com.amos.eroadcarcustomers.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.activity.MainActivity;
import com.amos.eroadcarcustomers.activity.MoreServiceActivity;
import com.amos.eroadcarcustomers.activity.QuestionActivity;
import com.amos.eroadcarcustomers.activity.RoadsideActivity;
import com.amos.eroadcarcustomers.adapter.HomeAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.HomeBean;
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

public class HomeFragment extends BaseFragment {
    private GridView home_gv;

    private CommonBean<ArrayList<HomeBean>> bean;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        home_gv = (GridView) mView.findViewById(R.id.home_gv);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        home_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (bean.getData().get(i).getHm_tab().equals("services")) {
                    ((BaseActivity) getActivity()).intent(MainActivity.class);
                } else if (bean.getData().get(i).getHm_tab().equals("more")) {
                    ((BaseActivity) getActivity()).intent(MoreServiceActivity.class);
                } else if (bean.getData().get(i).getHm_tab().equals("lujiu")) {
                    ((BaseActivity) getActivity()).intent(RoadsideActivity.class);
                } else {
                    ToastUtils.showShort("该功能正在开发中...");
                }
            }
        });

        getHomeModules();
    }


    private void getHomeModules() {
        mMainActivity.loadingDialog.setMessage("正在获取信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.GETHOMEMODULES;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<HomeBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<HomeBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<ArrayList<HomeBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<HomeBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<HomeBean>> arg0) {
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
                        home_gv.setAdapter(new HomeAdapter(getActivity(), bean.getData()));
                    } else {
                        ToastUtils.showShort(bean.getMsg());
                    }

                    break;

                case -1:
                    break;
                default:
                    break;
            }
        }
    };

}
