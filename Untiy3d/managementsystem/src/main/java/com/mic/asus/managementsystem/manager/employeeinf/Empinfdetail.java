package com.mic.asus.managementsystem.manager.employeeinf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mic.asus.managementsystem.R;

public class Empinfdetail extends Activity {

    EditText et_id;
    EditText et_name;
    EditText et_age;
    EditText et_sex;
    EditText et_position;
    EditText et_grade;
    EditText et_salary;
    TextView back;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_empinfdetail);

        intent=getIntent();
        Bundle bundle=intent.getExtras();

        et_id=(EditText)findViewById(R.id.et_empdetail_id);
        et_name=(EditText)findViewById(R.id.et_empdetail_name);
        et_age=(EditText)findViewById(R.id.et_empdetail_age);
        et_sex=(EditText)findViewById(R.id.et_empdetail_sex);
        et_position=(EditText)findViewById(R.id.et_empdetail_position);
        et_grade=(EditText)findViewById(R.id.et_empdetail_grade);
        et_salary=(EditText)findViewById(R.id.et_empdetail_salary);
        back=(TextView)findViewById(R.id.one_detail_head_back);

        et_id.setText(bundle.getString("id").toString());
        et_name.setText(bundle.getString("name").toString());
        et_age.setText(bundle.getString("age").toString());
        et_sex.setText(bundle.getString("sex").toString());
        et_position.setText(bundle.getString("position").toString());
        et_grade.setText(bundle.getString("grade").toString());
        et_salary.setText(bundle.getString("salary").toString());
        et_id.setEnabled(false);
        et_name.setEnabled(false);
        et_age.setEnabled(false);
        et_sex.setEnabled(false);
        et_position.setEnabled(false);
        et_grade.setEnabled(false);
        et_salary.setEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
