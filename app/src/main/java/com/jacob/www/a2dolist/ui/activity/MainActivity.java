package com.jacob.www.a2dolist.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.DaoSession;
import com.jacob.www.a2dolist.Date;
import com.jacob.www.a2dolist.DateDao;
import com.jacob.www.a2dolist.FriendVerify;
import com.jacob.www.a2dolist.FriendVerifyDao;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.Task;
import com.jacob.www.a2dolist.TaskDao;
import com.jacob.www.a2dolist.service.AlarmService;
import com.jacob.www.a2dolist.ui.fragment.GroupFragment;
import com.jacob.www.a2dolist.ui.fragment.ToDoFragment;
import com.jacob.www.a2dolist.ui.login.LogInActivity;
import com.jacob.www.a2dolist.util.CircleImageView;
import com.jacob.www.a2dolist.util.RxBus;
import com.jacob.www.a2dolist.util.SharePreferenceManager;
import com.jacob.www.a2dolist.util.SortTimeList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    AppBarLayout appBarLayout;
//    @BindView(R.id.tv_time)
//    TextView tvTime;
    DaoSession daoSession = BaseApp.getDaosession();
    FriendVerifyDao verifyDao = daoSession.getFriendVerifyDao();
    TaskDao taskDao = daoSession.getTaskDao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        JMessageClient.registerEventReceiver(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        TextView nickName = view.findViewById(R.id.nick_name);
        CircleImageView userHead = view.findViewById(R.id.user_head);
        nickName.setText(SharePreferenceManager.getCachedNickname());
        userHead.setImageBitmap(BitmapFactory.decodeFile(SharePreferenceManager.getCachedAvatarPath()));
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setVisibility(View.GONE);
        deletePastTask();
        switch2TodoList();


        SharedPreferences sharedPreferences  = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        Log.e("TAG",sharedPreferences.getString("ceshi","没有"));

    }
    DateDao dateDao = BaseApp.getDaosession().getDateDao();
    private void deletePastTask(){
        List<Date> dates = dateDao.queryBuilder().listLazy();
        List<java.util.Date> realDates = new ArrayList<>();
        java.util.Date dateNow = new java.util.Date();
        int i=0;
        if(dates!=null&&dates.size()!=0){
            Log.e("TAG","这里有了");
            for(Date entry:dates){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(entry.getDate().split("-")[0]), Integer.valueOf(entry.getDate().split("-")[1]), Integer.valueOf(entry.getDate().split("-")[2]),
                        Integer.valueOf(entry.getDate().split("-")[3]), Integer.valueOf(entry.getDate().split("-")[4]), Integer.valueOf(entry.getDate().split("-")[5]));
                realDates.add(calendar.getTime());
                if(dateNow.getTime()>realDates.get(i).getTime()){
                    dateDao.deleteByKey(dates.get(i).getId());
                    Log.e("TAG","这里删了");
                }
                i++;
            }
        }

        List<Date> dates1 = dateDao.queryBuilder().listLazy();
        SortTimeList sort = new SortTimeList();
        if(dates1!=null&&dates1.size()!=0){
            realDates.clear();
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
            Intent inten1t = new Intent(this,AlarmService.class);
            inten1t.putExtra("time",time);
            startService(inten1t);
        }

        List<Task> tasks = taskDao.queryBuilder().listLazy();
        for(Task task:tasks){
            year = task.getStartTime().split("年")[0];
            month = task.getStartTime().split("年")[1].split("月")[0];
            day = task.getStartTime().split("年")[1].split("月")[1].split("日")[0];
            hour = task.getStartTime().split("年")[1].split(",")[1].split(":")[0];
            minute = task.getStartTime().split("年")[1].split(",")[1].split(":")[1];
            calendar.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(day),Integer.valueOf(hour),Integer.valueOf(minute),0);
            java.util.Date date = calendar.getTime();
            Log.e("TAG","已成功设置");
            Log.e("TAG",date.toString()+"    "+ new java.util.Date().toString());
            if(date.getTime()<new java.util.Date().getTime()){
                task.setIsChecked(true);
                Log.e("TAG","已成功设置");
                taskDao.update(task);
            }
        }
    }
    String year,month,day,hour,minute;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todo) {
            switch2TodoList();
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_conf) {
            switch2Friend();
        } else if (id == R.id.nav_setting) {
            JMessageClient.logout();
            Log.e("看这个数字是多少",BaseApp.registerOrLogin+"");
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switch2TodoList() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ToDoFragment(this)).commit();
//        tvTime.setText("我的事件");
    }
    private void switch2Friend(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new GroupFragment(this)).commit();
    }
    /**
     * 收到聊天消息
     */

    public void onEvent(MessageEvent event){
        Message msg = event.getMessage();
        if(msg.getTargetType()== ConversationType.group){
            String str[] = ((TextContent)msg.getContent()).getText().split("-");
            String title = str[0];String content = str[1];String startTime=str[2];String endTime = str[3];
            Boolean isTomato = Boolean.valueOf(str[4]);   Boolean isLeader = Boolean.valueOf(str[5]);
            Boolean isChecked = Boolean.valueOf(str[6]);
            Task task = new Task();
            task.setContent(content);task.setEndTime(endTime);task.setIsChecked(isChecked);
            task.setIsLeader(isLeader); task.setIsTomato(isTomato); task.setStartTime(startTime);
            task.setTitle(title);
            taskDao.insert(task);
            String year,month,day,hour,minute;
            Calendar calendar = Calendar.getInstance();
            year = startTime.split("年")[0];
            month = startTime.split("年")[1].split("月")[0];
            day = startTime.split("年")[1].split("月")[1].split("日")[0];
            hour = startTime.split("年")[1].split(",")[1].split(":")[0];
            minute = startTime.split("年")[1].split(",")[1].split(":")[1];
            calendar.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(day),Integer.valueOf(hour),Integer.valueOf(minute),0);
            Long time = calendar.getTime().getTime()-new java.util.Date().getTime();
            Intent inten1t = new Intent(this,AlarmService.class);
            inten1t.putExtra("time",time);
            startService(inten1t);

            Boolean b = true;
            RxBus.getInstance().post(b);
        }
    }
    /**
     * 离线事件
     */
    public void onEvent(OfflineMessageEvent event){
        List<Message> newMsg = event.getOfflineMessageList();
        for(int i=0;i<newMsg.size();i++){
            if(newMsg.get(i).getTargetType()==ConversationType.group){
                String str[] = ((TextContent)newMsg.get(i).getContent()).getText().split("-");
                String title = str[0];String content = str[1];String startTime=str[2];String endTime = str[3];
                Boolean isTomato = Boolean.valueOf(str[4]);   Boolean isLeader = Boolean.valueOf(str[5]);
                Boolean isChecked = Boolean.valueOf(str[6]);
                Task task = new Task();

                String year,month,day,hour,minute;
                Calendar calendar = Calendar.getInstance();
                year = startTime.split("年")[0];
                month = startTime.split("年")[1].split("月")[0];
                day = startTime.split("年")[1].split("月")[1].split("日")[0];
                hour = startTime.split("年")[1].split(",")[1].split(":")[0];
                minute = startTime.split("年")[1].split(",")[1].split(":")[1];
                calendar.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(day),Integer.valueOf(hour),Integer.valueOf(minute),0);
                java.util.Date date = calendar.getTime();
                Log.e("TAG","已成功设置");
                Log.e("TAG",date.toString()+"    "+ new java.util.Date().toString());
                if(date.getTime()<new java.util.Date().getTime()){
                    task.setContent(content);task.setEndTime(endTime);task.setIsChecked(isChecked);
                    task.setIsLeader(isLeader); task.setIsTomato(isTomato); task.setStartTime(startTime);
                    task.setTitle(title);
                    taskDao.insert(task);
                    Log.e("onEvent","RxBus");
                }else {
                    task.setContent(content);task.setEndTime(endTime);task.setIsChecked(isChecked);
                    task.setIsLeader(isLeader); task.setIsTomato(isTomato); task.setStartTime(startTime);
                    task.setTitle(title);
                    taskDao.insert(task);
                    Log.e("onEvent","设置闹钟事件");
                    Long time = date.getTime()-new java.util.Date().getTime();
                    Intent inten1t = new Intent(this,AlarmService.class);
                    inten1t.putExtra("time",time);
                    startService(inten1t);
                }
                switch2TodoList();
            }
        }
//        Boolean b = true;
//        RxBus.getInstance().post(b);
    }
    /**
     * 收到好友变更事件
     * @param event
     */
    public void onEvent(ContactNotifyEvent event) {
        Log.e("TAG","收到事件");
        switch (event.getType()) {
            case invite_received://收到好友邀请
                Log.e("TAG","收到好友邀请");
                String userName = event.getFromUsername();
                Log.e("TAG",userName);
                //如果是同一个发出的请求则不予处理
                List<FriendVerify> verifies = verifyDao.queryBuilder().listLazy();
                for(FriendVerify verify : verifies){
                    if(verify.getUserName().equals(userName)){
                        break;
                    }
                }
                final String reason = event.getReason();
                JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        if(i==0){
                            Log.e("TAG","成功了啊");
                            FriendVerify verify = new FriendVerify();
//                            verify.setAvatar(userInfo.getAvatarFile().getAbsolutePath());
                            verify.setHello(reason);
                            verify.setNickName(userInfo.getNickname());
                            verify.setUserName(userInfo.getUserName());
                            verifyDao.insert(verify);
                            List<FriendVerify> list = verifyDao.queryBuilder().listLazy();
                            Log.e("TAG",""+list.size());
                            RxBus.getInstance().post(list.size());
                        }
                        Log.e("TAG","失败了");

                    }
                });

                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }
}
