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

import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imv_back;
    private TextView tv_signin;

    private TextInputLayout til_fullname, til_username, til_password, til_email;
    private EditText edt_fullname, edt_username, edt_password, edt_email;
    private Button btn_signup;


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
        til_email = findViewById(R.id.til_email);
        edt_email = findViewById(R.id.edt_email);
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
            if (!checkFielEmty(edt_email, til_email) || !checkFielEmty(edt_fullname, til_fullname) || !checkFielEmty(edt_username, til_username) || !checkFielEmty(edt_password, til_password)) {
                return;
            }
            if (!PasswordActivity.validatePass(edt_password, til_password, RegisterActivity.this)) {
                return;
            }

            register(edt_email.getText().toString(),edt_fullname.getText().toString(), edt_username.getText().toString(), edt_password.getText().toString());
        });
    }

    private boolean checkFielEmty(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().length() < 8) {
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            editText.requestFocus();
            if (editText == edt_email) {
                textInputLayout.setError("Email không hợp lệ!");
            } else if (editText == edt_username) {
                textInputLayout.setError("Tài khoản phải nhiều hơn 7 kí tự!");
            } else if (editText == edt_password) {
                textInputLayout.setError("Mật khẩu phải nhiều hơn 7 kí tự!");
            } else {
                textInputLayout.setError("Tên người dùng không hợp lệ");
            }
            return false;
        }
        if (editText == edt_email) {
            if(!TOOLS.isValidEmail(editText.getText().toString())){
                textInputLayout.setError("Email không hợp lệ!");
                return false;
            }
            textInputLayout.setHint("Nhập email");
        } else if (editText == edt_username) {
            textInputLayout.setHint("Nhập tài khoản");
        } else if (editText == edt_password) {
            textInputLayout.setHint("Nhập mật khẩu");
        } else {
            textInputLayout.setHint("Nhập tên người dùng");
        }
        textInputLayout.setError(null);
        editText.clearFocus();
        return true;
    }


    private void register(String email, String fullname, String username, String password) {
        Dialog dialog = TOOLS.createDialog(RegisterActivity.this);
        dialog.show();
        User user = new User();
        user.setFullname(fullname);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        ApiService.apiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        til_username.setError("Tên tài khoản đã được sử dụng");
                        edt_username.setSelection(edt_username.getSelectionEnd());
                        Toast.makeText(RegisterActivity.this, "Tên tài khoản đã được sử dụng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    assert response.body() != null;
                    intent.putExtra("username", response.body().getUsername());
                    intent.putExtra("password", response.body().getPassword());
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
    }
}