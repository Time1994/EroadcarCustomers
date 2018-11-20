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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.adapter.PingImageAdapter;
import com.amos.eroadcarcustomers.adapter.ServiceAdapter;
import com.amos.eroadcarcustomers.bean.CommonBean;
import com.amos.eroadcarcustomers.utils.Constant;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.amos.eroadcarcustomers.utils.Tool;
import com.amos.eroadcarcustomers.view.MyGridView;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class PingJiaActivity extends BaseActivity implements View.OnClickListener {
    private Button back_btn, sure_btn;
    private TextView title_tv, total_tv;
    private ImageView camera_iv;
    private EditText pingjia_et;
    private MyGridView pingimage_gv;
    private XLHRatingBar ratingBar;
    private int num = 200;
    private String rating = "5";
    private PingImageAdapter pingImageAdapter;
    private ArrayList<String> bitmapPath = new ArrayList<String>();
    private String shop , filepath1 = "", filepath2 = "", filepath3 = "";
    private CommonBean commonBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjia);
        sure_btn = (Button) findViewById(R.id.sure_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);
        total_tv = (TextView) findViewById(R.id.total_tv);
        camera_iv = (ImageView) findViewById(R.id.camera_iv);
        pingjia_et = (EditText) findViewById(R.id.pingjia_et);
        pingimage_gv = (MyGridView) findViewById(R.id.pingimage_gv);
        ratingBar = (XLHRatingBar) findViewById(R.id.ratingBar);
        back_btn.setOnClickListener(this);
        camera_iv.setOnClickListener(this);
        title_tv.setText("评论");
        total_tv.setText("0/200");
        sure_btn.setOnClickListener(this);

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
                total_tv.setText(number + "/200");
                selectionStart = pingjia_et.getSelectionStart();
                selectionEnd = pingjia_et.getSelectionEnd();
                if (number == 200) {
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
        ratingBar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                rating = countSelected + "";
                System.out.println("星级好评" + rating);
            }
        });
       shop = getIntent().getStringExtra("SHOP");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.sure_btn:
                submitComments();
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
        }
    }

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");


    private void submitComments() {
        String url = Constant.submitComments;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("cmt_shop", shop);
        multipartBodyBuilder.addFormDataPart("cmt_context", pingjia_et.getText().toString());
        multipartBodyBuilder.addFormDataPart("cmt_level", rating);
        multipartBodyBuilder.addFormDataPart("mm_name", application.getUserBean().getMm_name());

        if (filepath1 != "") {
            multipartBodyBuilder.addFormDataPart("cmt_img1", filepath1,
                    RequestBody.create(MEDIA_TYPE_JPG, new File(filepath1)));
        }
        if (filepath2 != "") {
            multipartBodyBuilder.addFormDataPart("cmt_img2", filepath2,
                    RequestBody.create(MEDIA_TYPE_JPG, new File(filepath2)));
        }
        if (filepath3 != "") {
            multipartBodyBuilder.addFormDataPart("cmt_img3", filepath3,
                    RequestBody.create(MEDIA_TYPE_JPG, new File(filepath3)));
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    if (commonBean.getState().equals("success")) {
                        ToastUtils.showLong("上传成功");
                        onBackPressed();
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
                pingImageAdapter = new PingImageAdapter(PingJiaActivity.this, bitmapPath);
                pingimage_gv.setAdapter(pingImageAdapter);
                filepath1 = bitmapPath.get(0);
                filepath2 = bitmapPath.get(1);
                filepath3 = bitmapPath.get(2);
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
                pingImageAdapter = new PingImageAdapter(PingJiaActivity.this, bitmapPath);
                pingimage_gv.setAdapter(pingImageAdapter);
                //                Tool.saveImage(Tool.ratio(bitmap, 1000, 600), path);
                filepath1 = bitmapPath.get(0);
                filepath2 = bitmapPath.get(1);
                filepath3 = bitmapPath.get(2);

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
}
