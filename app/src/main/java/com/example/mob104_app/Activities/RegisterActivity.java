package com.example.mob104_app.Activities;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imv_back, imv_password;
    private TextView tv_signin, tv_err_fullname, tv_err_username, tv_err_password;
    private EditText edt_fullname, edt_username, edt_password;
    private Button btn_signup;
    private boolean name, user, pass, isShow = true, hasUppercase = false,hasLowercase = false, hasDigit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back();
        changeLoginPage();
        checkInputFiel();
        showHidePassword();
    }


    private void checkInputFiel() {
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        tv_err_fullname = findViewById(R.id.tv_err_fullname);
        tv_err_username = findViewById(R.id.tv_err_username);
        tv_err_password = findViewById(R.id.tv_err_password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_password.getText().toString().isEmpty()) {
                    tv_err_password.setText("Vui lòng nhập mật khẩu!");
                    edt_password.requestFocus();
                    pass = false;
                } else {
                    if (edt_password.getText().toString().length()<8) {
                        tv_err_password.setText("Mật khẩu phải từ 8 đến 20 kí tự bao gồm kí tự in hoa,kí tự thường và số!");
                        edt_password.requestFocus();
                        pass = false;
                        return;
                    }
                    for (char c : edt_password.getText().toString().toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            hasUppercase = true;
                        } else if (Character.isDigit(c)) {
                            hasDigit = true;
                        }else {
                            hasLowercase = true;
                        }
                    }

                    if (!hasLowercase) {
                        tv_err_password.setText("Mật khẩu phải chứa ít nhất 1 kí tự thường!");
                        edt_password.requestFocus();
                        pass = false;
                        return;
                    }
                    if (!hasUppercase) {
                        tv_err_password.setText("Mật khẩu phải chứa ít nhất 1 kí tự in hoa!");
                        edt_password.requestFocus();
                        pass = false;
                        return;
                    }
                    if (!hasDigit) {
                        tv_err_password.setText("Mật khẩu phải chứa ít nhất 1 kí tự chữ số!");
                        edt_password.requestFocus();
                        pass = false;
                        return;
                    }
                    edt_password.clearFocus();
                    tv_err_password.setText(null);
                    pass = true;
                }

                if (edt_username.getText().toString().isEmpty()) {
                    tv_err_username.setText("Vui lòng nhập tài khoản!");
                    edt_username.requestFocus();
                    user = false;
                } else {
                    edt_username.clearFocus();
                    tv_err_username.setText(null);
                    user = true;
                }

                if (edt_fullname.getText().toString().isEmpty()) {
                    tv_err_fullname.setText("Vui lòng nhập tên người dùng!");
                    edt_fullname.requestFocus();
                    name = false;
                } else {
                    edt_fullname.clearFocus();
                    tv_err_fullname.setText(null);
                    name = true;
                }

                if (!name || !user || !pass) {
                    return;
                }

                register(edt_fullname.getText().toString(), edt_username.getText().toString(), edt_password.getText().toString());
            }
        });
    }


    private void register(String fullname, String username, String password) {
        Dialog dialog = new Dialog(RegisterActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_watting, null);
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
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("username", response.body().getUsername());
                    intent.putExtra("password", response.body().getPassword());
                    setResult(RESULT_OK, intent);
                    finish();
                    dialog.hide();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }

    private void showHidePassword() {
        imv_password = findViewById(R.id.imv_password);
        imv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                if (isShow) {
                    imv_password.setImageResource(R.drawable.visibility);
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    imv_password.setImageResource(R.drawable.visibility_off);
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                edt_password.setSelection(edt_password.getText().length());
            }
        });
    }


    private void changeLoginPage() {
        tv_signin = findViewById(R.id.tv_signin);
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
        imv_back = findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }

}