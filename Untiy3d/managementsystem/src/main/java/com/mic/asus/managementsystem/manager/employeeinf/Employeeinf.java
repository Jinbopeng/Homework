package com.mic.asus.managementsystem.manager.employeeinf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employeeinf extends Activity {


    String [] from={"id","name","age","sex","position","grade","salary"};
    int [] to={R.id.emp_threee_id,R.id.emp_three_name,R.id.emp_three_age,R.id.emp_three_sex,
            R.id.emp_three_position,R.id.emp_three_grade,R.id.emp_three_salary};
    ListView lv;
    SimpleAdapter adapter;
    List<empinf> list;
    TextView back;
    List<HashMap<String,String>> maps;
    HashMap<String,String> hashMap=new HashMap<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){//导入数据
                lv.setAdapter((SimpleAdapter)msg.obj);
            }
            else if(msg.what==100){//删除
                hashMap=transform(msg.obj.toString());
                getdata();
            }
            else if(msg.what==101){//修改
                getdata();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_employeeinf);
        Init();
    }

    //初始化函数
    public void Init(){
        back=(TextView)findViewById(R.id.empinf_tv_back);
        lv=(ListView)findViewById(R.id.empinf_lv);
        maps=new ArrayList<HashMap<String, String>>();
        getdata();
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> inf=maps.get(position);
                Intent intent=new Intent(Employeeinf.this,Empinfdetail.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",inf.get("id"));
                bundle.putString("name",inf.get("name"));
                bundle.putString("age",inf.get("age"));
                bundle.putString("sex",inf.get("sex"));
                bundle.putString("grade",inf.get("grade"));
                bundle.putString("position",inf.get("position"));
                bundle.putString("salary",inf.get("salary"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //获取数据
    public void getdata(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s="main=职工";
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
                        list=gson.fromJson(response+"",new TypeToken<List<empinf>>(){}.getType());
                        for(empinf e: list){
                            HashMap<String,String> map=new HashMap<>();
                            map.put("id",e.getId()+"");
                            map.put("name",e.getName());
                            map.put("age",e.getAge()+"");
                            map.put("sex",e.getSex());
                            map.put("position",e.getPosition());
                            map.put("grade",e.getGrade()+"");
                            map.put("salary",e.getSalary()+"");
                            maps.add(map);
                        }
                            adapter=new SimpleAdapter(Employeeinf.this,
                                maps, R.layout.empinf,from,to
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


    //删除函数(未考虑如果订单中有该服务员的信息怎么办)
    public void delete(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final String name=((TextView)info.targetView.findViewById(R.id.emp_three_name)).getText().toString();
        Log.d("msg",name);
            new AlertDialog.Builder(this).setTitle("删除:" + name)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteurl(name);
                            Toast.makeText(getApplicationContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", null) .show();
    }


    public void deleteurl(String name){
        final String s="main=删除员工信息&name="+name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL("http://192.168.1.106:8080/Myservlet/Delete");
//                    URL url=new URL("https://www.baidu.com");
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
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Intent intent =new Intent(Employeeinf.this, empinf_update.class);
        Bundle bundle=new Bundle();
        bundle.putString("name",((TextView)info.targetView.findViewById(R.id.emp_three_name)).getText().toString());
        bundle.putString("age",((TextView)info.targetView.findViewById(R.id.emp_three_age)).getText().toString());
        bundle.putString("grade",((TextView)info.targetView.findViewById(R.id.emp_three_grade)).getText().toString());
        bundle.putString("salary",((TextView)info.targetView.findViewById(R.id.emp_three_salary)).getText().toString());

        intent.putExtras(bundle);
        startActivityForResult(intent, 200);
    }

    public void updateurl(final String s){
        Log.d("msg","操蛋生活");
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
                        msg.what=101;
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
                Bundle b=data.getExtras();
                String s=b.getString("s");
                updateurl(s);
            }
        }
    }


}
