package com.example.mob104_app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.AddressAdapter;
import com.example.mob104_app.Models.Address;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;

public class AddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private LinearLayout ln_add_address;

    private static Activity activity;
    private boolean choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        activity = this;
        mapping();
        setToolbar();
        getListAddress();
        addAddress();
    }

    private void getListAddress() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(AddressActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        choose = getIntent().getBooleanExtra("choose", false);
        if (choose) {
            getSupportActionBar().setTitle(R.string.title_choose_address_pay);
            adapter = new AddressAdapter(AddressActivity.this, true);
        } else {
            adapter = new AddressAdapter(AddressActivity.this, false);
        }

        adapter.setData(LIST.listAddress);
        recyclerView.setAdapter(adapter);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_address);
        ln_add_address = findViewById(R.id.ln_add_address);
        recyclerView = findViewById(R.id.rcv_address);
    }

    private void addAddress() {
        ln_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getIntent().removeExtra("choose");
    }
}