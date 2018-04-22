package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.DaoSession;
import com.jacob.www.a2dolist.FriendVerify;
import com.jacob.www.a2dolist.FriendVerifyDao;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.ui.activity.CreateGroupActivity;
import com.jacob.www.a2dolist.ui.activity.FriendVerifyActivity;
import com.jacob.www.a2dolist.ui.activity.GroupActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/8/30.
 */

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private static final int ITEM_VERIFY = 0;
    private static final int ITEM_GROUP = 1;
    private static final int ITEM = 2;
    DaoSession daoSession = BaseApp.getDaosession();
    FriendVerifyDao verifyDao = daoSession.getFriendVerifyDao();
    private List<Conversation> mDatas = new ArrayList<>();
    public FriendAdapter(Context context){
        mContext = context;
    }

    public void refreshData(){
        notifyDataSetChanged();
    }

    public void addData(List<Conversation> list){
        mDatas = list;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if(viewType ==ITEM_VERIFY){
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_verify,parent,false);
            return new VerifyHodler(rootView);
        }else if(viewType==ITEM_GROUP){
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_gp,parent,false);
            return new GroupHodler(rootView);
        }else {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_group,parent,false);
            return new FriendHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VerifyHodler){
            List<FriendVerify> verifies = verifyDao.queryBuilder().listLazy();
            if(verifies.size()>0){
                ((VerifyHodler) holder).verify_dot.setVisibility(View.VISIBLE);
                ((VerifyHodler) holder).verify_dot.setText(verifies.size()+"");
            }else {
                ((VerifyHodler) holder).verify_dot.setVisibility(View.INVISIBLE);
            }
            ((VerifyHodler) holder).verify_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, FriendVerifyActivity.class));
                }
            });
        }else if(holder instanceof FriendHolder){
            Conversation convItem = mDatas.get(position-2);
            if(convItem.getType().equals(ConversationType.single)){
                UserInfo feedBack = (UserInfo) convItem.getTargetInfo();
                if (feedBack.getUserName().equals("feedback_Android")) {
                    JMessageClient.deleteSingleConversation("feedback_Android", feedBack.getAppKey());
                    mDatas.remove(position-2);
                    notifyDataSetChanged();
                }
            }
            Message lastMsg = convItem.getLatestMessage();
            if(lastMsg != null){
                String content = ((TextContent)lastMsg.getContent()).getText();
                ((FriendHolder) holder).message.setText(content);
            }
            ((FriendHolder) holder).nickName.setText(convItem.getTitle());
            UserInfo userInfo = (UserInfo) convItem.getTargetInfo();
            if(userInfo!=null){
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if(i ==0){
                            ((FriendHolder) holder).group_avatar.setImageBitmap(bitmap);
                        }else {
                            ((FriendHolder) holder).group_avatar.setImageResource(R.drawable.t1);
                        }
                    }
                });
            }else {
                ((FriendHolder) holder).group_avatar.setImageResource(R.drawable.t1);
            }
            if(convItem.getUnReadMsgCnt()>0){
                ((FriendHolder) holder).convNum.setVisibility(View.VISIBLE);
                if (convItem.getUnReadMsgCnt() < 100) {
                    ((FriendHolder) holder).convNum.setText(String.valueOf(convItem.getUnReadMsgCnt()));
                } else {
                    ((FriendHolder) holder).convNum.setText("99+");
                }
            }
        }else if(holder instanceof GroupHodler){
            ((GroupHodler) holder).group_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, GroupActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return ITEM_VERIFY;
        }else if(position==1){
            return ITEM_GROUP;
        }else {
            return ITEM;
        }
    }

    class FriendHolder extends RecyclerView.ViewHolder{
        CircleImageView group_avatar ;
        TextView nickName,message,convNum;
        public FriendHolder(View itemView) {
            super(itemView);
            group_avatar = itemView.findViewById(R.id.avatar_friend);
            nickName = itemView.findViewById(R.id.name_friend);
            message = itemView.findViewById(R.id.text_history_last);
            convNum = itemView.findViewById(R.id.conver_num);
        }
    }
    class VerifyHodler extends RecyclerView.ViewHolder{
        LinearLayout verify_layout;
        TextView verify_dot;
        public VerifyHodler(View itemView) {
            super(itemView);
            verify_dot = itemView.findViewById(R.id.friend_verificati_num);
            verify_layout = itemView.findViewById(R.id.verify_ll);
        }
    }

    class GroupHodler extends RecyclerView.ViewHolder{
        LinearLayout group_layout;
        TextView group_dot;
        public GroupHodler(View itemView) {
            super(itemView);
            group_dot = itemView.findViewById(R.id.group_verification_num);
            group_layout = itemView.findViewById(R.id.group_ll);
        }
    }
}
