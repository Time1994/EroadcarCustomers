package com.amos.eroadcarcustomers.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amos.eroadcarcustomers.MyApplication;
import com.amos.eroadcarcustomers.R;
import com.amos.eroadcarcustomers.utils.ImageCacheManager;
import com.amos.eroadcarcustomers.utils.LoadingDialog;
import com.amos.eroadcarcustomers.utils.Logger;
import com.amos.eroadcarcustomers.utils.ToastUtils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * @author amos
 */
public class BaseActivity extends FragmentActivity {
    public static RequestQueue mRequestQueue;
    public MyApplication application;

    public LoadingDialog loadingDialog;

    public String IMEI, PHONE;
    public Double latm = 31.095979, lngm = 121.581199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        application = (MyApplication) MyApplication.getContext();
        application.addActivity(this);

        mRequestQueue = Volley.newRequestQueue(MyApplication.getContext());
//		mRequestQueue = Volley.newRequestQueue(this, null, true, R.raw.cert);

        loadingDialog = new LoadingDialog(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            IMEI = telephonyManager.getImei();
            PHONE = telephonyManager.getLine1Number();
        } catch (Exception e) {
            Logger.getLogger().i("get imei =" + e);
        }


        if (PHONE != null && !PHONE.equals("")) {
            PHONE = PHONE.replace("+86", "");
        }

        if (IMEI == null || IMEI.equals("")) {
            IMEI = "0123456789";
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setCostomMsg(String msg) {
        ToastUtils.showLong(msg);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
    }

    public void intent(Class<?> context) {
        Intent intent = new Intent(this, context);
        startActivity(intent);
    }


    public void intentCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public DialogPlus dialogMessage;

    public void showDialogMessage(Context context, String text,
                                  OnClickListener listener) {
        dialogMessage = new DialogPlus.Builder(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_content))
                .setCancelable(true).setGravity(Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent).create();
        View content = dialogMessage.getHolderView();
        Button confirm = (Button) content.findViewById(R.id.btn_dialog_confirm);
        Button cancel = (Button) content.findViewById(R.id.btn_dialog_cancel);
        TextView text_content = (TextView) content
                .findViewById(R.id.text_content);

        text_content.setText(text);
        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });

        dialogMessage.show();
    }

    public void showDialogMessage(Context context, String title, String con,
                                  String confirmText, OnClickListener listener) {
        dialogMessage = new DialogPlus.Builder(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_content))
                .setCancelable(true).setGravity(Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent).create();
        View content = dialogMessage.getHolderView();
        Button confirm = (Button) content.findViewById(R.id.btn_dialog_confirm);
        Button cancel = (Button) content.findViewById(R.id.btn_dialog_cancel);
        TextView text_content = (TextView) content
                .findViewById(R.id.text_content);
        TextView text_title = (TextView) content
                .findViewById(R.id.text_dialog_title);

        text_title.setText(title);
        text_content.setText(con);
        confirm.setText(confirmText);

        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });

        dialogMessage.show();
    }

    public void showDialogMessage(Context context, String title, String con,
                                  String confirmText, String cancelText, OnClickListener listener) {
        dialogMessage = new DialogPlus.Builder(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_content))
                .setCancelable(true).setGravity(Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent).create();
        View content = dialogMessage.getHolderView();
        Button confirm = (Button) content.findViewById(R.id.btn_dialog_confirm);
        Button cancel = (Button) content.findViewById(R.id.btn_dialog_cancel);
        TextView text_content = (TextView) content
                .findViewById(R.id.text_content);
        TextView text_title = (TextView) content
                .findViewById(R.id.text_dialog_title);

        text_title.setText(title);
        text_content.setText(con);
        confirm.setText(confirmText);
        cancel.setText(cancelText);

        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });

        dialogMessage.show();
    }

    public void setImage(String path, final ImageView imageView, final int id) {
        try {
            Drawable mDefaultImageDrawable = getResources().getDrawable(id);
            ImageCacheManager.loadImage(path, ImageCacheManager
                    .getImageListener(imageView, mDefaultImageDrawable,
                            mDefaultImageDrawable));
        } catch (Exception e) {
            // TODO: handle exception
            Logger.getLogger().i("set image=" + e);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    public class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
    }

}
