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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.bean.CommonBean;
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
import com.example.macadress.*;
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

public class DataInfoActivity extends BaseActivity implements View.OnClickListener {
    private Button sure_btn, back_btn;
    private ClearEditText id_cet, name_cet, pai_cet, jia_cet;
    private RadioGroup sex_rg;
    private RadioButton man_rb, woman_rb;

    private ImageView img1_iv, img2_iv, img3_iv, img4_iv;


    private String sex = "男";
    private String IdCode = "";
    private String name = "";
    private CommonBean<ArrayList<UserBean>> userBean;

    private CommonBean commonBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datainfo);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        id_cet = (ClearEditText) findViewById(R.id.id_cet);
        name_cet = (ClearEditText) findViewById(R.id.name_cet);
        sex_rg = (RadioGroup) findViewById(R.id.sex_rg);
        man_rb = (RadioButton) findViewById(R.id.man_rb);
        woman_rb = (RadioButton) findViewById(R.id.woman_rb);

        img1_iv = (ImageView) findViewById(R.id.img1_iv);
        img2_iv = (ImageView) findViewById(R.id.img2_iv);
        img3_iv = (ImageView) findViewById(R.id.img3_iv);
        img4_iv = (ImageView) findViewById(R.id.img4_iv);

        pai_cet = (ClearEditText) findViewById(R.id.pai_cet);
        jia_cet = (ClearEditText) findViewById(R.id.jia_cet);


        back_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
        img1_iv.setOnClickListener(this);
        img2_iv.setOnClickListener(this);
        img3_iv.setOnClickListener(this);
        img4_iv.setOnClickListener(this);

        sex_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) DataInfoActivity.this
                        .findViewById(radioButtonId);
                sex = rb.getText().toString();
            }
        });

        verifyStoragePermissions(this);
    }

    private void fillInfo(String mm_id, String mm_cellphone, String mm_cardid, String mm_sex,
                          String mm_name) {
        loadingDialog.setMessage("正在提交信息...");
        loadingDialog.dialogShow();
        String url = Constant.fillInfo;
        Logger.getLogger().i("URL=" + url);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mm_id", mm_id);
        map.put("mm_cellphone", mm_cellphone);
        map.put("mm_cardid", mm_cardid);
        map.put("mm_sex", mm_sex);
        map.put("mm_name", mm_name);
        map.put("rb_imei", IMEI);
        map.put("rb_type", Constant.APPTYPE);
        map.put("rb_version", Constant.VERSION);
        GsonRequest<CommonBean<ArrayList<UserBean>>> requtst = new
                GsonRequest<CommonBean<ArrayList<UserBean>>>(Request.Method.POST,
                url, listener_code);
        requtst.setHashmap(map);
        mRequestQueue.add(requtst);
        //        mRequestQueue.start();
    }

    private RequesListener<CommonBean<ArrayList<UserBean>>> listener_code = new
            RequesListener<CommonBean<ArrayList<UserBean>>>() {
                @Override
                public void onResponse(CommonBean<ArrayList<UserBean>> arg0) {
                    // TODO Auto-generated method stub
                    userBean = arg0;
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
                    ToastUtils.showShort(userBean.getMsg());
                    if (userBean.getState().equals("success")) {
                        intent(HomeActivity.class);
                        finish();
                    }
                    break;
                case 2:
                    if (commonBean.getState().equals("success")) {
                        application.getUserBean().setMm_name(name);
                        application.getUserBean().setMm_car_lisence(pai_cet.getText().toString());
                        application.getUserBean().setMm_car_vin(jia_cet.getText().toString());
                        ToastUtils.showLong("上传成功");
                        onBackPressed();
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
                onBackPressed();
                break;
            case R.id.img1_iv:
                clickImg("one", v);
                break;
            case R.id.img2_iv:
                clickImg("two", v);
                break;
            case R.id.img3_iv:
                clickImg("three", v);
                break;
            case R.id.img4_iv:
                clickImg("four", v);
                break;
            case R.id.sure_btn:
//                if (name_cet.getText().toString().equals("")) {
//                    ToastUtils.showShort("请填写姓名");
//                    return;
//                }
//                if (id_cet.getText().toString().length() != 18) {
//                    ToastUtils.showShort("请填写正确的身份证");
//                    return;
//                }
                IdCode = id_cet.getText().toString();
                name = name_cet.getText().toString();

//                fillInfo(application.getUserBean().getMm_id(), application.getUserBean()
//                        .getMm_cellphone(), IdCode, sex, name);

                submitComments(application.getUserBean().getMm_id(), application.getUserBean()
                        .getMm_cellphone(), IdCode, sex, name, jia_cet.getText().toString(), pai_cet.getText().toString());
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
        }
    }

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");


    private void
    submitComments(String mm_id, String mm_cellphone, String mm_cardid, String mm_sex,
                                String mm_name, String mm_car_vin, String mm_car_lisence) {
        String url = Constant.fillInfo;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);


        multipartBodyBuilder.addFormDataPart("mm_id", mm_id);
        multipartBodyBuilder.addFormDataPart("mm_cellphone", mm_cellphone);
        multipartBodyBuilder.addFormDataPart("mm_cardid", mm_cardid);
        multipartBodyBuilder.addFormDataPart("mm_sex", mm_sex);
        multipartBodyBuilder.addFormDataPart("mm_name", mm_name);
        multipartBodyBuilder.addFormDataPart("mm_car_vin", mm_car_vin);
        multipartBodyBuilder.addFormDataPart("mm_car_lisence", mm_car_lisence);

        multipartBodyBuilder.addFormDataPart("rb_imei", IMEI);
        multipartBodyBuilder.addFormDataPart("rb_type", Constant.APPTYPE);
        multipartBodyBuilder.addFormDataPart("rb_version", Constant.VERSION);

        if (bitmapPath.get("one") != null && !bitmapPath.get("one").equals("")) {
            multipartBodyBuilder.addFormDataPart("mm_car_img1", bitmapPath.get("one"),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get("one"))));
        }
        if (bitmapPath.get("two") != null && !bitmapPath.get("two").equals("")) {
            multipartBodyBuilder.addFormDataPart("mm_car_img2", bitmapPath.get("two"),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get("two"))));
        }
        if (bitmapPath.get("three") != null && !bitmapPath.get("three").equals("")) {
            multipartBodyBuilder.addFormDataPart("mm_car_img3", bitmapPath.get("three"),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get("three"))));
        }
        if (bitmapPath.get("four") != null && !bitmapPath.get("four").equals("")) {
            multipartBodyBuilder.addFormDataPart("mm_car_img4", bitmapPath.get("four"),
                    RequestBody.create(MEDIA_TYPE_JPG, new File(bitmapPath.get("four"))));
        }

        multipartBodyBuilder.addFormDataPart("appcode", IMEI);
        multipartBodyBuilder.addFormDataPart("apptype", Constant.APPTYPE);
        multipartBodyBuilder.addFormDataPart("app_version", Constant.VERSION);

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
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        System.out.println("上传信息＝" + str);
                        try {
                            commonBean = new Gson().fromJson(str, new TypeToken<CommonBean>() {
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


    private void clickImg(String c, View v) {
        clickType = c;

        if (null != popupwindow && popupwindow.isShowing()) {
            popupwindow.dismiss();
        } else {
            initmPopupWindowView_photo();
            popupwindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }

    private void setImg(Bitmap b) {
        if (clickType.equals("one")) {
            img1_iv.setImageBitmap(b);
        } else if (clickType.equals("two")) {
            img2_iv.setImageBitmap(b);
        } else if (clickType.equals("three")) {
            img3_iv.setImageBitmap(b);
        } else if (clickType.equals("four")) {
            img4_iv.setImageBitmap(b);
        }
    }

    private void Photograph() {// 系统相机拍照
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 提示已经禁止
                ToastUtils.showShort("请在设置中打开权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            try {
                File imageFile = new File(
                        Environment.getExternalStorageDirectory(), "pingjia" + clickType
                        + ".jpg");// 通过路径创建保存文件
                path = imageFile.getPath();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    private String path = "", clickType = "";
    private Map<String, String> bitmapPath = new HashMap<>();

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
                bitmapPath.put(clickType, path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap = Tool.rotaingImageView(degree, bitmap);
                Tool.saveImage(Tool.ratio(bitmap, 1000, 1000), path);

                setImg(bitmap);
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
                bitmapPath.put(clickType, picturePath);

                setImg(bitmap);
            } catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
        if (null != popupwindow && popupwindow.isShowing()) {
            popupwindow.dismiss();
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
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


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
