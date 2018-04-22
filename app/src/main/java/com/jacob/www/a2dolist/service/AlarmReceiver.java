package com.jacob.www.a2dolist.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.Date;
import com.jacob.www.a2dolist.DateDao;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.Task;
import com.jacob.www.a2dolist.TaskDao;
import com.jacob.www.a2dolist.ui.activity.MainActivity;
import com.jacob.www.a2dolist.util.SortTaskTime;
import com.jacob.www.a2dolist.util.SortTimeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS-NB on 2017/9/24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    DateDao dateDao = BaseApp.getDaosession().getDateDao();
    TaskDao taskDao = BaseApp.getDaosession().getTaskDao();
    public AlarmReceiver(){

    }

    private void showNotify(Context context){

        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);
        List<Task> newTasks = taskDao.queryBuilder().listLazy();
        PendingIntent pd = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        int k =0;
        for(Task task :newTasks){
            if(task.getStartTime().split(":")[1].equals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()).split(":")[1])&&!task.getIsChecked()){
               task.setIsChecked(true);
                taskDao.update(task);
                break;
            }
            k++;
        }
        Notification.Builder builder = new Notification.Builder(context)
                .setOngoing(false)
                .setSmallIcon(R.drawable.add_logo)
                .setContentTitle(newTasks.get(k).getTitle()+":")
                .setContentText(newTasks.get(k).getContent())
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pd)
                .setDefaults(Notification.DEFAULT_ALL);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, builder.build());
    }
    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context,"任务来了",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                showNotify(context);
            }
        }).start();
        List<Date> dates = dateDao.queryBuilder().listLazy();
        List<java.util.Date> realDates = new ArrayList<>();
        int i=0;
        if (dates != null && dates.size() != 0) {
            SortTimeList sort = new SortTimeList();
            for (com.jacob.www.a2dolist.Date entry : dates) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(entry.getDate().split("-")[0]), Integer.valueOf(entry.getDate().split("-")[1]), Integer.valueOf(entry.getDate().split("-")[2]),
                        Integer.valueOf(entry.getDate().split("-")[3]), Integer.valueOf(entry.getDate().split("-")[4]), Integer.valueOf(entry.getDate().split("-")[5]));
                realDates.add(calendar.getTime());
                if(realDates.get(realDates.size()-1).getTime()<realDates.get(i).getTime()) {
                    i = realDates.size() - 1;
                    Log.e("TAG", "i是" + i);
                }
            }
            dateDao.deleteByKey(dates.get(i).getId());
            realDates.clear();
            List<com.jacob.www.a2dolist.Date> dates1 = dateDao.queryBuilder().listLazy();
            if(dates1!=null&&dates1.size()!=0){
                for (com.jacob.www.a2dolist.Date entry : dates1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Integer.valueOf(entry.getDate().split("-")[0]), Integer.valueOf(entry.getDate().split("-")[1]), Integer.valueOf(entry.getDate().split("-")[2]),
                            Integer.valueOf(entry.getDate().split("-")[3]), Integer.valueOf(entry.getDate().split("-")[4]), Integer.valueOf(entry.getDate().split("-")[5]));
                    realDates.add(calendar.getTime());
                }
                Collections.sort(realDates,sort);
                Long time = realDates.get(0).getTime()-new java.util.Date().getTime();
                Log.e("TAG",time
                        +"");
                Intent inten1t = new Intent(context,AlarmService.class);
                inten1t.putExtra("time",time);
                context.startService(inten1t);
            }
        }
    }
}