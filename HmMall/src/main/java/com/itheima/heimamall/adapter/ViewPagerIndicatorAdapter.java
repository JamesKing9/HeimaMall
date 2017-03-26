package com.itheima.heimamall.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lxj on 2016/8/1.
 */
public class ViewPagerIndicatorAdapter extends FragmentPagerAdapter {

    private ArrayList<PageInfo> pageInfos;

    public ViewPagerIndicatorAdapter(FragmentManager fm, ArrayList<PageInfo> pageInfos) {
        super(fm);
        this.pageInfos = pageInfos;
    }

    @Override
    public Fragment getItem(int position) {
        return pageInfos.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pageInfos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageInfos.get(position).getTitle();
    }
    public static class PageInfo{
        private Fragment fragment;
        private String title;

        public PageInfo(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }
        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
