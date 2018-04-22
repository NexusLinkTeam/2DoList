package com.jacob.www.a2dolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.model.data.ChatEntry;
import com.jacob.www.a2dolist.ui.adapter.ChatAdapter;
import com.jacob.www.a2dolist.ui.adapter.GroupAdapter;
import com.jacob.www.a2dolist.util.RxBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.model.Message;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ASUS-NB on 2017/9/5.
 */

public class GroupActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.group_recycler_view)
    RecyclerView groupRecyclerView;
    GroupAdapter mGroupListAdapter;
    Subscription subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        subscription= RxBus.getInstance().toObserverable(ChatEntry.class).subscribe(new Action1<ChatEntry>() {
            @Override
            public void call(ChatEntry entry) {
                initData();
            }
        });
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initData(){
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<Long> groupIDList) {
                if (responseCode == 0) {
                    mGroupListAdapter = new GroupAdapter(GroupActivity.this, groupIDList);
                    groupRecyclerView.setAdapter(mGroupListAdapter);
                    groupRecyclerView.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }else if(id==R.id.search_friend){
            startActivity(new Intent(this,CreateGroupActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
