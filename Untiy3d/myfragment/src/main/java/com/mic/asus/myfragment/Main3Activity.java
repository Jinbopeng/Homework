package com.mic.asus.myfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;

import com.mic.asus.myfragment.fragment.Moviefragment;
import com.mic.asus.myfragment.fragment.Musicfragment;
import com.mic.asus.myfragment.fragment.Myfragment;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends FragmentActivity {
    TextView tv_home,tv_life,tv_mine;
    ViewPager vp;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    List<Fragment> list=new ArrayList<>();
    String[] titles = new String[]{"菜谱", "发布", "我的"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_main3);
        Initcompoent();

    }

    public void Initcompoent(){
        tv_home=(TextView)findViewById(R.id.text_homepage);
        tv_life=(TextView)findViewById(R.id.text_publish);
        tv_mine=(TextView)findViewById(R.id.text_mine);
        vp=(ViewPager)findViewById(R.id.myviewpager);
        Moviefragment moviefragment=new Moviefragment();
        Musicfragment musicfragment=new Musicfragment();
        Myfragment myfragment=new Myfragment();
        list.add(moviefragment);
        list.add(musicfragment);
        list.add(myfragment);

        vp.setOffscreenPageLimit(3);
        FragmentAdapter adapter=new FragmentAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);

        tv_life.setTextColor(0xFFD1CBD6);
        tv_mine.setTextColor(0xFFD1CBD6);
        tv_home.setTextColor(0x30FF6600);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changecolor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.text_homepage:
                        vp.setCurrentItem(0,true);
                        Clearcolor();
                        tv_home.setTextColor(0x30FF6600);
                        break;
                    case R.id.text_publish:
                        vp.setCurrentItem(1,true);
                        Clearcolor();
                        tv_life.setTextColor(0x30FF6600);
                        break;
                    case R.id.text_mine:
                        vp.setCurrentItem(2,true);
                        Clearcolor();
                        tv_mine.setTextColor(0x30FF6600);
                        break;
                }
            }
        };
        tv_home.setOnClickListener(listener);
        tv_life.setOnClickListener(listener);
        tv_mine.setOnClickListener(listener);


    }

    public void changecolor(int position){
        switch(position){
            case 0:
                Clearcolor();
                tv_home.setTextColor(0x30FF6600);
                break;
            case 1:
                Clearcolor();
                tv_life.setTextColor(0x30FF6600);
                break;
            case 2:
                Clearcolor();
                tv_mine.setTextColor(0x30FF6600);
                break;
                default:
                    Clearcolor();
                    break;
        }
    }

    public void Clearcolor(){
        tv_life.setTextColor(0xFFD1CBD6);
        tv_mine.setTextColor(0xFFD1CBD6);
        tv_home.setTextColor(0xFFD1CBD6);
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
}