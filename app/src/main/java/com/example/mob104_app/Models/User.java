package com.example.mob104_app.Models;

import java.util.List;

public class User {
    private String _id;
    private String fullname;
    private String username;
    private String password;
    private String numberphone;


    //role định nghĩa phân quyền: true - admin, false - user
    private boolean role;
    private String email;
    private String image;
    private boolean sex;
    private String date;
    private String tokenNotify;


    public User() {
    }


    public User(String _id, String fullname, String username, String password, String numberphone, boolean role, String email,String image, boolean sex, String date,String tokenNotify) {
        this._id = _id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.numberphone = numberphone;
        this.role = role;
        this.email = email;
        this.image = image;
        this.sex = sex;
        this.date = date;
        this.tokenNotify =tokenNotify;
    }


    public boolean isSex() {
        return sex;
    }

    public String getTokenNotify() {
        return tokenNotify;
    }

    public void setTokenNotify(String tokenNotify) {
        this.tokenNotify = tokenNotify;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isRole() {
        return role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public boolean getRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", numberphone='" + numberphone + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
