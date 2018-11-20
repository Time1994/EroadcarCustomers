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

import org.json.JSONObject;

import java.util.HashMap;

public class RegistActivity extends BaseActivity implements OnClickListener {
    private Button login_btn, getcode_btn;
    private ClearEditText mobile_cet, pwd_cet, code_cet;
    private String CODE = "", mobilePhone = "", inputCode;
    private SharedPreferences prefs;
    private TextView forget_tv;
    private CommonBean<UserBean> bean;
    private YzCodeBean yzCodeBean;
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        login_btn = (Button) findViewById(R.id.login_btn);
        getcode_btn = (Button) findViewById(R.id.getcode_btn);
        mobile_cet = (ClearEditText) findViewById(R.id.mobile_cet);
        pwd_cet = (ClearEditText) findViewById(R.id.pwd_cet);
        code_cet = (ClearEditText) findViewById(R.id.code_cet);
        forget_tv = (TextView) findViewById(R.id.forget_tv);

        login_btn.setOnClickListener(this);
        forget_tv.setOnClickListener(this);
        getcode_btn.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getString("PHONE", "").equals("")) {
            mobile_cet.setText(prefs.getString("PHONE", ""));
        }

        pwd_cet.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (mobile_cet.getText().length() < 11) {
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


    private void signUp(String mm_cellphone, String verificateCode, String mm_sex, String
            mm_name, String mm_pwd, String mm_repwd, String mm_cardid, String apptype, String appcode) {
        loadingDialog.setMessage("正在注册...");
        loadingDialog.dialogShow();
        String url = Constant.signUp;
        System.out.println("url=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mm_cellphone", mm_cellphone);
        map.put("verificateCode", verificateCode);
        map.put("mm_sex", mm_sex);
        map.put("mm_name", mm_name);
        map.put("mm_pwd", mm_pwd);
        map.put("mm_repwd", mm_repwd);
        map.put("mm_cardid", mm_cardid);
        map.put("apptype", Constant.APPTYPE);
        map.put("appcode", IMEI);
        GsonRequest<CommonBean<UserBean>> requtst = new GsonRequest<CommonBean<UserBean>>(Method
                .POST,
                url, listener_login);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<UserBean>> listener_login = new
            RequesListener<CommonBean<UserBean>>() {
                @Override
                public void onResponse(CommonBean<UserBean> arg0) {
                    // TODO Auto-generated method stub
                    bean = arg0;
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
                    ToastUtils.showShort(bean.getMsg());
                    if (bean.getState().equals("success")) {
                        prefs.edit().putString("PHONE", bean.getData().getMm_cellphone()).commit();
                        prefs.edit().putString("USERID", bean.getData().getMm_id()).commit();
                        Intent intent = new Intent(RegistActivity.this,
                                MainActivity.class);
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

//                if (!inputCode.equals(CODE)) {
//                    ToastUtils.showShort("验证码输入有误，请核实后输入");
//                    return;
//                }

                //                memberLogin(mobilePhone, inputCode);
                signUp(mobilePhone, CODE, "", "", inputCode, inputCode, "", Constant.APPTYPE, IMEI);
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
            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
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
