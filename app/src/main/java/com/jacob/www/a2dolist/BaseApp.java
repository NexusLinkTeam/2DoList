package com.jacob.www.a2dolist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jacob.www.a2dolist.util.SharePreferenceManager;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public class BaseApp extends Application {
    private static Context mContext;
    public static long registerOrLogin = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init(this,true);
        SharePreferenceManager.init(this,"2DoList_config");
        mContext = this;
    }
    public static Context getAppContext(){
        return mContext;
    }
}
