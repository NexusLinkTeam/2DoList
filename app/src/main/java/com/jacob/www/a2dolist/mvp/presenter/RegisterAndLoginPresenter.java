package com.jacob.www.a2dolist.mvp.presenter;

import android.content.Context;

import com.jacob.www.a2dolist.ui.activity.LogInActivity;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public interface RegisterAndLoginPresenter extends BasePresenter{
    void register(LogInActivity logInActivity);

    void logIn(LogInActivity context, String uId, String password);
}
