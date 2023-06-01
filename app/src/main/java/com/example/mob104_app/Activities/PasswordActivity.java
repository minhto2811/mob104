package com.example.mob104_app.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edt_pass_old, edt_pass_new_1, edt_pass_new_2;
    private TextInputLayout til_pass_old, til_pass_new_1, til_pass_new_2;
    private Button btn_change_password_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mapping();
        setToolbar();
        changePassword();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_password);
        til_pass_old = findViewById(R.id.til_pass_old);
        til_pass_new_1 = findViewById(R.id.til_pass_new_1);
        til_pass_new_2 = findViewById(R.id.til_pass_new_2);
        edt_pass_old = findViewById(R.id.edt_pass_old);
        edt_pass_new_1 = findViewById(R.id.edt_pass_new_1);
        edt_pass_new_2 = findViewById(R.id.edt_pass_new_2);
        btn_change_password_now = findViewById(R.id.btn_change_password_now);

    }

    private void changePassword() {
        btn_change_password_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputFiel(edt_pass_old, til_pass_old,PasswordActivity.this) || !checkInputFiel(edt_pass_new_1, til_pass_new_1,PasswordActivity.this) || !checkInputFiel(edt_pass_new_2, til_pass_new_2,PasswordActivity.this)) {
                    return;
                }
                if (!checkPasswordNew(edt_pass_new_1, edt_pass_new_2, til_pass_new_2)) {
                    return;
                }
                if (!validatePass(edt_pass_new_1, til_pass_new_1,PasswordActivity.this) || !validatePass(edt_pass_new_2, til_pass_new_2,PasswordActivity.this)) {
                    return;
                }

                Dialog dialog = TOOLS.createDialog(PasswordActivity.this);

                dialog.show();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", ACCOUNT.user.getUsername());
                    postData.put("password", edt_pass_old.getText().toString().trim());
                    postData.put("passwordnew", edt_pass_new_2.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString = postData.toString();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);


                ApiService.apiService.changePassword(requestBody).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.isSuccessful()&&response.body()==1) {
                                ACCOUNT.user.setPassword(edt_pass_new_1.getText().toString().trim());
                                TOOLS.saveUser(PasswordActivity.this, ACCOUNT.user);
                                Toast.makeText(PasswordActivity.this, "Thay đổi mật khẩu thành công ", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                        }else {
                            Toast.makeText(PasswordActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(PasswordActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static boolean checkInputFiel(EditText editText, TextInputLayout textInputLayout,Context context) {
        String hint = "Nhập mật khẩu mới";
        if (editText.getText().toString().length() < 8) {
            editText.requestFocus();
            textInputLayout.setHintTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
            textInputLayout.setHint("Mật khẩu phải nhiều hơn 7 kí tự");
            return false;
        }
        textInputLayout.setHint(hint);
        editText.clearFocus();
        return true;

    }

    public static boolean validatePass(EditText editText, TextInputLayout textInputLayout, Context context) {
        String hint = textInputLayout.getHint().toString();
        boolean a = false, b = false, c = false;
        for (char ch : editText.getText().toString().toCharArray()) {
            if (Character.isUpperCase(ch)) {
                a = true;
            } else if (Character.isDigit(ch)) {
                b = true;
            } else {
                c = true;
            }
        }
        if (!a || !b || !c) {
            editText.requestFocus();
            textInputLayout.setHintTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
            textInputLayout.setHint("Mật khẩu >= 8 kí tự chứa A-Z,a-z,0-9.");
            return false;
        }
        textInputLayout.setHint(hint);
        editText.clearFocus();
        return true;
    }

    private boolean checkPasswordNew(EditText pass1, EditText pass2, TextInputLayout til2) {
        String hint = "Nhập mật khẩu mới. . .";
        if (!pass1.getText().toString().trim().equals(pass2.getText().toString().trim())) {
            til2.setHint("Mật khẩu không trùng khớp!");
            til2.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            pass2.requestFocus();
            return false;
        }
        til2.setHint(hint);
        pass2.clearFocus();
        return true;
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }

}