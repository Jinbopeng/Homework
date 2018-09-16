package com.mic.asus.managementsystem.Fragment.Clean;

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
import java.util.logging.Handler;

public class CleansAdapter extends RecyclerView.Adapter <CleansAdapter.VH> {
    Context context;
    List<desk> cleans=new ArrayList<>();
    OnitemclickListener listener;

    public CleansAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.desk3,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final desk d=getdesk(position);
        String isclean="是";
        if(d.getIsclean()==0) {
            isclean = "否";
            holder.tv_go.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }
        else
            holder.tv_go.setBackgroundColor(context.getResources().getColor(R.color.grey));
        holder.tv_id.setText(d.getId()+"");
        holder.tv_isclean.setText(isclean);
//        holder.tv_go.setText("打扫");

        final String result=isclean;
        holder.tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&result.equals("否")){
                    listener.onitemclick(v,d);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cleans.size();
    }

    public desk getdesk(int position){
        return cleans.get(position);
    }

    public void Adddata(List<desk> desks){
        cleans=desks;
        this.notifyDataSetChanged();
    }

    //设置监听器
    public interface OnitemclickListener{
        public void onitemclick(View v,desk d);
    }

    //注册方法
    public void setonitemclickListener(OnitemclickListener listener){
        this.listener=listener;
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tv_id;
        TextView tv_isclean;
        TextView tv_go;
        public VH(View itemView) {
            super(itemView);
            tv_id=(TextView)itemView.findViewById(R.id.desk3_id);
            tv_isclean=(TextView)itemView.findViewById(R.id.desk3_isclean);
            tv_go=(TextView)itemView.findViewById(R.id.desk3_go);
        }
    }

}
