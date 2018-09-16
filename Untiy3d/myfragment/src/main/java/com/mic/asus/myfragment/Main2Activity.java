package com.mic.asus.myfragment;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TableLayout;

import com.mic.asus.myfragment.fragment.Moviefragment;
import com.mic.asus.myfragment.fragment.Musicfragment;
import com.mic.asus.myfragment.fragment.Myfragment;

import java.util.ArrayList;

public class Main2Activity extends FragmentActivity {
    private TabLayout mtablayout;
    private ViewPager mviewpager;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private static String[] mTabTitles = {"电影", "音乐", "游戏"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mviewpager=(ViewPager)findViewById(R.id.viewPager);
        mtablayout= (TabLayout) findViewById(R.id.tabLayout);
        Moviefragment movie=new Moviefragment();
        Musicfragment music=new Musicfragment();
        Myfragment game=new Myfragment();
        fragments.add(movie);
        fragments.add(music);
        fragments.add(game);
        mviewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mtablayout.setupWithViewPager(mviewpager);
        //设置Tab title文字的正常颜色和选中颜色
        //默认是灰色和黑色，这里换成棕色和绿色
        mtablayout.setTabTextColors(Color.parseColor("#bc6e1c"),
                Color.parseColor("#236f28"));
    }



    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return mTabTitles[position]; //用于给Tab的设置标题
        }
    }


}
