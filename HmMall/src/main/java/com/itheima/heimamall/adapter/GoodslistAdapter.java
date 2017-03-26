package com.itheima.heimamall.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Goods;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.Logger;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxj on 2016/8/14.
 */
public class GoodslistAdapter extends RecyclerView.Adapter<GoodslistAdapter.GoodslistHolder> {

    protected ArrayList<Goods> list;

    public GoodslistAdapter(ArrayList<Goods> list) {
        this.list = list;
    }


    @Override
    public GoodslistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_goodslist, null);
        return new GoodslistHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodslistHolder holder, final int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(position);
                }
            }
        });

        Goods goods = list.get(position);

        String imgUrl = Url.ImagePrefix+goods.getImgs().get(0);
        Glide.with(holder.itemView.getContext())
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .crossFade(800)
                .into(holder.ivImage);
        holder.tvTitle.setText(goods.getName());
        holder.tvPrice.setText("￥"+Float.toString(goods.getPrice()));

        //设置删除线
        holder.tvOprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvOprice.setText("￥"+goods.getOprice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GoodslistHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_oprice)
        TextView tvOprice;

        public GoodslistHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    OnGoodsItemClickListener listener;
    public void setOnItemClickListener(OnGoodsItemClickListener listener){
        this.listener = listener;
    }
    public interface OnGoodsItemClickListener{
        void onItemClick(int position);
    }

}
