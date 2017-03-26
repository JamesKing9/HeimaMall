package com.itheima.heimamall.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.adapter.AddressAdapter;
import com.itheima.heimamall.bean.Address;
import com.itheima.heimamall.bean.AddressList;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.SimpleResult;
import com.itheima.heimamall.event.AddressEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by lxj on 2016/9/12.
 */
public class AddressManagerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.btn_back)
    View btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private AddressAdapter addressAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void setListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private ArrayList<Address> addressList = new ArrayList<>();
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        showTitle(true, "收货地址", "添加");
        swipeRefreshLayout.setColorSchemeResources(R.color.primary,android.R.color.holo_blue_light
                ,android.R.color.darker_gray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(addressList);
        recyclerView.setAdapter(addressAdapter);

        requestAddress();
    }

    @Override
    public void onRightTitleClick() {
        super.onRightTitleClick();
        startActivity(new Intent(this,AddAddressActivity.class));
    }

    private void requestAddress() {
        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        OkHttpEngine.create().post(Url.getAddress, params, new OkHttpCallback<AddressList>() {
                    @Override
                    public void onSuccess(AddressList baseBean) {
                        if(baseBean.isSuccess()){
                            if(baseBean.data!=null && baseBean.data.size()>0){
                                addressList.clear();
                                addressList.addAll(baseBean.data);
                                addressAdapter.notifyDataSetChanged();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                    @Override
                    public void onFail(IOException e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
    @Subscribe
    public void onAddressEvent(AddressEvent addressEvent){
        switch (addressEvent.code){
            case AddressEvent.Delete:
                deleteAddress((Integer) addressEvent.data);
                break;
            case AddressEvent.Update:
                requestAddress();
                break;
            case AddressEvent.Set:
                finish();
                break;
        }
    }


    public void deleteAddress(final int position){
        PostParams params = new PostParams();
        params.add("uid",HeimaMallApp.user.uid);
        params.add("id",addressList.get(position).id);
        OkHttpEngine.create().post(Url.delAddress, params, new OkHttpCallback<SimpleResult>() {
                    @Override
                    public void onSuccess(SimpleResult stringBaseBean) {
                        if(stringBaseBean.isSuccess()){
                            ToastUtil.showToast("删除成功！");
                            EventBus.getDefault().post(new AddressEvent(AddressEvent.BeDeleted,addressList.get(position).id));
                            addressList.remove(position);
                            addressAdapter.notifyItemRemoved(position);
                        }
                    }
                    @Override
                    public void onFail(IOException e) {

                    }
                });

    }

    @Override
    public void onRefresh() {
        requestAddress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
