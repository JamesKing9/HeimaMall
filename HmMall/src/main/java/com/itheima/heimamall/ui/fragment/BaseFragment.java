package com.itheima.heimamall.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.heimamall.ui.view.StateLayout;

import butterknife.ButterKnife;

/**
 * Created by lxj on 2016/8/1.
 */
public abstract class BaseFragment extends Fragment implements StateLayout.OnReloadListener{

    protected StateLayout stateLayout;//复用Fragment的View对象

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(stateLayout==null){
            stateLayout = new StateLayout(getContext());
            View contentView = inflater.inflate(getLayoutId(),null);
            stateLayout.setContentView(contentView);
            stateLayout.showLoadingView();//默认显示加载中界面
            stateLayout.setOnReloadListener(this);

            ButterKnife.bind(this, contentView);


            setListener();
            init();
            loadData();
        }
        return stateLayout;
    }

    /**
     * use to init something else.
     * This method is called after setListener() and before loadData();
     * So you can do some view-init things.
     */
    public void init() {}


    public abstract int getLayoutId();
    protected abstract void setListener();

    public abstract void loadData();

    @Override
    public void onReload() {
        stateLayout.showLoadingView();
        loadData();
    }
}
