package com.itheima.heimamall.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.adapter.CartListAdapter;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.CartItem;
import com.itheima.heimamall.bean.CartItemList;
import com.itheima.heimamall.bean.SimpleResult;
import com.itheima.heimamall.event.CartEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.activity.OrderConfirmActivity;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/8/1.
 */
public class CartFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_totalprice)
    TextView tvTotalprice;
    @BindView(R.id.tv_gopay)
    TextView tvGopay;


    ArrayList<CartItem> list = new ArrayList<>();
    private CartListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void setListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary,android.R.color.holo_blue_light
                ,android.R.color.darker_gray);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartListAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        getCartData(null);
    }
    @Subscribe
    public void getCartData(CartEvent cartEvent){
        selectedCart = null;
        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        OkHttpEngine.create().post(Url.cartList, params, new OkHttpCallback<CartItemList>(){
            @Override
            public void onSuccess(CartItemList baseBean) {
                stateLayout.showContentView();
                if(baseBean.isSuccess()){
                    if(baseBean.data!=null){
                        list.clear();
                        list.addAll(baseBean.data);
                        adapter.notifyDataSetChanged();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    stateLayout.showContentView();
                    ToastUtil.showToast(baseBean.msg);
                }
            }
            @Override
            public void onFail(IOException e) {
                stateLayout.showErrorView();
            }
        });

    }


    @OnClick({  R.id.tv_gopay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gopay:
                if(selectedCart==null || selectedCart.size()==0){
                    ToastUtil.showToast("请选择要下单的商品!");
                    return;
                }
                Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                intent.putExtra("cart",selectedCart);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        adapter.resetSelectCart();
        tvTotalprice.setText("总价：￥"+0);
        loadData();
    }

    private ArrayList<CartItem> selectedCart;
    @Subscribe
    public void updateTotalPrice(ArrayList<CartItem> selectedCart){
        this.selectedCart = selectedCart;
        float total = 0;
        for (int i = 0; i < selectedCart.size(); i++) {
            CartItem item = selectedCart.get(i);
            total += item.getGoods().getPrice()*item.getNum();
        }
        tvTotalprice.setText("总价：￥"+total);
    }
    @Subscribe
    public void deleteCartItem(final CartItem cartItem){
        DialogUtil.showProgressDialog(getActivity(),"delete","正在删除");

        PostParams params = new PostParams();
        params.add("uid",HeimaMallApp.user.uid);
        params.add("cid",cartItem.getId());
        OkHttpEngine.create().post(Url.deleteCart, params, new OkHttpCallback<SimpleResult>() {
            @Override
            public void onSuccess(SimpleResult stringBaseBean) {
                if(stringBaseBean.isSuccess()){
                    ToastUtil.showToast("删除成功!");
                    DialogUtil.dismissDialog("delete");
                    //更新adapter
                    list.remove(cartItem);
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtil.showToast(stringBaseBean.msg);
                    DialogUtil.dismissDialog("delete");
                }
            }
            @Override
            public void onFail(IOException e) {
                ToastUtil.showToast("请求失败");
                DialogUtil.dismissDialog("delete");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
