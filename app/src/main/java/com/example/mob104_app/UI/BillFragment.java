package com.example.mob104_app.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.mob104_app.Activities.BillActivity;
import com.example.mob104_app.Activities.LoginActivity;
import com.example.mob104_app.Adapter.FavouriteAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.Bill;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillFragment extends Fragment {

    public static final String TAG = BillFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_bill);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    private RecyclerView recyclerView;
    private LinearLayout ln_check, ln_bill_successful, ln_cancel, ln_shipping, ln_confirm, ln_wait_confirm, imvA, imvB, imvC;

    private TextView tvA, tvB, tvC;
    private Button btn_login;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        if (ACCOUNT.user == null) {
            ln_check.setVisibility(View.VISIBLE);
            login();
            return;
        }
        getBill();
        showRecently();
        history();
        waitForConfirmation();
        waitingForTheGoods();
        delivery();
        cancel();
    }

    private void login() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
                requireActivity().finish();
            }
        });
    }

    private void getBill() {
        Dialog dialog = TOOLS.createDialog(requireContext());
        dialog.show();
        ApiService.apiService.getBill(ACCOUNT.user.get_id()).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    LIST.listBill.clear();
                    LIST.listBill = response.body();
                    getCount();
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(requireContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCount() {
        int a = 0, b = 0, c = 0;
        for (int i = 0; i < LIST.listBill.size(); i++) {
            if (LIST.listBill.get(i).getStatus() == 0) {
                a += 1;
            } else if (LIST.listBill.get(i).getStatus() == 1) {
                b += 1;
            } else if (LIST.listBill.get(i).getStatus() == 2) {
                c += 1;
            }
        }
        if (a > 0) {
            imvA.setVisibility(View.VISIBLE);
            tvA.setText(String.valueOf(a));
        }

        if (b > 0) {
            imvB.setVisibility(View.VISIBLE);
            tvB.setText(String.valueOf(b));
        }
        if (c > 0) {
            imvC.setVisibility(View.VISIBLE);
            tvC.setText(String.valueOf(c));
        }

    }

    private void cancel() {
        gotoBillActivity(ln_cancel, 4);
    }

    private void delivery() {
        gotoBillActivity(ln_shipping, 2);
    }

    private void waitingForTheGoods() {
        gotoBillActivity(ln_confirm, 1);
    }

    private void waitForConfirmation() {
        gotoBillActivity(ln_wait_confirm, 0);
    }

    private void history() {
        gotoBillActivity(ln_bill_successful, 3);
    }

    private void gotoBillActivity(LinearLayout linearLayout, int status) {
        linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), BillActivity.class);
            intent.putExtra("status", status);
            intent.putExtra("ibf",true);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });

    }

    private void showRecently() {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        FavouriteAdapter adapter = new FavouriteAdapter(requireContext(), false);
        recyclerView.setAdapter(adapter);
        adapter.setData(LIST.listRecently);
    }

    private void mapping(View view) {
        ln_check = view.findViewById(R.id.ln_check);
        btn_login = view.findViewById(R.id.btn_login_fm);
        tvA = view.findViewById(R.id.tvA);
        tvB = view.findViewById(R.id.tvB);
        tvC = view.findViewById(R.id.tvC);
        imvA = view.findViewById(R.id.imvA);
        imvB = view.findViewById(R.id.imvB);
        imvC = view.findViewById(R.id.imvC);
        ln_shipping = view.findViewById(R.id.ln_shipping);
        ln_confirm = view.findViewById(R.id.ln_confirm);
        ln_cancel = view.findViewById(R.id.ln_cancel);
        ln_wait_confirm = view.findViewById(R.id.ln_wait_confirm);
        ln_bill_successful = view.findViewById(R.id.ln_bill_successful);
        recyclerView = view.findViewById(R.id.rcv_recently);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ACCOUNT.user!=null){
            getBill();
        }
    }
}