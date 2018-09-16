package com.mic.asus.managementsystem.manager.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.deskinf.Deskinf;
import com.mic.asus.managementsystem.manager.employeeinf.Employeeinf;
import com.mic.asus.managementsystem.manager.foodmenu.foodinf;
import com.mic.asus.managementsystem.manager.transactioninf.Transactioninf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ManagerMain extends Activity {
    Intent intent;
    TextView head;
    Button people;
    Button  food;
    Button desk;
    Button transacion;
    Button back;
    HashMap<String,String> result;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                result=transform(msg.obj.toString());
                Toast.makeText(ManagerMain.this,result.get("Msg"),Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_manager_main);


        intent=getIntent();
        Bundle bundle=intent.getExtras();
        String name=bundle.getString("username");
        head=(TextView) findViewById(R.id.manmain_tv);
        people=(Button)findViewById(R.id.manmain_emp);
        food=(Button)findViewById(R.id.manmain_food);
        desk=(Button)findViewById(R.id.manmain_desk);
        transacion=(Button)findViewById(R.id.manmain_transaction);
        back=(Button)findViewById(R.id.manmain_back);
        head.setText(name);
        clickbutton();
    }

    //点击按钮
    public void clickbutton(){
        final View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.manmain_emp:{
                        Intent intent=new Intent(ManagerMain.this,Employeeinf.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.manmain_food:{
                        Intent intent=new Intent(ManagerMain.this,foodinf.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.manmain_desk:{
                        Intent intent=new Intent(ManagerMain.this, Deskinf.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.manmain_transaction:{
                        Intent intent=new Intent(ManagerMain.this, Transactioninf.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.manmain_back:{
                        Logoff(head.getText().toString());
                        finish();
                        break;
                    }
                }
            }
        };
        people.setOnClickListener(listener);
        food.setOnClickListener(listener);
        desk.setOnClickListener(listener);
        transacion.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }

    //退出登录函数
    public void Logoff(final String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s="main=管理员"+"&name="+name;
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Logoff");
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.setRequestProperty("Charset","UTF-8");


                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
                    writer.write(s);
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
                        conn.disconnect();

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=response;
                        handler.sendMessage(msg);
                    }
                    else{
                        Log.d("msg","连接失败!");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //解析response数据的函数
    public HashMap<String,String> transform(String s){
        HashMap<String,String> map=new HashMap<>();
        String [] item=s.split(";");
        String [] iteminf;
        for(String r:item){
            iteminf=r.split("=");
            map.put(iteminf[0],iteminf[1]);
        }
        return map;
    }

}
