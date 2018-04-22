package com.jacob.www.a2dolist.mvp.model.Impl;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.jacob.www.a2dolist.mvp.model.ContactModel;
import com.jacob.www.a2dolist.mvp.model.OnTaskOverListener;
import com.jacob.www.a2dolist.util.SortConvList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ASUS-NB on 2017/9/4.
 */

public class ContactModelImpl implements ContactModel {
    private List<Conversation> mDatas = new ArrayList<Conversation>();

    public void requestFriend(final OnTaskOverListener listener, final Activity mContext) {
        mDatas = JMessageClient.getConversationList();
        Log.e("TAG","开始请求好友了哦");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDatas = JMessageClient.getConversationList();
                        Log.e("TAG","继续请求好友");
                        for(int i=0;i<mDatas.size();i++){
                            Log.e("TAG","得到的好友不为空"+i);
                            if(mDatas.get(i).getType()== ConversationType.group){
                                mDatas.remove(i--);
                            }
                        }
                        if(mDatas!=null&&mDatas.size()>0){

                            SortConvList sortConvList = new SortConvList();
                            Collections.sort(mDatas,sortConvList);
                        }else {
                            Log.e("TAG","得到的好友是空");
                            //如果没好友怎么办
                        }
                            listener.onSucceed(mDatas);
                    }
                });
            }
        },200);
    }
}
