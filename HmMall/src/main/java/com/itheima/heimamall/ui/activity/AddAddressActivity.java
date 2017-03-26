package com.itheima.heimamall.ui.activity;

import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.Province;
import com.itheima.heimamall.bean.SimpleResult;
import com.itheima.heimamall.event.AddressEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.dialog.AddressDialog;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.GsonUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/12.
 */
public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.et_receiver)
    TextInputEditText etReceiver;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.et_detail)
    TextInputEditText etDetail;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_zone)
    LinearLayout llZone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        showTitle(true, "添加收货地址", "保存");

    }

    private boolean checkInput(){
        if(TextUtils.isEmpty(etReceiver.getText())){
            ToastUtil.showToast("收货人不能为空！");
            return false;
        }
        if(TextUtils.isEmpty(etPhone.getText())){
            ToastUtil.showToast("手机号不能为空！");
            return false;
        }
        if(TextUtils.isEmpty(tvArea.getText())){
            ToastUtil.showToast("省市地址不能为空！");
            return false;
        }
        if(TextUtils.isEmpty(etDetail.getText())){
            ToastUtil.showToast("详细地址不能为空！");
            return false;
        }
        return true;
    }

    @Override
    public void onRightTitleClick() {
        if(!checkInput()){
            return;
        }

        DialogUtil.showProgressDialog(this, "save", "正在保存");
        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        params.add("receiver", etReceiver.getText().toString());
        params.add("phone", etPhone.getText().toString());
        params.add("area", tvArea.getText().toString());
        params.add("detail", etDetail.getText().toString());
        OkHttpEngine.create()
                    .post(Url.addAddress,params, new OkHttpCallback<SimpleResult>() {
                        @Override
                        public void onSuccess(SimpleResult stringBaseBean) {

                            if(stringBaseBean.isSuccess()){
                                DialogUtil.dismissDialog("save");
                                ToastUtil.showToast("添加成功!");
                                //通知地址列表界面更新数据
                                EventBus.getDefault().post(new AddressEvent(AddressEvent.Update, null));
                                finish();
                            }else {
                                DialogUtil.dismissDialog("save");
                                ToastUtil.showToast("添加失败!");
                            }
                        }
                        @Override
                        public void onFail(IOException e) {
                            DialogUtil.dismissDialog("save");
                            ToastUtil.showToast("请求失败!");
                        }
                    });
    }
    private Handler handler = new Handler();
    @OnClick(R.id.ll_zone)
    public void onClick() {
        final AddressDialog addressDialog = new AddressDialog(this);
        addressDialog.show();
        addressDialog.setOnZoneDialogConfirmListener(new AddressDialog.OnZoneDialogConfirmListener() {
            @Override
            public void onZoneDialogConfirm(String province, String city, String area) {
                addressDialog.dismiss();
                tvArea.setText(province+" "+city +" "+area);
            }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    InputStream is = getAssets().open("address.json");
                    final ArrayList<Province> provinceList = (ArrayList<Province>) GsonUtil.parseJsonToList(is,new TypeToken<List<Province>>(){}.getType());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            addressDialog.bindData(provinceList);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

}
