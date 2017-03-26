package com.itheima.heimamall.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.view.ViewTreeObserver;

import com.itheima.heimamall.R;
import com.lxj.ripplelayout.RippleLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/8/1.
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.waveLayout)
    RippleLayout waveLayout;
    @BindView(R.id.enter)
    View enter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setListener() {

    }
    @OnClick({R.id.enter})
    public void processClick(View view){
        switch (view.getId()){
            case R.id.enter:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        waveLayout.setRippleColor(Color.parseColor("#88FFFFFF"));
        enter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                enter.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                waveLayout.setRippleCenter(enter.getLeft()+enter.getWidth()/2,enter.getTop()+enter.getHeight()/2);
                waveLayout.startRipple();
            }
        });

    }



}
