package com.mic.asus.managementsystem.employee;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mic.asus.managementsystem.Fragment.Clean.CleanFragment;
import com.mic.asus.managementsystem.Fragment.Clean.CleansAdapter;
import com.mic.asus.managementsystem.Fragment.Cook.CookFragment;
import com.mic.asus.managementsystem.Fragment.Finishcook.FinishFragment;
import com.mic.asus.managementsystem.Fragment.MineFragment;
import com.mic.asus.managementsystem.Fragment.Order.OrderFragment;
import com.mic.asus.managementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class CookerMain extends FragmentActivity {
    TextView tv_cook,tv_finish,tv_mine;
    ViewPager vp;
    List<Fragment> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_cooker_main);
        Initcompoment();
    }


    //组件的初始化
    public void Initcompoment(){
        tv_cook=(TextView)findViewById(R.id.text_cook);
        tv_finish=(TextView)findViewById(R.id.text_finishcook);
        tv_mine=(TextView)findViewById(R.id.text_cookmine);
        vp=(ViewPager)findViewById(R.id.twoviewpager);
        list=new ArrayList<>();
        FragmentPagerAdapter adapter=new FragmentAdapter(getSupportFragmentManager());
        CookFragment f1=new CookFragment();
        FinishFragment f2=new FinishFragment();
        MineFragment f3=new MineFragment();


        list.add(f1);
        list.add(f2);
        list.add(f3);
        vp.setCurrentItem(0);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        tv_cook.setTextColor(getResources().getColor(R.color.yellow));


        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Changecolor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        //当点击下面的TextView时，进行fragment的更改和textview颜色的改变
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
                switch (v.getId()){
                    case R.id.text_cook:
                        vp.setCurrentItem(0,true);
                        tv_cook.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case R.id.text_finishcook:
                        vp.setCurrentItem(1,true);
                        tv_finish.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case R.id.text_cookmine:
                        vp.setCurrentItem(2,true);
                        tv_mine.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                }
            }
        };
        tv_cook.setOnClickListener(listener);
        tv_finish.setOnClickListener(listener);
        tv_mine.setOnClickListener(listener);
    }


    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    //当滑动时更改下面的颜色
    public void Changecolor(int position){
        Clear();
        switch (position){
            case 0:
                tv_cook.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 1:
                tv_finish.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 2:
                tv_mine.setTextColor(getResources().getColor(R.color.yellow));
                break;
        }
    }

    //清除开始的设置
    public void Clear(){
        tv_cook.setTextColor(getResources().getColor(R.color.grey));
        tv_finish.setTextColor(getResources().getColor(R.color.grey));
        tv_mine.setTextColor(getResources().getColor(R.color.grey));
    }

}
