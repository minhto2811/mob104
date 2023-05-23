package com.example.mob104_app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Adapter.ProductAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Banner;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BannerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView imv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        back();
        Banner banner = (Banner) getIntent().getSerializableExtra("banner");
        ImageView imageView = findViewById(R.id.imv_banner_event);
        recyclerView = findViewById(R.id.rcv_product_event);
        Glide.with(this)
                .load(TOOLS.doMainDevice + banner.getImage())
                .into(imageView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter(this);
        recyclerView.setAdapter(productAdapter);
        getListProductByBanner(banner.getEvent());
    }

    private void getListProductByBanner(String code) {
        if (LIST.listProductByBanner.isEmpty()) {
            ApiService.apiService.getListProductByBanner(code).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        Collections.sort(response.body(), new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return o1.getStatus().compareToIgnoreCase(o2.getStatus());
                            }
                        });
                        LIST.listProductByBanner = response.body();
                        productAdapter.setData(LIST.listProductByBanner);
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Toast.makeText(BannerActivity.this, "Lỗi server!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            productAdapter.setData(LIST.listProductByBanner);
        }
    }

    private void back() {
        imv_back = findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }

}