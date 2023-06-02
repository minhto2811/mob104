package com.example.mob104_app.Models;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    private String _id;

    private String id_user;
    private String name;
    private String phone;
    private String date;
    private int total;
    private int status;

    private List<Cart> list;

    private String address;

    private String methodShipping;
    private String methodPayment;

    public Bill() {
    }

    public Bill(String _id, String name, String phone, String id_user, String date, int total, int status, List<Cart> list, String address, String methodShipping, String methodPayment) {
        this._id = _id;
        this.id_user = id_user;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.total = total;
        this.status = status;
        this.list = list;
        this.address = address;
        this.methodShipping = methodShipping;
        this.methodPayment = methodPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMethodPayment() {
        return methodPayment;
    }

    public void setMethodPayment(String methodPayment) {
        this.methodPayment = methodPayment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethodShipping() {
        return methodShipping;
    }

    public void setMethodShipping(String methodShipping) {
        this.methodShipping = methodShipping;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "_id='" + _id + '\'' +
                ", id_user='" + id_user + '\'' +
                ", date='" + date + '\'' +
                ", total=" + total +
                ", status=" + status +
                ", list=" + list +
                ", address=" + address +
                ", methodShipping='" + methodShipping + '\'' +
                ", methodPayment='" + methodPayment + '\'' +
                '}';
    }
}
