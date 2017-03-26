package com.itheima.heimamall.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Order;
import com.itheima.heimamall.bean.OrderResult;
import com.itheima.heimamall.bean.SignResult;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.Logger;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.alipayutil.AliPayHelper;
import com.lxj.alipayutil.PayResult;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/17.
 */
public class PayActivity extends BaseActivity {
    @BindView(R.id.tv_totalprice)
    TextView tvTotalprice;
    @BindView(R.id.tv_gopay)
    TextView tvGopay;
    private Order order;
    private float total;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        showTitle(true, "选择付款方式", "");

        order = (Order) getIntent().getSerializableExtra("order");
        total = getIntent().getFloatExtra("total", 0f);
        tvTotalprice.setText("总价：￥"+ total);
    }

    @OnClick(R.id.tv_gopay)
    public void onClick() {
        DialogUtil.showProgressDialog(this,"pay","正在付款");

        //1.从服务器获取订单的签名信息
        getOrderSign("黑马商城订单","",total+"",order.getOrdernum());


    }

    @Override
    public void onLeftBtnClick() {
        onBackPressed();
    }

    /**
     * 从服务器获取签名信息
     */
    public void getOrderSign(final String subject, final String body, final String price, final String orderNum) {
        PostParams params = new PostParams();
        params.add("uid",HeimaMallApp.user.uid);
        params.add("orderInfo", AliPayHelper.getOrderInfo(subject,body,price,orderNum));


        OkHttpEngine.create().post(Url.genSign, params, new OkHttpCallback<SignResult>(){
            @Override
            public void onSuccess(SignResult orderSignBaseBean) {
                if(orderSignBaseBean.isSuccess()){
                    DialogUtil.dismissDialog("pay");

                    //执行支付
                    executePay(subject,body,price,orderNum,orderSignBaseBean.data.sign);
                }else {
                    DialogUtil.dismissDialog("pay");
                    ToastUtil.showToast(orderSignBaseBean.msg);
                }
            }
            @Override
            public void onFail(IOException e) {
                DialogUtil.dismissDialog("pay");
                ToastUtil.showToast("签名获取失败");
            }
        });

    }

    /**
     * 进行支付
     */
    private void executePay(String subject,String body,String price,String orderNum,String sign) {
        AliPayHelper.init("2088011085074233","917356107@qq.com");
        //进行支付
        AliPayHelper.pay(this, subject, body, price, orderNum, sign, new AliPayHelper.PayResultCallback() {
            @Override
            public void onSuccess(PayResult payResult) {

                //这里是支付宝sdk返回的支付成功的信息！
                //修改订单的状态为已经支付
                updateOrderState();
            }
            @Override
            public void onConfirming(PayResult payResult) {
                ToastUtil.showToast("支付确认中!");
                DialogUtil.dismissDialog("pay");
            }
            @Override
            public void onCancel(PayResult payResult) {
                ToastUtil.showToast("取消支付!");
                DialogUtil.dismissDialog("pay");
            }
            @Override
            public void onFail(PayResult payResult) {
                Logger.e("tag",payResult.toString());
                ToastUtil.showToast("支付失败!");
                DialogUtil.dismissDialog("pay");
            }
        });
    }

    /**
     * 更新订单的state
     */
    private void updateOrderState() {
        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        params.add("orderid",order.getId());
        params.add("state",Constant.OrderState.StateUnsend+"");
        OkHttpEngine.create().post(Url.updateOrder, params, new OkHttpCallback<OrderResult>(){
            @Override
            public void onSuccess(OrderResult orderBaseBean) {
                if(orderBaseBean.isSuccess()){
                    DialogUtil.dismissDialog("pay");
                    ToastUtil.showToast("支付成功!");

                    //跳转到订单详情
                    order = orderBaseBean.data;
                    Intent intent = new Intent(PayActivity.this, OrderDetailActivity.class);
                    intent.putExtra("order",order);
                    startActivity(intent);
                    finish();
                }else {
                    DialogUtil.dismissDialog("pay");
                    ToastUtil.showToast(orderBaseBean.msg);
                }
            }
            @Override
            public void onFail(IOException e) {
                DialogUtil.dismissDialog("pay");
                ToastUtil.showToast("请求失败!");
            }
        });

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
       DialogUtil.showConfirmDialog(this, "unpay", "提示", "您确定要取消付款吗？", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               //跳转到订单详情界面
               Intent intent = new Intent(PayActivity.this, OrderDetailActivity.class);
               intent.putExtra("order",order);
               startActivity(intent);

               finish();
           }
       });
    }


}
