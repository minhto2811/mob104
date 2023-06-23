package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Activities.ProductDetailActivity;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;
import com.example.mob104_app.UI.CartFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cart> list;
    private final Context context;
    private final boolean type1;
    private static final int VIEW_TYPE_TYPE = 0;
    private static final int VIEW_TYPE_TYPE1 = 1;


    public CartAdapter(Context context, boolean type1) {
        this.context = context;
        this.type1 = type1;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Cart> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (type1) {
            return VIEW_TYPE_TYPE1;
        } else {
            return VIEW_TYPE_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_TYPE) {
            View view = inflater.inflate(R.layout.cart_layout, parent, false);
            return new CartHolderView(view);
        } else {
            View view = inflater.inflate(R.layout.cart_layout_1, parent, false);
            return new CartHolderView1(view);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        Cart cart = list.get(position);
        if (cart != null) {
            int lastPrice = (cart.getPrice_product()*(100-cart.getSale())/100)*cart.getQuantity();
            if (holder1 instanceof CartHolderView) {
                CartHolderView holder = (CartHolderView) holder1;

                Glide.with(context).load(TOOLS.doMainDevice + cart.getImage()).into(holder.imv_image);
                holder.tv_name.setText(cart.getName_product());
                holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * lastPrice));
                holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                holder.cbox_add.setChecked(TOOLS.checkAllCarts);
                if (TOOLS.checkAllCarts) {
                    LIST.listBuyCart.add(cart);
                }

                holder.imv_image.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("id_product", cart.getId_product());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
                });
                holder.cbox_add.setOnClickListener(v -> {
                    if (holder.cbox_add.isChecked()) {
                        LIST.listBuyCart.add(cart);
                        if (LIST.listBuyCart.size() == list.size()) {
                            TOOLS.checkAllCarts = true;
                            CartFragment.setCheckByItem();
                        }
                    } else {
                        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
                            if (LIST.listBuyCart.get(i).get_id().equals(cart.get_id())) {
                                LIST.listBuyCart.remove(i);
                                break;
                            }
                        }
                        if (LIST.listBuyCart.size() < list.size()) {
                            TOOLS.checkAllCarts = false;
                            CartFragment.setCheckByItem();
                        }
                    }
                    CartFragment.showLayoutPay(LIST.listBuyCart);
                });
                holder.imv_subtract.setOnClickListener(v -> {
                    if (cart.getQuantity() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Xóa sản phẩm " + cart.getName_product() + " khỏi giỏ hàng?");
                        builder.setPositiveButton("Xóa", (dialog, which) -> deleteCart(holder, cart.get_id()));
                        builder.setNegativeButton("Hủy", null);
                        builder.create().show();
                        return;
                    }
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * lastPrice));
                    replaceCartItem(cart);
                });
                holder.imv_add.setOnClickListener(v -> {
                    cart.setQuantity(cart.getQuantity() + 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * lastPrice));
                    replaceCartItem(cart);
                });
                holder.ln_delete.setOnClickListener(v -> deleteCart(holder, cart.get_id()));
            } else if (holder1 instanceof CartHolderView1) {
                CartHolderView1 holder = (CartHolderView1) holder1;
                Glide.with(context).load(TOOLS.doMainDevice + cart.getImage()).into(holder.imv_image);
                holder.tv_name.setText(cart.getName_product());
                holder.tv_price.setText("Đơn giá: "+TOOLS.convertPrice(lastPrice));
                holder.tv_quantity.setText("Số Lượng: "+cart.getQuantity());
            }
        }

    }

    private void replaceCartItem(Cart cart) {
        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
            if(LIST.listBuyCart.get(i).get_id().equals(cart.get_id())){
                LIST.listBuyCart.get(i).setQuantity(cart.getQuantity());
                break;
            }
        }
        CartFragment.showLayoutPay(LIST.listBuyCart);
    }

    private void deleteCart(CartHolderView holder, String id) {
        Dialog dialog = TOOLS.createDialog(context);
        dialog.show();
        ApiService.apiService.deleteCart(id).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body() == 1) {
                        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
                            if (LIST.listBuyCart.get(i).get_id().equals(id)) {
                                LIST.listBuyCart.remove(i);
                                CartFragment.showLayoutPay(LIST.listBuyCart);
                                break;
                            }
                        }
                        list.remove(holder.getAdapterPosition());
                        if (list.size() == 0) {
                            CartFragment.ln_cart_emty.setVisibility(View.VISIBLE);
                        }
                        notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class CartHolderView extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_quantity;
        private final TextView tv_price;
        private final ImageView imv_image;
        private final ImageView imv_add;
        private final ImageView imv_subtract;
        private final CheckBox cbox_add;
        private final LinearLayout ln_delete;


        public CartHolderView(@NonNull View itemView) {
            super(itemView);
            ln_delete = itemView.findViewById(R.id.ln_delete);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
            imv_add = itemView.findViewById(R.id.imv_add);
            imv_subtract = itemView.findViewById(R.id.imv_subtract);
            cbox_add = itemView.findViewById(R.id.cbox_add);
        }
    }

    public static class CartHolderView1 extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_quantity;
        private final TextView tv_price;
        private final ImageView imv_image;


        public CartHolderView1(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
        }
    }
}
