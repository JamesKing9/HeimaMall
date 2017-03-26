package com.itheima.heimamall.ui.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.itheima.heimamall.R;
import com.itheima.heimamall.adapter.ZoneAdapter;
import com.itheima.heimamall.bean.City;
import com.itheima.heimamall.bean.Province;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/15.
 */
public class AddressDialog extends BaseDialog {

    @BindView(R.id.province_view)
    RecyclerView provinceView;
    @BindView(R.id.city_view)
    RecyclerView cityView;
    @BindView(R.id.area_view)
    RecyclerView areaView;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    ZoneAdapter<Province> provinceAdapter = new ZoneAdapter<>();
    ZoneAdapter<City> cityAdapter = new ZoneAdapter<>();
    ZoneAdapter<String> areaAdapter = new ZoneAdapter<>();

    public AddressDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_address;
    }
    private Province selectProvince;
    private City selectCity;
    private String selectArea;
    public void bindData(final ArrayList<Province> provincesList){
        if(provincesList==null)return;

        provinceView.setLayoutManager(new LinearLayoutManager(getContext()));
        provinceView.setAdapter(provinceAdapter);
        cityView.setLayoutManager(new LinearLayoutManager(getContext()));
        cityView.setAdapter(cityAdapter);
        areaView.setLayoutManager(new LinearLayoutManager(getContext()));
        areaView.setAdapter(areaAdapter);

        provinceAdapter.setData(provincesList);
        provinceAdapter.setOnZoneSelectListener(new ZoneAdapter.OnZoneSelectListener() {
            @Override
            public void onZoneSelect(Object obj) {
                selectProvince = (Province) obj;
                cityAdapter.setData(selectProvince.city);

                //重置其他2个选中的
                cityAdapter.resetSelect();
                areaAdapter.resetSelect();
            }
        });

        cityAdapter.setOnZoneSelectListener(new ZoneAdapter.OnZoneSelectListener() {
            @Override
            public void onZoneSelect(Object obj) {
                selectCity = (City) obj;
                areaAdapter.setData(selectCity.area);

                areaAdapter.resetSelect();
            }
        });

        areaAdapter.setOnZoneSelectListener(new ZoneAdapter.OnZoneSelectListener() {
            @Override
            public void onZoneSelect(Object obj) {
                selectArea = (String) obj;
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        if(selectArea!=null && selectCity!=null && selectProvince!=null && listener!=null){
            listener.onZoneDialogConfirm(selectProvince.name,selectCity.name,selectArea);
        }
    }
    private OnZoneDialogConfirmListener listener;
    public void setOnZoneDialogConfirmListener(OnZoneDialogConfirmListener listener){
        this.listener = listener;
    }

    public interface OnZoneDialogConfirmListener{
        void onZoneDialogConfirm(String province,String city,String area);
    }
}
