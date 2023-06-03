package com.example.mob104_app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.CartAdapter;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;
import java.util.Objects;

public class ExportBillActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_fullname_db, tv_numberphone_db, tv_address_db, tv_time_db, tv_ms_db, tv_mp_db, tv_price_db;

    private RecyclerView recyclerView;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_bill);
        mapping();
        setToolbar();
        showDetailBill();
    }

    private void showDetailBill() {
        bill = (Bill) getIntent().getSerializableExtra("bill");
        if (bill != null) {
            tv_fullname_db.setText("Họ tên: " + bill.getName());
            tv_numberphone_db.setText("Số điện thoại: " + bill.getPhone());
            tv_address_db.setText("Địa chỉ: " + bill.getAddress());
            tv_time_db.setText("Thời gian: " + bill.getDate());
            tv_mp_db.setText("Hình thức thanh toán: " + bill.getMethodPayment());
            tv_ms_db.setText("Phương thức vận chuyển: " + bill.getMethodShipping());
            tv_price_db.setText(TOOLS.convertPrice(bill.getTotal()));
            showListProduct(bill.getList());
        }
    }

    private void showListProduct(List<Cart> list) {
        LinearLayoutManager manager = new LinearLayoutManager(ExportBillActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(ExportBillActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        CartAdapter adapter = new CartAdapter(ExportBillActivity.this, true);
        recyclerView.setAdapter(adapter);
        adapter.setData(list);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_ex_bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
        recyclerView = findViewById(R.id.rcv_product_db);
        tv_price_db = findViewById(R.id.tv_price_db);
        tv_mp_db = findViewById(R.id.tv_mp_db);
        tv_ms_db = findViewById(R.id.tv_ms_db);
        tv_time_db = findViewById(R.id.tv_time_db);
        tv_address_db = findViewById(R.id.tv_address_db);
        tv_numberphone_db = findViewById(R.id.tv_numberphone_db);
        tv_fullname_db = findViewById(R.id.tv_fullname_db);
        toolbar = findViewById(R.id.toolbar_exbill);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.cancel_bill) {
            Intent intent = new Intent(ExportBillActivity.this,CancelOrderActivity.class);
            intent.putExtra("id_bill",bill.get_id());
            startActivity(intent);
            overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(bill.getStatus()==0) {
            getMenuInflater().inflate(R.menu.menu_cancel_bill, menu);
            MenuItem item = menu.getItem(0);
            String name = item.toString();
            SpannableString spannableString = new SpannableString(name);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, name.length(), 0);
            item.setTitle(spannableString);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }
}