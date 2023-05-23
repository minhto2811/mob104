package com.example.mob104_app.UI;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mob104_app.Adapter.BannerAdapter;
import com.example.mob104_app.Adapter.CategoryAdapter;
import com.example.mob104_app.Adapter.ProductAdapter;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;

import java.util.Objects;

import me.relex.circleindicator.CircleIndicator3;



public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_home);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private ProductAdapter productAdapter;
    private RecyclerView categoryRecyclerView;
    private RelativeLayout layout_category;
    private TextView tv_category;


    private ViewPager2 viewPager2;
    private final Handler slideHandler = new Handler();
    private int bannerSize = 0;
    private ImageView imv_arrow;

    private boolean arrow = true;
    public static SearchView searchView;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerViewProduct(view);
        initRecyclerViewCategory(view);
        initRecyclerViewBanner(view);
    }




    private void initRecyclerViewProduct(View view) {
        RecyclerView productRecyclerView = view.findViewById(R.id.rcv_home);
        productAdapter = new ProductAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.setData(LIST.listProduct);
    }


    private void initRecyclerViewCategory(View view) {
        tv_category = view.findViewById(R.id.tv_category);
        tv_category.setText(R.string.danh_m_c);
        imv_arrow = view.findViewById(R.id.imv_arrow);
        layout_category = view.findViewById(R.id.layout_category);
        categoryRecyclerView = view.findViewById(R.id.rcv_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);
        layout_category.setOnClickListener(v -> ShowHideCategory());
        categoryAdapter.setData(LIST.listCategory);
    }


    private void initRecyclerViewBanner(View view) {
        viewPager2 = view.findViewById(R.id.view_pager_2);
        CircleIndicator3 circleIndicator3 = view.findViewById(R.id.circle_indicator_3);
        circleIndicator3.setViewPager(viewPager2);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sildeRunable);
                slideHandler.postDelayed(sildeRunable, 3000);
            }
        });
        bannerSize = LIST.listBanner.size() - 1;
        BannerAdapter bannerAdapter = new BannerAdapter(requireActivity(), LIST.listBanner);
        viewPager2.setAdapter(bannerAdapter);
    }




    private void ShowHideCategory() {
        arrow = !arrow;
        if (arrow) {
            imv_arrow.setImageResource(R.drawable.ic_arrow_down);
            categoryRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout_category.setBackgroundResource(R.color.white);
            tv_category.setText(R.string.danh_m_c);
        } else {
            imv_arrow.setImageResource(R.drawable.ic_arrow_up);
            categoryRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            SpannableString spannableString = new SpannableString(getString(R.string.danh_m_c1));
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY),8,17,0);
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 8, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), 8, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            tv_category.setText(spannableString);
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
         searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private final Runnable sildeRunable = new Runnable() {
        @Override
        public void run() {
            int index = viewPager2.getCurrentItem();
            if (index == bannerSize) {
                index = -1;
            }
            viewPager2.setCurrentItem(index + 1);
        }
    };

    @Override
    public void onResume() {

        super.onResume();
        if (!arrow) {
            arrow = true;
            ShowHideCategory();
        }
    }


}