package com.jacob.www.a2dolist.util;

import java.util.Comparator;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ASUS-NB on 2017/8/29.
 */

public class SortConvList implements Comparator<Conversation> {
    @Override
    public int compare(Conversation o, Conversation t1) {
        if(o.getLastMsgDate()> t1.getLastMsgDate()){
            return -1;
        }else if(o.getLastMsgDate()<t1.getLastMsgDate()){
            return 1;
        }
        return 0;
    }
}
