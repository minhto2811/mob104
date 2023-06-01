package com.example.mob104_app.Models;

import java.io.Serializable;

public class MethodShipping implements Serializable {
    private String name;
    private int price;

    private String time;
    private int color;

    public MethodShipping() {
    }

    public MethodShipping(String name, int price, int color, String time) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.time = time;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
