package com.example.mob104_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mob104_app.R;

public class LoginActivity extends AppCompatActivity {

    EditText ed_email,ed_password;
    Button btn_login;
    TextView tv_signUp,tv_forgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_email=findViewById(R.id.login_email);
        ed_password=findViewById(R.id.login_password);

        btn_login=findViewById(R.id.login_button);
        tv_signUp=findViewById(R.id.signup);
        tv_forgotpass=findViewById(R.id.forgot_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private Boolean validateEmail(){
       String val=ed_email.getText().toString();
       String emailPattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
       if (val.isEmpty()) {
           ed_email.setError("Field cannot be empty");
           return true;
       }else if (!val.matches(emailPattern)) {
           ed_email.setError("Invalid email address");
           return false;
       }
       else{
           ed_email.setError(null);
           ed_email.setEnabled(true);
           return false;
       }
    }
    private Boolean validatePassword(){
        String valpw=ed_password.getText().toString();
        String passwordPattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        if (valpw.isEmpty()){
            ed_password.setError("Field cannot be empty");
            return true;
        }
        else if (!valpw.matches(passwordPattern)) {
            ed_password.setError("Password must contain at least one digit [0-9]," +
                    "lowercase Latin character [a-z]," +
                    "uppercase Latin character [A-Z]" +
                    "special character like ! @ # & ( )" +
                    "a length of at least 8 characters and a maximum of 20 characters");
            return false;
        }
        else{
            ed_password.setError(null);
            ed_password.setEnabled(true);
            return false;
        }
    }


    public void loginUser(){
        if (!validateEmail() | !validatePassword()){
            return;
        }
    }

    public void ShowHidePass(View view) {

        if(view.getId()==R.id.show_pass_btn){
            if(ed_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_off_24);
                //Show Password
                ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_24);
                //Hide Password
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}