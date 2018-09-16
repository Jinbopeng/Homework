package com.mic.asus.managementsystem.manager.Login;

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

public class ManagerRegister extends Activity {
    EditText name;
    EditText word;
    EditText age;
    RadioGroup sexrg;
    RadioGroup typerg;
    RadioButton sexrb;
    RadioButton typerb;
    Button submit;
    Button cancel;
    TextView back;
    TextView show;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                show.setText(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_manager_register);

        name=(EditText)findViewById(R.id.et_manregister_name);
        word=(EditText)findViewById(R.id.et_manregister_pw);
        age=(EditText)findViewById(R.id.et_manregister_age);
        sexrg=(RadioGroup)findViewById(R.id.mansex_rg);
        typerg=(RadioGroup)findViewById(R.id.mantype_rg);
        show=(TextView)findViewById(R.id.manregister_tvshow);
        submit=(Button)findViewById(R.id.manregister_sub);
        cancel=(Button)findViewById(R.id.manregister_can);
        back=(TextView) findViewById(R.id.manregister_head_back);
        clickbutton();
    }

    //点击按钮事件
    public void clickbutton(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.manregister_sub:
                    {
                        clicksubmit();
                        break;
                    }
                    case R.id.manregister_can:
                    {
                        clickcancel();
                        break;
                    }
                    case R.id.manregister_head_back:
                    {
                        finish();
                    }
                }
            }
        };
        submit.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }

    //提交按钮
    public void clicksubmit(){
        String username=name.getText().toString();
        String password=word.getText().toString();
        String userage=age.getText().toString();
        sexrb=(RadioButton)findViewById(sexrg.getCheckedRadioButtonId());
        typerb=(RadioButton)findViewById(typerg.getCheckedRadioButtonId());
        String usersex=sexrb.getText().toString();
        String usertype=typerb.getText().toString();
        String s="main="+"管理者"+"&username="+username+"&password="+password+"&age="+userage+"&sex="+usersex+"&type="+usertype;
        httpsubmit(s);
        finish();
    }

    //取消按钮
    public void clickcancel(){
        name.setText("");
        word.setText("");
        age.setText("");
        sexrb=(RadioButton)findViewById(R.id.mansex_man);
        typerb=(RadioButton)findViewById(R.id.mantype_main);
        sexrb.setChecked(true);
        typerb.setChecked(true);
    }

    //连接服务器
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
