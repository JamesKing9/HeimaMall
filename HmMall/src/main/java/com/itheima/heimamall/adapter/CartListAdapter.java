package com.itheima.heimamall.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.CartItem;
import com.itheima.heimamall.bean.Spec;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/9.
 */
public class CartListAdapter extends RecyclerView.Adapter {
    @BindView(R.id.tv_merchant)
    TextView tvMerchant;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_delete_cart)
    ImageView ivDeleteCart;
    private ArrayList<CartItem> list;

    private ArrayList<CartItem> selectedCart = new ArrayList<>();

    public CartListAdapter(ArrayList<CartItem> list) {
        this.list = list;
    }

    public void resetSelectCart(){
        selectedCart.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_cartlist, null);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartHolder cartHolder = (CartHolder) holder;
        CartItem item = list.get(position);
        Glide.with(cartHolder.itemView.getContext())
                .load(Url.ImagePrefix + item.getGoods().getImgs().get(0))
                .placeholder(R.drawable.default_image) /* 设置一个占位图，在图片没有加载过来的时候先显示这张图 */
                .centerCrop()
                .into(cartHolder.ivIcon);

        if(selectedCart.contains(item)){
            cartHolder.tvMerchant.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shopcart_checked,0,0,0);
        }else {
            cartHolder.tvMerchant.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shopcart_unchecked,0,0,0);
        }

        cartHolder.tvMerchant.setText(item.getGoods().getMerchant());
        cartHolder.tvName.setText(item.getGoods().getName());
        cartHolder.tvPrice.setText("￥" + item.getGoods().getPrice());
        cartHolder.tvNum.setText("数量  " + item.getNum());
        cartHolder.tvSpec.setText(buildSpec(item.getGoods().getSpecs()));

    }

    private String buildSpec(ArrayList<Spec> specs) {
        if(specs==null)return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < specs.size(); i++) {
            Spec spec = specs.get(i);
            builder.append(spec.getName());
            builder.append(":");
            builder.append(spec.getOptions());

            if (i < (specs.size() - 1)) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class CartHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_merchant)
        TextView tvMerchant;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_spec)
        TextView tvSpec;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.iv_delete_cart)
        ImageView ivDeleteCart;

        public CartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick({R.id.tv_merchant, R.id.iv_delete_cart})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_merchant:
                    CartItem item = list.get(getAdapterPosition());
                    if(selectedCart.contains(item)){
                        selectedCart.remove(item);
                    }else {
                        selectedCart.add(item);
                    }
                    notifyItemChanged(getAdapterPosition());

                    //更新总价
                    EventBus.getDefault().post(selectedCart);

                    break;
                case R.id.iv_delete_cart:
                    DialogUtil.showConfirmDialog((Activity) itemView.getContext(), "delete", "提示", "您真的要从购物车中删除该商品吗？"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //删除该购物车item
                                    EventBus.getDefault().post(list.get(getAdapterPosition()));
                                }
                            });
                    break;
            }
        }

    }

}
