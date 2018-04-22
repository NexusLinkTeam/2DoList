package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacob.www.a2dolist.FriendVerify;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.ui.activity.FriendVerifyActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS-NB on 2017/8/28.
 */

public class FriendVerifyAdapter extends RecyclerView.Adapter<FriendVerifyAdapter.VerifyViewHolder>  {

    List<FriendVerify> list = new ArrayList<>();
    private Context mContext;

    public FriendVerifyAdapter(FriendVerifyActivity context) {
        mContext = context;
        listener = context;
    }

    @Override
    public VerifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_verify,parent,false);
        VerifyViewHolder holder = new VerifyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(VerifyViewHolder holder, int position) {

        holder.hellotext.setText(list.get(position).getHello());
        holder.name.setText(list.get(position).getNickName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addData(FriendVerify entry){
        list.add(entry);
        notifyDataSetChanged();
    }
    public void deleteData(FriendVerify verify){
        int num;
        for(int i=0;i<list.size();i++){
            if(verify.getUserName().equals(list.get(i).getUserName())){
                num =i ;
                list.remove(num);
                notifyItemRemoved(num);
                break;
            }
        }
        if(list.size()==0){
            listener.onFinish();
        }

    }

    public interface OnItemClickListener{
        void onItemAgree(String uId);
        void onItemRefuse(String uId);
        void onFinish();
    }
    OnItemClickListener listener;
    class VerifyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatarImage;
        TextView name,hellotext;
        TextView btnAgree,btnRefuse;
        public VerifyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            hellotext = itemView.findViewById(R.id.tv_hello);
            btnAgree = itemView.findViewById(R.id.btn_agree);
            btnRefuse = itemView.findViewById(R.id.btn_refuse);
            btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemRefuse(list.get(getPosition()).getUserName());
                }
            });
            btnAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemAgree(list.get(getPosition()).getUserName());
                }
            });
        }
    }
}
