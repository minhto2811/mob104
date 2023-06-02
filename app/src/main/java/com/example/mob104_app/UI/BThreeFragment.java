package com.example.mob104_app.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob104_app.Adapter.BillAdapter;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;


public class BThreeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b_three, container, false);
    }
    private RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        showList();
    }

    private void showList() {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        BillAdapter adapter = new BillAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        adapter.setData(LIST.getListBillByStatus(3));
    }

    private void mapping(View view) {
        recyclerView = view.findViewById(R.id.rcv_bill_3);
    }
}