package com.mic.asus.managementsystem.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mic.asus.managementsystem.Fragment.Clean.CleanFragment;
import com.mic.asus.managementsystem.Fragment.MineFragment;
import com.mic.asus.managementsystem.Fragment.Order.OrderFragment;
import com.mic.asus.managementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMain extends FragmentActivity {
    TextView tv_order,tv_clean,tv_mine;
    ViewPager vp;
    android.support.v4.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    List<Fragment> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_employee_main);
        InitComponent();
    }


    //初始化
    public void InitComponent(){
        tv_order=(TextView)findViewById(R.id.text_order);
        tv_clean=(TextView)findViewById(R.id.text_clean);
        tv_mine =(TextView)findViewById(R.id.text_mine);
        vp=(ViewPager)findViewById(R.id.oneviewpager);

        OrderFragment orderFragment=new OrderFragment();
        CleanFragment cleanFragment=new CleanFragment();
        MineFragment mineFragment=new MineFragment();
        list.add(orderFragment);
        list.add(cleanFragment);
        list.add(mineFragment);

        FragmentAdapter adapter=new FragmentAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);
        vp.setCurrentItem(0);
        tv_order.setTextColor(getResources().getColor(R.color.yellow));

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //更改下面的颜色
                changecolor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.text_order:
                        cleanselect();
                        vp.setCurrentItem(0,true);
                        tv_order.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case R.id.text_clean:
                        cleanselect();
                        vp.setCurrentItem(1,true);
                        tv_clean.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case R.id.text_mine:
                        cleanselect();
                        vp.setCurrentItem(2,true);
                        tv_mine.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                }
            }
        };
        tv_order.setOnClickListener(listener);
        tv_clean.setOnClickListener(listener);
        tv_mine.setOnClickListener(listener);
    }





    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(android.support.v4.app.FragmentManager fm) {
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



    public void cleanselect(){
        tv_order.setTextColor(getResources().getColor(R.color.grey));
        tv_clean.setTextColor(getResources().getColor(R.color.grey));
        tv_mine.setTextColor(getResources().getColor(R.color.grey));
    }



    public void changecolor(int position){
        switch (position){
            case 0:
                cleanselect();
                tv_order.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 1:
                cleanselect();
                tv_clean.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 2:
                cleanselect();
                tv_mine.setTextColor(getResources().getColor(R.color.yellow));
                break;
        }
    }


    //ActivityA的onActivityResult
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null)
            Log.d("msg","ActivityA中data为空");
        Log.d("msg","ActivityA中request="+requestCode+";result="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        list.get(0).onActivityResult(requestCode,resultCode,data);
    }*/
}
