package com.amos.eroadcarcustomers.activity;

import android.content.ContentResolver;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.GuzhangAdapter;
import com.amos.eroadcarcustomers.adapter.LujiuImageAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.bean.GuzBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.utils.Tool;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.lzy.imagepicker.ImagePicker;
//import com.lzy.imagepicker.ui.ImageGridActivity;
//import com.lzy.imagepicker.view.CropImageView;
//import com.amos.eroadcarcustomers.utils.PicassoImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RoadsideActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn, sure_btn;
    private TextView title_tv, total_tv;
    private EditText pingjia_et;
    private ImageView que_iv, tel_iv, camera_iv;

    private MapView mapView;

    private GridView guzhang_gv, img_gv;

    private int num = 50;
    private CommonBean<String> commonBean;

    private CommonBean<ArrayList<GuzBean>> guzBean;

    public ArrayList<String> typeStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);
        total_tv = (TextView) findViewById(R.id.total_tv);
        pingjia_et = (EditText) findViewById(R.id.pingjia_et);

        que_iv = (ImageView) findViewById(R.id.que_iv);
        tel_iv = (ImageView) findViewById(R.id.tel_iv);
        camera_iv = (ImageView) findViewById(R.id.camera_iv);

        mapView = (MapView) findViewById(R.id.mapView);

        guzhang_gv = (GridView) findViewById(R.id.guzhang_gv);
        img_gv = (GridView) findViewById(R.id.img_gv);

        title_tv.setText("一键路救");
        total_tv.setText("0/50");

        back_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
        que_iv.setOnClickListener(this);
        tel_iv.setOnClickListener(this);
        camera_iv.setOnClickListener(this);

        pingjia_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = s.length();
                total_tv.setText(number + "/50");
                selectionStart = pingjia_et.getSelectionStart();
                selectionEnd = pingjia_et.getSelectionEnd();
                if (number == 50) {
                    ToastUtils.showShort("输入字数已达上限");
                }
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    pingjia_et.setText(s);
                    pingjia_et.setSelection(tempSelection);//设置光标在最后
                }
            }
        });


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


        getLujiuCauses();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.sure_btn:


                if (typeStrings.size() == 0) {
                    ToastUtils.showShort("请选择故障原因");
                    return;
                }
                if (pingjia_et.getText().toString().equals("")) {
                    ToastUtils.showShort("请输入故障说明");
                    return;
                }

                String s = "";
                for (int i = 0; i < typeStrings.size(); i++) {
                    s += typeStrings.get(i) + ",";
                }
                reqLujiu(pingjia_et.getText().toString(), s, latm + "", lngm + "");
                break;
            case R.id.camera_iv:
                if (null != popupwindow && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                } else {
                    initmPopupWindowView_photo();
                    popupwindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.btn_take_photo:// 拍照
                Photograph();
                break;
            case R.id.btn_pick_photo:// 从相册中选中
                choosePic();
                break;
            case R.id.btn_cancel:// 取消
                popupwindow.dismiss();
                break;
            case R.id.que_iv:
                intent(QuestionActivity.class);
                break;
            case R.id.tel_iv:
                intentCall("4009200665");
                break;
        }
    }


    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");


    private void getLujiuCauses() {
        loadingDialog.setMessage("正在获取信息...");
        loadingDialog.dialogShow();
        String url = Constant.GETLUJIUCAUSES;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<GuzBean>>> requtst = new GsonRequest<CommonBean<ArrayList<GuzBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<GuzBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<GuzBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<GuzBean>> arg0) {
                    // TODO Auto-generated method stub
                    guzBean = arg0;
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


    private void reqLujiu(String ljif_remark, String ljif_type, String ljif_point_x, String ljif_point_y) {
        loadingDialog.setMessage("正在上传路救信息...");
        loadingDialog.dialogShow();

        String url = Constant.REQLUJIU;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("ljif_car_vin", application.getUserBean().getMm_car_vin());
        multipartBodyBuilder.addFormDataPart("ljif_car_licence", application.getUserBean().getMm_car_lisence());
        multipartBodyBuilder.addFormDataPart("ljif_remark", ljif_remark);
        multipartBodyBuilder.addFormDataPart("ljif_type", ljif_type);
        multipartBodyBuilder.addFormDataPart("ljif_point_x", ljif_point_y);
        multipartBodyBuilder.addFormDataPart("ljif_point_y", ljif_point_x);

        if (bitmapPath.size() >= 1) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image1", bitmapPath.get(0),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(0))));
        }
        if (bitmapPath.size() >= 2) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image2", bitmapPath.get(1),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(1))));
        }
        if (bitmapPath.size() >= 3) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image3", bitmapPath.get(2),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(2))));
        }
        if (bitmapPath.size() >= 4) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image4", bitmapPath.get(3),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(3))));
        }
        if (bitmapPath.size() >= 5) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image5", bitmapPath.get(4),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(4))));
        }
        if (bitmapPath.size() >= 6) {
            multipartBodyBuilder.addFormDataPart("ljif_lujiu_image6", bitmapPath.get(5),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get(5))));
        }
        multipartBodyBuilder.addFormDataPart("appcode", IMEI);
        multipartBodyBuilder.addFormDataPart("apptype", Constant.APPTYPE);
        multipartBodyBuilder.addFormDataPart("app_version", Constant.VERSION);
        multipartBodyBuilder.addFormDataPart("mm_id", application.getUserBean().getMm_id());
        multipartBodyBuilder.addFormDataPart("mm_name", application.getUserBean().getMm_name());
        multipartBodyBuilder.addFormDataPart("mm_cellphone", application.getUserBean().getMm_cellphone());
        multipartBodyBuilder.addFormDataPart("mm_car_vin", application.getUserBean().getMm_car_vin());
        multipartBodyBuilder.addFormDataPart("mm_car_lisence", application.getUserBean().getMm_car_lisence());
        RequestBody requestBody = multipartBodyBuilder.build();
        okhttp3.Request.Builder RequestBuilder = new okhttp3.Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        okhttp3.Request request = RequestBuilder.build();
        mOkHttpClient.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        loadingDialog.dismiss();
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        System.out.println("上传信息＝" + str);
                        loadingDialog.dismiss();
                        try {
                            commonBean = new Gson().fromJson(str, new TypeToken<CommonBean<String>>() {
                            }.getType());
                            mHandler.sendEmptyMessage(2);
                        } catch (Exception e) {
                            mHandler.sendEmptyMessage(-1);
                            System.out.println("上传信息OKhttp报错" + e);
                        }

                        call.cancel();
                    }
                });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (guzBean.getState().equals("success")) {
                        guzhang_gv.setAdapter(new GuzhangAdapter(RoadsideActivity.this, guzBean.getData()));
                    }
                    break;

                case 2:
                    if (commonBean.getState().equals("success")) {
                        ToastUtils.showLong("路救信息上传成功");

                        Intent intent = new Intent(RoadsideActivity.this, RoadsideTActivity.class);
                        intent.putExtra("ljif_code", commonBean.getData());
                        intent.putExtra("ljif_state", "未路救");
                        startActivity(intent);
                        finish();
//                        intent(RoadsideTActivity.class);
                    }
                    break;
            }
        }
    };

    private void Photograph() {// 系统相机拍照
        try {
            File imageFile = new File(
                    Environment.getExternalStorageDirectory(), "pingjia" + (i++)
                    + ".jpg");// 通过路径创建保存文件
            path = imageFile.getPath();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void choosePic() {// 相册选择图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }


    private View customView;
    private PopupWindow popupwindow;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;

    // 照相弹出层
    public void initmPopupWindowView_photo() {
        // 获取自定义布局文件pop.xml的视图
        customView = getLayoutInflater().inflate(R.layout.pop_photo_choice,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.FILL_PARENT, getResources()
                .getDimensionPixelSize(R.dimen.thundred), true);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的
        popupwindow.setAnimationStyle(R.style.AnimationPhoto);
        // 获取控件
        btn_take_photo = (Button) customView.findViewById(R.id.btn_take_photo);// 拍照
        btn_pick_photo = (Button) customView.findViewById(R.id.btn_pick_photo);// 选中相册
        btn_cancel = (Button) customView.findViewById(R.id.btn_cancel);// 取消
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);

        backgroundAlpha(0.5f);
        popupwindow.setOnDismissListener(new poponDismissListener());

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        // 使其聚集
        popupwindow.setFocusable(true);
        popupwindow.setTouchable(true); // 设置PopupWindow可触摸
        // 设置允许在外点击消失
        popupwindow.setOutsideTouchable(true);
        // 刷新状态
        popupwindow.update();
    }


    private String path;
    private int i = 0;
    private ArrayList<String> bitmapPath = new ArrayList<>();

    // 获取从相册或相机传来的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {// 拍照
            try {
                // 获得图片
                int degree = Tool.readPictureDegree(path);
                bitmapPath.add(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap = Tool.rotaingImageView(degree, bitmap);
                Tool.saveImage(Tool.ratio(bitmap, 1000, 1000), path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {
            try {
                Uri selectedImage = geturi(data);// data.getData(); //
                // 获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex); // 获取照片路径
                cursor.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                bitmapPath.add(picturePath);
            } catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }


        if (null != popupwindow && popupwindow.isShowing()) {
            popupwindow.dismiss();
        }

        img_gv.setAdapter(new LujiuImageAdapter(RoadsideActivity.this, bitmapPath));
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
}
