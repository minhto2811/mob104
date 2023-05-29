package com.example.mob104_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Interface.ProvinceOnClick;
import com.example.mob104_app.Models.Province;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ADDRESS;

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {
    private Context context;
    private List<Province> list;

    private ProvinceOnClick provinceOnClick;

    public ProvinceAdapter(Context context, ProvinceOnClick provinceOnClick) {
        this.context = context;
        this.provinceOnClick = provinceOnClick;
    }

    public void setData(List<Province> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProvinceAdapter.ProvinceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceAdapter.ProvinceViewHolder holder, int position) {
        if (ADDRESS.province != null) {
            holder.btn_choose.setText(ADDRESS.province.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    provinceOnClick.ItemClick(ADDRESS.province);
                }
            });
            return;
        }
        Province province = list.get(position);
        if (province != null) {
            holder.btn_choose.setText(province.getName());
            holder.btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    provinceOnClick.ItemClick(province);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.province != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public class ProvinceViewHolder extends RecyclerView.ViewHolder {

        private Button btn_choose;

        public ProvinceViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
