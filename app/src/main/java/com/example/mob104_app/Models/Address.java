package com.example.mob104_app.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Address implements Serializable {
    private String _id;

    private String id_user;
    private String fullname;
    private String numberphone;
    private String province;
    private String district;
    private String wards;
    private String address;

    public Address() {
    }

    public Address(String _id, String id_user, String fullname, String numberphone, String province, String district, String wards, String address) {
        this._id = _id;
        this.id_user = id_user;
        this.fullname = fullname;
        this.numberphone = numberphone;
        this.province = province;
        this.district = district;
        this.wards = wards;
        this.address = address;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
