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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imv_back;
    private TextView tv_signin;

    private TextInputLayout til_fullname, til_username, til_password;
    private EditText edt_fullname, edt_username, edt_password;
    private Button btn_signup;
    private boolean name, user, pass, isShow = true, hasUppercase = false, hasLowercase = false, hasDigit = false;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapping();
        back();
        changeLoginPage();
        checkInputFiel();
    }

    private void mapping() {
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        til_fullname = findViewById(R.id.til_fullname);
        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);
        btn_signup = findViewById(R.id.btn_signup);
        imv_back = findViewById(R.id.imv_back);
        tv_signin = findViewById(R.id.tv_signin);
    }


    @SuppressLint("SetTextI18n")
    private void checkInputFiel() {

        btn_signup.setOnClickListener(v -> {
            if (!checkFielEmty(edt_fullname, til_fullname) || !checkFielEmty(edt_username, til_username) || !checkFielEmty(edt_password, til_password)) {
                return;
            }
            if(!PasswordActivity.validatePass(edt_password,til_password,RegisterActivity.this)){
                return;
            }

            register(edt_fullname.getText().toString(), edt_username.getText().toString(), edt_password.getText().toString());
        });
    }

    private boolean checkFielEmty(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().length() == 0) {
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            editText.requestFocus();
            if (editText == edt_username) {
                textInputLayout.setHint("Tài khoản không được để trống!");
            } else if (editText == edt_password) {
                textInputLayout.setHint("Mật khẩu không được để trống!");
            } else {
                textInputLayout.setHint("Tên người dùng không được để trống!");
            }
            return false;
        }
        if (editText == edt_username) {
            textInputLayout.setHint("Nhập tài khoản. . .");
        } else if (editText == edt_password) {
            textInputLayout.setHint("Nhập mật khẩu. . .");
        } else {
            textInputLayout.setHint("Nhập tên người dùng. . .");
        }
        editText.clearFocus();
        return true;
    }


    private void register(String fullname, String username, String password) {
        dialog = new Dialog(RegisterActivity.this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.layout_watting, null);
        Glide.with(RegisterActivity.this).asGif().load(R.drawable.spin).into((ImageView) view.findViewById(R.id.imv_watting));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        User user = new User();
        user.setFullname(fullname);
        user.setUsername(username);
        user.setPassword(password);
        ApiService.apiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    assert response.body() != null;
                    intent.putExtra("username", response.body().getUsername());
                    intent.putExtra("password", response.body().getPassword());
                    setResult(RESULT_OK, intent);
                    finish();
                    dialog.hide();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }




    private void changeLoginPage() {
        String text = "Bạn đã có tài khoản? Hãy đăng nhập";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        spannableString.setSpan(clickableSpan, 25, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_signin.setText(spannableString);
        tv_signin.setMovementMethod(LinkMovementMethod.getInstance());
        tv_signin.setHighlightColor(Color.TRANSPARENT);
    }

    private void back() {
        imv_back.setOnClickListener(v -> onBackPressed());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog !=null){
            dialog.dismiss();
        }
    }
}