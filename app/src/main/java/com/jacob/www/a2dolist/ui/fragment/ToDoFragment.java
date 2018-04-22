package com.jacob.www.a2dolist.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.DaoSession;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.Task;
import com.jacob.www.a2dolist.TaskDao;
import com.jacob.www.a2dolist.ui.activity.AddListActivity;
import com.jacob.www.a2dolist.ui.activity.MainActivity;
import com.jacob.www.a2dolist.ui.adapter.ToDoAdapter;
import com.jacob.www.a2dolist.util.CircleImageView;
import com.jacob.www.a2dolist.util.RxBus;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ASUS-NB on 2017/8/24.
 */
@SuppressLint("ValidFragment")
public class ToDoFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.friend_verification_num)
    TextView friendVerificationNum;
    private ToDoAdapter mAdapter;
    private MainActivity mActivity;

    public ToDoFragment(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("TAG2","onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("TAG2","onCreate");
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todolist, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        Log.e("TAG2","onCreateView");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG2","onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG2","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG2","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG2","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG2","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG2","onStop");
    }
    TextView verifyDot;
    DaoSession daoSession = BaseApp.getDaosession();
    TaskDao taskDao = daoSession.getTaskDao();
    Subscription subscription1,subscription2;

    private void initData() {
        List<Task> tasks = taskDao.queryBuilder().listLazy();
        List<Task> isCheckedTask  = new ArrayList<>();
        List<Task> unCheckedTask = new ArrayList<>();
        mAdapter = new ToDoAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int num=0;
        for (int i = 0; i < tasks.size(); i++) {
            if(!tasks.get(i).getIsChecked()){
                num++;
            }
        }
        List<Task> list = new ArrayList<>();
        for(int i=0;i<tasks.size();i++){
            if(tasks.get(i).getIsChecked()){
                isCheckedTask.add(tasks.get(i));
            }else {
                unCheckedTask.add(tasks.get(i));
            }
        }
        for(int i=0;i<num;i++){
            list.add(unCheckedTask.get(i));
        }
        list.add(new Task());
        for(int i=0;i<isCheckedTask.size();i++){
            list.add(isCheckedTask.get(i));
        }
        mAdapter.addData(list);
        Log.e("ToDoFragment",num+"");
        mAdapter.setDivisionPos(num);
        subscription1 =RxBus.getInstance().toObserverable(Task.class).subscribe(new Action1<Task>() {
            @Override
            public void call(Task entry) {
                Log.e("ToDoFragment","call");
                mAdapter.insertData(entry);
            }
        });
        subscription2=RxBus.getInstance().toObserverable(Integer.class).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer > 0) {
                    verifyDot.setVisibility(View.VISIBLE);
                    friendVerificationNum.setVisibility(View.VISIBLE);
                } else {
                    verifyDot.setVisibility(View.INVISIBLE);
                    friendVerificationNum.setVisibility(View.INVISIBLE);
                }
            }
        });
         subscription = RxBus.getInstance().toObserverable(Boolean.class).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    Subscription subscription;
    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        subscription1.unsubscribe();
        subscription2.unsubscribe();
    }

    private void initView() {
        mActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main);
        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        TextView nickName = view.findViewById(R.id.nick_name);
        CircleImageView userHead = view.findViewById(R.id.user_head);
        nickName.setText(SharePreferenceManager.getCachedNickname());
        userHead.setImageBitmap(BitmapFactory.decodeFile(SharePreferenceManager.getCachedAvatarPath()));
        navigationView.setNavigationItemSelectedListener(mActivity);
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_conf);
        verifyDot = item.getActionView().findViewById(R.id.verify_dot);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.fab)
    public void onClick() {
//        mAdapter.insertData(new ToDoEntry(false,false,false,"我是新来的","啦啦啦"));
        startActivity(new Intent(getContext(), AddListActivity.class));
    }
}