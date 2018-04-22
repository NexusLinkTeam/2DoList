package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacob.www.a2dolist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/9/6.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    List<Message> msgList = new ArrayList<>();
    public ChatAdapter(Context context, Conversation conv){
        mContext =context;
        if(conv!=null)
        msgList = conv.getAllMessage();
    }
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_chat,parent,false);
        return new ChatViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        UserInfo userInfo = msgList.get(position).getFromUser();
        String str[] = ((TextContent)(msgList.get(position).getContent())).getText().split("-");
        String title = str[0];
        String content = str[1];
        String time = str[2];
        holder.chatContent.setText(time+","+content);
        holder.chatTitle.setText(title);
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0){
                    holder.chatAvatar.setImageBitmap(bitmap);
                }
            }
        });
        Date date = new Date(msgList.get(position).getCreateTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.chatTime.setText(dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView chatTime,chatTitle,chatContent;
        CircleImageView chatAvatar;
        public ChatViewHolder(View itemView) {
            super(itemView);
            chatAvatar = itemView.findViewById(R.id.chat_avatar);
            chatContent = itemView.findViewById(R.id.chat_task_content);
            chatTime = itemView.findViewById(R.id.chat_time);
            chatTitle = itemView.findViewById(R.id.chat_task_title);
        }
    }
}
