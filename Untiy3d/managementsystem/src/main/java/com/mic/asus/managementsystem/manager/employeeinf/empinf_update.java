package com.mic.asus.managementsystem.manager.employeeinf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;


public class empinf_update extends Activity {

    EditText et_name;
    EditText et_age;
    EditText et_grade;
    EditText et_salary;
    Button yes;
    Button no;
    HashMap<String,String> map=new HashMap<>();
    Intent intent;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                map=(HashMap<String, String>) msg.obj;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_empinf_update);
        et_name=(EditText)findViewById(R.id.et_empupdate_name);
        et_age=(EditText)findViewById(R.id.et_empupdate_age);
        et_grade=(EditText)findViewById(R.id.et_empupdate_grade);
        et_salary=(EditText)findViewById(R.id.et_empupdate_salary);
        yes=(Button)findViewById(R.id.emp_update_sub);
        no=(Button)findViewById(R.id.emp_update_can);
        Init();
    }

    //初始化函数
    public void Init(){
        intent=getIntent();
        final Bundle bundle=intent.getExtras();
        String name=bundle.getString("name");
        String age=bundle.getString("age");
        String grade=bundle.getString("grade");
        String salary=bundle.getString("salary");

        et_name.setText(name);
        et_age.setText(age);
        et_grade.setText(grade);
        et_salary.setText(salary);
        et_name.setEnabled(false);
//        et_age.setEnabled(false);

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.emp_update_sub:
                  /*      mysubmit();
                        if(map.get("Msg").equals("修改成功")) {
                            setResult(RESULT_OK,null);
                            finish();
                        }
                        else*/
                  String name=et_name.getText().toString();
                  String age=et_age.getText().toString();
                  String grade=et_grade.getText().toString();
                  String salary=et_salary.getText().toString();
                  String s="main=管理员更改职工信息"+"&name="+name+"&age="+age+"&grade="+grade+"&salary="+salary;

                  Bundle b=new Bundle();
                  b.putString("s",s);
                  intent.putExtras(b);
                  setResult(RESULT_OK,intent);
                  finish();
//                  Toast.makeText(empinf_update.this,"修改失败",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.emp_update_can:
                        finish();
                        break;
                        default:
                            break;
                }
            }
        };

        yes.setOnClickListener(listener);
        no.setOnClickListener(listener);
    }

    //提交
    public void mysubmit(){
        String name=et_name.getText().toString();
        String age=et_age.getText().toString();
        String grade=et_grade.getText().toString();
        String salary=et_salary.getText().toString();
        final String s="?main=管理员更改职工信息"+"&name="+name+"&age="+age+"&grade="+grade+"&salary="+salary;
//        httpurl(s);
        Mythread mythread=new Mythread(s);
        mythread.run();
    }


    //http服务
    public void httpurl(final String s){
        Log.d("msg","还是到这里来了!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Update"+s);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    conn.setRequestProperty("Charset","UTF-8");

//                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
//                    writer.write(s);
//                    writer.close();

                    int code=conn.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK){
                        InputStream in =conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        StringBuilder response=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=transform(response.toString());
                        handler.sendMessage(msg);
                    }
                    else{
                        Log.d("msg","连接失败");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //分析服务器返回的数据
    public HashMap<String,String> transform(String s){
        HashMap<String,String> map=new HashMap<>();
        String [] item=s.split(";");
        String [] iteminf=new String[2];
        for(String r:item){
            iteminf=r.split("=");
            map.put(iteminf[0],iteminf[1]);
        }
        Log.d("msg","Msg="+map.get("Msg"));
        return map;
    }


    //线程
    class Mythread implements Runnable{
        String s;
        public Mythread(String s){
            this.s=s;
        }

        @Override
        public void run() {
            HttpURLConnection conn=null;
            try {
//                URL url=new URL("http://192.168.1.106:8080/Myservlet/Update");
                URL url=new URL("https://www.baidu.com");
                conn= (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(8000);
                conn.setConnectTimeout(8000);
                conn.setRequestProperty("Charset","UTF-8");

//                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
//                writer.write(s);
//                writer.close();

                int code=conn.getResponseCode();
                if(code==HttpURLConnection.HTTP_OK){
                    InputStream in =conn.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    in.close();
                    reader.close();
                    conn.disconnect();

                    Message msg=new Message();
                    msg.what=520;
                    msg.obj=transform(response.toString());
                    handler.sendMessage(msg);
                }
                else{
                    Log.d("msg","连接失败");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


