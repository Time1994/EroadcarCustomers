package com.amos.eroadcarcustomers.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.UserBean;

public class LoadingActivity extends BaseActivity {
    private Thread t;
    private CommonBean<UserBean> bean;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        t = new Thread() {
            public void run() {
                try {
                    // Looper.prepare();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tmHandler.post(tmUpdateResults);

            }
        };
        t.start();


    }

    @Override
    public void onBackPressed() {

    }

    final Handler tmHandler = new Handler();
    final Runnable tmUpdateResults = new Runnable() {
        @Override
        public void run() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoadingActivity.this);
            Intent intent = null;
            if (!prefs.getString("PHONE", "").equals("")) {
                UserBean bean = new UserBean();
                bean.setMm_id(prefs.getString("USERID", ""));
                bean.setMm_cellphone(prefs.getString("PHONE", ""));
                application.setUserBean(bean);
                intent = new Intent(LoadingActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(LoadingActivity.this, LoginActivity.class);
                intent.putExtra("FROM", "LOADING");
            }
            startActivity(intent);
            finish();
        }
    };

}
