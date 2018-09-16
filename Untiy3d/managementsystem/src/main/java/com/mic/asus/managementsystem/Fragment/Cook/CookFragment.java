package com.mic.asus.managementsystem.Fragment.Cook;

import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.deskinf.desk;
import com.mic.asus.managementsystem.manager.transactioninf.tdetail;
import com.mic.asus.managementsystem.manager.transactioninf.transaction;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookFragment extends Fragment {
    String cooker="";
    RecyclerView rv;
    RefreshLayout refreshLayout;
    CooksAdapter adapter;
    List<tdetail> list;
    HashMap<String,String> map;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {//初始化数据
            if(msg.what==520){
                adapter.AddData((List<tdetail>)msg.obj);
            }
            else if(msg.what==1314){//更新操作
                map=DBhelper.transform(msg.obj.toString());
                if(map.get("resCode").equals("100")) {
//                    onActivityCreated(null);
                    GetData();
                }
                else
                    Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==100){//修改厨师分数
                map=DBhelper.transform(msg.obj.toString());
                if(!map.get("resCode").equals("100"))
                    Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.cook_fragment,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Init();
        GetData();
    }

    //初始化
    public void Init(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("empLogin", Context.MODE_PRIVATE);
        cooker=sharedPreferences.getString("name","");
        refreshLayout=(RefreshLayout)getActivity().findViewById(R.id.cook_refresh);
        rv=(RecyclerView)getActivity().findViewById(R.id.cook_rv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(manager);
        adapter=new CooksAdapter(getActivity());
        rv.setAdapter(adapter);

        adapter.setonitemclickListener(new CooksAdapter.onitemclickListener() {
            @Override
            public void onitemclick(tdetail t,int type) {
                String id=t.getId()+"";
                String iscook=t.getIscook()+"";
                String name=t.getFoodname();
                String s="&id="+id+"&iscook="+iscook+"&name="+name+"&cooker="+cooker+"&number="+t.getNumber();
                UpdateData(s);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                GetData();
                refreshlayout.finishRefresh(3000);
            }
        });
    }


    //从服务器获取数据
    public void GetData(){
        final String s="main=厨师订单详情";
        final String endurl="Myservlet/Search";
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
                        StringBuilder response=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();

                        list=new ArrayList<>();
                        Gson gson=new Gson();
                        list=gson.fromJson(response.toString(), new TypeToken<List<tdetail>>(){}.getType());

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=list;
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


    //更新炒菜的状态
    public void UpdateData(String str){
        final String s="main=炒菜状态"+str;
        final String endurl="Myservlet/Update";
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
                        StringBuilder response=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();


                        Message msg=new Message();
                        msg.what=100;
                        msg.obj=response;
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


    //修改厨师分数
    public void Updategrade(String str){
        final String s="main=修改厨师分数"+str;
        final String endurl="Myservlet/Update";
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
                        StringBuilder response=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }

                        in.close();
                        reader.close();
                        conn.disconnect();


                        Message msg=new Message();
                        msg.what=1314;
                        msg.obj=response;
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













}
