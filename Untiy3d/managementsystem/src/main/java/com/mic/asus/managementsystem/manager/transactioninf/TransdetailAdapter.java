package com.mic.asus.managementsystem.manager.transactioninf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class TransdetailAdapter extends RecyclerView.Adapter<TransdetailAdapter.VH> {
    Context context;
    List<tdetail> transdetails=new ArrayList<>();

    public TransdetailAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.transdetail,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        tdetail t=getitem(position);
        String cooker="未接单";
        String iscook="未开始";
        if(t.getIscook()==1){
            cooker=t.getCooker();
            iscook="炒菜中";
        }
        else if(t.getIscook()==2){
            cooker=t.getCooker();
            iscook="已炒完";
        }
        holder.tv_name.setText(t.getFoodname());
        holder.tv_number.setText(t.getNumber()+"");
        holder.tv_iscook.setText(iscook);
        holder.tv_cooker.setText(cooker);
    }

    @Override
    public int getItemCount() {
        return transdetails.size();
    }


    public tdetail getitem(int position){
        return transdetails.get(position);
    }


    //添加数据
    public void AddData(List<tdetail> list){
        transdetails=list;
        this.notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_number;
        TextView tv_iscook;
        TextView tv_cooker;

        public VH(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.trans_foodname);
            tv_number=(TextView)itemView.findViewById(R.id.trans_number);
            tv_iscook=(TextView)itemView.findViewById(R.id.trans_iscook);
            tv_cooker=(TextView)itemView.findViewById(R.id.trans_cooker);
        }
    }
}
