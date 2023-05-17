package com.example.mob104_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mob104_app.Activities.LoginActivity;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.Objects;



public class SettingsFragment extends Fragment {
    public static final String TAG = SettingsFragment.class.getName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_settings);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private Button btn_logout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        logout();
        if (TOOLS.getToken(getContext()) == null) {
            gotoLogin();
            return;
        }
    }

    private void mapping(View view) {
        btn_logout = view.findViewById(R.id.btn_logout);
    }

    private void logout() {
        if (TOOLS.getToken(getContext()) == null) {
            btn_logout.setVisibility(View.INVISIBLE);
        } else {
            btn_logout.setVisibility(View.VISIBLE);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TOOLS.clearToken(getContext());
                    gotoLogin();
                }
            });
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
    }

    @Override
    public void onResume() {
        super.onResume();
        logout();
    }
}