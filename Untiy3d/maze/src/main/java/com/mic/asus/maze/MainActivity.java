package com.mic.asus.maze;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MainActivity extends Activity {

    boolean flag=true;
    int max = 10;
    int[][] d = { { 0, 0 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } }; // 方向的数组：下左上右
    int[][] fd= {{0,0},{1,0},{0,1},{-1,0},{0,-1}};			//下右上左
    int [] reverse= {0,3,4,1,2};
    int[][] maze; // 0表示墙，1表示通路
    int cur_x;
    int cur_y;
    int new_x;
    int new_y;
    //dfs寻路函数的变量
    int findend_x;
    int findend_y;
    int findcur_x;
    int findcur_y;
    ArrayList<Wall> list;                           //存放在列表中的walls
    List<Road> Findlist;                            //存放出路的list
    //dfs的迷宫生成函数变量
    List<Road> Dfslist;								//相当于存放已访问迷宫单元的栈
    List<Road> Nebour;							    //存放相邻的迷宫单元
    int wall_x,wall_y;								//墙的坐标

    private EditText num;
    private Button change;
    private Button find;
    private TextView back;
    private RadioGroup type_rg;
    private RadioButton type_rb;
//    private RadioButton rb_dfs;
    private RecyclerView rvlist;
    private CubeListAdapter adapter;
    //private List<Cube> cubes=new ArrayList<Cube>();
    private List<Cube> cubes;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        change=(Button)findViewById(R.id.btn_change);
        find=(Button)findViewById(R.id.btn_start);
        back=(TextView)findViewById(R.id.back);
        type_rg=(RadioGroup)findViewById(R.id.type_gp);
        num=(EditText)findViewById(R.id.et_num);


        ChickEvent();
        //recycview的初始化
        rvlist=findViewById(R.id.center_show);
        layoutManager=new GridLayoutManager(this,max*2+1);
        rvlist.setLayoutManager(layoutManager);
        adapter=new CubeListAdapter(this,layoutManager);
        //产生初始化的迷宫
        Produce();
        GetData();
        rvlist.setAdapter(adapter);
    }

    //点击事件
    public void ChickEvent(){

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_change: {
                        Change();
                        break;
                    }
                    case R.id.btn_start: {
                        Find();
                        break;
                    }
                    case R.id.back:
                    {
                        finish();
                        break;
                    }
                        default:
                            break;
                }
            }
        };
        change.setOnClickListener(listener);
        find.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }
    //
    public void Done(int m){
        /*layoutManager=new GridLayoutManager(this,m*2+1);
        rvlist.setLayoutManager(layoutManager);
        adapter=new CubeListAdapter(this,layoutManager);*/
        ((GridLayoutManager)layoutManager).setSpanCount(2*m+1);
        Produce();
        GetData();
//        ((GridLayoutManager)layoutManager).;
        rvlist.setAdapter(adapter);
    }


    //更改迷宫的函数
    public void Change(){
        String s;
        int number=0;
        s=num.getText().toString();
        if(s.equals(""))
            Toast.makeText(getApplicationContext(), "请输入行数:", Toast.LENGTH_SHORT).show();
        else{
                number = Integer.parseInt(s);
                if (number == 0)
                    Toast.makeText(MainActivity.this, "请输入行数:", Toast.LENGTH_SHORT).show();
                else if (number >= 3 && number <= 50) {
                    flag=true;
                    max = number;
                    ((GridLayoutManager)layoutManager).setSpanCount(2*max+1);
                    type_rb=(RadioButton)findViewById(type_rg.getCheckedRadioButtonId());
                    if("Prim算法".equals(type_rb.getText())) {
                        Produce();
                        GetData();
                    }
                    else
                    {
                        DfsGo();
                        GetData();
                    }
                }
                else
                    Toast.makeText(this, "请输入3-50之间的数字", Toast.LENGTH_SHORT).show();
            }
    }


    //迷宫寻路的函数
    public void Find(){
        //路线已经生成并且还未寻路
        if(flag) {
            FindGo();
            GetData();
            flag=false;
        }
        else
            Toast.makeText(this,"路线已经出来了，老哥！",Toast.LENGTH_SHORT).show();
    }

    //初始化
    public void MyInit(){
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,10);
        rvlist.setLayoutManager(layoutManager);
    }

    //适配器重新获取数据
    public void GetData(){
        Cube cube;
        for(int i=0;i<=max;i++)
            for(int j=0;j<=max;j++) {
                cube = new Cube(maze[i][j]);
                cubes.add(cube);
            }
        adapter.AddCubes(cubes);
        cubes.clear();
    }

    //生成pri迷宫
    public void Produce(){
        init();
        Go();
    }

    // 数据初始化
    public void init() {
        max=2*max;
        maze = new int[max+1][max+1];				//默认全部为0
        cubes=new ArrayList<Cube>();
        list = new ArrayList<Wall>();
        // 起始点的位置
        new_x = 1;
        new_y = 1;
        maze[1][1]= 1;
        maze[1][0]=1;
        Add(); // 将起始点的临墙加入list列表
    }

    // 添加墙的Cube对象的函数,并返回添加的个数;
    public int Add() {
        //new_x,new_y指的是当前的这个通路格子
        int cnt = 0;
        Wall wall;


        // 下面   横墙
        if (new_x + 1 <max&&maze[new_x+1][new_y]==0) {
            wall = new Wall(new_x + 1, new_y, 1);
            //判断是否能够加进去
            if (Judge(wall)) {
                list.add(wall);
                cnt++;
            }
        }

        // 上面	横墙
        if (new_x - 1 >= 1&&maze[new_x-1][new_y]==0) {
            wall = new Wall(new_x - 1, new_y, 3);
            if (Judge(wall)) {
                list.add(wall);
                cnt++;
            }
        }

        // 左边	竖墙
        if (new_y - 1 >= 1&&maze[new_x][new_y-1]==0) {
            wall = new Wall(new_x, new_y - 1, 2);
            if (Judge(wall)) {
                list.add(wall);
                cnt++;
            }
        }

        // 右边	竖墙
        if (new_y + 1<max&&maze[new_x][new_y+1]==0) {
            wall = new Wall(new_x, new_y + 1, 4);
            if (Judge(wall)) {
                list.add(wall);
                cnt++;
            }
        }
        return cnt;
    }

    //判断Cube是否满足条件加进去
    public boolean Judge(Wall w) {
        //判断要加进去的Cube是否在list中
        for (Wall a : list) {
            if (a.equal(w))
                return false;
        }
        return true;
    }

    //选择判断墙是否分隔了通路
    public boolean IsDepart(Wall w) {
        new_x=cur_x+d[w.getDirection()][0];
        new_y=cur_y+d[w.getDirection()][1];
        if(new_x<max&&new_x>=1&&new_y<max&&new_y>=1) {
            //分隔了,将墙打通，将方格设为通路
            if(maze[new_x][new_y]==0) {
                maze[cur_x][cur_y]=1;
                maze[new_x][new_y]=1;
                return true;
            }
            else
                return false;
        }
        return false;
    }

    // 执行创建过程函数
    public void Go() {
        while(list.size()!=0) {
            //list列表中随机选一道墙
            double test=Math.random();
            int random=(int)(test*list.size());
            //			System.out.println(random+"----"+list.size()+"----"+test);
            //			System.out.println(list+"");
            Wall w=list.get(random);
            //当前的墙的坐标
            cur_x=w.getX();
            cur_y=w.getY();
			/*
			//与该墙相隔的方块坐标
			new_x=cur_x+d[c.getDirection()][0];
			new_y=cur_y+d[c.getDirection()][1];
//			System.out.println("相隔方格的位置:("+new_x+","+new_y+")="+maze[new_x][new_y]);
			if(maze[new_x][new_y]==0) {
				maze[cur_x][cur_y]=1;			//该墙打通（该墙设为通路）
				if(new_x<max&&new_x>=1&&new_y<max&&new_y>=1)
				{
					maze[new_x][new_y]=1;			//相隔方块设为通路
//					System.out.println("("+cur_x+","+cur_y+")"+"=1");
					Add();
				}
			}*/
            if(IsDepart(w))
            {
                Add();
            }
            //将该墙移除列表
            list.remove(random);
        }
        maze[max-1][max]=1;
    }

    //打印输出函数
    public String print() {
        String s="";
        String line;
        for(int i=0;i<=max;i++) {
            for(int j=0;j<=max;j++) {
                if(maze[i][j]==0) {
                    line=" "+maze[i][j];
                    s +=line;
                }
                else {
                    line = "  ";
                    s+=line;
                }
            }
            s+="\n";
        }
        return s;
    }




    //深度优先生成迷宫
    //初始化
    public void DfsInit(int m,int start_x,int start_y) {
        cur_x=start_x;
        cur_y=start_y;
        max=2*m;
        maze=new int[max+1][max+1];
        maze[start_x][start_y]=1;
        Dfslist=new ArrayList<Road>();
    }

    //判断是否有未被标记的迷宫单元
    public boolean JudgeMark() {
        for(int i=1;i<max;i+=2)
            for(int j=1;j<max;j+=2)
                if(maze[i][j]==0)
                    return true;
        return false;
    }

    //判断是否有相邻的未标记的有效的迷宫单元,有就返回临格迷宫单元
    public Road JudgeNebour(int x,int y) {
        Nebour=new ArrayList<>();
        int Random=0;
        if(x+2<max&&maze[x+2][y]==0)      	//下
        {
            Road cube=new Road(x+2,y,1);
            Nebour.add(cube);
        }

        if(y+2<max&&maze[x][y+2]==0)	//右
        {
            Road cube=new Road(x,y+2,2);
            Nebour.add(cube);
        }

        if(x-2>0&&maze[x-2][y]==0)		//上
        {
            Road cube=new Road(x-2,y,3);
            Nebour.add(cube);
        }
        if(y-2>0&&maze[x][y-2]==0)		//左
        {
            Road cube=new Road(x,y-2,4);
            Nebour.add(cube);
        }
        if(Nebour.size()!=0)
        {
            Random=(int)(Math.random()*Nebour.size());
            return Nebour.get(Random);
        }
        else
            return  null;
    }

    //深度优先迷宫
    public void DfsGo(){
        DfsInit(max,1,1);
        Road cube=new Road(cur_x,cur_y,0);
        while(JudgeMark()) {
            cur_x=cube.getX();
            cur_y=cube.getY();
            //有临格未被访问
            if((cube=JudgeNebour(cur_x,cur_y))!=null)
            {
                Dfslist.add(cube);
                wall_x=cur_x+fd[cube.getDirection()][0];
                wall_y=cur_y+fd[cube.getDirection()][1];
                maze[wall_x][wall_y]=1;
                maze[cube.getX()][cube.getY()]=1;
            }
            else
            {
                cube=Dfslist.get(Dfslist.size()-1);
                Dfslist.remove(Dfslist.size()-1);
            }
        }
        maze[1][0]=1;
        maze[max-1][max]=1;
    }




    //深度优先寻路
    //初始化函数
    public void FindInit() {
        Findlist=new ArrayList<Road>();
        findcur_x=1;
        findcur_y=0;
        findend_x=max-1;
        findend_y=max;
    }

    //判断是否能加入list列表的函数
    public boolean FindJudgeAdd(Road road) {
        for(Road r:Findlist)
            if(r.Equal(road))
                return false;
        return true;
    }

    //判断是否达到终点
    public boolean JudgeEnd(Road r) {
        if(r.getX()==findend_x&&r.getY()==findend_y)
            return true;
        else
            return false;
    }

    public void FindGo() {
        FindInit();
        Road cur_road=new Road(findcur_x,findcur_y,4);
        Road new_road;
        Findlist.add(cur_road);
        while(!JudgeEnd(cur_road))
        {
            //四个方向还没有都访问完
            if(cur_road.getNextdir()<=4)
            {
                //确定下一个方块
                new_x=cur_road.getX()+fd[cur_road.getNextdir()][0];
                new_y=cur_road.getY()+fd[cur_road.getNextdir()][1];
                //下个方块是路并且没有出界
                if(new_x>=0&&new_x<=max&&new_y>=0&&new_y<=max)
                {
                    if(maze[new_x][new_y]==1)
                    {
                        new_road=new Road(new_x,new_y,reverse[cur_road.getNextdir()]);
                        //判断是否能加入list，如果不能说明有重复(出现了回环路)
                        if(FindJudgeAdd(new_road))
                            Findlist.add(new_road);
                    }
                }
                //更改当前路的nextdir
                cur_road.nextdiradd();
            }
            //四个方向都访问完了
            else
            {
                //将这个方块移除
                Findlist.remove(cur_road);
            }
                cur_road=Findlist.get(Findlist.size()-1);
        }
        //更改maze中的数据(在maze中添加迷宫路径)
        for(Road li:Findlist)
            maze[li.getX()][li.getY()]=2;
    }








}
