package com.jacob.www.a2dolist.mvp.view;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ASUS-NB on 2017/9/4.
 */

public interface ContactView extends BaseView{
    void getFriend();

    void showFriend(List<Conversation> list);
}
