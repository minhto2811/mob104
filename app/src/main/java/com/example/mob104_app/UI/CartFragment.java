package com.example.mob104_app.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.CartAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    public static final String TAG = CartFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_cart);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private LinearLayout ln_speed;
    private static TextView tv_check_all;
    private static CheckBox cbox_check_all;
    private int ListSize = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        showCarts();
        checkAll();
    }

    private void checkAll() {
        cbox_check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOOLS.checkAllCarts = cbox_check_all.isChecked();
                if(TOOLS.checkAllCarts){
                    TOOLS.quantityCheckAllCarts = ListSize;
                    tv_check_all.setText("Bỏ chọn tất cả");
                    adapter.notifyDataSetChanged();
                }else {
                    TOOLS.quantityCheckAllCarts = 0;
                    tv_check_all.setText("Chọn tất cả");
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showCarts() {
        adapter = new CartAdapter(requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        if (TOOLS.getUser(requireContext()) != null) {
            ApiService.apiService.getCarts(TOOLS.getUser(requireContext()).get_id()).enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                    if (response.isSuccessful() && response.body().size() > 0) {
                        ListSize = response.body().size();
                        adapter.setData(response.body());
                        ln_speed.setVisibility(View.VISIBLE);
                        recyclerView.setPadding(0, 0, 0, 140);
                    }
                }

                @Override
                public void onFailure(Call<List<Cart>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void mapping(View view) {
        cbox_check_all = view.findViewById(R.id.cbox_check_all);
        tv_check_all = view.findViewById(R.id.tv_check_all);
        ln_speed = view.findViewById(R.id.ln_speed);
        recyclerView = view.findViewById(R.id.rcv_cart);

    }

    public static void setCheckByItem(){
        if(TOOLS.checkAllCarts){
            if(!cbox_check_all.isChecked()){
                cbox_check_all.setChecked(true);
                tv_check_all.setText("Bỏ chọn tất cả");
            }
        }else {
            if(cbox_check_all.isChecked()){
                cbox_check_all.setChecked(false);
                tv_check_all.setText("Chọn tất cả");
            }
        }
    }
}