package com.example.mob104_app.Api;

import com.example.mob104_app.Models.Banner;
import com.example.mob104_app.Models.Category;
import com.example.mob104_app.Models.Favourite;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.Tools.TOOLS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String url  = TOOLS.doMainDevice + "/api/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("product/home")
    Call<List<Product>> getAllProduct();

    @GET("product/category/{category}")
    Call<List<Product>> getListProductByCategory(@Path("category") String category);

    @GET("product/related/{category}")
    Call<List<Product>> getListProductRelated(@Path("category") String category);

    @GET("product/banner/{banner_code}")
    Call<List<Product>> getListProductByBanner(@Path("banner_code") String banner_code);

    @GET("banner/home")
    Call<List<Banner>> getAllBanner();

    @GET("category/home")
    Call<List<Category>> getAllCategory();

    @POST("user/add")
    Call<User> createUser(@Body User user);

    @POST("user/login")
    Call<User> loginUser(@Body RequestBody requestBody);

    @POST("user/info")
    Call<User> getInfoUser(@Body JsonObject jsonObject);

    @Multipart
    @POST("user/update/image/{username}")
    Call<User> changeAvatar(@Part MultipartBody.Part image, @Path("username") String id);
    @POST("user/password")
    Call<User> changePassword(@Body RequestBody requestBody);
    @POST("favourites/add/{id_user}")
    Call<Favourite> addToFavourite(@Path("id_user") String id_user,@Body RequestBody id_product);

    @GET("favourites/{id_user}")
    Call<List<String>> getListFavourite(@Path("id_user") String id_user);

    @GET("favourites/product/{id_user}")
    Call<List<Product>> getListProductByFavourite(@Path("id_user") String id_user);

    @POST("favourites/update/{id_user}")
    Call<Favourite> delToFavourite(@Path("id_user") String id_user,@Body RequestBody id_product);



}
