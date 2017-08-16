package com.jacob.www.a2dolist.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.presenter.Impl.RegisterAndLoginPresenterImpl;
import com.jacob.www.a2dolist.mvp.presenter.RegisterAndLoginPresenter;
import com.jacob.www.a2dolist.mvp.view.RegisterAndLoginView;
import com.jacob.www.a2dolist.util.BitmapLoader;
import com.jacob.www.a2dolist.util.ClearWriteEditText;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public class LogInActivity extends BaseActivity implements RegisterAndLoginView{
    @InjectView(R.id.login_userName)
    public ClearWriteEditText loginUserName;
    @InjectView(R.id.login_passWord)
    public ClearWriteEditText loginPassWord;
    @InjectView(R.id.btn_login)
    public Button btnLogin;
    @InjectView(R.id.login_register)
    public TextView loginRegister;
    @InjectView(R.id.login_userLogo)
    public ImageView loginUserLogo;
    @InjectView(R.id.login_desc)
    public TextView loginDesc;
    private RegisterAndLoginPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
        presenter = new RegisterAndLoginPresenterImpl();
        presenter.injectView(this);
    }

    private void initView() {

        if (loginUserName.getText().length() == 0 || loginPassWord.getText().length() == 0) {
            btnLogin.setEnabled(false);
        }
        //退出重现上次的账号和头像
        String userName = SharePreferenceManager.getCachedUsername();
        String userAvatar = SharePreferenceManager.getCachedAvatarPath();
        Bitmap bitmap = BitmapLoader.getBitmapFromFile(userAvatar, mAvatarSize, mAvatarSize);
        if (bitmap != null) {
            loginUserLogo.setImageBitmap(bitmap);
        } else {

        }
        loginUserName.setText(userName);
        if (userName != null) {
            loginUserName.setSelection(userName.length());//设置光标位置
        }
        loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (loginUserName.getText().length() == 0 || loginPassWord.getText().length() == 0) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (loginUserName.getText().length() == 0 || loginPassWord.getText().length() == 0) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_login, R.id.login_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                presenter.logIn(this,loginUserName.getText().toString().trim(),loginPassWord.getText().toString().trim());
                break;
            case R.id.login_register:
                presenter.register(this);
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {

    }
}
