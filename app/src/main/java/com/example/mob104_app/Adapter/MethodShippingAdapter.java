package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Interface.MethodShippingOnClick;
import com.example.mob104_app.Models.MethodShipping;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;


public class MethodShippingAdapter extends RecyclerView.Adapter<MethodShippingAdapter.MethodShippingViewHolder> {
    private List<MethodShipping> list;

    private final MethodShippingOnClick methodShippingOnClick;

    public MethodShippingAdapter(MethodShippingOnClick methodShippingOnClick) {
        this.methodShippingOnClick = methodShippingOnClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MethodShipping> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MethodShippingAdapter.MethodShippingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_shipping_layout, parent, false);
        return new MethodShippingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MethodShippingAdapter.MethodShippingViewHolder holder, int position) {
        MethodShipping shipping = list.get(position);
        if (shipping != null) {
            holder.ln_ms.setBackgroundResource(shipping.getColor());
            holder.tv_type_ms.setText(shipping.getName());
            holder.tv_price_ms.setText(TOOLS.convertPrice(shipping.getPrice()));
            holder.tv_time_ms.setText(shipping.getTime());
            holder.ln_ms.setOnClickListener(v -> methodShippingOnClick.ItemClick(shipping));
        }

    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class MethodShippingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_type_ms, tv_price_ms, tv_time_ms;
        private final LinearLayout ln_ms;

        public MethodShippingViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type_ms = itemView.findViewById(R.id.tv_type_ms);
            tv_price_ms = itemView.findViewById(R.id.tv_price_ms);
            tv_time_ms = itemView.findViewById(R.id.tv_time_ms);
            ln_ms = itemView.findViewById(R.id.ln_ms);
        }
    }
}
