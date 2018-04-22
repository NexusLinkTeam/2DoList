package com.jacob.www.a2dolist.util;

import com.jacob.www.a2dolist.Task;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by ASUS-NB on 2017/9/7.
 */

public class SortTaskTime implements Comparator<Task> {
    String year,month,day,hour,minute;
    Calendar calendar = Calendar.getInstance();
    @Override
    public int compare(Task task, Task t1) {
        if(setTime(task).getTime()>setTime(t1).getTime()+5000){
            return 1;
        }else if(setTime(task).getTime()<setTime(t1).getTime()-5000){
            return -1;
        }
        return 0;
    }
    private Date setTime(Task task){
        year = task.getStartTime().split("年")[0];
        month = task.getStartTime().split("年")[1].split("月")[0];
        day = task.getStartTime().split("年")[1].split("月")[1].split("日")[0];
        hour = task.getStartTime().split("年")[1].split(",")[1].split(":")[0];
        minute = task.getStartTime().split("年")[1].split(",")[1].split(":")[1];
        calendar.set(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day),Integer.valueOf(hour),Integer.valueOf(minute),0);
        return calendar.getTime();
    }
}
