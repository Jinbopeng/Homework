package com.mic.asus.managementsystem;

import android.os.Message;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.manager.employeeinf.empinf;
import com.mic.asus.managementsystem.manager.foodmenu.food;
import com.mic.asus.managementsystem.manager.foodmenu.foodinf;

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
import java.util.List;

public class DBhelper {
    public static String mylocalhost="http://192.168.43.25:8080/";


    //Post方法
    public static String myhttpPost(final String endurl, final String s){
        final StringBuilder response=new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+endurl);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    conn.setRequestProperty("Charset","UTF-8");

                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
                    writer.write(s);
                    writer.close();

                    int code=conn.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK){
                        InputStream in =conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();

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
        return response.toString();
    }
    //GET方法
    public static String myhttpGet(final String endurl, final String s){
        final StringBuilder response=new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+endurl+s);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    conn.setRequestProperty("Charset","UTF-8");

                    int code=conn.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK){
                        InputStream in =conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();

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
        return response.toString();
    }

    //transform函数
    public static HashMap<String,String> transform(String s){
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
