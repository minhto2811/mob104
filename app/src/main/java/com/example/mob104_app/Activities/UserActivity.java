package com.example.mob104_app.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText edt_fullname_user, edt_numberphone_user, edt_email_user, edt_select_date_user;
    private TextView tv_err_fullname_user, tv_err_numberphone_user, tv_err_email_user, tv_err_select_date_user;
    private RadioButton rbtn_male, rbtn_female;
    private CheckBox cbox_confirm;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mapping();
        setToolbar();
        setData();
        selectDate();
        update();
    }

    private void selectDate() {
        edt_select_date_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                edt_select_date_user.setText(dateFormat.format(calendar.getTime()));
                            }
                        },
                        (ACCOUNT.user.getDate() != null) ? Integer.valueOf(ACCOUNT.user.getDate().substring(6, 10)) : calendar.get(Calendar.YEAR),
                        (ACCOUNT.user.getDate() != null) ? Integer.valueOf(ACCOUNT.user.getDate().substring(3, 5)) : calendar.get(Calendar.MONTH),
                        (ACCOUNT.user.getDate() != null) ? Integer.valueOf(ACCOUNT.user.getDate().substring(0, 2)) : calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    private void update() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFiel(edt_fullname_user, tv_err_fullname_user)
                        || !checkFiel(edt_numberphone_user, tv_err_numberphone_user)
                        || !checkFiel(edt_email_user, tv_err_email_user)
                        || !checkFiel(edt_select_date_user, tv_err_select_date_user)) {
                    return;
                }
                if (!cbox_confirm.isChecked()) {
                    Toast.makeText(UserActivity.this, "Vui lòng xác nhận thông tin cập nhật!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Dialog dialog = TOOLS.createDialog(UserActivity.this);
                dialog.show();
                User user = ACCOUNT.user;
                user.setFullname(edt_fullname_user.getText().toString().trim());
                user.setNumberphone(edt_numberphone_user.getText().toString().trim());
                user.setEmail(edt_email_user.getText().toString().trim());
                user.setSex(rbtn_male.isChecked() ? true : false);
                user.setDate(edt_select_date_user.getText().toString().trim());
                ApiService.apiService.updateInfo(user).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.isSuccessful()) {
                            if (response.body() == 1) {
                                ACCOUNT.user = user;
                                TOOLS.saveUser(UserActivity.this, user);
                                Toast.makeText(UserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(UserActivity.this, "Không có sự thay đổi thông tin", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(UserActivity.this, "Email hoặc số điện thoại đã được sử dụng!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private boolean checkFiel(EditText editText, TextView textView) {
        if (editText.getText().toString().length() == 0) {
            editText.requestFocus();
            textView.setVisibility(View.VISIBLE);
            return false;
        }

        if (editText == edt_numberphone_user) {
            if (!TOOLS.isValidPhoneNumber(editText.getText().toString().trim())) {
                textView.setVisibility(View.VISIBLE);
                editText.requestFocus();
                return false;
            }
        } else if (editText == edt_email_user) {
            if (!TOOLS.isValidEmail(editText.getText().toString().trim())) {
                textView.setVisibility(View.VISIBLE);
                editText.requestFocus();
                return false;
            }
        }
        textView.setVisibility(View.GONE);
        editText.clearFocus();
        return true;
    }


    private void setData() {
        edt_fullname_user.setText(ACCOUNT.user.getFullname());
        edt_numberphone_user.setText(ACCOUNT.user.getNumberphone());
        edt_email_user.setText(ACCOUNT.user.getEmail());
        if (ACCOUNT.user.getSex()) {
            rbtn_male.setChecked(true);
        } else {
            rbtn_female.setChecked(true);
        }
        edt_select_date_user.setText(ACCOUNT.user.getDate());
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
        btn_confirm = findViewById(R.id.btn_confirm);
        cbox_confirm = findViewById(R.id.cbox_confirm);
        rbtn_female = findViewById(R.id.rbtn_female);
        rbtn_male = findViewById(R.id.rbtn_male);
        tv_err_select_date_user = findViewById(R.id.tv_err_select_date_user);
        tv_err_email_user = findViewById(R.id.tv_err_email_user);
        tv_err_numberphone_user = findViewById(R.id.tv_err_numberphone_user);
        tv_err_fullname_user = findViewById(R.id.tv_err_fullname_user);
        edt_select_date_user = findViewById(R.id.edt_select_date_user);
        edt_email_user = findViewById(R.id.edt_email_user);
        edt_numberphone_user = findViewById(R.id.edt_numberphone_user);
        edt_fullname_user = findViewById(R.id.edt_fullname_user);
        toolbar = findViewById(R.id.toolbar_user);
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