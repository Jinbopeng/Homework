package com.mic.asus.managementsystem.manager.foodmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mic.asus.managementsystem.R;

import javax.xml.transform.Result;

public class Foodinf_update extends Activity {
    EditText et_name;
    EditText et_price;
    EditText et_type;
    EditText et_hot;
    EditText et_rest;
    Button submit;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_foodinf_update);
        Init();
    }

    public void Init(){
        et_name=(EditText)findViewById(R.id.et_foodupdate_name);
        et_price=(EditText)findViewById(R.id.et_foodupdate_price);
        et_type=(EditText)findViewById(R.id.et_foodupdate_type);
        et_hot=(EditText)findViewById(R.id.et_foodupdate_hot);
        et_rest=(EditText)findViewById(R.id.et_foodupdate_rest);
        submit=(Button)findViewById(R.id.food_update_sub);
        cancel=(Button)findViewById(R.id.food_update_can);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.food_update_sub:
                        update();
                        finish();
                        break;
                    case R.id.food_update_can:
                        finish();
                        break;
                        default:
                            break;
                }
            }
        };
        submit.setOnClickListener(listener);
        cancel.setOnClickListener(listener);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        et_name.setText(bundle.getString("name"));
        et_price.setText(bundle.getString("price"));
        et_type.setText(bundle.getString("type"));
        et_hot.setText(bundle.getString("hot"));
        et_rest.setText(bundle.getString("rest"));
        et_name.setEnabled(false);
    }


    //修改函数
    public void update(){
        String name=et_name.getText().toString();
        String price=et_price.getText().toString();
        String type=et_type.getText().toString();
        String hot=et_hot.getText().toString();
        String rest=et_rest.getText().toString();
        String s="main=菜谱"+"&name="+name+"&price="+price+"&type="+type+"&hot="+hot
                +"&rest="+rest;
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle.putString("s",s);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

    }

}
