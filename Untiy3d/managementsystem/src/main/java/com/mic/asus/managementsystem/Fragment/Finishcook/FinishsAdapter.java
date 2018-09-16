package com.mic.asus.managementsystem.Fragment.Finishcook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.transactioninf.tdetail;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

public class FinishsAdapter extends RecyclerView.Adapter<FinishsAdapter.VH> {
    private Context context;
    private List<tdetail> foods=new ArrayList<>();

    public FinishsAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.finish_item,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        tdetail t=getitem(position);
        holder.tv_name.setText(t.getFoodname());
        holder.tv_number.setText(t.getNumber()+"");
        holder.tv_id.setText(t.getId()+"");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public tdetail getitem(int position){
        return foods.get(position);
    }

    public void AddData(List<tdetail> list){
        foods=list;
        this.notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_number;
        TextView tv_id;

        public VH(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_finish_name);
            tv_number=(TextView)itemView.findViewById(R.id.tv_finish_number);
            tv_id =(TextView)itemView.findViewById(R.id.tv_finish_id);
        }

    }
}
