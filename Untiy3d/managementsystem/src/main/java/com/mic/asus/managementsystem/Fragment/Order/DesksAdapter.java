package com.mic.asus.managementsystem.Fragment.Order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.deskinf.desk;

import java.util.ArrayList;
import java.util.List;

public class DesksAdapter extends RecyclerView.Adapter<DesksAdapter.VH> {
    Context context;
    List<desk> list=new ArrayList<>();
    OnitemclickListener listener;

    public DesksAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.desk2,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final desk d=getdesk(position);
        int occupy=d.getOccupy();
        int capacity=d.getCapacity();
        final int rest=capacity-occupy;

        holder.tv_id.setText(d.getId()+"");
        holder.tv_capacity.setText(d.getCapacity()+"");
        holder.tv_rest.setText(rest+"");
        if(rest==0)
            holder.tv_open.setText("满");
        holder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&rest!=0){
                    listener.onitemClick(v,d);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public desk getdesk(int position){
        return list.get(position);
    }

    //添加数据
    public void  Adddesks(List<desk> desks){
        list=desks;
        this.notifyDataSetChanged();
    }

    //注册方法
    public void setonItemClickListener(OnitemclickListener listener){
        this.listener=listener;
    }

    //监听器接口
    public interface OnitemclickListener{
        public void onitemClick(View v,desk d);
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tv_id;
        TextView tv_capacity;
        TextView tv_rest;
        TextView tv_open;
        public VH(View itemView) {
            super(itemView);
            tv_id=(TextView)itemView.findViewById(R.id.desk2_id);
            tv_capacity=(TextView)itemView.findViewById(R.id.desk2_capacity);
            tv_rest=(TextView)itemView.findViewById(R.id.desk2_rest);
            tv_open=(TextView)itemView.findViewById(R.id.desk2_open);
        }
    }


}
