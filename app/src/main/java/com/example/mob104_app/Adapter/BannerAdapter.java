package com.example.mob104_app.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mob104_app.Models.Banner;
import com.example.mob104_app.UI.BannerFragment;

import java.util.List;

public class BannerAdapter extends FragmentStateAdapter {
    private final List<Banner> list;

    public BannerAdapter(@NonNull FragmentActivity fragmentActivity,List<Banner> list) {
        super(fragmentActivity);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Banner banner = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("banner",banner);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }
}
