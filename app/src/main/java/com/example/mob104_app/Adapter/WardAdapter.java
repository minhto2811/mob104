package com.example.mob104_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Interface.DistrictOnClick;
import com.example.mob104_app.Interface.WardOnClick;
import com.example.mob104_app.Models.District;
import com.example.mob104_app.Models.Ward;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ADDRESS;

import java.util.List;

public class WardAdapter extends RecyclerView.Adapter<WardAdapter.WardViewHolder> {
    private Context context;
    private List<Ward> list;

    private WardOnClick wardOnClick;

    public WardAdapter(Context context, WardOnClick wardOnClick) {
        this.context = context;
        this.wardOnClick = wardOnClick;
    }

    public void setData(List<Ward> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WardAdapter.WardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new WardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WardAdapter.WardViewHolder holder, int position) {
        if (ADDRESS.ward != null) {
            holder.btn_choose.setText(ADDRESS.ward.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wardOnClick.ItemClick(ADDRESS.ward);
                }
            });
            return;
        }
        Ward ward = list.get(position);
        if (ward != null) {
            holder.btn_choose.setText(ward.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wardOnClick.ItemClick(ward);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.ward != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public class WardViewHolder extends RecyclerView.ViewHolder {

        private Button btn_choose;

        public WardViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
