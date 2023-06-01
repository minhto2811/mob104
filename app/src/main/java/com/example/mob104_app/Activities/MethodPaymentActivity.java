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

import com.example.mob104_app.Adapter.MethodPaymentAdapter;
import com.example.mob104_app.Interface.MethodPaymentOnClick;
import com.example.mob104_app.Models.MethodPayment;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;

import java.util.Objects;

public class MethodPaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MethodPaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_payment);
        mapping();
        setToolbar();
        showListPayment();
    }

    private void showListPayment() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MethodPaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MethodPaymentActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new MethodPaymentAdapter(MethodPaymentActivity.this, new MethodPaymentOnClick() {
            @Override
            public void ItemClick(MethodPayment methodPayment) {
                Intent intent = new Intent();
                intent.putExtra("method_payment", methodPayment);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setData(LIST.listMethodPayment());
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_method_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_method_payment);
        recyclerView = findViewById(R.id.rcv_method_payment);
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