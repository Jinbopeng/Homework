package com.mic.asus.managementsystem.manager.transactioninf;

import java.io.Serializable;

public class transaction  implements Serializable{
    private int id;//订单号
    private String ordertime;//订单时间
    private int people;//人数
    private int deskid;//桌号
    private int ischeck;//是否结账
    private String waiter;//服务员名字
    private int totalprice;//该订单的总价格


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getDeskid() {
        return deskid;
    }

    public void setDeskid(int deskid) {
        this.deskid = deskid;
    }

    public int getIscheck() {
        return ischeck;
    }

    public void setIscheck(int ischeck) {
        this.ischeck = ischeck;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

}
