package com.amos.eroadcarcustomers.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.UserBean;
import com.amos.eroadcarcustomers.bean.YzCodeBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.view.ClearEditText;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;


import java.util.HashMap;

public class ForgetActivity extends BaseActivity implements OnClickListener {
    private Button login_btn, getcode_btn, back_btn;
    private ClearEditText mobile_cet, pwd_cet, code_cet;

    private TextView title_tv;
    private RelativeLayout top_rl;


    private String CODE = "", mobilePhone = "", inputCode;
    private SharedPreferences prefs;
    private YzCodeBean yzCodeBean;
    private CommonBean commonBean;
    private CommonBean<UserBean> bean;
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        login_btn = (Button) findViewById(R.id.login_btn);
        getcode_btn = (Button) findViewById(R.id.getcode_btn);

        mobile_cet = (ClearEditText) findViewById(R.id.mobile_cet);
        pwd_cet = (ClearEditText) findViewById(R.id.pwd_cet);
        code_cet = (ClearEditText) findViewById(R.id.code_cet);

        back_btn = (Button) findViewById(R.id.back_btn);

        title_tv = (TextView) findViewById(R.id.title_tv);

//        top_rl = (RelativeLayout) findViewById(R.id.top_rl);

        if (getIntent().getStringExtra("FROM").equals("forget")) {
            title_tv.setText(getString(R.string.forget_pwd));
        } else {
            title_tv.setText(getString(R.string.modify));
        }

//        top_rl.setBackgroundColor(getResources().getColor(R.color.transparent));

        back_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        getcode_btn.setOnClickListener(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getString("PHONE", "").equals("")) {
            mobile_cet.setText(prefs.getString("PHONE", ""));
            getcode_btn.setEnabled(true);
        }

        pwd_cet.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (code_cet.getText().length() != 4) {
                        pwd_cet.clearFocus();
                        code_cet.requestFocus();
                        ToastUtils.showShort("请输入验证码");
                    }
                }
            }
        });


        code_cet.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (mobile_cet.getText().length() != 11) {
                        pwd_cet.clearFocus();
                        mobile_cet.requestFocus();
                        ToastUtils.showShort("请输入手机号");
                    }
                }
            }
        });

        code_cet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (mobile_cet.getText().toString().equals("")) {
                    mobile_cet.requestFocus();
                    ToastUtils.showShort("请输入手机号");
                } else if (mobile_cet.getText().toString().length() != 11) {
                    mobile_cet.requestFocus();
                    ToastUtils.showShort("请输入正确的手机号");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        pwd_cet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (code_cet.getText().toString().equals("")) {
                    code_cet.requestFocus();
                    ToastUtils.showShort("请输入验证码");
                } else if (code_cet.getText().toString().length() != 4) {
                    code_cet.requestFocus();
                    ToastUtils.showShort("请输入验证码");
                }

                if (s.length() > 0) {
                    login_btn.setEnabled(true);
                } else {
                    login_btn.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        mobile_cet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() == 11) {
                    getcode_btn.setEnabled(true);
                } else {
                    getcode_btn.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


    }


    private void modifyPassword(String mm_cellphone, String newpassword, String verifycode) {
        loadingDialog.setMessage("正在获取验证码...");
        loadingDialog.dialogShow();
        String url = Constant.modifyPassword;
        System.out.println("url=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("app_code", IMEI);
        map.put("app_type", Constant.APPTYPE);
        map.put("app_version", Constant.VERSION);
        map.put("mm_cellphone", mm_cellphone);
        map.put("newpassword", newpassword);
        map.put("verifycode", verifycode);


        GsonRequest<CommonBean> requtst = new GsonRequest<CommonBean>(Method
                .POST,
                url, listener_modifyPassword);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean> listener_modifyPassword = new
            RequesListener<CommonBean>() {
                @Override
                public void onResponse(CommonBean arg0) {
                    // TODO Auto-generated method stub
                    commonBean = arg0;
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

    private void getVerificateCode(String mm_cellphone) {
        // TODO Auto-generated method stub
        loadingDialog.setMessage("正在获取验证码...");
        loadingDialog.dialogShow();
        String url = Constant.getVerificateCode;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("app_code", IMEI);
        map.put("app_type", Constant.APPTYPE);
        map.put("app_version", Constant.VERSION);
        map.put("mm_cellphone", mm_cellphone);
        GsonRequest<YzCodeBean> requtst = new GsonRequest<YzCodeBean>(
                Method.POST, url, listener_getVerificateCode);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        Logger.getLogger().i("url==" + url);

    }

    private RequesListener<YzCodeBean> listener_getVerificateCode = new RequesListener<YzCodeBean>() {
        @Override
        public void onResponse(YzCodeBean arg0) {
            // TODO Auto-generated method stub
            yzCodeBean = arg0;
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


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(commonBean.getMsg());
                    if (commonBean.getState().equals("success")) {
//                       prefs.edit().putString("PHONE", bean.getData().getMem_mobile()).commit();
//                       prefs.edit().putString("USERID", bean.getData().getMem_id()).commit();
                        Intent intent = new Intent(ForgetActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 2:
                    if (yzCodeBean.getState().equals("success")) {
                        ToastUtils.showShort("验证码已发送,请注意查收短信!");
                        CODE = yzCodeBean.getData();
                        mobilePhone = mobile_cet.getText().toString();
                        MyCountDownTimer mc = new MyCountDownTimer(60000, 1000);
                        mc.start();
                    } else {
                        ToastUtils.showShort(yzCodeBean.getMsg());
                    }
                    break;


                case -1:
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.login_btn:
                mobilePhone = mobile_cet.getText().toString();
                inputCode = pwd_cet.getText().toString();
                modifyPassword(mobilePhone, inputCode, CODE);
//                if (!inputCode.equals(CODE)) {
//                    ToastUtils.showShort("验证码输入有误，请核实后输入");
//                    return;
//                }

                //                memberLogin(mobilePhone, inputCode);
//                memberLogin(mobilePhone, inputCode, "1", "", "");

//                UserBean userBean = new UserBean();
//                userBean.setMem_mobile("18602101879");
//                userBean.setMem_id("1");
//                application.setUserBean(userBean);
//
//                Intent intent = new Intent(LoginActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
//                finish();

                break;

            case R.id.getcode_btn:


                getVerificateCode(mobile_cet.getText().toString());

                MyCountDownTimer mc = new MyCountDownTimer(60000, 1000);
                mc.start();

                break;

//            case R.id.qq_iv:
//                ToastUtils.showShort("正在跳转到QQ...");
//                if (!mTencent.isSessionValid()) {
//                    mTencent.login(this, "", new qqLoginListener());
//                }
//                break;
//            case R.id.weixin_iv:
//                ToastUtils.showShort("正在跳转到微信...");
//                SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "robot";
//                api.sendReq(req);
//                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (from != null && from.equals("LOADING")) {
            Intent intent = new Intent(ForgetActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 继承 CountDownTimer 防范
     * <p>
     * 重写 父类的方法 onTick() 、 onFinish()
     */

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getcode_btn.setEnabled(true);
            getcode_btn.setText(R.string.getcode);
            getcode_btn.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getcode_btn.setText(millisUntilFinished / 1000 + "秒后重发");
            getcode_btn.setTextColor(getResources().getColor(R.color.grayz));
            getcode_btn.setEnabled(false);
        }
    }


}
