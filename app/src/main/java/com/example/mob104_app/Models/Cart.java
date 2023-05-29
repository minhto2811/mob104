package com.example.mob104_app.Models;

public class Cart {
    private String _id;
    private String id_user;
    private String id_product;
    private String name_product;
    private String price_product;
    private Integer quantity;
    private Integer sale;

    public Cart() {
    }

    public Cart(String _id, String id_user, String id_product, String name_product, String price_product, Integer quantity,Integer sale) {
        this._id = _id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.quantity = quantity;
        this.sale = sale;
    }


    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getPrice_product() {
        return price_product;
    }

    public void setPrice_product(String price_product) {
        this.price_product = price_product;
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
