package com.example.mob104_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.mob104_app.Activities.CreateBillActivity;
import com.example.mob104_app.Activities.LoginActivity;
import com.example.mob104_app.Activities.MainActivity;
import com.example.mob104_app.Adapter.CartAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.Models.Cart;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.LIST;
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
    private LinearLayout ln_speed, ln_check_cart;
    public static LinearLayout ln_cart_emty;
    private static TextView tv_check_all, tv_price_pay;
    private static CheckBox cbox_check_all;

    private static LinearLayout ln_pay;
    private Button btn_pay, btn_login_cart,btn_buy_cart;
    private static int price_pay;
    private List<Cart> cartList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        if (ACCOUNT.user == null) {
            login();
            ln_check_cart.setVisibility(View.VISIBLE);
            return;
        }
        showCarts();
        checkAll();
        pay();
        home();
    }

    private void home() {
        btn_buy_cart.setOnClickListener(v -> {
            MainActivity.bottomNavigationView.show(MainActivity.HOME, true);
            MainActivity.replaceFragment(new HomeFragment(),HomeFragment.TAG,MainActivity.HOME,requireContext());
        });
    }

    private void login() {
        btn_login_cart.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            requireActivity().finish();
        });
    }

    private void pay() {
        btn_pay.setOnClickListener(v -> {
            Bill bill = new Bill();
            bill.setStatus(0);
            bill.setId_user(ACCOUNT.user.get_id());
            bill.setTotal(price_pay);
            bill.setList(LIST.listBuyCart);
            Intent intent = new Intent(requireContext(), CreateBillActivity.class);
            intent.putExtra("bill", bill);
            startActivity(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }

    public static void showLayoutPay(List<Cart> list) {
        if (list.size() == 0) {
            ln_pay.setVisibility(View.GONE);
            return;
        }
        ln_pay.setVisibility(View.VISIBLE);
        price_pay = 0;
        for (int i = 0; i < list.size(); i++) {
            Cart cart = list.get(i);
            price_pay += (cart.getPrice_product()*(100-cart.getSale())/100 )* cart.getQuantity();
        }
        tv_price_pay.setText(TOOLS.convertPrice(price_pay));
    }

    private void checkAll() {
        cbox_check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOOLS.checkAllCarts = cbox_check_all.isChecked();
                if (TOOLS.checkAllCarts) {
                    tv_check_all.setText("Bỏ chọn tất cả");
                    showLayoutPay(cartList);
                } else {
                    LIST.listBuyCart.clear();
                    tv_check_all.setText("Chọn tất cả");
                    showLayoutPay(LIST.listBuyCart);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showCarts() {
        adapter = new CartAdapter(requireContext(),false);
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
                        cartList = response.body();
                        adapter.setData(response.body());
                        ln_speed.setVisibility(View.VISIBLE);
                        recyclerView.setPadding(0, 0, 0, 140);
                        ln_cart_emty.setVisibility(View.GONE);
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
        btn_buy_cart = view.findViewById(R.id.btn_buy_cart);
        ln_cart_emty = view.findViewById(R.id.ln_cart_emty);
        ln_check_cart = view.findViewById(R.id.ln_check_cart);
        btn_login_cart = view.findViewById(R.id.btn_login_cart);
        btn_pay = view.findViewById(R.id.btn_pay);
        tv_price_pay = view.findViewById(R.id.tv_price_pay);
        ln_pay = view.findViewById(R.id.ln_pay);
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