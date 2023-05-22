package com.example.mob104_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.TOOLS;

import java.util.List;



public class ImageAdapter extends PagerAdapter {

    private  List<String> list;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imv_product_slide);
        TextView textView = view.findViewById(R.id.tv_index);
        String url = list.get(position);

        textView.setText((position+1)+"/"+getCount());
        Glide.with(container.getContext())
                .load(TOOLS.doMainDevice + url)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
