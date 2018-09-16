package com.mic.asus.maze;

public class Wall {
    private int x;
    private int y;
    private int direction;						//0表示横墙，1表示竖墙

    public Wall(int mx, int my, int md) {
        x = mx;
        y = my;
        direction = md;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    // 判断两个Cube对象是否相等
    public boolean equal(Wall c) {
        if (c.getX() == x && c.getY() == y)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Wall [x=" + x + ", y=" + y + ", direction=" + direction + "]";
    }
}
