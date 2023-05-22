package com.example.mob104_app.Models;

import java.util.List;

public class Favourite {
    private String id_user;
    private List<String> list_id_product;

    public Favourite(String id_user, List<String> list_id_product) {
        this.id_user = id_user;
        this.list_id_product = list_id_product;
    }

    public Favourite() {
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public List<String> getList_id_product() {
        return list_id_product;
    }

    public void setList_id_product(List<String> list_id_product) {
        this.list_id_product = list_id_product;
    }
}
