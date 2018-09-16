package com.mic.asus.managementsystem.manager.transactioninf;


//作为传送订单部分数据的一个辅助类
public class tdetail {
    int id;
    String foodname;
    int number;
    int iscook;         //0表示未炒，1表示正在炒，2表示已经炒完
    String cooker;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIscook() {
        return iscook;
    }

    public void setIscook(int iscook) {
        this.iscook = iscook;
    }

    public String getCooker() {
        return cooker;
    }

    public void setCooker(String cooker) {
        this.cooker = cooker;
    }
}
