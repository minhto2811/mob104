package com.example.mob104_app.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
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
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEXT = 555;
    private ImageView imv_back, imv_password;
    private TextView tv_signup, tv_err_username, tv_err_password, tv_rules;
    private EditText edt_username, edt_password;
    private Button btn_login;
    private boolean isShow = true, checkUser, checkPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        back();
        changeRegistrationPage();
        login();
        showHidePassword();
    }

    private void mapping() {
        imv_back = findViewById(R.id.imv_back);
        imv_password = findViewById(R.id.imv_password);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);
        tv_err_username = findViewById(R.id.tv_err_username);
        tv_err_password = findViewById(R.id.tv_err_password);
    }

    private void login() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void back() {
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void checkAccount(String user, String pass) {
        Dialog dialog = new Dialog(LoginActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_watting, null);
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

        ApiService.apiService.loginUser(requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    if (token != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        TOOLS.saveToken(LoginActivity.this, token);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }

    private void showHidePassword() {
        imv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                if (isShow) {
                    imv_password.setImageResource(R.drawable.visibility_off);
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    imv_password.setImageResource(R.drawable.visibility);
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                edt_password.setSelection(edt_password.getText().length());
            }
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