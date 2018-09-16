package com.mic.asus.managementsystem.manager.deskinf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Deskinf extends Activity {
    ListView lv;
    TextView addinf;
    TextView back;
    SimpleAdapter adapter;
    String [] from={"id","capacity","occupy","isempty","isclean"};
    int [] to={R.id.desk_three_id,R.id.desk_three_capacity,R.id.desk_three_occupy,R.id.desk_three_isempty,R.id.desk_three_isclean};
    List<desk> list=new ArrayList<>();
    HashMap<String,String> hashMap=new HashMap<>();
    HashMap<String,String> response;
    List<HashMap<String,String>> maps=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                lv.setAdapter((SimpleAdapter)msg.obj);
            }
            if(msg.what==100){
                response=DBhelper.transform(msg.obj.toString());
                Toast.makeText(Deskinf.this,""+response.get("Msg"),Toast.LENGTH_SHORT).show();
//                onCreate(null);
                getData();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_deskinf);
        lv=(ListView)findViewById(R.id.deskinf_lv);
        addinf=(TextView)findViewById(R.id.deskinf_tv_add);
        back=(TextView)findViewById(R.id.deskinf_tv_back);
        buttonclick();
        getData();
        registerForContextMenu(lv);
    }


    //点击事件
    public void buttonclick(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.deskinf_tv_back:
                        finish();
                        break;
                    case R.id.deskinf_tv_add:
                        AddData();
                        break;
                }
            }
        };
        back.setOnClickListener(listener);
        addinf.setOnClickListener(listener);
    }

    //从服务器获取数据
    public void getData(){
        final String endurl="Myservlet/Search";
        final String s="main=餐桌";
//        String response;
//        response=DBhelper.myhttpPost(endurl,s);
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

                        maps=new ArrayList<>();
                        Gson gson=new Gson();
                        list=gson.fromJson(response.toString(), new TypeToken<List<desk>>(){}.getType());
                        for(desk d:list){
                            hashMap=new HashMap<String,String>();
                            String isempty="是";
                            String isclean="是";
                            if(d.getIsempty()==0)       //不为空
                                isempty="否";
                            if(d.getIsclean()==0)       //未打扫
                                isclean="否";
                            hashMap.put("id",d.getId()+"");
                            hashMap.put("capacity",d.capacity+"");
                            hashMap.put("occupy",d.getOccupy()+"");
                            hashMap.put("isempty",isempty);
                            hashMap.put("isclean",isclean);
                            maps.add(hashMap);
                        }
                        adapter=new SimpleAdapter(Deskinf.this,maps,R.layout.desk,from,to);

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=adapter;
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

    //添加数据
    public void AddData(){
        Intent intent=new Intent(Deskinf.this,Deskinf_add.class);
        startActivityForResult(intent,200);
    }

    //添加数据
    public void Addhttpurl(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Addinf");
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

    //修改数据
    public void Updatehttpurl(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Update");
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

    //修改函数
    public void update(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String id=((TextView)info.targetView.findViewById(R.id.desk_three_id)).getText().toString();
        String capacity=((TextView)info.targetView.findViewById(R.id.desk_three_capacity)).getText().toString();
        Intent intent=new Intent(Deskinf.this,Deskinf_update.class);
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("capacity",capacity);
        intent.putExtras(bundle);
        startActivityForResult(intent,100);
    }

    //删除函数
    public void delete(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final String id=((TextView)info.targetView.findViewById(R.id.desk_three_id)).getText().toString();
        new AlertDialog.Builder(this).setTitle("删除:" + id)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Deletehttpurl(id);
                    }
                })
                .setNegativeButton("取消", null) .show();
    }

    //删除数据
    public void Deletehttpurl(String id){
        final String s="main=餐桌&"+"id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Delete");
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


    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("操作");
        getMenuInflater().inflate(R.menu.manage,menu);
    }

    //上下文菜单的点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        boolean flag=true;
        String isempty=((TextView)info.targetView.findViewById(R.id.desk_three_isempty)).getText().toString();
        if(isempty.equals("否"))
            flag=false;
        switch (item.getItemId()){
            case R.id.delete:
                if(flag)
                    delete(item);
                else
                    Toast.makeText(Deskinf.this,"正在用餐不能删除",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.update:
                if(flag)
                    update(item);
                else
                    Toast.makeText(Deskinf.this,"正在用餐不能修改",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==200){//添加
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                String s=bundle.getString("s");
                Addhttpurl(s);
            }
        }
        else if(requestCode==100){//修改
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                String s=bundle.getString("s");
                Updatehttpurl(s);
            }
        }

    }


}
