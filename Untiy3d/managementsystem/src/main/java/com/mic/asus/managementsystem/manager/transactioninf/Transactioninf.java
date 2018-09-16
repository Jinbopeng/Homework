package com.mic.asus.managementsystem.manager.transactioninf;

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
import com.mic.asus.managementsystem.manager.deskinf.Deskinf;
import com.mic.asus.managementsystem.manager.deskinf.desk;

import org.w3c.dom.Text;

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

public class Transactioninf extends Activity {
    ListView lv;
    TextView tv_back;
    String [] from={"id","ordertime","deskid","ischeck","waiter","totalprice","people"};
    int [] to={R.id.trans_three_id,R.id.trans_three_time,R.id.trans_three_deskid,R.id.trans_three_check,R.id.trans_three_waiter,R.id.trans_three_price,R.id.trans_three_people};
    SimpleAdapter adapter;
    HashMap<String,String> hashMap=new HashMap<>();
    List<transaction> list=new ArrayList<>();
    List<HashMap<String,String>> maps=new ArrayList<>();//存放服务器返回的数据
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){//获取数据
                lv.setAdapter((SimpleAdapter)msg.obj);
            }
            else if(msg.what==100){//删除数据
                HashMap<String,String> map=DBhelper.transform(msg.obj.toString());
                if(!map.get("resCode").equals("100"))
                    Toast.makeText(getApplicationContext(),"结账的后更新数据失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_transactioninf);
        Init();
        getdata();
    }

    //从服务器获取数据
    public void getdata(){
        final String endurl="Myservlet/Search";
        final String s="main=订单";

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
                        list=gson.fromJson(response.toString(), new TypeToken<List<transaction>>(){}.getType());
                        for(transaction d:list){
                            String ischeck="否";
                            if(d.getIscheck()==1)
                                ischeck="是";
                            hashMap=new HashMap<String,String>();
                            hashMap.put("id",d.getId()+"");
                            hashMap.put("ordertime",d.getOrdertime()+"");
                            hashMap.put("people",d.getPeople()+"");
                            hashMap.put("deskid",d.getDeskid()+"");
                            hashMap.put("ischeck",ischeck);
                            hashMap.put("waiter",d.getWaiter()+"");
                            hashMap.put("totalprice",d.getTotalprice()+"");
                            maps.add(hashMap);
                        }
                        adapter=new SimpleAdapter(Transactioninf.this,maps,R.layout.transaction,from,to);

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

    //初始化
    public void Init(){
        lv=(ListView)findViewById(R.id.Transinf_lv);
        tv_back=(TextView)findViewById(R.id.Transinf_tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> inf=maps.get(position);
                transaction t=new transaction();
                Intent intent=new Intent(Transactioninf.this,Transdetail.class);
                Bundle bundle=new Bundle();
//                bundle.putString("id",inf.get("id")+"");
//                bundle.putString("totalprice",inf.get("totalprice")+"");
                t.setDeskid(Integer.parseInt(inf.get("deskid")));
                t.setId(Integer.parseInt(inf.get("id")));
                if(inf.get("ischeck").equals("是"))
                    t.setIscheck(1);
                else
                    t.setIscheck(0);
//                t.setIscheck(Integer.parseInt(inf.get("ischeck")));
                t.setOrdertime(inf.get("ordertime"));
                t.setPeople(Integer.parseInt(inf.get("people")));
                t.setWaiter(inf.get("waiter"));
                t.setTotalprice(Integer.parseInt(inf.get("totalprice")));
                bundle.putSerializable("transaction",t);
                intent.putExtras(bundle);
                startActivityForResult(intent,200);
            }
        });
    }



    //删除函数
    public void delete(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String ischeck=((TextView) info.targetView.findViewById(R.id.trans_three_check)).getText().toString();
        final String id=((TextView)info.targetView.findViewById(R.id.trans_three_id)).getText().toString();
        Log.d("msg",id);
        if(ischeck.equals("是")) {
            new AlertDialog.Builder(this).setTitle("删除:" + id)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteurl(id);
                            Toast.makeText(getApplicationContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
        else
            Toast.makeText(getApplicationContext(), "还未结账，不能删除!", Toast.LENGTH_SHORT).show();
    }

    //与服务器连接进行删除
    public void deleteurl(String id){
        final String s="main=订单&id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Delete");
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

    //将服务器中的相应订单结账
    public void checkurl(String id){
        final String s="main=订单&id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Update");
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

    //更改餐桌,服务员信息（座位占用，清洁,服务员的分数等）
    public void updatedesk(String str){
        final String s="main=结账餐桌"+str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Update");
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

    //更改服务员的分数
    public void updatewaiter(String str){

    }


    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("操作");
        getMenuInflater().inflate(R.menu.mange_single,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.singdelete:
                delete(item);
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                transaction t= (transaction) bundle.getSerializable("transaction");
                String id=t.getId()+"";
                String deskid=t.getDeskid()+"";
                String waiter=t.getWaiter();
                String people=t.getPeople()+"";
                String totalprice=t.getTotalprice()+"";
                //订单结账
                checkurl(id);
                //更改餐桌,服务员的信息
                String str1="&deskid="+deskid+"&people="+people+"&waiter="+waiter+"&totalprice="+totalprice;
                updatedesk(str1);
                checkurl(id);
                getdata();
            }
        }
    }
}
