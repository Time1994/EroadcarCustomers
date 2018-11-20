package com.amos.eroadcarcustomers.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.activity.BaseActivity;
import com.amos.eroadcarcustomers.activity.DataInfoActivity;
import com.amos.eroadcarcustomers.adapter.PersonalImgAdapter;
import com.amos.eroadcarcustomers.adapter.RoadsideAdapter;
import com.amos.eroadcarcustomers.bean.CenterBean;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.view.RoundImageView;
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
 * Created by Administrator on 2018/7/29.
 */

public class PersonalCenterFragment extends BaseFragment {
    private Button back_btn, other_btn;
    private TextView title_tv, name_tv, idnum_tv, chepai_tv, jia_tv;
    private RoundImageView photo_iv;
    private ImageView man_iv, woman_iv;
    private SmartRefreshLayout refreshLayout;
    private GridView img_gv;
    private ScrollView all_sv;

    private CommonBean<CenterBean> centerBean;

    @Override
    protected void initView() {
        back_btn = (Button) mView.findViewById(R.id.back_btn);
        other_btn = (Button) mView.findViewById(R.id.other_btn);
        title_tv = (TextView) mView.findViewById(R.id.title_tv);
        name_tv = (TextView) mView.findViewById(R.id.name_tv);
        idnum_tv = (TextView) mView.findViewById(R.id.idnum_tv);
        photo_iv = (RoundImageView) mView.findViewById(R.id.photo_iv);
        man_iv = (ImageView) mView.findViewById(R.id.man_iv);
        woman_iv = (ImageView) mView.findViewById(R.id.woman_iv);

        chepai_tv = (TextView) mView.findViewById(R.id.chepai_tv);
        jia_tv = (TextView) mView.findViewById(R.id.jia_tv);

        img_gv = (GridView) mView.findViewById(R.id.img_gv);

        all_sv = (ScrollView) mView.findViewById(R.id.all_sv);
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.refreshLayout);

        back_btn.setOnClickListener(this);
        title_tv.setText("个人中心");
        back_btn.setVisibility(View.GONE);
        other_btn.setOnClickListener(this);


    }

    private void getPersonalCenter(String mm_id) {
        mMainActivity.loadingDialog.setMessage("正在获取个人信息...");
        mMainActivity.loadingDialog.dialogShow();
        String url = Constant.getPersonalCenter;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", mMainActivity.IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        map.put("mm_id", mm_id);
        GsonRequest<CommonBean<CenterBean>> requtst = new
                GsonRequest<CommonBean<CenterBean>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mMainActivity.mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_personalcenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPersonalCenter(mMainActivity.application.getUserBean().getMm_id());
        try {

            if (mMainActivity.application.getUserBean().getMm_name().equals("")) {
                mMainActivity.intent(DataInfoActivity.class);
            }
        } catch (Exception e) {
            System.out.println("报错：" + e);
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPersonalCenter(mMainActivity.application.getUserBean().getMm_id());
                refreshLayout.finishRefresh(/*,false*/);//传入false表示刷新失败

            }
        });
    }


    private RequesListener<CommonBean<CenterBean>> listener_code = new
            RequesListener<CommonBean<CenterBean>>() {
                @Override
                public void onResponse(CommonBean<CenterBean> arg0) {
                    // TODO Auto-generated method stub
                    centerBean = arg0;
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

    public void onResume() {
        super.onResume();
        getPersonalCenter(mMainActivity.application.getUserBean().getMm_id());
    }

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

                        chepai_tv.setText(centerBean.getData().getMm_car_lisence());
                        jia_tv.setText(centerBean.getData().getMm_car_vin());

                        if (centerBean.getData().getMm_sex().equals("男")) {
                            man_iv.setImageResource(R.mipmap.icon_rediobutton_s);
                        } else if (centerBean
                                .getData().getMm_sex().equals("女")) {
                            woman_iv.setImageResource(R.mipmap.icon_rediobutton_s);
                        } else {
                            man_iv.setImageResource(R.mipmap.icon_radiobutton);
                            man_iv.setImageResource(R.mipmap.icon_radiobutton);
                        }

                        ArrayList<HomeBean> list = new ArrayList<>();
                        HomeBean bean1 = new HomeBean();
                        bean1.setHm_module("正面");
                        bean1.setHm_img(centerBean.getData().getMm_car_img1());
                        list.add(bean1);

                        HomeBean bean2 = new HomeBean();
                        bean2.setHm_module("背面");
                        bean2.setHm_img(centerBean.getData().getMm_car_img2());
                        list.add(bean2);

                        HomeBean bean3 = new HomeBean();
                        bean3.setHm_module("左前侧45度");
                        bean3.setHm_img(centerBean.getData().getMm_car_img3());
                        list.add(bean3);

                        HomeBean bean4 = new HomeBean();
                        bean4.setHm_module("右前侧45度");
                        bean4.setHm_img(centerBean.getData().getMm_car_img4());
                        list.add(bean4);

                        img_gv.setAdapter(new PersonalImgAdapter(getActivity(), list));

                        all_sv.post(new Runnable() {
                            @Override
                            public void run() {

                                all_sv.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
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
                break;
            case R.id.other_btn:
                mMainActivity.intent(DataInfoActivity.class);
                break;
        }
    }
}
