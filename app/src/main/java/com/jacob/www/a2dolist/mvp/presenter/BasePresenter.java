package com.jacob.www.a2dolist.mvp.presenter;

import android.support.annotation.NonNull;

import com.jacob.www.a2dolist.mvp.view.BaseView;


/**
 * Created by 猿人 on 2017/4/9.
 */

public interface BasePresenter {

    void injectView(@NonNull BaseView view);
}
