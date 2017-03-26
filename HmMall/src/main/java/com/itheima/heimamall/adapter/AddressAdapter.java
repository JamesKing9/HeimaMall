package com.itheima.heimamall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Address;
import com.itheima.heimamall.event.AddressEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/12.
 */
public class AddressAdapter extends RecyclerView.Adapter {
    ArrayList<Address> addressList;
    @BindView(R.id.tv_receiver)
    TextView tvReceiver;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    public AddressAdapter(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_addresslist, null);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddressHolder addressHolder = (AddressHolder) holder;

        Address address = addressList.get(position);

        addressHolder.tvReceiver.setText(address.receiver + "     " + address.phone);
        addressHolder.tvArea.setText(address.area + ' ' + address.detail);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class AddressHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_receiver)
        TextView tvReceiver;
        @BindView(R.id.tv_area)
        TextView tvArea;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.ll_container)
        LinearLayout ll_container;

        public AddressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tv_delete,R.id.ll_container})
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_delete:
                    EventBus.getDefault().post(new AddressEvent(AddressEvent.Delete,getAdapterPosition()));
                    break;
                case R.id.ll_container:
                    EventBus.getDefault().post(new AddressEvent(AddressEvent.Set,addressList.get(getAdapterPosition())));
                    break;

            }
        }
    }


}
