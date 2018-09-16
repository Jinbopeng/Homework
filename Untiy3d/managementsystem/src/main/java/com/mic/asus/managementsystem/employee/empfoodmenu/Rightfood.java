package com.mic.asus.managementsystem.employee.empfoodmenu;

import java.util.Objects;

public class Rightfood {
    String name;
    int head_id;
    int price;
    int hot;
    int rest;
    int number;
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHead_id() {
        return head_id;
    }

    public void setHead_id(int head_id) {
        this.head_id = head_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof Rightfood))
            return false;
        Rightfood food=(Rightfood)obj;
        return name.equals(food.name)&&
                type.equals(food.type)&&
                head_id==food.head_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,type,head_id);
    }

    @Override
    public String toString() {
        String s="name="+name+" type="+type;
        return s;
    }
}
