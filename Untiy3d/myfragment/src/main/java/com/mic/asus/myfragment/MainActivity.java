package com.mic.asus.myfragment;

import android.app.Activity;



import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mic.asus.myfragment.fragment.Moviefragment;
import com.mic.asus.myfragment.fragment.Musicfragment;
import com.mic.asus.myfragment.fragment.Myfragment;
import com.mic.asus.myfragment.fragment.Navfragment;

public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private ImageView image_music,image_movie,image_my;
    private TextView text_music,text_movie,text_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitComponent();
        Moviefragment moviefragment=new Moviefragment();
        Navfragment navfragment=new Navfragment();
        LoadFragment(R.id.buttom,navfragment);
        LoadFragment(R.id.content,moviefragment);
        //设置选中状态
        image_movie.setImageResource(R.drawable.moviegreen);
        text_movie.setTextColor(Color.GREEN);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSelection();
                switch (v.getId()){
                    case R.id.image_movie:
                        fragment=new Moviefragment();
                        LoadFragment(R.id.content,fragment);
                        text_movie.setTextColor(Color.GREEN);
                        image_movie.setImageResource(R.drawable.moviegreen);
                        break;
                    case R.id.image_music:
                        fragment=new Musicfragment();
                        LoadFragment(R.id.content,fragment);
                        text_music.setTextColor(Color.GREEN);
                        image_music.setImageResource(R.drawable.musicgreen);
                        break;
                    case R.id.image_my:
                        fragment=new Myfragment();
                        LoadFragment(R.id.content,fragment);
                        text_my.setTextColor(Color.GREEN);
                        image_my.setImageResource(R.drawable.mygreen);
                        break;
                }
            }
        };
        image_movie.setOnClickListener(listener);
        image_music.setOnClickListener(listener);
        image_my.setOnClickListener(listener);
    }
    public void InitComponent(){
        image_movie = (ImageView) findViewById(R.id.image_movie);
        image_music = (ImageView) findViewById(R.id.image_music);
        image_my = (ImageView) findViewById(R.id.image_my);
        text_movie = (TextView) findViewById(R.id.text_movie);
        text_music = (TextView) findViewById(R.id.text_music);
        text_my = (TextView) findViewById(R.id.text_my);
    }

    public void LoadFragment(int ContainViewid,Fragment fragment){
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        transaction.replace(ContainViewid,fragment);
        transaction.commit();
    }

    public void ClearSelection(){
        image_movie.setImageResource(R.drawable.movieblue);
        image_music.setImageResource(R.drawable.musicblue);
        image_my.setImageResource(R.drawable.myblue);
        text_movie.setTextColor(Color.parseColor("#82858b"));
        text_music.setTextColor(Color.parseColor("#82858b"));
        text_my.setTextColor(Color.parseColor("#82858b"));
    }



}
