package com.cheng.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView mTvLabel; // 标签
    private TextView mTvDateTime; // 日期

    public MyViewHolder(View itemView) {
        super(itemView);
        mTvLabel = (TextView) itemView.findViewById(R.id.item_text);
        mTvDateTime = (TextView) itemView.findViewById(R.id.item_date);
    }

    public TextView getTvLabel() {
        return mTvLabel;
    }

    public TextView getTvDateTime() {
        return mTvDateTime;
    }

}