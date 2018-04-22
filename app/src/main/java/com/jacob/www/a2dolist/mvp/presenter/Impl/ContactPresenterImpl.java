package com.jacob.www.a2dolist.mvp.presenter.Impl;

import android.app.Activity;
import android.util.Log;

import com.jacob.www.a2dolist.mvp.model.ContactModel;
import com.jacob.www.a2dolist.mvp.model.Impl.ContactModelImpl;
import com.jacob.www.a2dolist.mvp.model.OnTaskOverListener;
import com.jacob.www.a2dolist.mvp.presenter.ContactPresenter;
import com.jacob.www.a2dolist.mvp.view.ContactView;
import com.jacob.www.a2dolist.ui.activity.CreateGroupActivity;
import com.jacob.www.a2dolist.ui.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ASUS-NB on 2017/9/4.
 */

public class ContactPresenterImpl extends BasePresenterImpl<ContactView> implements ContactPresenter,OnTaskOverListener {
    ContactModel model = new ContactModelImpl();
    @Override
    public void getFriend(Activity activity) {
        model.requestFriend(this,activity);
    }

    @Override
    public void onSucceed(Object o) {
        if(o instanceof ArrayList&&mView instanceof GroupFragment){
            ((GroupFragment)mView).showFriend((List<Conversation>)o);
        }else if (o instanceof ArrayList&&mView instanceof CreateGroupActivity){
            ((CreateGroupActivity)mView).showFriend((List<Conversation>)o);
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        Log.e("ContactPresenterImpl",throwable.getMessage());
    }
}
