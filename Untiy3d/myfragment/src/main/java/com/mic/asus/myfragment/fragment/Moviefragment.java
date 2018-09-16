package com.mic.asus.myfragment.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mic.asus.myfragment.Main4Activity;
import com.mic.asus.myfragment.R;

import org.jetbrains.annotations.Nullable;

public class Moviefragment extends Fragment {
    Button start;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Init();
    }

    public void Init(){
        start=(Button)getActivity().findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Main4Activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("msg","已转送到Main4\n");
                intent.putExtras(bundle);
                startActivityForResult(intent,200);;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null)
            Log.d("msg","数据为空");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if(resultCode==getActivity().RESULT_OK){
                Log.d("msg","成功");
            }
            else{
                Log.d("msg","resultcode不一样;resultcode="+resultCode);
            }
        }
        else
            Log.d("msg","requestcode不一样;requestcode="+requestCode);

    }
}
