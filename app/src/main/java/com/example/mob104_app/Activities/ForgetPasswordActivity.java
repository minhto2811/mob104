package com.example.mob104_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mob104_app.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText ed_emailForgotPass;
    Button btn_forgotPass;
    TextView tv_backlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ed_emailForgotPass=findViewById(R.id.forgot_password_email);
        btn_forgotPass=findViewById(R.id.reset_button);
        tv_backlogin=findViewById(R.id.login);
        btn_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPass();
            }
        });
        tv_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private Boolean validateEmail(){
        String val=ed_emailForgotPass.getText().toString();
        String emailPattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (val.isEmpty()) {
            ed_emailForgotPass.setError("Field cannot be empty");
            return true;
        }else if (!val.matches(emailPattern)) {
            ed_emailForgotPass.setError("Invalid email address");
            return false;
        }
        else{
            ed_emailForgotPass.setError(null);
            ed_emailForgotPass.setEnabled(true);
            return false;
        }
    }
    public void forgotPass(){
        if (!validateEmail()){
            return;
        }
    }
}