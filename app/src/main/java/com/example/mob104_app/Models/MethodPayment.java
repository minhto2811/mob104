package com.example.mob104_app.Models;

import java.io.Serializable;

public class MethodPayment implements Serializable {
    private String name;
    private boolean enable;

    public MethodPayment() {
    }

    public MethodPayment(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
