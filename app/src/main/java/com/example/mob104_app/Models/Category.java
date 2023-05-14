package com.example.mob104_app.Models;

import java.io.Serializable;

public class Category implements Serializable {
    private final String image;
    private String name;

    public Category(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
