package com.mic.asus.managementsystem.employee.empfoodmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class stickAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    LayoutInflater inflater;
    Context context;
    List<Rightfood> rightfoods=new ArrayList<>();
    onitemclickListener listener;

    public stickAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        Rightfood food=(Rightfood)getItem(position);
        headVH holder;
        if(convertView==null){
            holder=new headVH();
            convertView=inflater.inflate(R.layout.right_head_item,parent,false);
            holder.type=convertView.findViewById(R.id.right_head);
            holder.item=convertView.findViewById(R.id.main_right_head);
            convertView.setTag(holder);
        }
        else
            holder= (headVH) convertView.getTag();
        holder.type.setText(food.getType());
        holder.item.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return ((Rightfood)getItem(position)).getHead_id();
    }

    @Override
    public int getCount() {
        return rightfoods.size();
    }

    @Override
    public Object getItem(int position) {
        return rightfoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Rightfood food=(Rightfood) getItem(position);
        final VH holder;
        if(convertView==null){
            holder=new VH();
            convertView=inflater.inflate(R.layout.right_item,parent,false);
            holder.name=convertView.findViewById(R.id.right_item_name);
            holder.price=convertView.findViewById(R.id.right_item_price);
            holder.hot=(TextView) convertView.findViewById(R.id.right_item_hot);
            holder.rest=(TextView) convertView.findViewById(R.id.right_item_rest);
            holder.number=convertView.findViewById(R.id.item_num);
            holder.plus=convertView.findViewById(R.id.plus);
            holder.minus=convertView.findViewById(R.id.minus);
            holder.picture=convertView.findViewById(R.id.img_picture);
            convertView.setTag(holder);
        }
        else
            holder= (VH) convertView.getTag();
        holder.name.setText(food.getName());
        holder.price.setText(food.getPrice()+"");
        holder.hot.setText(food.getHot()+"");
        holder.rest.setText(food.getRest()+"");
        holder.number.setText(food.getNumber()+"");
        holder.plus.setImageResource(R.drawable.plus1);
        holder.minus.setImageResource(R.drawable.minus);
        int number=Integer.parseInt(holder.number.getText().toString());
        if(number==0)
            holder.minus.setVisibility(View.GONE);
        else
            holder.minus.setVisibility(View.VISIBLE);
        holder.picture.setImageResource(R.drawable.picture);

        //加号点击事件
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(holder.number.getText().toString());
                int hot=Integer.parseInt(holder.hot.getText().toString());
                int rest=Integer.parseInt(holder.rest.getText().toString());
                if(rest>0) {
                    if (number == 0) {
                        holder.minus.setAnimation(getShowAnimation());
                        holder.minus.setVisibility(View.VISIBLE);
                    }
                    holder.number.setText(number + 1 + "");
                    food.setNumber(number + 1);
                    food.setHot(hot+1);
                    food.setRest(rest-1);
                    holder.hot.setText(hot+1+"");
                    holder.rest.setText(rest-1+"");
                    if (listener != null) {
                        listener.onitemclick(food, 0);
                    }
                }
            }
        });


        //减号点击事件
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(holder.number.getText().toString())-1;
                int hot=Integer.parseInt(holder.hot.getText().toString());
                int rest=Integer.parseInt(holder.rest.getText().toString());
                if(number>=0)
                {
                    if(number==0){
                        holder.minus.setAnimation(getHiddenAnimation());
                        holder.minus.setVisibility(View.GONE);
                    }
                    holder.number.setText(number+"");
                    food.setNumber(number);
                    food.setHot(hot-1);
                    food.setRest(rest+1);
                    holder.hot.setText(hot-1+"");
                    holder.rest.setText(rest+1+"");
                    if(listener!=null){
                        listener.onitemclick(food,1);
                    }
                }
            }
        });



        return convertView;
    }

    public void AddData(List<Rightfood> list){
        this.rightfoods=list;
        this.notifyDataSetChanged();
    }


    public interface onitemclickListener{
        public void onitemclick(Rightfood food, int type);
    }

    public void setonitemclickListener(onitemclickListener listener){
        this.listener=listener;
    }


    class headVH{
        TextView type;
        LinearLayout item;
    }

    class VH{
        TextView name;
        TextView price;
        TextView hot;
        TextView rest;
        TextView number;
        ImageView plus;
        ImageView minus;
        ImageView picture;
    }



    //动画效果
    //显示减号的动画
    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    //隐藏减号的动画
    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }





}
