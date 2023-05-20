package com.example.mob104_app.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private final String _id;
    private String name;
    private final ArrayList<String> image;
    private final String description;
    private final int price;
    private String category;
    private final String status;
    private int sold;
    private int sale;


    public Product(String id, String name, ArrayList<String> image, String description, int price, String category, String status, int sold, int sale) {
        this._id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = status;
        this.sold = sold;
        this.sale = sale;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<String> getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getSold() {
        return sold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", sold=" + sold +
                ", sale=" + sale +
                '}';
    }
}
