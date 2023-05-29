package com.example.mob104_app.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;
import com.example.mob104_app.UI.CartFragment;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolderView> {

    private List<Cart> list;
    private Context context;


    public CartAdapter(Context context) {
        this.context = context;

    }

    public void setData(List<Cart> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.CartHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false);
        return new CartHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartHolderView holder, int position) {
        Cart cart = list.get(position);
        if (cart != null) {
            Glide.with(context).load(TOOLS.doMainDevice + cart.getImage()).into(holder.imv_image);
            holder.tv_name.setText(cart.getName_product());
            holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * cart.getPrice_product()) + "VND");
            holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
            holder.cbox_add.setChecked(TOOLS.checkAllCarts);
            holder.cbox_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbox_add.isChecked()) {
                        TOOLS.quantityCheckAllCarts++;
                        if (TOOLS.quantityCheckAllCarts == list.size()) {
                            TOOLS.checkAllCarts = true;
                            CartFragment.setCheckByItem();
                        }
                    } else {
                        TOOLS.quantityCheckAllCarts--;
                        if (TOOLS.quantityCheckAllCarts < list.size()) {
                            TOOLS.checkAllCarts = false;
                            CartFragment.setCheckByItem();
                        }
                    }
                }
            });
            holder.imv_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cart.getQuantity() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Xóa sản phẩm " + cart.getName_product() + " khỏi giỏ hàng?");
                        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                        return;
                    }
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * cart.getPrice_product()) + "VND");
                }
            });
            holder.imv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText("Tổng tiền: " + TOOLS.convertPrice(cart.getQuantity() * cart.getPrice_product()) + "VND");
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public class CartHolderView extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_quantity, tv_price;
        private ImageView imv_image, imv_add, imv_subtract;
        private CheckBox cbox_add;

        public CartHolderView(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
            imv_add = itemView.findViewById(R.id.imv_add);
            imv_subtract = itemView.findViewById(R.id.imv_subtract);
            cbox_add = itemView.findViewById(R.id.cbox_add);
        }
    }
}
