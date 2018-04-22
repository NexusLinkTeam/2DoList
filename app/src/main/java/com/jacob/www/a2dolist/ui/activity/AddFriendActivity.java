package com.jacob.www.a2dolist.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacob.www.a2dolist.R;
import com.wevey.selector.dialog.MDEditDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/8/27.
 */

public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.et_searchUser)
    EditText etSearchUser;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.search_name)
    TextView mSearchName;
    @BindView(R.id.search_result)
    LinearLayout searchResult;
    ProgressDialog dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_addBtn)
    TextView searchAddBtn;
    @BindView(R.id.avatar_friend)
    CircleImageView avatarFriend;
    UserInfo myInfo,friendInfo;
    MDEditDialog dialog6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSearch.setEnabled(false);
        etSearchUser.addTextChangedListener(new TextChange());
        dialog = new ProgressDialog(this);
    }

    @OnClick({R.id.iv_clear, R.id.btn_search, R.id.search_addBtn, R.id.search_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etSearchUser.setText("");
                break;
            case R.id.btn_search:
                hideKb();
                String searchName = etSearchUser.getText().toString().trim();
                if (!TextUtils.isEmpty(searchName)) {
                    dialog.show();
                    JMessageClient.getUserInfo(searchName, new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                dialog.dismiss();
                                Log.e("TAG","gotResult");
                                searchResult.setVisibility(View.VISIBLE);
                                friendInfo = userInfo;
                                myInfo= JMessageClient.getMyInfo();
                                if(userInfo.isFriend()||userInfo.getUserName().equals(myInfo.getUserName())){
                                }else {
                                    searchAddBtn.setVisibility(View.VISIBLE);
                                }
                                avatarFriend.setImageResource(R.drawable.t1);
                                Log.e("TAG",userInfo.getUserName());
                                mSearchName.setText(userInfo.getNickname());
                            } else {
                                Toast.makeText(AddFriendActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.search_addBtn:
                dialog6 = new MDEditDialog.Builder(AddFriendActivity.this)
                        .setTitleVisible(true)
                        .setInputTpye(InputType.TYPE_CLASS_TEXT)
                        .setTitleText("打个招呼吧")
                        .setTitleTextSize(20)
                        .setTitleTextColor(R.color.black_light)
                        .setContentText("嗨，我是"+myInfo.getNickname())
                        .setContentTextSize(18)
                        .setMaxLength(7)
                        .setMaxLines(1)
                        .setContentTextColor(R.color.colorPrimary)
                        .setButtonTextSize(14)
                        .setLeftButtonTextColor(R.color.colorPrimary)
                        .setLeftButtonText("取消")
                        .setRightButtonTextColor(R.color.colorPrimary)
                        .setRightButtonText("发送")
                        .setLineColor(R.color.colorPrimary)
                        .setOnclickListener(new MDEditDialog.OnClickEditDialogListener() {
                            @Override
                            public void clickLeftButton(View view, String text) {
                                dialog6.dismiss();
                            }
                            @Override
                            public void clickRightButton(View view, String text) {
                                ContactManager.sendInvitationRequest(friendInfo.getUserName(), "c91b0418d8554d5bc9b32f74", text, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if(i==0){
                                            Toast.makeText(AddFriendActivity.this,"好友申请已发送，请等待对方确认",Toast.LENGTH_SHORT).show();
                                            dialog6.dismiss();
                                        }else {
                                            Toast.makeText(AddFriendActivity.this,"好友申请发送失败",Toast.LENGTH_SHORT).show();
                                            dialog6.dismiss();
                                        }
                                    }
                                });
                            }
                        })
                        .setMinHeight(0.3f)
                        .setWidth(0.8f)
                        .build();
                dialog6.show();
                break;
            case R.id.search_result:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


    public class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int end, int count) {
            if (etSearchUser.getText().length() > 0) {
                ivClear.setVisibility(View.VISIBLE);
                btnSearch.setEnabled(true);
            } else {
                ivClear.setVisibility(View.INVISIBLE);
                btnSearch.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKb() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //获取当前焦点的view
        if (manager.isActive() && getCurrentFocus() != null) {
            //焦点view所依附的window的令牌(token)
            if (getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
