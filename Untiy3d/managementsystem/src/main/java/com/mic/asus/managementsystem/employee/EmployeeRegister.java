package com.mic.asus.managementsystem.employee;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class EmployeeRegister extends Activity {
    TextView back;
    EditText name;
    EditText word;
    TextView show;
    RadioGroup sexrg;
    RadioButton sexrb;
    RadioGroup typerg;
    RadioButton typerb;
    EditText age;
    Button submit;
    Button cancel;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                //Toast.makeText(this,""+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                show.setText(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_employee_register);

        back=(TextView)findViewById(R.id.empregister_head_back);
        name=(EditText)findViewById(R.id.et_empregister_name);
        word=(EditText)findViewById(R.id.et_empregister_pw);
        sexrg=(RadioGroup)findViewById(R.id.empsex_rg);
        typerg=(RadioGroup)findViewById(R.id.emptype_rg);
        age=(EditText)findViewById(R.id.et_empregister_age);
        submit=(Button)findViewById(R.id.empregister_sub);
        cancel=(Button)findViewById(R.id.empregister_can);
        show=(TextView)findViewById(R.id.empregister_tvshow);
        buttonclick();
    }

    //点击事件
    public void buttonclick(){
        View.OnClickListener listener =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.empregister_head_back:{
                        finish();
                        break;
                    }
                    case R.id.empregister_sub:{
                        clicksubmit();
                        break;
                    }
                    case R.id.empregister_can:{
                        clickcancel();
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        back.setOnClickListener(listener);
        submit.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
    }

    //提交
    public void clicksubmit(){
        String username=name.getText().toString();
        String password=word.getText().toString();
        String userage=age.getText().toString();
        sexrb=sexrg.findViewById(sexrg.getCheckedRadioButtonId());
        typerb=typerg.findViewById(typerg.getCheckedRadioButtonId());
        String usersex=sexrb.getText().toString();
        String usertype=typerb.getText().toString();
        String s="main="+"职工"+"&username="+username+"&password="+password+"&age="+userage+"&sex="+usersex+"&type="+usertype;
        httpsubmit(s);
    }

    //取消
    public void clickcancel(){
        name.setText("");
        word.setText("");
        age.setText("");
        sexrb=(RadioButton)findViewById(R.id.empsex_man);
        typerb=(RadioButton)findViewById(R.id.emptype_waiter);
        sexrb.setChecked(true);
        typerb.setChecked(true);
    }

    //提交数据到服务器
    public void httpsubmit(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Emp_Register");
                    conn=(HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    conn.setRequestProperty("Charset", "UTF-8");


                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
                    writer.write(data);
                    writer.close();

                    int code=conn.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK){
                        InputStream in=conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        StringBuilder response=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        reader.close();
                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=response;
                        handler.sendMessage(msg);
                    }
                    else{
                        Log.d("msg","未成功连接");
                    }
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
