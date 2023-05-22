package com.example.mob104_app.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Adapter.ImageAdapter;
import com.example.mob104_app.Adapter.ProductAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Favourite;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.glailton.expandabletextview.ExpandableTextView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ViewPager viewPager_product;
    private ImageAdapter imageAdapter;
    private TextView tv_text_price, tv_text_price_splashsale, tv_name, tv_category, tv_status, tv_price, tv_price_new_detail, tv_sold_detail, tv_sale;
    private Button btn_add, btn_sold_out;
    private int quan = 1;
    private List<String> listImage;
    private int price_new;
    private ExpandableTextView expand_tv;

    private RatingBar ratingbar_default;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager_product.getCurrentItem() == listImage.size() - 1) {
                viewPager_product.setCurrentItem(0);
            } else {
                viewPager_product.setCurrentItem(viewPager_product.getCurrentItem() + 1);
            }
        }
    };
    private ImageView imv_back, imv_icon_sale, imv_banner, imv_sale, imv_favourite;
    private Product product;

    private RecyclerView rcv_product_related;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mapping();
        back();
        showDetailProduct();
        sale();
        related();
        favourite();
    }

    private void favourite() {

        if (checkFavourite(product.getId())) {
            imv_favourite.setImageResource(R.drawable.mark);
        }

        imv_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFavourite(product.getId())) {
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("id_product", product.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String jsonString = postData.toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
                    ApiService.apiService.addToFavourite(ACCOUNT.user.get_id(), requestBody).enqueue(new Callback<Favourite>() {
                        @Override
                        public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                            if (response.isSuccessful()) {
                                LIST.listFavourite.add(product.getId());
                               imv_favourite.setImageResource(R.drawable.mark);
                            }
                        }

                        @Override
                        public void onFailure(Call<Favourite> call, Throwable t) {
                            Toast.makeText(ProductDetailActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã được thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean checkFavourite(String id) {
        boolean rs = false;
        for (int i = 0; i < LIST.listFavourite.size(); i++) {
            if (LIST.listFavourite.get(i).equals(id)) {
                rs = true;
                break;
            }
        }
        return rs;
    }

    private void related() {
        ProductAdapter productAdapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcv_product_related.setLayoutManager(gridLayoutManager);
        rcv_product_related.setAdapter(productAdapter);
        ApiService.apiService.getListProductRelated(product.getCategory()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getId().equals(product.getId())) {
                            response.body().remove(i);
                            break;
                        }
                    }
                    Collections.sort(response.body(), new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return o1.getStatus().compareToIgnoreCase(o2.getStatus());
                        }
                    });
                    productAdapter.setData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e( "ProductDetailActivity","onFailure");
            }
        });
    }
    private void sale() {
        if (product.getSale() > 0) {
            tv_sale.setText("-" + product.getSale() + "%");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            if (!product.getStatus().equalsIgnoreCase("Còn hàng")) {
                imv_icon_sale.setImageResource(R.drawable.fire);
                tv_sale.setLayoutParams(layoutParams);
                imv_banner.setImageResource(R.drawable.flash_sales_5);
                imv_sale.setImageResource(R.drawable.sold_out);
                btn_sold_out.setText(product.getStatus());
            } else {
                btn_sold_out.setLayoutParams(layoutParams);
            }
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            if (product.getStatus().equalsIgnoreCase("Còn hàng")) {
                imv_icon_sale.setImageResource(R.drawable.hot);
                tv_sale.setLayoutParams(layoutParams);
                imv_banner.setImageResource(R.drawable.flash_sales_5);
                imv_sale.setImageResource(R.drawable.gift);
                btn_sold_out.setLayoutParams(layoutParams);
            } else {
                btn_sold_out.setText(product.getStatus());
                imv_icon_sale.setImageResource(R.drawable.fire);
                tv_sale.setLayoutParams(layoutParams);
                imv_banner.setImageResource(R.drawable.flash_sales_5);
                imv_sale.setImageResource(R.drawable.sold_out);
            }


        }
    }

    private void mapping() {
        imv_favourite = findViewById(R.id.imv_favourite);
        btn_sold_out = findViewById(R.id.btn_sold_out);
        imv_sale = findViewById(R.id.imv_sale);
        rcv_product_related = findViewById(R.id.rcv_product_related);
        imv_banner = findViewById(R.id.imv_banner);
        imv_icon_sale = findViewById(R.id.imv_icon_sale);
        viewPager_product = findViewById(R.id.vpg_product_detail);
        tv_text_price_splashsale = findViewById(R.id.tv_text_price_splashsale);
        tv_sale = findViewById(R.id.tv_sale);
        tv_text_price = findViewById(R.id.tv_text_price);
        tv_name = findViewById(R.id.tv_name_detail);
        expand_tv = findViewById(R.id.expand_tv);
        tv_category = findViewById(R.id.tv_category_detail);
        tv_status = findViewById(R.id.tv_status_detail);
        tv_price = findViewById(R.id.tv_price_detail);
        tv_sold_detail = findViewById(R.id.tv_sold_detail);
        tv_price_new_detail = findViewById(R.id.tv_price_new_detail);
        btn_add = findViewById(R.id.btn_add_detail);
        ratingbar_default = findViewById(R.id.ratingbar_default);
        imageAdapter = new ImageAdapter(this);
        viewPager_product.setAdapter(imageAdapter);
    }


    private void showDetailProduct() {
        product = (Product) getIntent().getSerializableExtra("product");
        Log.e("showDetailProduct: ", product.toString());
        listImage = product.getImage();
        imageAdapter.setData(listImage);
        tv_name.setText(product.getName());
        if (product.getSold() > 0) {
            tv_sold_detail.setText("Đã bán: " + product.getSold());
            ratingbar_default.setVisibility(View.VISIBLE);
        } else {
            tv_sold_detail.setText("Chưa bán sản phẩm nào.");
            ratingbar_default.setVisibility(View.INVISIBLE);
        }
        String description = "Mô tả: " + product.getDescription().trim();

        expand_tv.setText(description);


        tv_category.setText(": " + product.getCategory());
        tv_status.setText(": " + product.getStatus());
        if (product.getSale() > 0) {
            String text = ": " + TOOLS.convertPrice(product.getPrice()) + "VND";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StrikethroughSpan(), 1, text.length(), 0);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 1, text.length(), 0);
            tv_price.setText(spannableString);
            price_new = product.getPrice() - product.getPrice() * product.getSale() / 100;
            tv_price_new_detail.setText(": " + TOOLS.convertPrice(price_new) + "VND");
            tv_text_price_splashsale.setVisibility(View.VISIBLE);
            tv_text_price.setText("Giá bán cũ");
        } else {
            tv_text_price.setText("Giá bán");
            tv_text_price_splashsale.setVisibility(View.INVISIBLE);
            tv_price.setText(": " + TOOLS.convertPrice(product.getPrice()) + "VND");
        }


        handler.postDelayed(runnable, 2000);
        viewPager_product.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog dialog = new BottomSheetDialog(ProductDetailActivity.this, R.style.AppBottomSheetDialogTheme);
                View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.add_layout, null);
                ImageView imv_product_add = view1.findViewById(R.id.imv_product_add);
                ImageButton imb_mines_add = view1.findViewById(R.id.imb_mines_add);
                ImageButton imb_pluss_add = view1.findViewById(R.id.imb_pluss_add);
                Button btn_add_add = view1.findViewById(R.id.btn_add_add);
                TextView tv_name_add = view1.findViewById(R.id.tv_name_add);
                TextView tv_price_add = view1.findViewById(R.id.tv_price_add);
                TextView tv_quantity_add = view1.findViewById(R.id.tv_quantity_add);

                Glide.with(ProductDetailActivity.this).load(TOOLS.doMainDevice + product.getImage().get(0))
                        .into(imv_product_add);
                tv_name_add.setText(product.getName());
                if (product.getSale() > 0) {
                    tv_price_add.setText(TOOLS.convertPrice(price_new * quan) + "VND");
                } else {
                    tv_price_add.setText(TOOLS.convertPrice(product.getPrice() * quan) + "VND");
                }
                tv_quantity_add.setText(String.valueOf(quan));
                imb_mines_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quan <= 1) {
                            Toast.makeText(ProductDetailActivity.this, "Số lượng không được nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        quan--;
                        refreshPrice(product.getPrice() - product.getPrice() * product.getSale() / 100, tv_price_add, tv_quantity_add);
                    }
                });
                imb_pluss_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quan > 1000) {
                            Toast.makeText(ProductDetailActivity.this, "Số lượng không được lớn hơn 1000", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        quan++;
                        refreshPrice(product.getPrice() - product.getPrice() * product.getSale() / 100, tv_price_add, tv_quantity_add);
                    }
                });

                btn_add_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });


                dialog.setContentView(view1);
                dialog.show();

            }
        });
    }


    private void refreshPrice(int price, TextView tv_price, TextView tv_quantity) {
        tv_quantity.setText(String.valueOf(quan));
        tv_price.setText(TOOLS.convertPrice(price * quan) + "VND");
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