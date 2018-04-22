package com.jacob.www.a2dolist.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacob.www.a2dolist.BaseApp;
import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.Task;
import com.jacob.www.a2dolist.TaskDao;
import com.jacob.www.a2dolist.mvp.model.data.IsCheckedEntry;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ASUS-NB on 2017/8/25.
 */

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    Observable<IsCheckedEntry> observable;
    private List<Task> toDoEntries= new ArrayList<>();
    private int[] array = new int[50];
    boolean isClick  = false;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_DIVISION = 2;
    private int divisionPos=0;
    TaskDao taskDao = BaseApp.getDaosession().getTaskDao();
    Subscriber subscriber= new Subscriber() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Object o) {
            IsCheckedEntry entry = (IsCheckedEntry)o;
            if(entry.isChecked()){
                notifyItemMoved(entry.getPos(),divisionPos);
                int temp = array[entry.getPos()];
                toDoEntries.get(array[entry.getPos()]).setIsChecked(true);
                Task task = taskDao.queryBuilder().where(TaskDao.Properties.Title.eq(toDoEntries.get(array[entry.getPos()]).getTitle())).unique();
                task.setIsChecked(true);
                taskDao.update(task);
                for(int i = entry.getPos(); i<divisionPos; i++){
                    array[i]=array[i+1];
                }
                array[divisionPos] = temp;
                if(divisionPos!=0)
                    divisionPos--;
                for(int i=0;i<toDoEntries.size()+1;i++){
                    Log.e("ToDoAdapter",array[i]+"");
                }
            }else {
                Log.e("notifyItemMoved",divisionPos+"");
                notifyItemMoved(entry.getPos(),divisionPos);
                int temp = array[entry.getPos()];
                toDoEntries.get(array[entry.getPos()]).setIsChecked(false);
                Task task = taskDao.queryBuilder().where(TaskDao.Properties.Title.eq(toDoEntries.get(array[entry.getPos()]).getTitle())).unique();
                task.setIsChecked(false);
                taskDao.update(task);
                for(int i = entry.getPos(); i>divisionPos; i--){
                    array[i]=array[i-1];
                }
                array[divisionPos] = temp;
                for(int i=0;i<toDoEntries.size()+1;i++){
                    Log.e("ToDoAdapter",array[i]+"");
                }
                    divisionPos++;
            }
        }
    };
    public void addData(List<Task> entries){
        int i=0;
        for(Task entry:entries){
            toDoEntries.add(entry);
            array[i] = i;
            i++;
        }
        array[i] = i;
        notifyDataSetChanged();
    }
    public void insertData(Task entry){
        divisionPos++;
//        Log.e("TAG",toDoEntries.size()+"00'");
        toDoEntries.add(0,entry);
        for(int i=toDoEntries.size()-1;i>=0;i--){
            array[i+1]= array[i];
        }
        for(int i=1;i<toDoEntries.size();i++){
            array[i]++;
        }
        array[0] = 0;
        for(int i=0;i<toDoEntries.size();i++){
//            Log.e("TAG",array[i]+"");
        }
        Log.e("TAG","notifyItemInserted");
        notifyItemInserted(0);
        notifyDataSetChanged();
    }
    public void setDivisionPos(int pos){
        divisionPos = pos;
    }
    public ToDoAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==divisionPos){
            return VIEW_TYPE_DIVISION;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        RecyclerView.ViewHolder holder;
        if(viewType==VIEW_TYPE_NORMAL)
        {
             rootView = LayoutInflater.from(mContext).inflate(R.layout.item_todolist,parent,false);
            holder = new ToDoViewHolder(rootView);
            return holder;
        }
        else if(viewType==VIEW_TYPE_DIVISION){
             rootView = LayoutInflater.from(mContext).inflate(R.layout.item_todolist_division,parent,false);
             holder = new ToDoViewHolderDivision(rootView);
            Log.e("TAG","VIEW_TYPE_DIVISION");
             return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.e("onBindViewHolder",position+"");
        if(holder instanceof ToDoViewHolder){
            Log.e("onBindViewHolder",array[position]+"");
            ((ToDoViewHolder)holder).cb_list.setText(toDoEntries.get(array[position]).getTitle());
            ((ToDoViewHolder)holder).tv_time.setText(toDoEntries.get(array[position]).getStartTime());
            Boolean b = toDoEntries.get(array[position]).getIsChecked();
            Log.e("TAG",String.valueOf(b));
            ((ToDoViewHolder)holder).cb_list.setChecked(toDoEntries.get(array[position]).getIsChecked());
            if(toDoEntries.get(array[position]).getIsTomato()){
                ((ToDoViewHolder)holder).img_isTomato.setVisibility(View.VISIBLE);
            }else {
                ((ToDoViewHolder)holder).img_isTomato.setVisibility(View.GONE);
            }
            if(toDoEntries.get(array[position]).getIsLeader()){
                ((ToDoViewHolder)holder).img_isLeader.setVisibility(View.VISIBLE);
            }else {
                ((ToDoViewHolder)holder).img_isLeader.setVisibility(View.GONE);
            }
        }else if(holder instanceof  ToDoViewHolderDivision){
            Log.e("TAG","ToDoViewHolderDivision");
        }
    }

    @Override
    public int getItemCount() {
        return toDoEntries.size();
    }
    class ToDoViewHolder extends RecyclerView.ViewHolder{
        CheckBox cb_list;
        TextView tv_time;
        ImageView img_isTomato,img_isLeader;
        public ToDoViewHolder(View itemView) {
            super(itemView);
            cb_list =itemView.findViewById(R.id.cb_list);
            tv_time = itemView.findViewById(R.id.tv_time);
            img_isLeader = itemView.findViewById(R.id.img_is_leader);
            img_isTomato = itemView.findViewById(R.id.img_is_tomato);
            cb_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b&&!toDoEntries.get(array[getPosition()]).getIsChecked()){
                        observable = Observable.create(new Observable.OnSubscribe<IsCheckedEntry>() {
                            @Override
                            public void call(Subscriber<? super IsCheckedEntry> subscriber) {
                                subscriber.onNext(new IsCheckedEntry(true,getPosition()));
                            }
                        });
                        observable.subscribe(subscriber);
                    }else if(!b&&toDoEntries.get(array[getPosition()]).getIsChecked()){
                        observable = Observable.create(new Observable.OnSubscribe<IsCheckedEntry>() {
                            @Override
                            public void call(Subscriber<? super IsCheckedEntry> subscriber) {
                                subscriber.onNext(new IsCheckedEntry(false,getPosition()));
                            }
                        });
                        observable.subscribe(subscriber);
                    }
                }
            });
        }
    }
    class ToDoViewHolderDivision extends RecyclerView.ViewHolder{

        public ToDoViewHolderDivision(View itemView) {
            super(itemView);
        }
    }
}
