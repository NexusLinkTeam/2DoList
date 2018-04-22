package com.jacob.www.a2dolist.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.ui.login.LogInActivity;
import com.jacob.www.a2dolist.ui.activity.MainActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        BaseApp.registerOrLogin =1;
        initData();
    }
    private void  initData(){
        UserInfo userInfo = JMessageClient.getMyInfo();
        if(userInfo==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this,LogInActivity.class));
                }
            },500);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
            },500);
        }
        finish();
    }
}
