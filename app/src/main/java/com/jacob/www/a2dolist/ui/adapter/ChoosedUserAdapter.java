package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.model.data.ChatEntry;
import com.jacob.www.a2dolist.ui.activity.CreateGroupActivity;
import com.jacob.www.a2dolist.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/9/5.
 */

public class ChoosedUserAdapter extends RecyclerView.Adapter<ChoosedUserAdapter.ChooseViewHolder>{
    private CreateGroupActivity mContext;
    List<UserInfo> userInfos = new ArrayList<>();
    public ChoosedUserAdapter(CreateGroupActivity context){
        mContext =context;
    }

    public void addData(UserInfo userInfo){
        userInfos.add(userInfo);
        notifyDataSetChanged();
    }
    public void deleteData(UserInfo userInfo){
        userInfos.remove(userInfo);
        notifyDataSetChanged();
    }
    public void finishAdd(String groupName){
        JMessageClient.createGroup(groupName, "", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if(i==0){
                    List<String> strings = new ArrayList<String>();
                    for(UserInfo userInfo:userInfos){
                        strings.add(userInfo.getUserName());
                    }
                    JMessageClient.addGroupMembers(l, BaseApp.APPKEY, strings, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if(i==0){
                                Toast.makeText(mContext,"会议组创建成功",Toast.LENGTH_SHORT).show();
                                RxBus.getInstance().post(new ChatEntry(""));
                                mContext.onFinish();
                            }else {
                                Toast.makeText(mContext,"会议组创建失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(mContext,"会议组创建失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public ChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_avatar,parent,false);
        return new ChooseViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ChooseViewHolder holder, int position) {
        userInfos.get(position).getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0){
                    holder.avatar.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    class ChooseViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        public ChooseViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
