package com.mic.asus.service;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends Activity {

    Button login;
    Button register;
    EditText ed_username;
    EditText ed_password;
    TextView result;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                result.setText(msg.obj.toString());
            }
            if(msg.what==100){
                result.setText(msg.obj.toString());
            }
            if(msg.what==200){
                result.setText(msg.obj.toString());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button)findViewById(R.id.btn_login);
        register=(Button)findViewById(R.id.btn_register);
        ed_username=(EditText)findViewById(R.id.username);
        ed_password=(EditText)findViewById(R.id.password);
        result=(TextView)findViewById(R.id.tv_result);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=ed_username.getText().toString();
                String password=ed_password.getText().toString();
                Loginpost(username,password);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=ed_username.getText().toString();
                String password=ed_password.getText().toString();
                Register(username,password);
            }
        });
    }

    public void Httpurltest(){
        //网络请求比较耗时，应该放在子线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
//                    申明一个URL，以百度为例要用https
                    URL url=new URL("https://www.baidu.com");
//                    打开URL链接
                    connection =(HttpURLConnection)url.openConnection();
//                    设置请求方法
                    connection.setRequestMethod("GET");
//                    设置连接建立的超时时间
                    connection.setConnectTimeout(8000);
//                    设置网络报文的首发超时时间
                    connection.setReadTimeout(8000);
//                    通过连接的输出流获取报文，然后就是Java的流处理
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Message msg=new Message();
                    msg.what=520;
                    msg.obj=response;
                    handler.sendMessage(msg);

//                    关闭连接
                    reader.close();
                    connection.disconnect();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void Register(String u,String p){
        final String s="?name="+u+"&password="+p;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("http://192.168.1.102:8080/ServletTest/RegisterServlet"+s);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    reader.close();
                    connection.disconnect();

                    Message msg=new Message();
                    msg.what=100;
                    msg.obj=response;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void Login(String u,String p){
        final String s="?username="+u+"&password="+p;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("http://192.168.1.102:8080/ServletTest/LoginServlet"+s);
//                    URL url=new URL("https://www.baidu.com");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestProperty("Charset", "UTF-8"); //设置uft-8字符集

                    Log.d("msg","准备开始连接");
                    connection.connect();
                    Log.d("msg","here0");
                    int code=connection.getResponseCode();
                    if(code==connection.HTTP_OK) {
                        Log.d("msg","已成功连接");
                        InputStream in = connection.getInputStream();
                        Log.d("msg", "here1");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        connection.disconnect();

                        Message msg=new Message();
                        msg.what=200;
                        msg.obj=response;
                        handler.sendMessage(msg);
                    }
                    else
                    {
                        Log.d("msg","连接不成功");
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void Loginpost(String u,String p){
        final String s="username="+u+"&password="+p;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("http://192.168.1.102:8080/ServletTest/LoginServlet");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestProperty("Charset", "UTF-8");

                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
                    writer.write(s);
                    writer.close();

//                    DataOutputStream out=new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes(s);
                    Log.d("msg","准备开始连接");
                    int datacode=connection.getResponseCode();
                    if(datacode==connection.HTTP_OK){
                        Log.d("msg","已经连接上");
                        InputStream in=connection.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        StringBuilder response=new StringBuilder();
                        String line="";
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        Log.d("msg","准备发送");
                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=response;
                        handler.sendMessage(msg);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
