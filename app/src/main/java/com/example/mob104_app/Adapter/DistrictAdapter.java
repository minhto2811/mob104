package com.example.mob104_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Interface.DistrictOnClick;
import com.example.mob104_app.Models.District;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ADDRESS;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> {
    private Context context;
    private List<District> list;

    private DistrictOnClick districtOnClick;

    public DistrictAdapter(Context context, DistrictOnClick districtOnClick) {
        this.context = context;
        this.districtOnClick = districtOnClick;
    }

    public void setData(List<District> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictAdapter.DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictAdapter.DistrictViewHolder holder, int position) {
        if (ADDRESS.district != null) {
            holder.btn_choose.setText(ADDRESS.district.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    districtOnClick.ItemClick(ADDRESS.district);
                }
            });
            return;
        }
        District district = list.get(position);
        if (district != null) {
            holder.btn_choose.setText(district.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    districtOnClick.ItemClick(district);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.district != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public class DistrictViewHolder extends RecyclerView.ViewHolder {

        private Button btn_choose;

        public DistrictViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
