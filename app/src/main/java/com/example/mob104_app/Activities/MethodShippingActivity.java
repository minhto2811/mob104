package com.example.mob104_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.MethodShippingAdapter;
import com.example.mob104_app.Interface.MethodShippingOnClick;
import com.example.mob104_app.Models.MethodShipping;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;

import java.util.Objects;

public class MethodShippingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MethodShippingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_shipping);
        mapping();
        setToolbar();
        showListMethodShipping();
    }

    private void showListMethodShipping() {
        LinearLayoutManager manager = new LinearLayoutManager(MethodShippingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MethodShippingActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new MethodShippingAdapter(MethodShippingActivity.this, new MethodShippingOnClick() {
            @Override
            public void ItemClick(MethodShipping methodShipping) {
                Intent intent = new Intent();
                intent.putExtra("method_shipping",methodShipping);
                setResult(RESULT_OK,intent);
                finish();
                overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setData(LIST.listMethodShipping());
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_method_shipping);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_method_shipping);
        recyclerView = findViewById(R.id.rcv_method_shipping);
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