package com.itheima.heimamall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.CartItem;
import com.itheima.heimamall.bean.Order;
import com.itheima.heimamall.event.OrderEvent;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.Url;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/18.
 */
public class OrderListAdapter extends RecyclerView.Adapter {
    private ArrayList<Order> list;

    public OrderListAdapter(ArrayList<Order> list) {
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_orderlist, null);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderHolder orderHolder = (OrderHolder) holder;
        Order order = list.get(position);

        float total = 0;
        int num = 0;
        ArrayList<CartItem> goodsinfos = order.getCartinfos();
        orderHolder.llGoods.removeAllViews();
        for (int i = 0; i < goodsinfos.size(); i++) {
            CartItem goodsInfo = goodsinfos.get(i);
            num += goodsInfo.getNum();
            total += goodsInfo.getNum()*goodsInfo.getGoods().getPrice();

            //显示商品图片
            ImageView imageView = new ImageView(orderHolder.itemView.getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width,width));
            orderHolder.llGoods.addView(imageView);

            Glide.with(orderHolder.itemView.getContext())
                    .load(Url.ImagePrefix+goodsInfo.getGoods().getImgs().get(0))
                    .placeholder(R.drawable.default_image)
                    .crossFade()
                    .into(imageView);
        }
        orderHolder.tvNum.setText("共"+num+"件");
        orderHolder.tvPrice.setText("总价：￥"+total);

        switch (order.getState()){
            case Constant.OrderState.StateUnpay:
                orderHolder.tvState.setText("待付款");
                break;
            case Constant.OrderState.StateUnsend:
                orderHolder.tvState.setText("待发货");
                break;
            case Constant.OrderState.StateUnreceived:
                orderHolder.tvState.setText("等待收货");
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    int width;
    public class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.ll_goods)
        LinearLayout llGoods;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.ll_container)
        LinearLayout ll_container;

         public OrderHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
             width = view.getContext().getResources().getDimensionPixelOffset(R.dimen.dp40);
        }

        @OnClick({R.id.ll_container})
        public void onClick(){
            EventBus.getDefault().post(new OrderEvent(OrderEvent.EnterDetail,getAdapterPosition()));
        }
    }


}
