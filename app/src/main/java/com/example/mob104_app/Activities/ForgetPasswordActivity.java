package com.example.mob104_app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextView tv_err_email;
    private EditText edt_email;
    private Button btn_forgot_password;
    private ImageView imv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mapping();
        back();
        validateEmail();
    }

    private void validateEmail(){
        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TOOLS.isValidEmail(edt_email.getText().toString())){
                    tv_err_email.setText("Email không hợp lệ!");
                    return;
                }
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        Toast.makeText(this, "Chúng tôi đã gửi mail xác nhận mật khẩu.", Toast.LENGTH_LONG).show();
    }
    private void back(){
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void mapping() {
        edt_email = findViewById(R.id.edt_email);
        tv_err_email = findViewById(R.id.tv_err_email);
        imv_back = findViewById(R.id.imv_back);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }
}