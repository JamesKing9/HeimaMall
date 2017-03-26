package com.itheima.heimamall.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.activity.ImageScaleActivity;
import com.itheima.heimamall.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by lxj on 2016/9/5.
 */
public class BannerAdapter extends PagerAdapter {

    private ArrayList<String> urlList;

    public BannerAdapter(ArrayList<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());

        Glide.with(container.getContext())
             .load(Url.ImagePrefix+urlList.get(position))
             .centerCrop()
             .placeholder(R.drawable.default_image)
             .crossFade(500)
             .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), ImageScaleActivity.class);
                intent.putStringArrayListExtra("urlList",urlList);
                intent.putExtra("position",position);
                ((AppCompatActivity)container.getContext()).startActivity(intent);
            }
        });


        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
