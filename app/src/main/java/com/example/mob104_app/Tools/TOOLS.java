package com.example.mob104_app.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mob104_app.Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class TOOLS {

    public static String doMainDevice = "http://10.0.2.2:3000";
    public static String  USER= "USER";
    public static String  DEFAULT_ADDRESS= "DEFAULT_ADDRESS";

    private static  Gson gson = new Gson();



    public static String convertPrice(int price) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(price);
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.apply();
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(USER, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(USER,null);
        User user = gson.fromJson(string, User.class);
        return user;
    }

    public static void saveDefaulAddress(Context context,String id_address){
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_ADDRESS, id_address);
        editor.apply();
    }

    public static void clearDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(DEFAULT_ADDRESS,null);
        return string;
    }



    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\d{10}";
        return phoneNumber.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return !email.isEmpty() && email.matches(emailPattern);
    }

    public static JsonObject convertJson(String key, String value) {
        String token = "{\"" + key + "\": \"" + value + "\"}";
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(token, JsonObject.class);
        return jsonObject;
    }

    public static String covertToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}



