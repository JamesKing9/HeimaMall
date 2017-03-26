package com.itheima.heimamall.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.itheima.heimamall.adapter.GoodslistAdapter;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.Goods;
import com.itheima.heimamall.bean.GoodsList;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.activity.GoodsDetailActivity;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lxj on 2016/8/5.
 */
public class GoodsListFragment extends BaseListFragment implements GoodslistAdapter.OnGoodsItemClickListener{

    public static String CATEGORY = "category";

    private String category;

    @Override
    public void init() {
        super.init();
        category = getArguments().getString(CATEGORY);

        ((GoodslistAdapter)adapter).setOnItemClickListener(this);
    }

    @Override
    public String getUrl() {
        return String.format(Url.getGoodsList,category,isRefresh?0:page);
    }


    private OkHttpCallback<GoodsList> callback = new OkHttpCallback<GoodsList>() {
        @Override
        public void onSuccess(GoodsList goodsList) {
            stateLayout.showContentView();
            if(goodsList.isSuccess()){
                if (goodsList.data != null && goodsList.data.size() > 0) {
                    //如果是下拉刷新，则清空原有集合的数据
                    if (isRefresh) {
                        list.clear();
                        isRefresh = false;
                    }

                    list.addAll(goodsList.data);
                    page++;

                    adapter.notifyDataSetChanged();
                    recyclerView.refreshComplete();
                    recyclerView.loadMoreComplete();
                } else {
                    recyclerView.refreshComplete();
                    recyclerView.setNoMore(true);
                }
            }else {
                ToastUtil.showToast(goodsList.msg);
                recyclerView.refreshComplete();
                recyclerView.loadMoreComplete();
            }
        }

        @Override
        public void onFail(IOException e) {
            if (list.size() == 0) {
                stateLayout.showErrorView();
            } else {
                ToastUtil.showToast("加载失败，请重试");
            }
            recyclerView.refreshComplete();
            recyclerView.loadMoreComplete();
        }
    };

    @Override
    public PostParams getPostParams() {
        return null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("goods",(Goods)list.get(position));
        startActivity(intent);

    }


    @Override
    public RecyclerView.Adapter getAdapter() {
        return new GoodslistAdapter( list);
    }

    @Override
    public OkHttpCallback getCallback() {
        return callback;
    }


}
