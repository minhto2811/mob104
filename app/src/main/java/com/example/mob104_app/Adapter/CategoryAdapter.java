package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Activities.CategoryActivity;
import com.example.mob104_app.Models.Category;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;




public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private final Context context;
    private List<Category> list;


    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Category> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = list.get(position);
        if(category!=null){
            Glide.with(context)
                    .load(TOOLS.doMainDevice + category.getImage())
                    .into(holder.imv_category);
            holder.item_category.setOnClickListener(v -> {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category", category);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private final ImageView imv_category;
        private final CardView item_category;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            imv_category = itemView.findViewById(R.id.imv_category);
            item_category = itemView.findViewById(R.id.item_category);
        }
    }

}
