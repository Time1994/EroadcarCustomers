package com.amos.eroadcarcustomers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.amos.eroadcarcustomers.bean.UserBean;
import com.baidu.mapapi.SDKInitializer;

import java.util.LinkedList;
import java.util.List;


public class MyApplication extends Application {
    private static Context sContext;
    private List<Activity> activityList = new LinkedList<Activity>();
    private UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        SDKInitializer.initialize(this);
           }

    public static Context getContext() {
        return sContext;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            try {
                activity.finish();
            } catch (Exception ex) {

            }
        }
        // System.exit(0);
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }

}
