package com.mic.asus.managementsystem.manager.foodmenu;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.manager.employeeinf.Empinfdetail;
import com.mic.asus.managementsystem.manager.employeeinf.Employeeinf;
import com.mic.asus.managementsystem.manager.employeeinf.empinf;

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

public class foodinf extends Activity {
    ListView lv;
    SimpleAdapter adapter;
    TextView addbtn;
    TextView backbtn;
    String [] from={"id","name","price","hot","rest","type"};
    int [] to={R.id.food_three_id,R.id.food_three_name,R.id.food_three_price,R.id.food_three_hot,
            R.id.food_three_rest,R.id.food_three_type};
    List<food> list=new ArrayList<>();
    HashMap<String,String> map=new HashMap<>();
    HashMap<String,String> response;
    List<HashMap<String,String>> maps=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                lv.setAdapter((SimpleAdapter)msg.obj);
            }
            else if(msg.what==100){
                response=DBhelper.transform(msg.obj.toString());
                Toast.makeText(foodinf.this,""+response.get("Msg"),Toast.LENGTH_SHORT).show();
//                onCreate(null);
                getdata();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_foodinf);
        lv=(ListView) findViewById(R.id.foodinf_lv);
        backbtn=(TextView) findViewById(R.id.foodinf_tv_back);
        addbtn=(TextView) findViewById(R.id.foodinf_tv_add);
        registerForContextMenu(lv);
        clickbutton();
        getdata();
        detail();
    }

    //详细信息
    public void detail(){
        maps=new ArrayList<>();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> inf=maps.get(position);
                Intent intent=new Intent(foodinf.this,Foodinfdetail.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",inf.get("id"));
                bundle.putString("name",inf.get("name"));
                bundle.putString("price",inf.get("price"));
                bundle.putString("type",inf.get("type"));
                bundle.putString("hot",inf.get("hot"));
                bundle.putString("rest",inf.get("rest"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //按钮
    public void clickbutton(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.foodinf_tv_back:
                        finish();
                        break;
                    case R.id.foodinf_tv_add:
                        addfood();
                        break;
                        default:
                            break;
                }
            }
        };
        backbtn.setOnClickListener(listener);
        addbtn.setOnClickListener(listener);
    }

    //从数据库获取数据
    public void getdata(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s="main=菜谱";
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Search");
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


                        Gson gson=new Gson();
                        list=gson.fromJson(response+"",new TypeToken<List<food>>(){}.getType());
                        maps=new ArrayList<>();
                        for(food e: list){
                            map=new HashMap<>();
                            map.put("id",e.getId()+"");
                            map.put("name",e.getName());
                            map.put("price",e.price+"");
                            map.put("hot",e.getHot()+"");
                            map.put("rest",e.getRest()+"");
                            map.put("type",e.getType());
                            maps.add(map);
                        }
                        adapter=new SimpleAdapter(foodinf.this,
                                maps, R.layout.food,from,to
                        );

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

    //添加菜
    public void addfood(){
        Intent intent=new Intent(foodinf.this,Addfood.class);
        startActivityForResult(intent,200);
    }

    //传递数据
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

    //修改函数
    public void update(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String name=((TextView)info.targetView.findViewById(R.id.food_three_name)).getText().toString();
        String price=((TextView)info.targetView.findViewById(R.id.food_three_price)).getText().toString();
        String type=((TextView)info.targetView.findViewById(R.id.food_three_type)).getText().toString();
        String hot=((TextView)info.targetView.findViewById(R.id.food_three_hot)).getText().toString();
        String rest=((TextView)info.targetView.findViewById(R.id.food_three_rest)).getText().toString();
        Intent intent=new Intent(foodinf.this,Foodinf_update.class);
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putString("price",price);
        bundle.putString("type",type);
        bundle.putString("hot",hot);
        bundle.putString("rest",rest);
        intent.putExtras(bundle);
        startActivityForResult(intent,100);
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

    //删除函数
    public void delete(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final String name=((TextView)info.targetView.findViewById(R.id.food_three_name)).getText().toString();
        new AlertDialog.Builder(this).setTitle("删除:" + name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Deletehttpurl(name);
                    }
                })
                .setNegativeButton("取消", null) .show();
    }

    //删除数据
    public void Deletehttpurl(String name){
        final String s="main=菜谱"+"&name="+name;
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                delete(item);
                return true;
            case R.id.update:
                update(item);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==200){
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                String s=bundle.getString("s");
                Addhttpurl(s);
            }
        }
        else if (requestCode==100){
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                String s=bundle.getString("s");
                Updatehttpurl(s);
            }
        }


    }

}
