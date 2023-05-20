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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Adapter.ImageAdapter;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import io.github.glailton.expandabletextview.ExpandableTextView;

public class ProductDetailActivity extends AppCompatActivity {
    private ViewPager viewPager_product;
    private ImageAdapter imageAdapter;
    private TextView tv_text_price, tv_text_price_splashsale, tv_name, tv_category, tv_status, tv_price, tv_price_new_detail, tv_sold_detail, tv_sale;
    private Button btn_add;
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
    private ImageView imv_back, imv_icon_sale, imv_banner, imv_sale;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mapping();
        back();
        showDetailProduct();
        sale();
    }

    private void sale() {
        if (product.getSale() > 0) {
            tv_sale.setText("-" + product.getSale() + "%");
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            imv_icon_sale.setImageResource(R.drawable.fire);
            tv_sale.setLayoutParams(layoutParams);
            imv_banner.setImageResource(R.drawable.flash_sales_5);
            imv_sale.setImageResource(R.drawable.gift);
        }
    }

    private void mapping() {
        imv_sale = findViewById(R.id.imv_sale);
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
                        .error(R.drawable.watting)
                        .placeholder(R.drawable.watting)
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