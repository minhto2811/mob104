package com.example.mob104_app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Activities.AddAddressActivity;
import com.example.mob104_app.Activities.AddressActivity;
import com.example.mob104_app.Models.Address;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressviewHolder> {
    private Context context;
    private List<Address> list;
    private boolean choose;


    public AddressAdapter(Context context, boolean choose) {
        this.context = context;
        this.choose = choose;
    }


    public void setData(List<Address> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressAdapter.AddressviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_layout, parent, false);
        return new AddressviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressviewHolder holder, int position) {
        Address address = list.get(position);
        if (address != null) {
            holder.tv_name.setText(address.getFullname());
            holder.tv_numberphone.setText(address.getNumberphone());
            holder.tv_more.setText(address.getAddress()+'.');
            holder.tv_address.setText(address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince() + '.');
            holder.ln_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    intent.putExtra("address", address);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
                }
            });

            if (TOOLS.getDefaulAddress(context) == null) {
                if (position == 0) {
                    holder.tv_default.setVisibility(View.VISIBLE);
                    TOOLS.saveDefaulAddress(context, address.get_id());
                }
                return;
            }
            if (TOOLS.getDefaulAddress(context).equals(address.get_id())) {
                holder.tv_default.setVisibility(View.VISIBLE);
            } else {
                holder.tv_default.setVisibility(View.GONE);
            }
        }
        if (!choose) {
            holder.btn_choose_address.setVisibility(View.GONE);
        }
        holder.btn_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chọn địa chỉ thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("address", address);
                ((Activity)context).setResult(((Activity)context).RESULT_OK, intent);
                ((Activity)context).finish();
                ((Activity)context).overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public class AddressviewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_numberphone, tv_more, tv_address, tv_default;
        private LinearLayout ln_address;
        private CheckBox btn_choose_address;

        public AddressviewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose_address = itemView.findViewById(R.id.btn_choose_address);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_numberphone = itemView.findViewById(R.id.tv_numberphone);
            tv_more = itemView.findViewById(R.id.tv_more);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_default = itemView.findViewById(R.id.tv_default);
            ln_address = itemView.findViewById(R.id.ln_address);
        }
    }
}
