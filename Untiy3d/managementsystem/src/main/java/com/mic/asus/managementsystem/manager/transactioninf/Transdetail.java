package com.mic.asus.managementsystem.manager.transactioninf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;

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
import java.util.List;

public class Transdetail extends Activity {
    boolean ok=false;//表示所有的菜已经全部炒完
    Intent intent;
    transaction t;
    String id;
    String totalprice;
    Button btn_check;
    RecyclerView rv;
    TextView tv_back;
    TextView tv_id,tv_totalprice;
    List<tdetail> list;
    TransdetailAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){//获取数据
                adapter.AddData((List<tdetail>)msg.obj);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_transdetail);
        intent=getIntent();
        Bundle bundle=intent.getExtras();
//        id=bundle.getString("id");
//        totalprice=bundle.getString("totalprice");
        t= (transaction) bundle.getSerializable("transaction");
        Log.d("msg","到了这里!\n");
        Init();
        Getdata(t.getId()+"");
    }

    //初始化
    public void Init(){
        rv=(RecyclerView)findViewById(R.id.transdetail_rv);
        tv_id=(TextView)findViewById(R.id.transdetail_tv_id1);
        tv_totalprice=(TextView)findViewById(R.id.transdetail_tv_price1);
        tv_back=(TextView)findViewById(R.id.Transdetail_tv_back);
        tv_id.setText(t.getId()+"");
        tv_totalprice.setText(t.getTotalprice()+"");
        btn_check=(Button)findViewById(R.id.btn_check);
        if(t.getIscheck()==1)
            btn_check.setText("已经结过账了");
        final RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(manager);
        adapter=new TransdetailAdapter(this);
        rv.setAdapter(adapter);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Transdetail_tv_back:
                        finish();
                        break;
                    case R.id.btn_check:
                        if(t.getIscheck()==0){
                            Judgeok();
                            if(ok)
                                Check();
                            else
                                Toast.makeText(getApplicationContext(),"还有菜未炒完！",Toast.LENGTH_SHORT).show();
                        }

                        else
                            Toast.makeText(getApplicationContext(),"已经结过账了！",Toast.LENGTH_SHORT).show();
                        break;
                        default:
                            finish();
                            break;
                }
            }
        };
        tv_back.setOnClickListener(listener);
        btn_check.setOnClickListener(listener);
    }

    //从服务器获取数据
    public void Getdata(String id){
        final String s="main=订单详情&id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(DBhelper.mylocalhost+"Myservlet/Search");
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

                        Gson gson=new Gson();
                        list=gson.fromJson(response.toString(),new TypeToken<List<tdetail>>(){}.getType());


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

    //结账
    public void Check(){
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.alterdialog,null);

        TextView tv_ordertime=(TextView)view.findViewById(R.id.alter_ordertime);
        TextView tv_id=(TextView)view.findViewById(R.id.alter_id);
        TextView tv_deskid=(TextView)view.findViewById(R.id.alter_deskid);
        TextView tv_people=(TextView)view.findViewById(R.id.alter_people);
        TextView tv_totalprice=(TextView)view.findViewById(R.id.alter_totalprice);
        TextView tv_waiter=(TextView)view.findViewById(R.id.alter_waiter);
        tv_ordertime.setText(t.getOrdertime());
        tv_id.setText(t.getId()+"");
        tv_deskid.setText(t.getDeskid()+"");
        tv_people.setText(t.getPeople()+"");
        tv_totalprice.setText(t.getTotalprice()+"");
        tv_waiter.setText(t.getWaiter()+"");

        new AlertDialog.Builder(Transdetail.this)
                .setTitle("结账")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        intent=getIntent();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("id", t.getId()+"");
//                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }


    //判断是否所有菜都已经炒完
    public void Judgeok(){
        ok=true;
        for(tdetail td:list){
            if(td.getIscook()!=2) {
                ok=false;
                break;
            }
        }
    }
}
