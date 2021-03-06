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
import android.widget.EditText;
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
import java.util.HashMap;

public class ManagerLogin extends Activity {
    EditText name;
    EditText word;
    Button login;
    Button register;
    TextView show;
    HashMap<String,String> map=new HashMap<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
//                show.setText(msg.obj.toString());
                map=transform(msg.obj.toString());
                Toast.makeText(getApplicationContext(),""+map.get("Msg"),Toast.LENGTH_SHORT).show();
                if(map.get("resCode").equals("100")){
                    Intent intent=new Intent(ManagerLogin.this,ManagerMain.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",name.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_manager_login);

        name=(EditText) findViewById(R.id.man_username);
        word=(EditText) findViewById(R.id.man_password);
        login=(Button)findViewById(R.id.manbtn_login);
        register=(Button)findViewById(R.id.manbtn_register);
        show=(TextView)findViewById(R.id.manlogin_show);
        clickbutton();
    }

    //点击事件
    public void clickbutton(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.manbtn_login:{
                        Login();
                        break;
                    }
                    case R.id.manbtn_register:{
                        Intent intent=new Intent(ManagerLogin.this,ManagerRegister.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        login.setOnClickListener(listener);
        register.setOnClickListener(listener);
    }

    //登录函数
    public void Login(){
        String username=name.getText().toString();
        String password=word.getText().toString();
        final String s="main="+"管理员"+"&username="+username+"&password="+password;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Login");
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
}
