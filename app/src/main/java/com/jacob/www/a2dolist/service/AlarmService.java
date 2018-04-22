package com.jacob.www.a2dolist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ASUS-NB on 2017/9/21.
 */

public class AlarmService extends Service {
    AlarmManager am;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        long time=  intent.getLongExtra("time",0);
        Log.e(this.getClass().getName(),"收到了time:"+time);
        setAlarm(time);
        return super.onStartCommand(intent, flags, startId);
    }

    public void setAlarm(long millimeter){
        Intent receiver =  new Intent(this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,receiver,0);
        long time  = millimeter+System.currentTimeMillis();
//        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,millimeter,pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP,time,pendingIntent);
            Log.e(this.getPackageName(),"第一");
        }else {
            am.set(AlarmManager.RTC_WAKEUP,millimeter,pendingIntent);
            Log.e(this.getPackageName(),"第二");
        }
    }
}
