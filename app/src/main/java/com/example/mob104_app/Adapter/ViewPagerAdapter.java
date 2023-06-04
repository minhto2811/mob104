package com.example.mob104_app.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mob104_app.UI.BFourFragment;
import com.example.mob104_app.UI.BOneFragment;
import com.example.mob104_app.UI.BThreeFragment;
import com.example.mob104_app.UI.BTwoFragment;
import com.example.mob104_app.UI.BZeroFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BZeroFragment();
            case 1:
                return new BOneFragment();
            case 2:
                return new BTwoFragment();
            case 3:
                return new BThreeFragment();
            case 4:
                return new BFourFragment();
            default:
                return new BThreeFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String s = "";
        switch (position) {
            case 0:
                s = "Chờ xác nhận";
                break;
            case 1:
                s = "Chuẩn bị hàng";
                break;
            case 2:
                s = "Đang giao hàng";
                break;
            case 3:
                s = "Đã giao";
                break;
            case 4:
                s = "Đã hủy";
                break;
            default:
                s = "Đã giao";
                break;
        }
        return s;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
