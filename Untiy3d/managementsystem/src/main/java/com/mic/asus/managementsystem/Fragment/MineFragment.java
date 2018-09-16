package com.mic.asus.managementsystem.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

public class MineFragment extends Fragment {
    TextView detail;
    TextView logoff;
    TextView name;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                HashMap<String,String> response=new HashMap<>();
                response=DBhelper.transform(msg.obj.toString());
                if(response.get("msg").equals("职员退出成功")){
                    Toast.makeText(getActivity(),"成功退出", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else{
                    Toast.makeText(getActivity(),""+response.get("msg"),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mine_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Init();
    }

    public void Init(){
        detail=(TextView)getActivity().findViewById(R.id.mine_employee_detail);
        logoff=(TextView)getActivity().findViewById(R.id.mine_employee_logoff);
        name=(TextView)getActivity().findViewById(R.id.id_name);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("empLogin", Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("name","");
        name.setText(username);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.mine_employee_detail:
                        Detail();
                        break;
                    case R.id.mine_employee_logoff:
                        Logoff(username);
                        break;
                }
            }
        };
        detail.setOnClickListener(listener);
        logoff.setOnClickListener(listener);
    }

    //修改资料
    public void Detail(){
        getActivity().onBackPressed();
    }

    //退出登录
    public void Logoff(final String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s="main=职工"+"&name="+name;
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

}
