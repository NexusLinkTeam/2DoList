package com.jacob.www.a2dolist.mvp.presenter.login;

import com.jacob.www.a2dolist.mvp.presenter.BasePresenter;
import com.jacob.www.a2dolist.ui.login.LogInActivity;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public interface RegisterAndLoginPresenter extends BasePresenter {
    void register(LogInActivity logInActivity);

    void logIn(LogInActivity context, String uId, String password);
}
