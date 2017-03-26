package com.itheima.heimamall.ui.fragment;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.util.Logger;
import com.itheima.heimamall.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by lxj on 2016/8/5.
 */
public abstract class BaseListFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    @BindView(R.id.iv_arrow)
    ImageView iv_arrow;
    ArrayList list = new ArrayList();
    int page = 0;


    RecyclerView.Adapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setListener() {
        recyclerView.setLoadingListener(this);
    }

    @Override
    public void init() {
        adapter = getAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ScrollListener());

        hideArrow();
    }

    private void hideArrow() {
        iv_arrow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_arrow.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                iv_arrow.setTranslationY(200);
            }
        });
    }


    public abstract String getUrl();


    public abstract PostParams getPostParams();

    @Override
    public void loadData() {
        String url = getUrl();
        Logger.e(this, url);
        if (getPostParams() == null) {
            OkHttpEngine.create()
                    .get(url, getCallback());
        } else {
            OkHttpEngine.create()
                    .post(url, getPostParams(), getCallback());
        }
    }



    boolean isRefresh = false;

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 0;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    public abstract RecyclerView.Adapter getAdapter();


    @OnClick(R.id.iv_arrow)
    public void onClick() {
        recyclerView.smoothScrollToPosition(0);
        ViewCompat.animate(iv_arrow).translationY(200).setDuration(500).start();
    }

    public abstract OkHttpCallback getCallback();

    class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING ) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    View lastChild = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
                    if (layoutManager.getPosition(lastChild) > 20) {
                        ViewCompat.animate(iv_arrow).translationY(0).setDuration(500).start();
                    } else {
                        ViewCompat.animate(iv_arrow).translationY(200).setDuration(500).start();
                    }
                }
            }
        }

    }
}
