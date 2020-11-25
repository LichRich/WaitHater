package com.mally.ch02.waitHater.ui.utils;

public class ListItem {

    String name;
    String num;

//    case: busStop list
    public ListItem(String name) {
        this.name = name;
    }

//    case: bus list
    public ListItem(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
