package com.mic.asus.managementsystem.Contextmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.employeeinf.empinf;

import java.util.ArrayList;
import java.util.List;

public class empinfAdapter extends RecyclerView.Adapter<empinfAdapter.VH> {
    Context context;
    List<empinf> list=new ArrayList<>();
    OnitemclickListener listener;

    public empinfAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.empinf,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final empinf inf=getempinf(position);
        holder.name.setText(inf.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.Onclick(v,inf);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public empinf getempinf(int position){
        return list.get(position);
    }

    public void Addempinf(List<empinf> li){
        list.addAll(li);
        this.notifyDataSetChanged();
    }


    //注册事件的方法
    public void setonItemclickListener(OnitemclickListener listener){
        this.listener=listener;
    }


    //设置监听器
    public  interface OnitemclickListener{
        public void Onclick(View v,empinf e);
    }

    class VH extends RecyclerView.ViewHolder{
        private TextView name;
        public VH(View itemView) {
            super(itemView);
//            name=(TextView)itemView.findViewById(R.id.emp_name);
        }
    }



}
