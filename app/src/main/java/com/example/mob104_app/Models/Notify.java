package com.example.mob104_app.Models;

import java.util.Date;

public class Notify {
    private String _id;
    private String id_user;
    private String id_bill;
    private Date time;
    private int status;

    public Notify() {
    }

    public Notify(String _id, String id_user, String id_bill, Date time, int status) {
        this._id = _id;
        this.id_user = id_user;
        this.id_bill = id_bill;
        this.time = time;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
