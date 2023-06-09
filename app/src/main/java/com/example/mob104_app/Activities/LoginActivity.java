package com.example.mob104_app.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Address;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEXT = 555;
    private ImageView imv_back;
    private TextView tv_signup, tv_forgot_password;
    private TextInputLayout til_username, til_password;
    private EditText edt_username, edt_password;
    private Button btn_login;
    private int check = 0;

    private static final int REQUEST_CODE_FORGET = 555;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        back();
        changeRegistrationPage();
        login();
        forgotPassword();
    }

    private void forgotPassword() {
        tv_forgot_password.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FORGET);
            overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }

    private void mapping() {
        imv_back = findViewById(R.id.imv_back);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);
        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);
    }

    @SuppressLint("SetTextI18n")
    private void login() {

        btn_login.setOnClickListener(v -> {
            if (!checkFielEmty(edt_username, til_username) || !checkFielEmty(edt_password, til_password)) {
                return;
            }
            checkAccount(edt_username.getText().toString(), edt_password.getText().toString());
        });
    }

    private boolean checkFielEmty(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            if (editText == edt_username) {
                textInputLayout.setHint("Tài khoản không được để trống!");
            } else {
                textInputLayout.setHint("Mật khẩu không được để trống!");
            }
            editText.requestFocus();
            return false;
        }
        if (editText == edt_username) {
            textInputLayout.setHint("Nhập tài khoản. . .");
        } else {
            textInputLayout.setHint("Nhập mật khẩu. . .");
        }
        editText.clearFocus();
        return true;
    }

    private void back() {
        imv_back.setOnClickListener(v -> onBackPressed());
    }


    private void checkAccount(String user, String pass) {

        Dialog dialog = TOOLS.createDialog(LoginActivity.this);
        dialog.show();
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", user);
            postData.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = postData.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);

        ApiService.apiService.loginUser(requestBody).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user1 = response.body();
                    if (user1.getUsername() != null) {
                        TOOLS.saveUser(LoginActivity.this, user1);
                        ACCOUNT.user = user1;
                        ApiService.apiService.getListProductByFavourite(user1.get_id()).enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                if (response.isSuccessful()) {
                                    if(response.body()!=null){
                                        LIST.getListProductByFavourite = response.body();
                                    }
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }
                                gotoSettings();
                            }

                            @Override
                            public void onFailure(Call<List<Product>> call, Throwable t) {
                                gotoSettings();
                            }
                        });
                        ApiService.apiService.getAddress(user1.get_id()).enqueue(new Callback<List<Address>>() {
                            @Override
                            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                                if (response.isSuccessful()) {
                                    if(response.body()!=null){
                                        LIST.listAddress = response.body();
                                    }
                                }
                                gotoSettings();
                            }

                            @Override
                            public void onFailure(Call<List<Address>> call, Throwable t) {
                                gotoSettings();
                            }
                        });

                        ApiService.apiService.getRecently(ACCOUNT.user.get_id()).enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        Collections.reverse(response.body());
                                        LIST.listRecently = response.body();
                                    }
                                }
                                gotoSettings();
                            }

                            @Override
                            public void onFailure(Call<List<Product>> call, Throwable t) {
                                gotoSettings();
                            }
                        });

                        String token = TOOLS.getToken(LoginActivity.this);
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("tokenNotify", token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonString = postData.toString();
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);

                        ApiService.apiService.tokenNotify(ACCOUNT.user.get_id(), requestBody).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.isSuccessful()&&response.body()!=null){
                                    if(response.body()==1){
                                        user1.setTokenNotify(token);
                                        TOOLS.saveUser(LoginActivity.this,user1);
                                        ACCOUNT.user = user1;
                                    }
                                }
                                gotoSettings();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                gotoSettings();
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
    }

    private void gotoSettings() {
        check++;
        if (check < 3) {
            return;
        }
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }


    private void changeRegistrationPage() {
        String text = "Bạn chưa có tài khoản? Hãy đăng ký";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEXT);
                overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }

            @Override
            public CharacterStyle getUnderlying() {
                return super.getUnderlying();
            }
        };
        spannableString.setSpan(clickableSpan, 27, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_signup.setText(spannableString);
        tv_signup.setMovementMethod(LinkMovementMethod.getInstance());
        tv_signup.setHighlightColor(Color.TRANSPARENT);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NEXT || requestCode == REQUEST_CODE_FORGET) && resultCode == RESULT_OK) {
            assert data != null;
            edt_username.setText(data.getStringExtra("username"));
            edt_password.setText(data.getStringExtra("password"));
            edt_username.clearFocus();
            edt_password.clearFocus();
            til_username.setHint("Nhập tài khoản. . .");
            til_password.setHint("Nhập mật khẩu. . .");
        }

    }



}