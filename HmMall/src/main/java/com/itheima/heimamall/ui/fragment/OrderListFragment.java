package com.itheima.heimamall.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.itheima.heimamall.adapter.OrderListAdapter;
import com.itheima.heimamall.bean.GoodsList;
import com.itheima.heimamall.bean.Order;
import com.itheima.heimamall.bean.OrderList;
import com.itheima.heimamall.event.OrderEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.activity.OrderDetailActivity;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by lxj on 2016/8/5.
 */
public class OrderListFragment extends BaseListFragment {

    public static String STATE = "STATE";

    private int state;

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);

        state = getArguments().getInt(STATE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    public String getUrl() {
        return Url.getOrderList;
    }


    @Override
    public PostParams getPostParams() {
        PostParams postParams = new PostParams();
        postParams.add("uid", HeimaMallApp.user.uid);
        postParams.add("state",state+"");
        return postParams;
    }

    @Subscribe
    public void onItemClick(OrderEvent orderEvent) {
        if(list.size()>0){
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order",(Order)list.get((Integer) orderEvent.data-1));
            startActivity(intent);
        }
    }

    private OkHttpCallback<OrderList> callback = new OkHttpCallback<OrderList>() {
        @Override
        public void onSuccess(OrderList goodsList) {
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
    public RecyclerView.Adapter getAdapter() {
        return new OrderListAdapter(list);
    }

    @Override
    public OkHttpCallback getCallback() {
        return callback;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
