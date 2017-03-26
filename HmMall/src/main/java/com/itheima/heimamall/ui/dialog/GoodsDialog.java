package com.itheima.heimamall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.Goods;
import com.itheima.heimamall.bean.Spec;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.view.FlowLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/9/5.
 */
public class GoodsDialog extends BaseDialog {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_reduce)
    ImageView ivReduce;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.ll_spec)
    LinearLayout llSpec;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_dialog)
    LinearLayout llDialog;

    int num = 1;

    public GoodsDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_goods;
    }

    @Override
    protected void onStart() {
        super.onStart();
        num = 1;
    }

    public void bindData(Goods goods) {
        Glide.with(getContext())
                .load(Url.ImagePrefix + goods.getImgs().get(0))
                .placeholder(R.drawable.default_image)
                .centerCrop()
                .into(ivIcon);
        tvName.setText(goods.getName());
        tvPrice.setText("￥" + goods.getPrice());
        tvNum.setText(""+num);


        //动态生成规格布局
        ArrayList<Spec> specs = goods.getSpecs();
        if (specs != null && specs.size() > 0) {
            for (int i = 0; i < specs.size(); i++) {
                Spec spec = specs.get(i);
                SpecLayout specLayout = new SpecLayout();
                //填充数据
                specLayout.bindData(spec);
                llSpec.addView(specLayout.specView);
                //改变对话框的高度
//                updateDialogHeight();

            }
        }
    }


    class SpecLayout implements View.OnClickListener{
        public View specView;
        @BindView(R.id.tv_spec_name)
        TextView tvSpecName;
        @BindView(R.id.flowLayout)
        FlowLayout flowLayout;
        private final int hpadding;
        private final int vpadding;

        public SpecLayout() {
            specView = View.inflate(getContext(), R.layout.layout_goods_spec, null);
            ButterKnife.bind(this,specView);
            vpadding = getContext().getResources().getDimensionPixelSize(R.dimen.dp5);
            hpadding = getContext().getResources().getDimensionPixelSize(R.dimen.dp12);
        }
        String[] options = null;
        public void bindData(Spec spec) {
            tvSpecName.setText(spec.getName());
            //
            options = spec.getOptions().split("#");
            flowLayout.setAdapter(new FlowLayout.FlowLayoutAdapter() {
                @Override
                public int getCount() {
                    return options.length;
                }
                @Override
                public View getView(int position) {
                    TextView textView = new TextView(getContext());
                    textView.setBackgroundResource(R.drawable.bg_num_picker);
                    textView.setTextColor(getContext().getResources().getColor(R.color.black));
                    textView.setPadding(hpadding,vpadding,hpadding,vpadding);

                    textView.setText(options[position]);
                    textView.setTag(position);

                    textView.setOnClickListener(SpecLayout.this);

                    return textView;
                }
            });
        }

        @Override
        public void onClick(View v) {
            reset();
            TextView textView = (TextView) flowLayout.getChildAt((int)v.getTag());
            textView.setBackgroundResource(R.drawable.bg_num_picker_checked);
            textView.setTextColor(getContext().getResources().getColor(R.color.white));

            //记录所选的规格
            Spec selectedSpec = new Spec();
            selectedSpec.setName(tvSpecName.getText().toString());
            selectedSpec.setOptions(textView.getText().toString());

            specMap.remove(selectedSpec.getName());
            specMap.put(selectedSpec.getName(),selectedSpec);
        }
        private void reset(){
            for (int i = 0; i < flowLayout.getChildCount(); i++) {
                TextView textView = (TextView) flowLayout.getChildAt(i);
                textView.setBackgroundResource(R.drawable.bg_num_picker);
                textView.setTextColor(getContext().getResources().getColor(R.color.black));
            }
        }
    }

    private HashMap<String,Spec> specMap = new HashMap<>();

    @OnClick({R.id.iv_reduce, R.id.iv_add, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reduce:
                if(num==1)return;
                num--;
                tvNum.setText(""+num);
                break;
            case R.id.iv_add:
                num++;
                tvNum.setText(""+num);
                break;
            case R.id.btn_confirm:
                if(listener!=null){
                    Collection<Spec> values =  specMap.values();
                    if(values!=null){
                        ArrayList<Spec> list = new ArrayList<>();
                        Iterator<Spec> iterator = values.iterator();
                        while (iterator.hasNext()){
                            list.add(iterator.next());
                        }
                        listener.onGoodsConfirm(list,num);
                    }else {
                        listener.onGoodsConfirm(null,num);
                    }
                }
                dismiss();
                break;
        }
    }
    OnGoodsDialogConfirmListener listener;
    public void setOnGoodsConfirmListener(OnGoodsDialogConfirmListener listener){
        this.listener = listener;
    }
    public interface OnGoodsDialogConfirmListener{
        void onGoodsConfirm(List<Spec> specList,int num);
    }
}
