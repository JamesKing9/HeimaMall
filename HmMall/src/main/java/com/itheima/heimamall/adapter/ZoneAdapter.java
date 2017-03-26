package com.itheima.heimamall.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.City;
import com.itheima.heimamall.bean.Province;
import com.itheima.heimamall.util.Logger;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/15.
 */
public class ZoneAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<T> list = new ArrayList<>();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_zone, null);
        return new ZoneHolder(view);
    }

    private int selectPosition = -1;

    public void resetSelect(){
        selectPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rvholder, int position) {

        ZoneHolder holder = (ZoneHolder) rvholder;

        T zone = list.get(position);
        if(zone instanceof Province){
            holder.tvName.setText(((Province) zone).name);
        }else if(zone instanceof City){
            holder.tvName.setText(((City) zone).name);
        }else if(zone instanceof String){
            holder.tvName.setText((String) zone);
        }

        holder.tvName.setBackgroundColor(position==selectPosition
                ?holder.itemView.getResources().getColor(R.color.primary):Color.WHITE);
        holder.tvName.setTextColor(position==selectPosition?Color.WHITE:Color.BLACK);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<T> zoneList){
        list.clear();
        list.addAll(zoneList);
        notifyDataSetChanged();
    }

    class ZoneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public ZoneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick (R.id.tv_name)
        public void onClick(){
            selectPosition = getAdapterPosition();
            notifyDataSetChanged();

            if(listener!=null){
                listener.onZoneSelect(list.get(selectPosition));
            }
        }
    }
    OnZoneSelectListener listener;
    public void setOnZoneSelectListener(OnZoneSelectListener listener){
        this.listener = listener;
    }
    public interface OnZoneSelectListener{
        void onZoneSelect(Object obj);
    }
}
