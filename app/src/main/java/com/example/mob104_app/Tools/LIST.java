package com.example.mob104_app.Tools;

import com.example.mob104_app.Models.Address;
import com.example.mob104_app.Models.Banner;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.Models.Category;
import com.example.mob104_app.Models.Favourite;
import com.example.mob104_app.Models.Product;

import java.util.ArrayList;
import java.util.List;


public class LIST {
    public static List<Product> listProduct = new ArrayList<>();
    public static List<Category> listCategory = new ArrayList<>();
    public static List<Banner> listBanner = new ArrayList<>();
    public static List<Product> listProductByBanner = new ArrayList<>();
    public static List<Product> listProductByCategory = new ArrayList<>();

    public static List<String> listFavourite = new ArrayList<>();

    public static List<Product> getListProductByFavourite = new ArrayList<>();
    public static List<Address> listAddress = new ArrayList<>();
    public static List<Cart> listCart = new ArrayList<>();
}
