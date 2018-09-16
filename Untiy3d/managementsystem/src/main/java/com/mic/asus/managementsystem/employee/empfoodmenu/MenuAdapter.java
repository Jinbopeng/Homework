package com.mic.asus.managementsystem.employee.empfoodmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {
    Context context;
    List<Caixi> menus=new ArrayList<>();
    int selectposition=0;
    onitemclickListener listener;

    public MenuAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.left_item,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final Caixi c=getCaixi(position);
        holder.menutype.setText(c.getTitle());
        holder.menutype.setTextSize(14);
        holder.menutype.setGravity(Gravity.CENTER);
        if(position==selectposition)
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        else
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectposition(position);
                if(listener!=null){
                    listener.onitemclick(position,c);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }



    public Caixi getCaixi(int position){
        return menus.get(position);
    }

    public void AddData(List<Caixi> list){
        menus=list;
        this.notifyDataSetChanged();
    }


    public void setSelectposition(int position){
        selectposition=position;
        this.notifyDataSetChanged();
    }


    public interface onitemclickListener{
        public void onitemclick(int position,Caixi caixi);
    }

    public void setonitemclickListener(onitemclickListener listener){
        this.listener=listener;
    }

    class VH extends RecyclerView.ViewHolder{
        TextView menutype;
        public VH(View itemView) {
            super(itemView);
            menutype=(TextView)itemView.findViewById(R.id.left_tv);
        }
    }
}
