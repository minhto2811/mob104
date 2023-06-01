package com.example.mob104_app.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputLayout til_username, til_code, til_password_new;
    private EditText edt_username, edt_code, edt_password_new;
    private Button btn_forgot_password;
    private ImageView imv_back;
    private boolean OK;

    private FloatingActionButton fbtn_phone;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mapping();
        createDialog();
        back();
        resetPassword();
        phone();
    }

    private void createDialog() {
        dialog = TOOLS.createDialog(ForgetPasswordActivity.this);
    }

    private void phone() {
        fbtn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        String phoneNumber = "0981999666";
                        Uri dialUri = Uri.parse("tel:" + phoneNumber);
                        Intent intent = new Intent(Intent.ACTION_VIEW, dialUri);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(ForgetPasswordActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .check();

            }
        });
    }

    private boolean checkInputFiel(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().length() < 8) {
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            editText.requestFocus();
            if (editText == edt_username) {
                textInputLayout.setHint("Tài khoản phải nhiều hơn 7 kí tự!");
            } else if (editText == edt_password_new) {
                textInputLayout.setHint("Mật khẩu phải nhiều hơn 7 kí tự!");
            } else {
                textInputLayout.setHint("Mã xác minh không hợp lệ");
            }
            return false;
        }
        if (editText == edt_username) {
            textInputLayout.setHint("Nhập tài tên tài khoản");
        } else if (editText == edt_password_new) {
            textInputLayout.setHint("Nhập mật khẩu mới");
        } else {
            textInputLayout.setHint("Nhập mã xác minh");
        }
        editText.clearFocus();
        return true;
    }

    private void resetPassword() {
        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputFiel(edt_username, til_username)) {
                    return;
                }
                if (!OK) {
                    dialog.show();

                    ApiService.apiService.getCodeConfirmPassword(edt_username.getText().toString().trim()).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == 1) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Kiểm tra email được liên kết với tài khoản", Toast.LENGTH_SHORT).show();
                                    OK = true;
                                    showInput();
                                } else if (response.body() == -1) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Tài khoản chưa được liên kết với email nào, hãy gọi cho chúng tôi để đặt mật khẩu mới", Toast.LENGTH_SHORT).show();
                                } else if (response.body() == 0) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Lỗi khi gửi email xác thực", Toast.LENGTH_SHORT).show();
                                }
                                dialog.hide();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(ForgetPasswordActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });
                } else {
                    if (!checkInputFiel(edt_code, til_code)
                            || !checkInputFiel(edt_password_new, til_password_new)
                            || !PasswordActivity.validatePass(edt_password_new, til_password_new, ForgetPasswordActivity.this)) {
                        return;
                    }
                    dialog.show();
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("password", edt_password_new.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String jsonString = postData.toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
                    ApiService.apiService.changePasswordNew(edt_code.getText().toString().trim(), requestBody).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == 1) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Thay đổi mật khẩu mới thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("username", edt_username.getText().toString().trim());
                                    intent.putExtra("password", edt_password_new.getText().toString().trim());
                                    setResult(RESULT_OK, intent);
                                    onBackPressed();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Mã thông báo không hợp lệ hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                                }
                            }
                            dialog.hide();
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(ForgetPasswordActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });


                }
            }
        });
    }


    private void showInput() {
        til_code.setVisibility(View.VISIBLE);
        til_password_new.setVisibility(View.VISIBLE);
        btn_forgot_password.setText("Xác nhận");
    }

    private void back() {
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void mapping() {
        fbtn_phone = findViewById(R.id.fbtn_phone);
        edt_password_new = findViewById(R.id.edt_password_new);
        edt_code = findViewById(R.id.edt_code);
        til_password_new = findViewById(R.id.til_passnew);
        til_code = findViewById(R.id.til_code);
        edt_username = findViewById(R.id.edt_username);
        til_username = findViewById(R.id.til_username);
        imv_back = findViewById(R.id.imv_back);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}