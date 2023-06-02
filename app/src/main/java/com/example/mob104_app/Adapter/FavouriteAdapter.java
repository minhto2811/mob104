package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Activities.ProductDetailActivity;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Favourite;
import com.example.mob104_app.Models.Product;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> implements Filterable {

    private Context context;
    private List<Product> list;
    private List<Product> listNew;

    private boolean isFav;

    public FavouriteAdapter(Context context, boolean isFav) {
        this.context = context;
        this.isFav = isFav;
    }

    public void setData(List<Product> list) {
        this.list = list;
        this.listNew = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_layout, parent, false);
        return new FavouriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteHolder holder, int position) {
        Product product = list.get(position);
        if (product != null) {
            Glide.with(context).load(TOOLS.doMainDevice + product.getImage().get(0)).into(holder.imv_image_favourite);
            holder.tv_name_favourite.setText(product.getName());
            holder.tv_status_favourite.setText(product.getStatus());
            holder.tv_price_favourite.setText(TOOLS.convertPrice(product.getPrice() - product.getPrice() * product.getSale() / 100));
            holder.ln_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
                }
            });
            holder.ln_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog1 = TOOLS.createDialog(context);
                    dialog1.show();
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("id_product", product.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String jsonString = postData.toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
                    if (!isFav) {

                        ApiService.apiService.deleteRecently(ACCOUNT.user.get_id(), requestBody).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body() == 1) {
                                        list.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());
                                    }
                                }
                                dialog1.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                dialog1.dismiss();
                            }
                        });

                    } else {
                        ApiService.apiService.delToFavourite(ACCOUNT.user.get_id(), requestBody).enqueue(new Callback<Favourite>() {
                            @Override
                            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    list.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                                dialog1.dismiss();
                            }


                            @Override
                            public void onFailure(Call<Favourite> call, Throwable t) {
                                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                        });

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
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
                    for (Product product : listNew) {
                        if (TOOLS.covertToString(product.getName().toLowerCase()).contains(TOOLS.covertToString(strSearch.toLowerCase().trim()))) {
                            list1.add(product);
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

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        private TextView tv_name_favourite, tv_status_favourite, tv_price_favourite;
        private ImageView imv_image_favourite;
        private LinearLayout ln_favourite, ln_delete;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            tv_price_favourite = itemView.findViewById(R.id.tv_price_favourite);
            tv_name_favourite = itemView.findViewById(R.id.tv_name_favourite);
            tv_status_favourite = itemView.findViewById(R.id.tv_status_favourite);
            imv_image_favourite = itemView.findViewById(R.id.imv_image_favourite);
            ln_delete = itemView.findViewById(R.id.ln_delete);
            ln_favourite = itemView.findViewById(R.id.ln_favourite);
        }
    }
}
