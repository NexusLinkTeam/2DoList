package com.jacob.www.a2dolist.util;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ASUS-NB on 2017/9/24.
 */

public class SortTimeList implements Comparator<Date> {
    @Override
    public int compare(Date date, Date t1) {
        if(date.getTime()>t1.getTime()){
            return 1;
        }else if(date.getTime()<t1.getTime()){
            return -1;
        }
        return 0;
    }
}
