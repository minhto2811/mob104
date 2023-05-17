package com.example.mob104_app.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.text.DecimalFormat;

public class TOOLS {
    //192.168.137.1
    public static String doMainDevice = "http://10.0.2.2:3000";
    public static String TOKEN = "TOKEN";


    public static String convertPrice(int price) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(price);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = ((Activity) context).getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN,null);
    }



    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\d{10}";
        return phoneNumber.matches(regex);
    }

    public static boolean isValidEmail(String email){
        String emailPattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return !email.isEmpty() && email.matches(emailPattern);
    }

}



