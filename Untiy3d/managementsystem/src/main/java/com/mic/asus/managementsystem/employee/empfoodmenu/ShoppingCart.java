package com.mic.asus.managementsystem.employee.empfoodmenu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart implements Serializable{
    private int cartnumber;//商品总数
    private int cartprice;//商品总价
    private Map<Rightfood,Integer> cartsingle;//一种商品的数量

    public ShoppingCart(){
        cartnumber=0;
        cartprice=0;
        cartsingle=new HashMap<>();
    }

    public int getCartnumber() {
        return cartnumber;
    }

    public void setCartnumber(int cartnumber) {
        this.cartnumber = cartnumber;
    }

    public int getCartprice() {
        return cartprice;
    }

    public void setCartprice(int cartprice) {
        this.cartprice = cartprice;
    }

    public Map<Rightfood, Integer> getCartsingle() {
        return cartsingle;
    }

    public void setCartsingle(Map<Rightfood, Integer> cartsingle) {
        this.cartsingle = cartsingle;
    }



    //添加菜到购物车
    public boolean Addfood(Rightfood food){
        int number=food.getNumber();
        cartsingle.put(food,number);
        cartprice=cartprice+food.getPrice();
        cartnumber=cartnumber+1;
        return true;
    }

    //减少菜
    public boolean Subfood(Rightfood food){
        int number=food.getNumber();
        if(number==0)
            cartsingle.remove(food);
        else
            cartsingle.put(food,number);
        cartprice=cartprice-food.getPrice();
        cartnumber=cartnumber-1;
        return true;
    }

    //获取菜名的总数
    public int Getcount(){
        return cartsingle.size();
    }

    //清空购物车
    public void Clear(){
        cartprice=0;
        cartnumber=0;
        cartsingle.clear();
    }

    public String show(){
        String s=cartsingle.toString()+"totalnumber"+cartnumber;
        return s;
    }

}
