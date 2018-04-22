package com.jacob.www.a2dolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.model.data.ChatEntry;
import com.jacob.www.a2dolist.ui.adapter.ChatAdapter;
import com.jacob.www.a2dolist.util.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ASUS-NB on 2017/9/6.
 */

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.chat_recycler_view)
    RecyclerView chatRecyclerView;
    ChatAdapter mAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Long groupId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        groupId = getIntent().getLongExtra("groupId",0);
    }
    Subscription subscription;
    private void initData(){
        Log.e("ChatActivity","initData");
         subscription=RxBus.getInstance().toObserverable(ChatEntry.class).subscribe(new Action1<ChatEntry>() {
            @Override
            public void call(ChatEntry entry) {
                Log.e("ChatActivity","call");
                Message message = JMessageClient.createGroupTextMessage(groupId,entry.getText());
                JMessageClient.sendMessage(message);
                conversation = JMessageClient.getGroupConversation(groupId);
                mAdapter = new ChatAdapter(ChatActivity.this,conversation);
                chatRecyclerView.setAdapter(mAdapter);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
            }
        });

        if(groupId==0){
            Log.e("ChatActivity","groupId==0");
        }else {
            Log.e("ChatActivity","groupId!=0");
            conversation = JMessageClient.getGroupConversation(groupId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    conversation = JMessageClient.getGroupConversation(groupId);
                    mAdapter = new ChatAdapter(ChatActivity.this,conversation);
                    chatRecyclerView.setAdapter(mAdapter);
                    chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                }
            },200);
        }
    }
//    CompositeSubscription compositeSubscription;
//    private void addRxBusEvent(Subscription subscription){
//        if(compositeSubscription ==null){
//            compositeSubscription = new CompositeSubscription();
//        }
//        compositeSubscription.add(subscription);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ChatActivity","onDestroy");
        subscription.unsubscribe();
    }
    Conversation conversation;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Intent intent = new Intent();
        intent.setClass(this,AddListActivity.class);
        intent.putExtra("type","chat");
        intent.putExtra("groupId",groupId);
        startActivity(intent);
    }
}
