package com.jacob.www.a2dolist.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jacob.www.a2dolist.FriendVerify;
import com.jacob.www.a2dolist.FriendVerifyDao;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.presenter.ContactPresenter;
import com.jacob.www.a2dolist.mvp.presenter.Impl.ContactPresenterImpl;
import com.jacob.www.a2dolist.mvp.view.ContactView;
import com.jacob.www.a2dolist.ui.activity.AddFriendActivity;
import com.jacob.www.a2dolist.ui.activity.MainActivity;
import com.jacob.www.a2dolist.ui.adapter.FriendAdapter;
import com.jacob.www.a2dolist.util.CircleImageView;
import com.jacob.www.a2dolist.util.RxBus;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.model.Conversation;
import rx.functions.Action1;

/**
 * Created by ASUS-NB on 2017/9/3.
 */
@SuppressLint("ValidFragment")
public class GroupFragment extends Fragment implements ContactView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.group_recycler_view)
    RecyclerView groupRecyclerView;
    @BindView(R.id.friend_verification_num)
    TextView friendVerificationNum;
    private MainActivity mActivity;
    private FriendAdapter mAdapter;
    private ContactPresenter presenter = new ContactPresenterImpl();
    private DaoSession daoSession = BaseApp.getDaosession();
    FriendVerifyDao verifyDao = daoSession.getFriendVerifyDao();

    public GroupFragment(MainActivity activity) {
        mActivity = activity;
    }
    public GroupFragment() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG1","onCreate");
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("TAG1","onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG1","onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG1","onActivityCreated");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_friend, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        Log.e("TAG1","onCreateView");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG1","onResume");
        getFriend();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG1","onStart");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG1","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG1","onStop");
    }

    private void initView() {
        mActivity.setSupportActionBar(toolbar);
        presenter.injectView(this);
        toolbar.inflateMenu(R.menu.group_menu);
        Log.e("GroupFragment", "initView");
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
        inflater.inflate(R.menu.group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_friend:
                startActivity(new Intent(mActivity, AddFriendActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    TextView verifyDot;

    private void initData() {
        mAdapter = new FriendAdapter(mActivity);
        groupRecyclerView.setAdapter(mAdapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        RxBus.getInstance().toObserverable(Integer.class).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == 0) {
                    verifyDot.setVisibility(View.INVISIBLE);
                    friendVerificationNum.setVisibility(View.INVISIBLE);
                } else {
                    verifyDot.setVisibility(View.VISIBLE);
                    friendVerificationNum.setVisibility(View.VISIBLE);
                }
            }
        });
        RxBus.getInstance().toObserverable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mAdapter.refreshData();
                List<FriendVerify> list = verifyDao.queryBuilder().listLazy();
                if (list.size() == 0) {
                    verifyDot.setVisibility(View.INVISIBLE);
                    friendVerificationNum.setVisibility(View.INVISIBLE);
                } else {
                    verifyDot.setVisibility(View.VISIBLE);
                    friendVerificationNum.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void getFriend() {
        presenter.getFriend(mActivity);
    }

    @Override
    public void showFriend(List<Conversation> list) {
        mAdapter.addData(list);
    }
}
