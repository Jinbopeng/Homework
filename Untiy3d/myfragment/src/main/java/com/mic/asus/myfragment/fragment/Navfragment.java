package com.mic.asus.myfragment.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mic.asus.myfragment.R;



public class Navfragment extends Fragment {
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private ImageView image_music,image_movie,image_my;
    private TextView text_music,text_movie,text_my;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.nav_fragment,container,false);
        return view;
    }

    public void InitComponent(View view){
        image_movie = (ImageView)view.findViewById(R.id.image_movie);
        image_music = (ImageView) view.findViewById(R.id.image_music);
        image_my = (ImageView) view.findViewById(R.id.image_my);
        text_movie = (TextView) view.findViewById(R.id.text_movie);
        text_music = (TextView)view.findViewById(R.id.text_music);
        text_my = (TextView)view.findViewById(R.id.text_my);
    }

}
