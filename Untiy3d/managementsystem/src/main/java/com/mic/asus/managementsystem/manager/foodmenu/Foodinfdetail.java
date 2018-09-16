package com.mic.asus.managementsystem.manager.foodmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;

public class Foodinfdetail extends Activity {
    EditText et_id;
    EditText et_name;
    EditText et_price;
    EditText et_type;
    EditText et_hot;
    EditText et_rest;
    TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_foodinfdetail);
        et_id=(EditText)findViewById(R.id.et_foodinfdetail_id);
        et_name=(EditText)findViewById(R.id.et_foodinfdetail_name);
        et_price=(EditText)findViewById(R.id.et_foodinfdetail_price);
        et_type=(EditText)findViewById(R.id.et_foodinfdetail_type);
        et_hot=(EditText)findViewById(R.id.et_foodinfdetail_hot);
        et_rest=(EditText)findViewById(R.id.et_foodinfdetail_rest);
        tv_back=(TextView)findViewById(R.id.three_detail_head_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getdata();
    }

    public void getdata(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        et_id.setText(bundle.getString("id"));
        et_name.setText(bundle.getString("name"));
        et_price.setText(bundle.getString("price"));
        et_type.setText(bundle.getString("type"));
        et_hot.setText(bundle.getString("hot"));
        et_rest.setText(bundle.getString("rest"));
        et_id.setEnabled(false);
        et_name.setEnabled(false);
        et_price.setEnabled(false);
        et_type.setEnabled(false);
        et_hot.setEnabled(false);
        et_rest.setEnabled(false);
    }
}
