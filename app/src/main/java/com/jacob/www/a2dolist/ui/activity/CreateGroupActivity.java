package com.jacob.www.a2dolist.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.presenter.ContactPresenter;
import com.jacob.www.a2dolist.mvp.presenter.Impl.ContactPresenterImpl;
import com.jacob.www.a2dolist.mvp.view.ContactView;
import com.jacob.www.a2dolist.ui.adapter.ChoosedUserAdapter;
import com.jacob.www.a2dolist.ui.adapter.UserAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ASUS-NB on 2017/9/5.
 */

public class CreateGroupActivity extends AppCompatActivity implements ContactView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.user_recycler_view)
    RecyclerView userRecyclerView;
    @BindView(R.id.add_user)
    TextView addUser;
    UserAdapter mUserAdapter;
    ChoosedUserAdapter mChooseAdapter;
    ContactPresenter presenter;
    @BindView(R.id.group_name)
    TextInputEditText groupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter=new ContactPresenterImpl();
        presenter.injectView(this);
    }

    private void initData() {
        getFriend();
        mChooseAdapter = new ChoosedUserAdapter(this);
        recyclerView.setAdapter(mChooseAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
    }

    @OnClick(R.id.add_user)
    public void onClick() {
        if(TextUtils.isEmpty(groupName.getText().toString())){
            Toast.makeText(this,"会议组名称不能为空",Toast.LENGTH_SHORT).show();
        }else{
            mChooseAdapter.finishAdd(groupName.getText().toString());
        }
    }

    public void onFinish(){
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        presenter.getFriend(this);
    }

    @Override
    public void showFriend(List<Conversation> list) {
        mUserAdapter = new UserAdapter(this, list, mChooseAdapter);
        userRecyclerView.setAdapter(mUserAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
