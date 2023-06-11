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

import com.example.mob104_app.Activities.BillActivity;
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

    public void setData(List<Notify> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifyAdapter.NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_layout,parent,false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.NotifyViewHolder holder, int position) {
        Notify notify = list.get(position);
        if(notify!=null){
            holder.ln_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BillActivity.class);
                    intent.putExtra("status",notify.getStatus());
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
                    ((Activity)context).finish();
                }
            });
            holder.tv_time.setText(String.valueOf(notify.getTime()));
            String body  = "";
            switch (notify.getStatus()){
                case 0: body = " đã đặt thành công";break;
                case 1: body = " đã được xác nhận tồn tại trong kho";break;
                case 2: body = " đang giao hàng";break;
                case 3: body = " đã giao hàng thành công";break;
                case 4: body = " đã bị hủy";break;
            }
            holder.tv_body.setText("Đơn hàng có mã "+notify.getId_bill().toUpperCase()+body);
        }

    }

    @Override
    public int getItemCount() {
        return (list!=null)? list.size():0;
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time,tv_body;
        private LinearLayout ln_notify;
        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            ln_notify = itemView.findViewById(R.id.ln_notify);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_body = itemView.findViewById(R.id.tv_body);
        }
    }
}
