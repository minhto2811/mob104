package com.example.mob104_app.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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

import com.bumptech.glide.Glide;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;
import com.example.mob104_app.UI.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEXT = 555;
    private ImageView imv_back, imv_password;
    private TextView tv_signup, tv_err_username, tv_err_password, tv_forgot_password;
    private EditText edt_username, edt_password;
    private Button btn_login;
    private boolean isShow = false, checkUser, checkPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        back();
        changeRegistrationPage();
        login();
        showHidePassword();
        forgotPassword();
    }

    private void forgotPassword() {
        tv_forgot_password.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
        });
    }

    private void mapping() {
        imv_back = findViewById(R.id.imv_back);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        imv_password = findViewById(R.id.imv_password);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);
        tv_err_username = findViewById(R.id.tv_err_username);
        tv_err_password = findViewById(R.id.tv_err_password);
    }

    @SuppressLint("SetTextI18n")
    private void login() {

        btn_login.setOnClickListener(v -> {
            if (edt_password.getText().toString().trim().isEmpty()) {
                tv_err_password.setText("Mật khẩu không được để trống!");
                edt_password.requestFocus();
                checkPass = false;
            } else {
                tv_err_password.setText(null);
                edt_password.clearFocus();
                checkPass = true;
            }


            if (edt_username.getText().toString().trim().isEmpty()) {
                tv_err_username.setText("Tài khoản không được để trống!");
                edt_username.requestFocus();
                checkUser = false;
            } else {
                tv_err_username.setText(null);
                edt_username.clearFocus();
                checkUser = true;
            }


            if (!checkUser || !checkPass) {
                return;
            }

            checkAccount(edt_username.getText().toString(), edt_password.getText().toString());
        });
    }

    private void back() {
        imv_back.setOnClickListener(v -> onBackPressed());
    }


    private void checkAccount(String user, String pass) {

        Dialog dialog = new Dialog(LoginActivity.this);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.layout_watting, null);
        Glide.with(LoginActivity.this).asGif().load(R.drawable.spin).into((ImageView) view.findViewById(R.id.imv_watting));
        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                    if (user1 != null) {
                        TOOLS.saveUser(LoginActivity.this, user1);
                        ACCOUNT.user = user1;
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        setResult(SettingsFragment.REQUEST_LOGIN);
                        finish();
                        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
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

    private void showHidePassword() {
        imv_password.setOnClickListener(v -> {
            isShow = !isShow;
            if (isShow) {
                imv_password.setImageResource(R.drawable.visibility_off);
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                imv_password.setImageResource(R.drawable.visibility);
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            edt_password.setSelection(edt_password.getText().length());
        });
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
        if (requestCode == REQUEST_CODE_NEXT && resultCode == RESULT_OK) {
            assert data != null;
            edt_username.setText(data.getStringExtra("username"));
            edt_password.setText(data.getStringExtra("password"));
            tv_err_username.setText(null);
            edt_username.clearFocus();
            checkUser = true;
            tv_err_password.setText(null);
            edt_password.clearFocus();
            checkPass = true;
        }
    }

}