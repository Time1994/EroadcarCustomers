package com.amos.eroadcarcustomers.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.MoreAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.HomeBean;
import com.amos.eroadcarcustomers.bean.UserBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.utils.Tool;
import com.amos.eroadcarcustomers.view.ClearEditText;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.GsonRequest;
import com.android.volley.toolbox.gson.RequesListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amos on 2018/7/28.
 */

public class MoreServiceActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn;
    private TextView title_tv;

    private GridView img_gv;

    private CommonBean<ArrayList<HomeBean>> commonBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_service);

        title_tv = (TextView) findViewById(R.id.title_tv);
        back_btn = (Button) findViewById(R.id.back_btn);

        img_gv = (GridView) findViewById(R.id.img_gv);

        title_tv.setText("更多服务");

        back_btn.setOnClickListener(this);

        getRecommends();
    }

    private void getRecommends() {
        loadingDialog.setMessage("正在获取信息...");
        loadingDialog.dialogShow();
        String url = Constant.GETRECOMMENDS;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<HomeBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<HomeBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
    }

    private RequesListener<CommonBean<ArrayList<HomeBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<HomeBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<HomeBean>> arg0) {
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
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (commonBean.getState().equals("success")) {
                        img_gv.setAdapter(new MoreAdapter(MoreServiceActivity.this, commonBean.getData()));
                    }

                    ToastUtils.showShort(commonBean.getMsg());
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
                onBackPressed();
                break;

        }
    }

}
