package com.cheng.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<DataModel> mDataModels;
    private List<Integer> mHeights;

    MyAdapter(List<DataModel> dataModels) {
        if (dataModels == null) {
            throw new IllegalArgumentException("DataModel must not be null");
        }
        mDataModels = dataModels;
        mHeights = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataModel dataModel = mDataModels.get(position);

        // 随机高度, 模拟瀑布效果.
        if (mHeights.size() <= position) {
            mHeights.add((int) (100 + Math.random() * 300));
        }

        ViewGroup.LayoutParams lp = holder.getTvLabel().getLayoutParams();
        lp.height = mHeights.get(position);

        holder.getTvLabel().setLayoutParams(lp);

        holder.getTvLabel().setText(dataModel.getLabel());
        holder.getTvDateTime().setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                .format(dataModel.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return mDataModels.size();
    }

    public void addData(int position) {
        DataModel model = new DataModel();
        model.setDateTime(getBeforeDay(new Date(), position));
        model.setLabel("No. " + (int) (new Random().nextDouble() * 20.0f));

        mDataModels.add(position, model);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDataModels.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 获取日期的前一天
     *
     * @param date 日期
     * @param i    偏离
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }
}