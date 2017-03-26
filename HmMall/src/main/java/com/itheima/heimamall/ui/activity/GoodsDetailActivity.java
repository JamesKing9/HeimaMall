package com.itheima.heimamall.ui.activity;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itheima.heimamall.R;
import com.itheima.heimamall.adapter.BannerAdapter;
import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.Goods;
import com.itheima.heimamall.bean.SimpleResult;
import com.itheima.heimamall.bean.Spec;
import com.itheima.heimamall.event.CartEvent;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.dialog.GoodsDialog;
import com.itheima.heimamall.ui.view.FlowLayout;
import com.itheima.heimamall.ui.view.SquareViewPager;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.GsonUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/8/31.
 */
public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    SquareViewPager viewpager;
    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_oprice)
    TextView tvOprice;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;
    @BindView(R.id.tv_merchant)
    TextView tvMerchant;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.btn_addcart)
    Button btnAddcart;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    private Goods goods;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void setListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvIndicator.setText((position + 1) + "/" + viewpager.getAdapter().getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void initData() {
        showTitle(true, "商品详情", null);

        goods = (Goods) getIntent().getSerializableExtra("goods");

        refreshUI();

    }

    private void refreshUI() {
        if (goods != null) {
            BannerAdapter bannerAdapter = new BannerAdapter(goods.getImgs());
            viewpager.setAdapter(bannerAdapter);

            tvName.setText(goods.getName());
            tvPrice.setText("￥" + goods.getPrice());
            tvOprice.setText("￥" + goods.getOprice());
            tvOprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSales.setText("销量  " + goods.getSales());
            tvMerchant.setText(goods.getMerchant());
            tvDesc.setText(goods.getDesc());

            tvIndicator.setText("1/" + bannerAdapter.getCount());

            String[] services = goods.getService().split("#");
            flowLayout.setAdapter(new MyAdapter(services));

        }
    }

    @OnClick({R.id.btn_addcart, R.id.btn_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addcart:
                if (HeimaMallApp.user == null) {
                    ToastUtil.showToast("请先登录！");
                    return;
                }
                DialogUtil.showGoodsDialog(this, goods, new GoodsDialog.OnGoodsDialogConfirmListener() {
                    @Override
                    public void onGoodsConfirm(List<Spec> specList, int num) {
                        if (specList != null && specList.size() > 0) {
                            submitCart(specList, num);
                        } else {
                            if (goods.getSpecs() == null || goods.getSpecs().size() == 0) {
                                //提交购物车
                                submitCart(null, num);
                            }
                        }
                    }
                });
                break;
        }
    }

    /**
     * 提交购物车
     *
     * @param specList
     */
    private void submitCart(List<Spec> specList, final int num) {
        DialogUtil.showProgressDialog(this, "cart", "正在添加");

        PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        params.add("gid", goods.getId());
        params.add("num", num + "");
        params.add("category", goods.getCategory());
        params.add("specs", GsonUtil.toJson(specList));//商品规格数据
        OkHttpEngine.create().post(Url.addCart, params, new OkHttpCallback<SimpleResult>() {

            @Override
            public void onSuccess(SimpleResult stringBaseBean) {
                if (stringBaseBean.isSuccess()) {
                    ToastUtil.showToast("添加成功！");
                    DialogUtil.dismissDialog("cart");

                    //更新Cart界面
                    EventBus.getDefault().post(new CartEvent(0, null));
                } else {
                    ToastUtil.showToast(stringBaseBean.msg);
                    DialogUtil.dismissDialog("cart");
                }
            }
            @Override
            public void onFail(IOException e) {
                ToastUtil.showToast("请求失败");
                DialogUtil.dismissDialog("cart");
            }
        });
    }

    class MyAdapter implements FlowLayout.FlowLayoutAdapter {
        private String[] services;
        Drawable drawable;

        public MyAdapter(String[] services) {
            this.services = services;
            drawable = getResources().getDrawable(R.mipmap.ic_service);
        }

        @Override
        public int getCount() {
            return services.length;
        }

        @Override
        public View getView(int position) {
            TextView textView = new TextView(GoodsDetailActivity.this);
            textView.setCompoundDrawablePadding(5);
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            textView.setTextSize(15);
            textView.setTextColor(getResources().getColor(R.color.secondary_text));
            textView.setText(services[position]);

            return textView;
        }
    }
}
