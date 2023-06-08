package com.example.mob104_app.Adapter;

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

import com.example.mob104_app.Activities.MainActivity;
import com.example.mob104_app.Models.Notify;
import com.example.mob104_app.R;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    private Context context;
    private List<Notify> list;

    public NotifyAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Notify> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifyAdapter.NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_layout, parent, false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.NotifyViewHolder holder, int position) {
        Notify notify = list.get(position);
        if (notify != null) {
            String noti = "";
            holder.tv_time.setText(String.valueOf(notify.getTime()));
            if (notify.getStatus() == 0) {
                noti = "đặt thành công";
            } else if (notify.getStatus() == 1) {
                noti = "đã được xác nhận";
            } else if (notify.getStatus() == 2) {
                noti = "đang giao";
            } else if (notify.getStatus() == 3) {
                noti = "đã giao thành công";
            } else if (notify.getStatus() == 4) {
                noti = "đã hủy thành công";
            }
            holder.tv_body.setText("Đơn hàng " + notify.getId_bill().toUpperCase() + " " + noti);
            holder.ln_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("bill",2);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
                    ((Activity) context).finish();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time, tv_body;
        private LinearLayout ln_notify;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_body = itemView.findViewById(R.id.tv_body);
            tv_time = itemView.findViewById(R.id.tv_time);
            ln_notify = itemView.findViewById(R.id.ln_notify);
        }
    }
}
