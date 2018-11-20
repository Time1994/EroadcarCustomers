package com.amos.eroadcarcustomers.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.amos.eroadcarcustomers.R;


/**
 * Created by amos on 14-3-17.
 * 加载中对话框显示
 */
public class LoadingDialog {
    private Dialog loadingDialog;
    private TextView textView;
    public LoadingDialog(Context context){
        loadingDialog=new Dialog(context, R.style.MyDialog_loading);
        loadingDialog.setContentView(R.layout.dialog_loading);
        textView= (TextView) loadingDialog.findViewById(R.id.loading_message);
    }

    /**
     * 设置消息显示
     * @param message
     */
    public void setMessage(String message) {
        textView.setText(message);
    }
    /**
     * 关闭对话框
     */
    public void dismiss(){
        loadingDialog.dismiss();
    }
    /**
     * 显示对话框
     */
    public void dialogShow(){
        loadingDialog.show();
    }
}

