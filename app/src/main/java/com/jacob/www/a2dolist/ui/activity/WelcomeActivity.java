package com.jacob.www.a2dolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jacob.www.a2dolist.MainActivity;
import com.jacob.www.a2dolist.R;

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
        initData();
    }
    private void  initData(){
        UserInfo userInfo = JMessageClient.getMyInfo();
        if(userInfo==null){
            startActivity(new Intent(this,LogInActivity.class));

        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
