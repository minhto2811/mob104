package com.example.mob104_app.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.NotifyAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Notify;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotifyActivity extends AppCompatActivity {
    private Toolbar  toolbar;
  private RecyclerView  recyclerView;
  private NotifyAdapter adapter;
  private LinearLayout ln_check_notify;
  private Button btn_login_notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_notify);
        mapping();
        setToolbar();
        if(ACCOUNT.user==null){
            Log.e( "onCreate: ", ACCOUNT.user.toString());
            ln_check_notify.setVisibility(View.VISIBLE);
            return;
        }
        showListNotify();
        login();
    }

    private void login() {
        btn_login_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifyActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
            }
        });

    }

    private void showListNotify() {
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new NotifyAdapter(this);
        recyclerView.setAdapter(adapter);
        getListData();
    }

    private void getListData() {
        Dialog dialog = TOOLS.createDialog(this);
        dialog.show();
        ApiService.apiService.getNotify(ACCOUNT.user.get_id()).enqueue(new Callback<List<Notify>>() {
            @Override
            public void onResponse(Call<List<Notify>> call, Response<List<Notify>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    Collections.reverse(response.body());
                    adapter.setData(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Notify>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(NotifyActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_notify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
         toolbar = findViewById(R.id.toolbar_notify);
         recyclerView = findViewById(R.id.rcv_notify);
         ln_check_notify = findViewById(R.id.ln_check_notify);
        btn_login_notify = findViewById(R.id.btn_login_notify);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(ACCOUNT.user!=null){
            showListNotify();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }

}