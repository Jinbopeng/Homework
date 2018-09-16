package com.mic.asus.managementsystem.manager.deskinf;

import java.io.Serializable;

public class desk implements Serializable{
    int id;
    int capacity;
    int occupy;
    int isempty;         //0有人，1为空
    int isclean;         //0为未打扫，1为已打扫

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupy() {
        return occupy;
    }

    public void setOccupy(int occupy) {
        this.occupy = occupy;
    }

    public int getIsempty() {
        return isempty;
    }

    public void setIsempty(int isempty) {
        this.isempty = isempty;
    }

    public int getIsclean() {
        return isclean;
    }

    public void setIsclean(int isclean) {
        this.isclean = isclean;
    }
}
