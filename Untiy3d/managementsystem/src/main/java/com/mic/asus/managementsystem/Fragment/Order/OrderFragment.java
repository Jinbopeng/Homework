package com.mic.asus.managementsystem.Fragment.Order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.employee.empfoodmenu.Cartfood;
import com.mic.asus.managementsystem.employee.empfoodmenu.Empfood;
import com.mic.asus.managementsystem.employee.empfoodmenu.Orderinf;
import com.mic.asus.managementsystem.employee.empfoodmenu.Rightfood;
import com.mic.asus.managementsystem.employee.empfoodmenu.ShoppingCart;
import com.mic.asus.managementsystem.manager.deskinf.desk;
import com.mic.asus.managementsystem.manager.foodmenu.Addfood;

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
import java.util.Map;
import java.util.regex.Pattern;


public class OrderFragment extends Fragment {
    RecyclerView rv;
    EditText et_num;
    Button search;
    DesksAdapter adapter;
    String username;
    List<desk> desks=new ArrayList<>();
    HashMap<String,String> result;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==520){
                adapter.Adddesks((List<desk>)msg.obj);
            }
            else if(msg.what==1314){
                result=DBhelper.transform(msg.obj.toString());
                if(result.get("resCode").equals("100"))
                    onActivityCreated(null);
                else
                    Toast.makeText(getActivity(),""+result.get("Msg"),Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.order_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onActivityCreated(savedInstanceState);
        Init();
        Getdata();
    }

    //初始化
    public void Init(){
        rv= (RecyclerView) getActivity().findViewById(R.id.order_rv);
        et_num=(EditText) getActivity().findViewById(R.id.people_et);
        search=(Button)getActivity().findViewById(R.id.people_btn);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(manager);
        adapter=new DesksAdapter(getActivity());
        rv.setAdapter(adapter);
        //获取当前服务员的信息
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("empLogin", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("name","");


        adapter.setonItemClickListener(new DesksAdapter.OnitemclickListener() {
            @Override
            public void onitemClick(View v, desk d) {
                Intent intent=new Intent(getActivity(), Empfood.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("desk",d);
                intent.putExtras(bundle);
                startActivityForResult(intent,200);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=et_num.getText().toString();
                if(!str.equals("")) {
                    int num = Integer.parseInt(str);
                    if (num > 0) {
                        String s = "main=条件餐桌" + "&people=" + num;
                        SearchGetdata(s);
                    } else
                        Toast.makeText(getActivity(), "请输入正确数字!", Toast.LENGTH_SHORT).show();
                }
                    else{
                    Toast.makeText(getActivity(), "请输入正确数字!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    //初始化时给adapter填充数据
    public void Getdata(){
        final String s="main=餐桌";
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

                        desks=new ArrayList<>();
                        Gson gson=new Gson();
                        desks=gson.fromJson(response.toString(), new TypeToken<List<desk>>(){}.getType());

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=desks;
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

    //查询后给adapter填充数据
    public void SearchGetdata(final String s){
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

                        desks=new ArrayList<>();
                        Gson gson=new Gson();
                        desks=gson.fromJson(response.toString(), new TypeToken<List<desk>>(){}.getType());

                        Message msg=new Message();
                        msg.what=520;
                        msg.obj=desks;
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

    //判断字符串是否为整数
    public boolean IsInteger(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches()&&Integer.parseInt(str)>0;
    }

    //添加订单信息（要记得更改开桌后的餐桌信息）
    public void Addtransinf(final String s){
        final String endurl="Myservlet/Addinf";
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        Log.d("msg","FragmentB的onActivityresult中"+"request="+requestCode+";result="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == getActivity().RESULT_OK) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString("cart");
//                Gson gson = new Gson();
//                List<Cartfood> cart = gson.fromJson(str, new TypeToken<List<Cartfood>>() {}.getType());
                String waiter = username;
                int deskid=bundle.getInt("deskid");
                int people=bundle.getInt("people");
                int totalprice=bundle.getInt("totalprice");
                String s="main=订单"+"&people="+people+"&deskid="+deskid+"&waiter="+waiter+"&totalprice="+totalprice+"&cart="+str;
                Addtransinf(s);
            }
            else if(resultCode==200){
                onActivityCreated(null);
            }
        }
    }

}
