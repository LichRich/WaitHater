package com.mally.ch02.waitHater.ui.utils;

public class ListItem {

    boolean busIsHere;
    String name, num, id;

//    case: busStop list
    public ListItem(String name, String id) {
        this.name = name;
        this.id = id;
    }

//    case: busStop list check bus is here
    public ListItem(boolean isHere, String name) {
        this.busIsHere = isHere;
        this.name = name;
    }

//    case: bus list
    public ListItem(String num, String name, String id) {
        this.num = num;
        this.name = name;
        this.id = id;
    }

    public boolean isBusIsHere() {
        return busIsHere;
    }

    public void setBusIsHere(boolean busIsHere) {
        this.busIsHere = busIsHere;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
