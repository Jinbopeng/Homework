package com.mic.asus.maze;

//路线的方格
class Road{
    private int x;
    private int y;
    private int direction;						//回头路的方向
    private int nextdir=1;						//下一次访问的方向
    public Road(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        if(direction==nextdir)
            nextdir++;
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

    public int getNextdir() {
        return nextdir;
    }

    public void setNextdir(int nextdir) {
        this.nextdir = nextdir;
    }

    public void nextdiradd() {
        nextdir++;
        if(nextdir==direction)
            nextdir++;
    }


    public boolean Equal(Road r) {
        if(r.getX()==x&&r.getY()==y)
            return true;
        else
            return false;
    }

}
