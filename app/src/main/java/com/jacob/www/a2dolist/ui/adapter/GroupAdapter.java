package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.ui.activity.ChatActivity;
import com.jacob.www.a2dolist.util.SharePreferenceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/9/5.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private Context mContext;
    private List<Long> mGroupList;
    private Map<Long, String> mGroupName = new HashMap<>();
    private String groupName;
    public GroupAdapter(Context context,List<Long> list){
        mGroupList = list;
        mContext = context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_real_group,parent,false);
        return new GroupViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        final Long groupId = mGroupList.get(position);
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                if(i==0){
                    if (TextUtils.isEmpty(groupInfo.getGroupName())) {
                        List<UserInfo> groupMembers = groupInfo.getGroupMembers();
                        StringBuilder builder = new StringBuilder();
                        if (groupMembers.size() <= 5) {
                            groupName = getGroupName(groupMembers, builder);
                        } else {
                            List<UserInfo> newGroupMember = groupMembers.subList(0, 5);
                            groupName = getGroupName(newGroupMember, builder);
                        }
                    } else {
                        groupName = groupInfo.getGroupName();
                    }
                    mGroupName.put(groupId, groupName);
                    holder.groupName.setText(groupName);
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("groupId",Context.MODE_PRIVATE);
                    String task = sharedPreferences.getString(groupId+"","");
                    if(task.equals(""))
                    holder.lastMsg.setText("最近任务：暂无");
                    else
                        holder.lastMsg.setText("最近任务："+task);
//                    holder.groupAvatar.setImageResource(R.drawable.group);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }
    private String getGroupName(List<UserInfo> groupMembers, StringBuilder builder) {
        for (UserInfo info : groupMembers) {
            String noteName = info.getNotename();
            if (TextUtils.isEmpty(noteName)) {
                noteName = info.getNickname();
                if (TextUtils.isEmpty(noteName)) {
                    noteName = info.getUserName();
                }
            }
            builder.append(noteName);
            builder.append(",");
        }

        return builder.substring(0, builder.lastIndexOf(","));
    }
    class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView groupName,convNum,lastMsg;
        CircleImageView groupAvatar;
        LinearLayout linearLayout;
        public GroupViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.container);
            groupAvatar = itemView.findViewById(R.id.avatar_group);
            groupName = itemView.findViewById(R.id.name_group);
            convNum = itemView.findViewById(R.id.conver_num);
            lastMsg = itemView.findViewById(R.id.text_history_last);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext,ChatActivity.class);
                    Log.e("onClick","ChatActivity"+getPosition()+":"+mGroupList.get(getPosition()));
                    intent.putExtra("groupId",mGroupList.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
