package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Activities.ProductDetailActivity;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    private final Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    private List<Product> list;
    private List<Product> listNew;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> list) {
        this.list = list;
        this.listNew = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false);
        return new ProductHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = list.get(position);
        if (product != null) {
            holder.tv_name.setText(product.getName());
            holder.tv_sale.setText("-" + product.getSale() + "%");
            holder.tv_price.setText(TOOLS.convertPrice(product.getPrice() - product.getPrice()* product.getSale()/100));
            Glide
                    .with(context)
                    .load(TOOLS.doMainDevice + product.getImage().get(0))
                    .into(holder.imv_image);

            if(!product.getStatus().equalsIgnoreCase("Còn hàng")){
                holder.imv_image_1.setVisibility(View.VISIBLE);
            }

            holder.layout_product.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bạn chắc chắn muốn ẩn sản phẩm này?");
                builder.setPositiveButton("Tiếp tục", (dialog, which) -> {

                });

                builder.setNegativeButton("Quay lại", (dialog, which) -> {

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            });

            holder.btn_show_info.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list = listNew;
                } else {
                    List<Product> list1 = new ArrayList<>();
                    for (Product student : listNew) {
                        if (TOOLS.covertToString(student.getName().toLowerCase()).contains(TOOLS.covertToString(strSearch.toLowerCase().trim()))) {
                            list1.add(student);
                        }
                    }
                    list = list1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ProductHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_price;
        private final TextView tv_sale;
        private final ImageView imv_image,imv_image_1;
        private final CardView layout_product;
        private final Button btn_show_info;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sale = itemView.findViewById(R.id.tv_sale);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
            imv_image_1 = itemView.findViewById(R.id.imv_image_1);
            layout_product = itemView.findViewById(R.id.layout_product);
            btn_show_info = itemView.findViewById(R.id.btn_show_info);
        }

    }


}
