package com.mic.asus.managementsystem.employee.empfoodmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mic.asus.managementsystem.DBhelper;
import com.mic.asus.managementsystem.R;
import com.mic.asus.managementsystem.employee.EmployeeMain;
import com.mic.asus.managementsystem.manager.deskinf.desk;
import com.mic.asus.managementsystem.manager.foodmenu.Addfood;
import com.mic.asus.managementsystem.manager.foodmenu.food;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class Empfood extends Activity implements AbsListView.OnScrollListener {
    desk d;
    String [] menu={"热菜","凉菜","烘焙","汤类","五谷杂粮","甜品小吃","酒水饮料"};
    TextView tv_back,tv_totalprice,tv_go;
    EditText et_people;
    List<food> foods;
    List<Caixi> caixis;
    Caixi caixi;
    List<Rightfood> rightfoods;
    List<Rightfood> shopingcart;
    List<Cartfood> cartfoods;//这是一个不得已的办法
    Rightfood rightfood;
    RecyclerView rv;
    MenuAdapter adapter;
    boolean isScroll=false;
    StickyListHeadersListView sticklist;
    stickAdapter stickAdapter;
    ImageView cart;
    QBadgeView bv;
    int number=0;
    ShoppingCart mycart;//购物车


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            caixis=new ArrayList<>();
            if(msg.what==520){
                stickAdapter.AddData(rightfoods);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_empfood);
        Init();
        GetData();
    }

    //初始化
    public void Init(){
        //获取组件
        tv_totalprice=(TextView)findViewById(R.id.total_price);
        et_people=(EditText)findViewById(R.id.main_title_et);
        tv_go=(TextView)findViewById(R.id.gonow);//点击下单
        rv =(RecyclerView)findViewById(R.id.left_rv);
        sticklist=findViewById(R.id.right_sticklist);
        cart=findViewById(R.id.cart);
        bv=new QBadgeView(this);
        shopingcart=new ArrayList<>();
        mycart=new ShoppingCart();
        //设置badgeview
        bv.bindTarget(cart);
        bv.setBadgeBackgroundColor(getResources().getColor(R.color.colorAccent));
        bv.setBadgeTextColor(getResources().getColor(R.color.white));
        bv.setBadgeTextSize(20,true);
        bv.setBadgePadding(3,true);

        //recycview的配置以及返回按钮
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(manager);
        tv_back=(TextView)findViewById(R.id.main_exit);

        //适配器的点击事件
        adapter=new MenuAdapter(this);
        adapter.setonitemclickListener(new MenuAdapter.onitemclickListener() {
            @Override
            public void onitemclick(int position,Caixi caixi) {
                sticklist.setSelection(caixi.getGroupindex());
                isScroll=false;
            }
        });
        stickAdapter=new stickAdapter(this);
        rv.setAdapter(adapter);
        sticklist.setAdapter(stickAdapter);
        sticklist.setOnScrollListener(this);
        stickAdapter.setonitemclickListener(new stickAdapter.onitemclickListener() {
            @Override
            public void onitemclick(Rightfood food, int type) {
                //type:0表示加号按钮按下，1表示减号按钮按下
                switch (type){
                    case 0:
                        number++;
                        bv.setBadgeNumber(number);
                        mycart.Addfood(food);
                        Log.d("msg",mycart.show());
                        break;
                    case 1:
                        number--;
                        bv.setBadgeNumber(number);
                        mycart.Subfood(food);
                        Log.d("msg",mycart.show());
                        break;
                }
                tv_totalprice.setText(mycart.getCartprice()+"");
            }
        });

        //将左边的菜单栏设置数据源
        caixis=new ArrayList<>();
        for(int i=0;i<7;i++){
            caixi=new Caixi();
            caixi.setId(i);
            caixi.setTitle(menu[i]);
            caixis.add(caixi);
        }
        adapter.AddData(caixis);

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.main_exit:
                        setResult(200,null);
                        finish();
                        break;
                    case R.id.gonow:
                        Goorder();
                        break;
                }
            }
        };
        tv_back.setOnClickListener(listener);
        tv_go.setOnClickListener(listener);

    }

    //获取菜谱信息
    public void GetData(){
        final String s="main=条件菜谱";
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

                        foods=new ArrayList<>();
                        Gson gson=new Gson();
                        foods=gson.fromJson(response.toString(), new TypeToken<List<food>>(){}.getType());
                        Dealwith();

                        Message msg=new Message();
                        msg.what=520;
//                        msg.obj=foods;
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

    //处理返回的数据
    public void Dealwith(){
        rightfoods=new ArrayList<>();
        Caixi c;
        for(int i=0;i<7;i++){
            c=caixis.get(i);
            for(int j=0;j<foods.size();j++){
                if(j==0) {
                    c.setGroupindex(rightfoods.size());
                    Log.d("msg","\n"+rightfoods.size());
                }
                if(foods.get(j).getType().equals(c.getTitle())) {
                    rightfood = new Rightfood();
                    rightfood.setHead_id(i);
                    rightfood.setName(foods.get(j).getName());
                    rightfood.setPrice(foods.get(j).getPrice());
                    rightfood.setHot(foods.get(j).getHot());
                    rightfood.setRest(foods.get(j).getRest());
                    rightfood.setType(foods.get(j).getType());
                    rightfood.setNumber(0);
                    rightfoods.add(rightfood);
                }
            }
        }

    }


    public String  Dealwith2(){
        cartfoods=new ArrayList<>();
        Cartfood cartfood;
        String str="";
        for(Rightfood f:mycart.getCartsingle().keySet()){
            cartfood=new Cartfood();
            cartfood.setName(f.getName());
            cartfood.setRest(f.getRest());
            cartfood.setHot(f.getHot());
            cartfood.setType(f.getType());
            cartfood.setNumber(mycart.getCartsingle().get(f));
            cartfoods.add(cartfood);
        }
        Gson gson=new Gson();
        str=gson.toJson(cartfoods);
        return str;
    }




    //下单
    public void Goorder(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        d=(com.mic.asus.managementsystem.manager.deskinf.desk) bundle.getSerializable("desk");
        int rest=d.getCapacity()-d.getOccupy();
        String s=et_people.getText().toString();
        if(mycart.getCartnumber()!=0) {
            if (!s.equals("")) {
                int people = Integer.parseInt(et_people.getText().toString());
                if (people > rest)
                    Toast.makeText(this, "人数超过了!", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "下单成功!", Toast.LENGTH_SHORT).show();
                    //接受传过来的餐桌信息
                    Bundle bundle1 = new Bundle();
                    String cart = Dealwith2();
                    Log.d("msg",  cart+ "\n");
                    bundle1.putString("cart", cart);
                    bundle1.putInt("deskid", d.getId());
                    bundle1.putInt("people", people);
                    bundle1.putInt("totalprice",mycart.getCartprice());
                    intent.putExtras(bundle1);
                    this.setResult(RESULT_OK, intent);
                    finish();
                }
            } else
                Toast.makeText(this, "请输入用餐人数!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "请选择菜!", Toast.LENGTH_SHORT).show();

    }







    //右边sticklist的滑动
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        isScroll=true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(isScroll) {
            Rightfood rightfood = rightfoods.get(firstVisibleItem);
            adapter.setSelectposition(rightfood.getHead_id());
        }
    }
}
