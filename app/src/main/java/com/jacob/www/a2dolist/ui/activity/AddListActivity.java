package com.jacob.www.a2dolist.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.DaoSession;
import com.jacob.www.a2dolist.DateDao;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.Task;
import com.jacob.www.a2dolist.TaskDao;
import com.jacob.www.a2dolist.mvp.model.data.ChatEntry;
import com.jacob.www.a2dolist.service.AlarmService;
import com.jacob.www.a2dolist.util.RxBus;
import com.jacob.www.a2dolist.util.SortTimeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS-NB on 2017/8/25.
 */

public class AddListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_list_title)
    EditText tvListTitle;
    @BindView(R.id.tv_list_content)
    EditText tvListContent;
    @BindView(R.id.is_tomato_switch)
    Switch isTomatoSwitch;
    @BindView(R.id.start_day)
    TextView startDay;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_day)
    TextView endDay;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.end_layout)
    RelativeLayout endLayout;
    @BindView(R.id.tv_time)
    TextView tvTitle;
    String type;
    Long groupId;
    private Calendar calendar= Calendar.getInstance();
    private String dataArray[] = {"周一","周二","周三","周四","周五","周六","周日"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("添加事件");
        type = getIntent().getStringExtra("type");
        groupId = getIntent().getLongExtra("groupId",0);
        isTomatoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    endLayout.setVisibility(View.VISIBLE);
                } else {
                    endLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    DaoSession daoSession = BaseApp.getDaosession();
    TaskDao taskDao  =daoSession.getTaskDao();
    DateDao dateDao = daoSession.getDateDao();
    private void judgeTime(Intent serviceIntent,Long time){
        com.jacob.www.a2dolist.Date myDate = new com.jacob.www.a2dolist.Date();
        myDate.setDate(year+"-"+(month-1)+"-"+day+"-"+hour+"-"+minute+"-"+"0");
        dateDao.insert(myDate);
        List<com.jacob.www.a2dolist.Date> dates = dateDao.queryBuilder().listLazy();
        List<Date> realDates = new ArrayList<>();
        if(dates!=null&&dates.size()!=0){
            SortTimeList sort = new SortTimeList();
            for(com.jacob.www.a2dolist.Date entry:dates){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(entry.getDate().split("-")[0]),Integer.valueOf(entry.getDate().split("-")[1]),Integer.valueOf(entry.getDate().split("-")[2]),
                        Integer.valueOf(entry.getDate().split("-")[3]),Integer.valueOf(entry.getDate().split("-")[4]),Integer.valueOf(entry.getDate().split("-")[5]));
                realDates.add(calendar.getTime());
            }
            Collections.sort(realDates,sort);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.e("TAG",format.format(nowDate));
            Log.e("TAG",format.format(realDates.get(0)));
            if(format.format(nowDate).equals(format.format(realDates.get(0)))){
                Log.e(this.getPackageName(),"现在设置的闹钟是第一个呢");
                serviceIntent.putExtra("time",time);
                startService(serviceIntent);
            }
        }
    }
    Date nowDate;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.confirm) {
            if(hour==0||day ==0){
                Toast.makeText(this,"您还没有设置时间",Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent serviceIntent =  new Intent(this, AlarmService.class);
            mTime.set(year,month-1,day,hour,minute,0);
            nowDate = mTime.getTime();
            Log.e(this.getPackageName(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
            Date dateNow = new Date();
            Long time = nowDate.getTime()-dateNow.getTime();
            if(time<0){
                Toast.makeText(this,"时间设置不合理",Toast.LENGTH_SHORT).show();
                return true;
            }
            judgeTime(serviceIntent,time);
            if(TextUtils.isEmpty(tvListTitle.getText().toString().trim())){
                Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(tvListContent.getText().toString().trim())){
                Toast.makeText(this,"内容不能为空",Toast.LENGTH_SHORT).show();
            }else {
                Task entry;
                if(isTomatoSwitch.isChecked()){
                    entry = new Task();
                    entry.setIsChecked(false);entry.setStartTime(startDay.getText().toString().trim()+","+startTime
                            .getText().toString().trim());entry.setIsTomato(true);entry.setIsLeader(false);entry.setEndTime(endDay.getText().toString().trim()+","+endTime.getText().toString().trim());
                    entry.setContent(tvListContent.getText().toString());entry.setTitle(tvListTitle.getText().toString());
                }else {
                    entry = new Task();
                    entry.setIsChecked(false);entry.setStartTime(startDay.getText().toString().trim()+","+startTime
                            .getText().toString().trim());entry.setIsTomato(false);entry.setIsLeader(false);entry.setEndTime(endDay.getText().toString().trim()+","+endTime.getText().toString().trim());
                    entry.setContent(tvListContent.getText().toString());entry.setTitle(tvListTitle.getText().toString());
                }
                taskDao.insert(entry);
                if(type!=null&&type.equals("chat")){
                    StringBuilder builder = new StringBuilder();
                    builder.append(entry.getTitle()+"-").append(entry.getContent()+"-").append(entry.getStartTime()+"-")
                            .append(entry.getEndTime()+"-").append(entry.getIsTomato()+"-").append("true-").append(entry.getIsChecked());
                    ChatEntry entry1 = new ChatEntry(builder.toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("groupId",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(groupId+"",entry.getTitle());
                    editor.apply();
                    RxBus.getInstance().post(entry1);
                    Log.e("addList",entry.getIsLeader()+":"+entry.getIsChecked());
                    RxBus.getInstance().post(entry);
                }else {
                    RxBus.getInstance().post(entry);
                }

            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_list, menu);
        return true;
    }
    int year=0,month=0,day=0,hour=0,minute=0;
    Calendar mTime = Calendar.getInstance();
    @OnClick({R.id.start_day, R.id.start_time, R.id.end_day, R.id.end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_day:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        int temp = calendar.get(Calendar.DAY_OF_WEEK)-2;
                        if(temp<0){
                            temp+=7;
                        }
                        String text = i+"年"+(i1+1)+"月"+i2+"日"+dataArray[temp];
                        year = i;
                        month = i1+1;
                        day = i2;
                        Log.e(this.getClass().getName(),text);
                        startDay.setText(text);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.start_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String text = i+":"+i1;

                        hour = i;
                        if(i1<10){
                            text =i+":"+"0"+i1;
                        }
                        minute = i1;
                        startTime.setText(text);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
                break;
            case R.id.end_day:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        int temp = calendar.get(Calendar.DAY_OF_WEEK)-2;
                        if(temp<0){
                            temp+=7;
                        }
                        String text = i+"年"+i1+"月"+i2+"日"+dataArray[temp];
                        endDay.setText(text);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.end_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String text = i+":"+i1;
                        endTime.setText(text);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
                break;
        }
    }
}
