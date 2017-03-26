package com.itheima.heimamall.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Address;
import com.itheima.heimamall.bean.AddressList;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.CartItem;
import com.itheima.heimamall.bean.Order;
import com.itheima.heimamall.bean.OrderResult;
import com.itheima.heimamall.event.AddressEvent;
import com.itheima.heimamall.event.CartEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.GsonUtil;
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
 * Created by lxj on 2016/9/12.
 */
public class OrderConfirmActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    View btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_receiver)
    TextView tvReceiver;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.ll_goods)
    LinearLayout llGoods;
    @BindView(R.id.tv_totalprice)
    TextView tvTotalprice;
    @BindView(R.id.tv_gopay)
    TextView tvGopay;
    private ArrayList<CartItem> cartList;
    private float total;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_confirm;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        showTitle(true,"确认订单",null);

        cartList = (ArrayList<CartItem>) getIntent().getSerializableExtra("cart");

        updateGoodsInfo();

        getAddress();
    }

    Address address;
    private void getAddress() {
        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);

        OkHttpEngine.create().post(Url.getAddress, params, new OkHttpCallback<AddressList>(){
                    @Override
                    public void onSuccess(AddressList baseBean) {
                        if(baseBean.isSuccess()){
                            if(baseBean.data!=null && baseBean.data.size()>0){
                                address = baseBean.data.get(0);
                            }
                            updateAddressInfo(address);
                        }
                    }

                    @Override
                    public void onFail(IOException e) {

                    }
                });

    }

    public void updateAddressInfo(Address address) {
        if(address==null){
            tvReceiver.setText("请设置收货地址");
            tvArea.setText("");
        }else {
            tvReceiver.setText(address.receiver+"   "+address.phone);
            tvArea.setText(address.area+"  "+address.detail);
        }

    }

    @Subscribe
    public void setAddressInfo(AddressEvent adddressEvent){
        switch (adddressEvent.code){
            case AddressEvent.Set:
                address = (Address) adddressEvent.data;
                updateAddressInfo(address);
                break;
            case AddressEvent.BeDeleted:
                if(address!=null){
                    String deleteId = (String) adddressEvent.data;
                    if(deleteId.equals(address.id)){
                        address = null;
                        updateAddressInfo(address);
                    }
                }
                break;
        }
    }

    int width;
    private void updateGoodsInfo() {
        width = getResources().getDimensionPixelOffset(R.dimen.dp40);
        if(cartList==null)return;
        int number = 0;
        for (int i = 0; i < cartList.size(); i++) {
            CartItem item = cartList.get(i);
            number += item.getNum();
            total += item.getGoods().getPrice()*item.getNum();
            //显示商品图片
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width,width));
            llGoods.addView(imageView);
            Glide.with(this)
                 .load(Url.ImagePrefix+item.getGoods().getImgs().get(0))
                 .placeholder(R.drawable.default_image)
                 .crossFade()
                 .into(imageView);

        }
        tvNumber.setText("共"+number+"件");
        tvTotalprice.setText("总价：￥"+ total);


    }

    @OnClick({R.id.ll_address, R.id.tv_gopay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                //跳转到收货地址界面
                startActivity(new Intent(this,AddressManagerActivity.class));
                break;
            case R.id.tv_gopay:
                if(address==null){
                    ToastUtil.showToast("请添加收货地址信息！");
                    return;
                }
                //跳转到付款界面
                submitOrder();
                break;
        }
    }
    Order order;
    /**
     * 提交订单
     */
    private void submitOrder() {
        DialogUtil.showProgressDialog(this,"submit","正在提交订单");
        PostParams params = new PostParams();
        params.add("uid",HeimaMallApp.user.uid);
        params.add("addressid",address.id);
        params.add("cartitems",GsonUtil.toJson(cartList));
        OkHttpEngine.create().post(Url.submitOrder, params, new OkHttpCallback<OrderResult>(){
            @Override
            public void onSuccess(OrderResult orderBaseBean) {
                if(orderBaseBean.isSuccess()){
                    order = orderBaseBean.data;
                    //通知购物车界面刷新数据
                    EventBus.getDefault().post(new CartEvent(CartEvent.Update,null));

                    DialogUtil.dismissDialog("submit");
                    //跳转支付界面
                    Intent intent = new Intent(OrderConfirmActivity.this, PayActivity.class);
                    intent.putExtra("order",order);
                    intent.putExtra("total",total);
                    startActivity(intent);

                    //
                    finish();
                }else {
                    ToastUtil.showToast(orderBaseBean.msg);
                    DialogUtil.dismissDialog("submit");
                }
            }

            @Override
            public void onFail(IOException e) {
                ToastUtil.showToast("请求失败");
                DialogUtil.dismissDialog("submit");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
