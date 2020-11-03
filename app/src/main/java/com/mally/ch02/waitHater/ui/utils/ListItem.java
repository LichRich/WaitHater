package com.mally.ch02.waitHater.ui.utils;

public class ListItem {

    String name;
    int num;

//    case: busStop list
    ListItem(String name) {
        this.name = name;
    }

//    case: bus list
    ListItem(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
