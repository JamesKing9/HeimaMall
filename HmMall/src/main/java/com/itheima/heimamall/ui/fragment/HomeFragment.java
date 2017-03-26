package com.itheima.heimamall.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.adapter.ViewPagerIndicatorAdapter;
import com.itheima.heimamall.global.Constant;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by lxj on 2016/8/1.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<ViewPagerIndicatorAdapter.PageInfo> pageInfos = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void loadData() {

        preparePageInfo();

        viewPager.setAdapter(new ViewPagerIndicatorAdapter(getChildFragmentManager(), pageInfos));

        tabLayout.setupWithViewPager(viewPager);

        stateLayout.showContentView();

    }


    private void preparePageInfo() {
        String[] titles = getResources().getStringArray(R.array.goods_category);
        pageInfos.add(new ViewPagerIndicatorAdapter.PageInfo(createFragment(Constant.GoodsCategory.Women), titles[0]));
        pageInfos.add(new ViewPagerIndicatorAdapter.PageInfo(createFragment(Constant.GoodsCategory.Man), titles[1]));
        pageInfos.add(new ViewPagerIndicatorAdapter.PageInfo(createFragment(Constant.GoodsCategory.Child), titles[2]));
        pageInfos.add(new ViewPagerIndicatorAdapter.PageInfo(createFragment(Constant.GoodsCategory.Phone), titles[3]));
        pageInfos.add(new ViewPagerIndicatorAdapter.PageInfo(createFragment(Constant.GoodsCategory.Book), titles[4]));
    }

    private Fragment createFragment(String category) {
        GoodsListFragment goodsListFragment = new GoodsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GoodsListFragment.CATEGORY, category);
        goodsListFragment.setArguments(bundle);
        return goodsListFragment;
    }

}
