package com.mic.asus.managementsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mic.asus.managementsystem.employee.EmployeeLogin;
import com.mic.asus.managementsystem.manager.Login.ManagerLogin;

public class MainActivity extends Activity {
    //main
    Button btn_manager;
    Button btn_employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_main);

        btn_manager=(Button)findViewById(R.id.btn_manager);
        btn_employee=(Button)findViewById(R.id.btn_employee);
        clickbutton();
    }

    public void clickbutton(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btn_manager:
                    {
                        Intent intent=new Intent(MainActivity.this,ManagerLogin.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.btn_employee:
                    {
                        Intent intent=new Intent(MainActivity.this,EmployeeLogin.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        btn_manager.setOnClickListener(listener);
        btn_employee.setOnClickListener(listener);
    }
}
