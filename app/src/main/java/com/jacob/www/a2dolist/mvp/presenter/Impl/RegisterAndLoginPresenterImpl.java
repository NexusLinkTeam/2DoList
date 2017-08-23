package com.jacob.www.a2dolist.mvp.presenter.Impl;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.MainActivity;
import com.jacob.www.a2dolist.mvp.presenter.RegisterAndLoginPresenter;
import com.jacob.www.a2dolist.mvp.view.RegisterAndLoginView;
import com.jacob.www.a2dolist.ui.activity.FinishRegisterActivity;
import com.jacob.www.a2dolist.ui.activity.LogInActivity;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public class RegisterAndLoginPresenterImpl extends BasePresenterImpl<RegisterAndLoginView> implements RegisterAndLoginPresenter {
    @Override
    public void register(LogInActivity logInActivity) {
        logInActivity.loginPassWord.setText("");
        BaseApp.registerOrLogin++;
        if (BaseApp.registerOrLogin % 2 == 0) {
            logInActivity.btnLogin.setText("注册");
            logInActivity.loginRegister.setText("立即登陆");
            logInActivity.loginDesc.setText("已有账号? ");
        } else {
            logInActivity.btnLogin.setText("登录");
            logInActivity.loginRegister.setText("立即注册");
            logInActivity.loginDesc.setText("还没有账号? ");
        }
    }

    @Override
    public void logIn(final LogInActivity context, String uId, String password) {
        if (TextUtils.isEmpty(uId)) {
            Toast.makeText(context,"用户名不能为空",Toast.LENGTH_SHORT).show();
            context.loginUserName.setShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
            context.loginPassWord.setShakeAnimation();
            return;
        }
        if (uId.length() < 4 || uId.length() > 128) {
            context.loginUserName.setShakeAnimation();
            Toast.makeText(context,"用户名为4-128位字符",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4 || password.length() > 128) {
            context.loginUserName.setShakeAnimation();
            Toast.makeText(context,"密码为4-128位字符",Toast.LENGTH_SHORT).show();
            return;
        }
        if (isContainChinese(uId)) {
            context.loginUserName.setShakeAnimation();
            Toast.makeText(context,"用户名不支持中文",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!whatStartWith(uId)) {
            context.loginUserName.setShakeAnimation();
            Toast.makeText(context,"用户名以字母或者数字开头",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!whatContain(uId)) {
            context.loginUserName.setShakeAnimation();
            Toast.makeText(context,"只能含有: 数字 字母 下划线 . - @",Toast.LENGTH_SHORT).show();
            return;
        }
        //登录
        if(BaseApp.registerOrLogin%2==1){
            mView.showProgress();
            JMessageClient.login(uId, password, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    mView.hideProgress();
                    if(i ==0){
                        UserInfo myInfo = JMessageClient.getMyInfo();
                        File  avatarFile = myInfo.getAvatarFile();
                        if(avatarFile!=null){
                            SharePreferenceManager.setCachedAvatarPath(avatarFile.getAbsolutePath());
                            Log.e("TAG",avatarFile.getAbsolutePath()+" ++"+avatarFile.getPath());
                        }else {
                            SharePreferenceManager.setCachedAvatarPath(null);
                            Log.e("TAG","没有");
                        }
                        Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, MainActivity.class));
                        context.finish();
                    }else {

                        Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            //注册
            Intent intent = new Intent(context, FinishRegisterActivity.class);
            intent.putExtra("uid",uId);
            intent.putExtra("password",password);
            context.startActivity(intent);
        }
    }
    private boolean isContainChinese(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private boolean whatStartWith(String str) {
        Pattern pattern = Pattern.compile("^([A-Za-z]|[0-9])");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    private boolean whatContain(String str) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z][a-zA-Z0-9_\\-@\\.]{3,127}$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
