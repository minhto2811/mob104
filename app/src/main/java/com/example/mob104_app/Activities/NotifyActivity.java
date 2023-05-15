package com.example.mob104_app.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mob104_app.R;

import java.util.Objects;



public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_notify);
        Toolbar toolbar = findViewById(R.id.toolbar_notify);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_notify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }


    private void goBack(){
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}