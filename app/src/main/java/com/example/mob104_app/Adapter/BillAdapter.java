package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Activities.ExportBillActivity;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private final Context context;
    private List<Bill> list;

    public BillAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Bill> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_layout, parent, false);
        return new BillViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BillAdapter.BillViewHolder holder, int position) {
        Bill bill = list.get(position);
        if (bill != null) {
            holder.tv_id_bill.setText("Mã đơn: "+bill.get_id().toUpperCase());
            holder.tv_fullname_bill.setText(bill.getName());
            holder.tv_numberphone_bill.setText(bill.getPhone());
            holder.tv_date_bill.setText("Thời gian đặt: " + bill.getDate());
            StringBuilder s = new StringBuilder("Sản phẩm: ");
            for (int i = 0; i < bill.getList().size(); i++) {
                s.append("x").append(bill.getList().get(i).getQuantity()).append(" ").append(bill.getList().get(i).getName_product());
                if (i < bill.getList().size() - 1) {
                    s.append(", ");
                }
            }
            holder.tv_product_bill.setText(s.toString());
            holder.tv_price_bill.setText(TOOLS.convertPrice(bill.getTotal()));
            holder.ln_item_bill.setOnClickListener(v -> {
                Intent intent = new Intent(context, ExportBillActivity.class);
                intent.putExtra("bill", bill);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            });
        }

    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_fullname_bill;
        private final TextView tv_id_bill;
        private final TextView tv_numberphone_bill;
        private final TextView tv_product_bill;
        private final TextView tv_date_bill;
        private final TextView tv_price_bill;
        private final LinearLayout ln_item_bill;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id_bill = itemView.findViewById(R.id.tv_id_bill);
            ln_item_bill = itemView.findViewById(R.id.ln_item_bill);
            tv_fullname_bill = itemView.findViewById(R.id.tv_fullname_bill);
            tv_numberphone_bill = itemView.findViewById(R.id.tv_numberphone_bill);
            tv_product_bill = itemView.findViewById(R.id.tv_product_bill);
            tv_date_bill = itemView.findViewById(R.id.tv_date_bill);
            tv_price_bill = itemView.findViewById(R.id.tv_price_bill);
        }
    }
}
