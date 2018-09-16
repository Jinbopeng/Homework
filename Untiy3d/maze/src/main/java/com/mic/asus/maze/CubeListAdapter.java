package com.mic.asus.maze;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CubeListAdapter extends RecyclerView.Adapter<CubeListAdapter.VH> {
    private Context context;
    private List<Cube> list=new ArrayList<>();
    private double itemscale;
    private RecyclerView.LayoutManager layoutManager;


    public CubeListAdapter(Context c,RecyclerView.LayoutManager lay){
        context=c;
        layoutManager=lay;

    }
    @NonNull
    @Override

    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          itemscale=parent.getWidth();
//        Log.d("msg","宽度为:"+itemscale+"\n");                   //宽度为recycleview在屏幕中的宽度
//        Log.d("msg","高度为:"+parent.getHeight()+"\n");          //高度为recycleview在屏幕中的高度
//        Log.d("msg","执行here\n");
//        ((GridLayoutManager)layoutManager).setSpanCount((int) Math.sqrt( getItemCount()));
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.cube,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        //1为通路，0为墙
        final Cube cube=getCube(position);
//        Log.d("msg","imageview的宽度:"+holder.img.getWidth()+"\n");
//        Log.d("msg","item的宽度:"+holder.itemView.getWidth()+"\n");
        ViewGroup.LayoutParams lp=holder.itemView.getLayoutParams();
//        Log.d("msg",((GridLayoutManager)layoutManager).getSpanCount()+"\n");
//        Log.d("msg",Math.sqrt( getItemCount())+"\n");
        lp.height=(int) itemscale/(int) Math.sqrt( getItemCount());

//        Log.d("msg","img高度为:"+lp.height+"\n");

        if(cube.getStatus()==1)
            holder.img.setImageResource(R.drawable.road);
        else if(cube.getStatus()==0)
            holder.img.setImageResource(R.drawable.border);
        else
            holder.img.setImageResource(R.drawable.find);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Cube getCube(int position){
        return list.get(position);
    }

    public void AddCubes(List<Cube> li){
        list.clear();
        list.addAll(li);
        this.notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{
        private ImageView img;
        public VH(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.cube_img);
        }
    }

}
