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
import android.widget.Toast;

import com.example.mob104_app.R;

public class SignupActivity extends AppCompatActivity {
    EditText ed_emailSignup,ed_passSignup,ed_repass;
    Button btn_signup;
    TextView tv_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ed_emailSignup=findViewById(R.id.signup_email);
        ed_passSignup=findViewById(R.id.signup_password);
        ed_repass=findViewById(R.id.signup_confirm_password);
        btn_signup=findViewById(R.id.signup_button);
        tv_login=findViewById(R.id.login);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private Boolean validateEmail(){
        String val=ed_emailSignup.getText().toString();
        String noWhiteSpace="\\A\\w{4,20}\\z";
        String emailPattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (val.isEmpty()){
            ed_emailSignup.setError("Field cannot be empty");
            return true;
        } else if (val.matches(noWhiteSpace)) {
            ed_emailSignup.setError("White Spaces are not allowed");
            return false;
        }else if (!val.matches(emailPattern)) {
            ed_emailSignup.setError("Invalid email address");
            return false;
        }
        else{
            ed_emailSignup.setError(null);
            ed_emailSignup.setEnabled(true);
            return false;
        }
    }
    private Boolean validatePassword(){
        String valpw=ed_passSignup.getText().toString();
        String passwordPattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        String rePass=ed_repass.getText().toString();
        if (valpw.isEmpty()){
            ed_passSignup.setError("Field cannot be empty");
            return true;
        } else if (!valpw.matches(passwordPattern)) {
            ed_passSignup.setError("Password must contain at least one digit [0-9]," +
                    "lowercase Latin character [a-z]," +
                    "uppercase Latin character [A-Z]" +
                    "special character like ! @ # & ( )" +
                    "a length of at least 8 characters and a maximum of 20 characters");
            return false;
        }
        else if (!valpw.equals(rePass)){
            ed_repass.setError("Password does not match");
            return false;

        }
        else{
            ed_passSignup.setError(null);
            ed_passSignup.setEnabled(true);
            return false;
        }
    }

    private Boolean validateRepass() {
        String rePass = ed_repass.getText().toString();
        if (rePass.isEmpty()) {
            ed_repass.setError("Field cannot be empty");
            return true;
        }else{
            ed_repass.setError(null);
            ed_repass.setEnabled(true);
            return false;
        }
    }

    public void signUpUser(){
        if (!validateEmail() | !validatePassword() | !validateRepass()){
            return;
        }
    }

    public void ShowHidePass(View view) {

        if(view.getId()==R.id.show_passsignup_btn){
            if(ed_passSignup.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_off_24);
                //Show Password
                ed_passSignup.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_24);
                //Hide Password
                ed_passSignup.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHideRePass(View view) {

        if(view.getId()==R.id.show_repass_btn){
            if(ed_repass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_off_24);
                //Show Password
                ed_repass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.baseline_visibility_24);
                //Hide Password
                ed_repass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}