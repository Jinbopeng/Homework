package com.mic.asus.managementsystem.manager.deskinf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mic.asus.managementsystem.R;

public class Deskinf_add extends Activity {
    Button btn_sub;
    Button btn_can;
    EditText et_id;
    EditText et_capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_deskinf_add);
        btn_sub=(Button)findViewById(R.id.desk_add_sub);
        btn_can=(Button)findViewById(R.id.desk_add_can);
        et_id=(EditText)findViewById(R.id.et_deskadd_id);
        et_capacity=(EditText)findViewById(R.id.et_deskadd_capacity);
        buttonclick();
    }


    //点击事件
    public void buttonclick(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.desk_add_sub:
                        submit();
                        break;
                    case R.id.desk_add_can:
                        finish();
                        break;
                        default:
                            break;
                }
            }
        };
        btn_sub.setOnClickListener(listener);
        btn_can.setOnClickListener(listener);
    }

    //提交事件
    public void submit(){
        Intent intent=getIntent();
        String id=et_id.getText().toString();
        String capacity=et_capacity.getText().toString();
        String s="main=餐桌&id="+id+"&capacity="+capacity;
        Bundle bundle=new Bundle();
        bundle.putString("s",s);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

}