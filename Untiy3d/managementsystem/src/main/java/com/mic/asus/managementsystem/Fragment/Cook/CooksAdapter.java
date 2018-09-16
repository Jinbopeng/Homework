package com.mic.asus.managementsystem.Fragment.Cook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.transactioninf.tdetail;

import java.util.ArrayList;
import java.util.List;

public class CooksAdapter extends RecyclerView.Adapter<CooksAdapter.VH> {
    private Context context;
    private List<tdetail> foods=new ArrayList<>();
    private onitemclickListener listener;

    public CooksAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cook_item,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        final tdetail t=getitem(position);
        String status="未开始";
        if(t.getIscook()==1)
            status="炒菜中";
        holder.tv_transid.setText(t.getId()+"");
        holder.tv_name.setText(t.getFoodname());
        holder.tv_number.setText(t.getNumber()+"");
        holder.tv_status.setText(status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){//未开始
                    if(t.getIscook()==0){
                        holder.tv_status.setText("炒菜中");
                        t.setIscook(1);
                        listener.onitemclick(t,0);
                    }
                    else if(t.getIscook()==1){//炒菜中
                        RemoveData(position);
                        t.setIscook(2);
                        listener.onitemclick(t,1);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public tdetail getitem(int position){
        return foods.get(position);
    }

    //添加数据
    public void AddData(List<tdetail> list){
        foods=list;
        this.notifyDataSetChanged();
    }

    //移除数据
    public void RemoveData(int position){
        foods.remove(position);
        this.notifyDataSetChanged();
    }

    public interface onitemclickListener{
        public void onitemclick(tdetail t,int type);
    }

    public void setonitemclickListener(onitemclickListener listener){
        this.listener=listener;
    }


    class VH extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_number;
        TextView tv_status;
        TextView tv_transid;

        public VH(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_cook_name);
            tv_number=(TextView)itemView.findViewById(R.id.tv_cook_number);
            tv_status=(TextView)itemView.findViewById(R.id.tv_cook_status);
            tv_transid=(TextView)itemView.findViewById(R.id.tv_cook_transid);
        }
    }
}
