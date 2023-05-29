package com.example.mob104_app.Models;

public class Cart {
    private String _id;
    private String id_user;
    private String id_product;
    private Integer quantity;

    public Cart() {
    }

    public Cart(String _id, String id_user, String id_product, Integer quantity) {
        this._id = _id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.quantity = quantity;
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

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
