package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jacob.www.a2dolist.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/9/5.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context mContext;
    List<Conversation> list = new ArrayList<>();
    ChoosedUserAdapter adapter;
    public UserAdapter(Context context,List<Conversation> list,ChoosedUserAdapter adapter){
        mContext = context;
        this.list = list;
        this.adapter = adapter;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        UserInfo userInfo = (UserInfo) list.get(position).getTargetInfo();
        holder.nickName.setText(list.get(position).getTitle());
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0){
                    holder.userAvatar.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userAvatar;
        CheckBox checkBox;
        TextView nickName;
        public UserViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            checkBox = itemView.findViewById(R.id.checkbox);
            nickName = itemView.findViewById(R.id.nick_name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        adapter.addData((UserInfo)list.get(getPosition()).getTargetInfo());
                    }else {
                        adapter.deleteData((UserInfo)list.get(getPosition()).getTargetInfo());
                    }
                }
            });
        }
    }
}
