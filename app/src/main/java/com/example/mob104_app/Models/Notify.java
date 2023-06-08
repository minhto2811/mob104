package com.example.mob104_app.Models;

import java.util.Date;

public class Notify {
    private String id_bill;
    private String id_user;
    private int status;
    private Date time;

    public Notify() {
    }

    public Notify(String id_bill, String id_user, int status, Date time) {
        this.id_bill = id_bill;
        this.id_user = id_user;
        this.status = status;
        this.time = time;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
