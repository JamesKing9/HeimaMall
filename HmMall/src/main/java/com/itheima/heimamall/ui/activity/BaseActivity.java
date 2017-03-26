package com.itheima.heimamall.ui.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.itheima.heimamall.R;

import butterknife.ButterKnife;

/**
 * Created by lxj on 2016/8/1.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initTitleBar();

        //view初始化完毕，就设置设置各种监听器
        setListener();

        //加载数据
        initData();
    }


    View btnBack;
    TextView tvTitle;
    TextView tvTitleRight;
    private void initTitleBar() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitleRight = (TextView) findViewById(R.id.tv_title_right);
    }

    /**
     * 显示标题
     * @param title
     */
    protected void showTitle(boolean isShowBack,String title,String titleRight){

        if(isShowBack){
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftBtnClick();
                }
            });
        }else {
            btnBack.setVisibility(View.GONE);
        }

        if(tvTitle!=null && !TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
        if(titleRight!=null && !TextUtils.isEmpty(titleRight)){
            tvTitleRight.setText(titleRight);
            tvTitleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightTitleClick();
                }
            });
        }
    }


    public void onRightTitleClick(){
    }
    public void onLeftBtnClick(){
        finish();
    }

    public abstract int getLayoutId();

    protected abstract void setListener();

    protected abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
