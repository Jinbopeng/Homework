package com.mic.asus.managementsystem.employee.empfoodmenu;

public class Orderinf {
    int deskid;
    int people;
    String waiter;
    ShoppingCart cart;

    public int getDeskid() {
        return deskid;
    }

    public void setDeskid(int deskid) {
        this.deskid = deskid;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
