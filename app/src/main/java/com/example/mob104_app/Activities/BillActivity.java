package com.example.mob104_app.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mob104_app.Adapter.ViewPagerAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mapping();
        setToolbar();
        if(!LIST.listBill.isEmpty()){
            setTablayout();
        }else {
            Dialog dialog = TOOLS.createDialog(BillActivity.this);
            dialog.show();
            ApiService.apiService.getBill(ACCOUNT.user.get_id()).enqueue(new Callback<List<Bill>>() {
                @Override
                public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        LIST.listBill.clear();
                        LIST.listBill = response.body();
                        setTablayout();
                    }
                }

                @Override
                public void onFailure(Call<List<Bill>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(BillActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    private void setTablayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        int status = getIntent().getIntExtra("status", 0);
        viewPager.setCurrentItem(status);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_bill_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_bill);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
    }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LIST.listBill.clear();
    }
}