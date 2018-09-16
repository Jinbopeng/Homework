package com.mic.asus.managementsystem.employee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class EmployeeLogin extends Activity {

    EditText name;
    EditText word;
    TextView show;
    Button submit;
    Button register;
    HashMap<String,String> map=new HashMap<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
//                show.setText(msg.obj.toString());
                map=transform(msg.obj.toString());
                Toast.makeText(getApplicationContext(),""+map.get("Msg"),Toast.LENGTH_SHORT).show();
                if(map.get("resCode").equals("100")&&map.get("Msg").equals("服务员")){
                    Intent intent=new Intent(EmployeeLogin.this,EmployeeMain.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",name.getText().toString());
                    intent.putExtras(bundle);
                    SharedPreferences sharedPreferences=getSharedPreferences("empLogin",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name.getText().toString());
                    editor.commit();
                    startActivity(intent);
                }
                else if(map.get("resCode").equals("100")&&map.get("Msg").equals("厨师"))
                {
                    Intent intent=new Intent(EmployeeLogin.this,CookerMain.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",name.getText().toString());
                    intent.putExtras(bundle);
                    SharedPreferences sharedPreferences=getSharedPreferences("empLogin",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name.getText().toString());
                    editor.commit();
                    startActivity(intent);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_employee_login);

        submit=(Button)findViewById(R.id.empbtn_login);
        register=(Button)findViewById(R.id.empbtn_register);
        name=(EditText)findViewById(R.id.emplogin_name);
        word=(EditText)findViewById(R.id.emplogin_word);
        show=(TextView)findViewById(R.id.emplogin_show);
        clickbutton();

    }

    public void clickbutton(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.empbtn_login:{
                        Login();
                        break;
                    }
                    case R.id.empbtn_register:{
                        Intent intent=new Intent(EmployeeLogin.this,EmployeeRegister.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        submit.setOnClickListener(listener);
        register.setOnClickListener(listener);
    }

    //登录函数
    public void Login(){
        String username=name.getText().toString();
        String password=word.getText().toString();
        final String s="main="+"职工"+"&username="+username+"&password="+password;
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
                        Log.d("msg","已经成功连接!");
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
