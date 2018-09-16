package com.mic.asus.managementsystem.manager.foodmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;

import java.util.HashMap;

public class Addfood extends Activity {
    EditText et_name;
    EditText et_price;
    EditText et_type;
    EditText et_rest;
    Button submit;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_addfood);

        et_name=(EditText)findViewById(R.id.et_foodadd_name);
        et_price=(EditText)findViewById(R.id.et_foodadd_price);
        et_type=(EditText)findViewById(R.id.et_foodadd_type);
        et_rest=(EditText)findViewById(R.id.et_foodadd_rest);
        submit=(Button)findViewById(R.id.food_add_sub);
        cancel=(Button)findViewById(R.id.food_add_can);
        buttonclick();
    }

    //按钮的点击事件
    public void buttonclick(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.food_add_sub:
                        Adddata();
                        finish();
                        break;
                    case R.id.food_add_can:
                        finish();
                        break;
                        default:
                            finish();
                            break;
                }
            }
        };
        submit.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
    }

    //添加数据函数
    public void Adddata(){
        String name=et_name.getText().toString();
        String price=et_price.getText().toString();
        String type=et_type.getText().toString();
        String rest=et_rest.getText().toString();

        String s="main=菜谱&name="+name+"&price="+price+"&type="+type+"&rest="+rest;
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle.putString("s",s);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
    }

}
