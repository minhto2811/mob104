package com.example.mob104_app.Api;

import com.example.mob104_app.Models.Address;
import com.example.mob104_app.Models.Banner;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.Models.Category;
import com.example.mob104_app.Models.District;
import com.example.mob104_app.Models.Favourite;
import com.example.mob104_app.Models.Notify;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.Models.Province;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.Models.Ward;
import com.example.mob104_app.Tools.TOOLS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @POST("user/tokenNotify/{id_user}")
    Call<Integer> tokenNotify(@Path("id_user") String id_user,@Body RequestBody requestBody);

    @POST("user/update/info")
    Call<Integer> updateInfo(@Body User user);

    @Multipart
    @POST("user/update/image/{username}")
    Call<User> changeAvatar(@Part MultipartBody.Part image, @Path("username") String id);

    @POST("user/password")
    Call<Integer> changePassword(@Body RequestBody requestBody);

    @GET("user/forgetpassword/{username}")
    Call<Integer> getCodeConfirmPassword(@Path("username") String username);

    @POST("user/reset-password/{resetToken}")
    Call<Integer> changePasswordNew(@Path("resetToken") String resetToken,@Body RequestBody password);

    @POST("favourites/add/{id_user}")
    Call<Favourite> addToFavourite(@Path("id_user") String id_user, @Body RequestBody id_product);



    @GET("favourites/product/{id_user}")
    Call<List<Product>> getListProductByFavourite(@Path("id_user") String id_user);

    @POST("favourites/update/{id_user}")
    Call<Favourite> delToFavourite(@Path("id_user") String id_user, @Body RequestBody id_product);

    @GET("address/provinces")
    Call<List<Province>> getProvinces();

    @GET("address/districts/{parent_code}")
    Call<List<District>> getDistricts(@Path("parent_code") String parent_code);

    @GET("address/wards/{parent_code}")
    Call<List<Ward>> getWards(@Path("parent_code") String parent_code);

    @POST("address/addNew/{id_user}")
    Call<Address> saveNewAddress(@Path("id_user") String id_user, @Body Address address);

    @GET("address/all/{id_user}")
    Call<List<Address>> getAddress(@Path("id_user") String id_user);

    @POST("address/update")
    Call<Address> updateAddress(@Body Address Address);

    @POST("address/delete/{id_address}")
    Call<Integer> deleteAdsress(@Path("id_address") String id_address);

    @POST("cart/add")
    Call<Cart> addCart(@Body Cart cart);

    @GET("cart/{id_user}")
    Call<List<Cart>> getCarts(@Path("id_user") String id_user);

    @GET("cart/delete/{id_cart}")
    Call<Integer> deleteCart(@Path("id_cart") String id_cart);

    @POST("bill/add/{token}")
    Call<Bill> createBill(@Path("token") String token_device,@Body Bill bill);

    @GET("bill/{id_user}")
    Call<List<Bill>> getBill(@Path("id_user") String id_user);

    @POST("bill/cancel/{id_bill}/{token}")
    Call<Integer> cancelBill(@Path("id_bill") String id_bill,@Path("token") String token);

    @POST("recently/add/{id_user}")
    Call<Integer> addRecently(@Path("id_user") String id_user, @Body RequestBody requestBody);




    @GET("recently/{id_user}")
    Call<List<Product>> getRecently(@Path("id_user") String id_user);

    @POST("recently/delete/{id_user}")
    Call<Integer> deleteRecently(@Path("id_user") String id_user, @Body RequestBody requestBody);


    @GET("notify/{id_user}")
    Call<List<Notify>> getNotify(@Path("id_user") String id_cart);
}
