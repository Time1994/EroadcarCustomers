package com.amos.eroadcarcustomers.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.amos.eroadcarcustomers.activity.HomeActivity;
import com.amos.eroadcarcustomers.utils.Logger;

/**
 * Created by xaozu on 15/8/19. 所以fragment的父类
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
    public View mView;
    public Intent mIntent;
    public HomeActivity mMainActivity;
    public LayoutInflater inflate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutView(), container, false);
        initView();
        //		SDKInitializer.initialize(getActivity().getApplicationContext());
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mIntent == null) {
            mIntent = new Intent();
        }
        try {
            mMainActivity = (HomeActivity) getActivity();
        } catch (Exception e) {
            Logger.getLogger().i("e=" + e);
        }
        if (inflate == null) {
            inflate = LayoutInflater.from(getActivity());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // 得到页面布局
    protected abstract int getLayoutView();

    // 得到页面控件
    protected abstract void initView();

    @Override
    public void onClick(View v) {

    }
}
