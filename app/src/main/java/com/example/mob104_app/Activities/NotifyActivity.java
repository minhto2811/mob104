package com.example.mob104_app.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotifyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NotifyAdapter adapter;
    private LinearLayout ln_notify_emty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_notify);
        mapping();
        if (ACCOUNT.user != null) {
            setToolbar();
            setNotify();
        }
    }

    private void setNotify() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new NotifyAdapter(this);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        Dialog dialog = TOOLS.createDialog(this);
        dialog.show();
        ApiService.apiService.getNotify(ACCOUNT.user.get_id()).enqueue(new Callback<List<Notify>>() {
            @Override
            public void onResponse(Call<List<Notify>> call, Response<List<Notify>> response) {
                if (response.isSuccessful() && response.body() !=null && !response.body().isEmpty()) {
                    adapter.setData(response.body());
                    ln_notify_emty.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Notify>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(NotifyActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
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
        ln_notify_emty = findViewById(R.id.ln_notify_emty);
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
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }
}