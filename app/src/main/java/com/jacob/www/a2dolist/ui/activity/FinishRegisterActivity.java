package com.jacob.www.a2dolist.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.MainActivity;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.model.data.EventEntry;
import com.jacob.www.a2dolist.util.ClearWriteEditText;
import com.jacob.www.a2dolist.util.RxBus;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import rx.functions.Action1;

/**
 * Created by ASUS-NB on 2017/8/16.
 */

public class FinishRegisterActivity extends BaseActivity {
    @InjectView(R.id.nick_name_et)
    ClearWriteEditText nickNameEt;
    @InjectView(R.id.tv_nickCount)
    TextView tvNickCount;
    @InjectView(R.id.finish_btn)
    Button finishBtn;
    @InjectView(R.id.upload_image)
    ImageView uploadImage;
    String photoPath ;
    String uid;
    String password;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        ButterKnife.inject(this);
        uid = getIntent().getStringExtra("uid");
        password = getIntent().getStringExtra("password");
        RxBus.getInstance().toObserverable(EventEntry.class).subscribe(new Action1<EventEntry>() {
            @Override
            public void call(EventEntry eventEntry) {
                photoPath = eventEntry.photos.get(0).getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                uploadImage.setImageBitmap(bitmap);
            }
        });
        nickNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(nickNameEt.getText().length()== 0||photoPath==null){
                    finishBtn.setEnabled(false);
                }else {
                    finishBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num = 30 - editable.length();
                tvNickCount.setText(String.valueOf(num));
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.upload_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.upload_image:
                startActivity(new Intent(this, PhotoChooseActivity.class));
                break;
        }
    }

    @OnClick(R.id.finish_btn)
    public void onClick() {
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.show();
        if(isNetWork())
        JMessageClient.register(uid, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i ==0){
                    JMessageClient.login(uid, password, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if(i ==0){
                                UserInfo myUserInfo = JMessageClient.getMyInfo();
                                if(myUserInfo!=null){
                                    myUserInfo.setNickname(nickNameEt.getText().toString().trim());
                                }
                                JMessageClient.updateMyInfo(UserInfo.Field.nickname, myUserInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if(i==0){
                                        }
                                    }
                                });
                                SharePreferenceManager.setCachedNickname(nickNameEt.getText().toString().trim());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JMessageClient.updateUserAvatar(new File(photoPath), new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                if(i==0){
                                                    SharePreferenceManager.setCachedAvatarPath(photoPath);
                                                    startActivity(new Intent(FinishRegisterActivity.this,MainActivity.class));
                                                    finish();
                                                    BaseApp.finishActivity(LogInActivity.class);
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }
                    });
                }
            }
        });
        else
            Toast.makeText(this,"请检查你的网络设置",Toast.LENGTH_SHORT).show();
    }

}
