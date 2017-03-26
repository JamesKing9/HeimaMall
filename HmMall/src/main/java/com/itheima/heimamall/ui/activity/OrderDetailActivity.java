package com.itheima.heimamall.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Address;
import com.itheima.heimamall.bean.CartItem;
import com.itheima.heimamall.bean.Order;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DateUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/18.
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_receiver)
    TextView tvReceiver;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_totalprice)
    TextView tvTotalprice;
    @BindView(R.id.tv_gopay)
    TextView tvGopay;
    @BindView(R.id.ll_goods)
    LinearLayout ll_goods;
    private Order order;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        showTitle(true, "订单详情", "");
        order = (Order) getIntent().getSerializableExtra("order");

        showOrderInfo();
    }
    float total = 0;
    int width;
    private void showOrderInfo() {
        width = getResources().getDimensionPixelOffset(R.dimen.dp40);
        //address info
        Address address = order.getAddress();
        tvReceiver.setText(address.receiver +" "+address.phone);
        tvArea.setText(address.area +" "+address.detail);

        //order info

        int num = 0;
        ArrayList<CartItem> goodsinfos = order.getCartinfos();
        for (int i = 0; i < goodsinfos.size(); i++) {
            CartItem goodsInfo = goodsinfos.get(i);
            num += goodsInfo.getNum();
            total += goodsInfo.getNum()*goodsInfo.getGoods().getPrice();

            //显示商品图片
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width,width));
            ll_goods.addView(imageView);
            Glide.with(this)
                    .load(Url.ImagePrefix+goodsInfo.getGoods().getImgs().get(0))
                    .placeholder(R.drawable.default_image)
                    .crossFade()
                    .into(imageView);
        }
        tvNumber.setText("共"+num+"件");
        tvTotalprice.setText("总价：￥"+total);
        tvOrderNum.setText(order.getOrdernum());
        tvCreateTime.setText(DateUtil.to_ymd_hms(order.getCreatetime()));
        
        //
        int state = order.getState();
        tvGopay.setText(state== Constant.OrderState.StateUnsend
            ?"待发货":"去付款");
        if(state== Constant.OrderState.StateUnsend){
            tvGopay.setClickable(false);
        }
    }


    @OnClick(R.id.tv_gopay)
    public void onClick() {
        //跳转支付界面
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("order",order);
        intent.putExtra("total",total);
        startActivity(intent);
        //
        finish();
    }
}
