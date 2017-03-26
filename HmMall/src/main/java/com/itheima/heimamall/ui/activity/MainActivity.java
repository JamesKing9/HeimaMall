package com.itheima.heimamall.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.itheima.heimamall.R;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.ui.fragment.BaseFragment;
import com.itheima.heimamall.ui.fragment.CartFragment;
import com.itheima.heimamall.ui.fragment.HomeFragment;
import com.itheima.heimamall.ui.fragment.MeFragment;

import java.util.ArrayList;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_cart)
    RadioButton rbCart;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    ArrayList<BaseFragment> fragments;
    @Override
    protected void setListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {

        prepareFragments();

        showFragment(0);


//        OkHttpEngine.create()
//                    .get(String.format(Url.getGoodsList,"man",0),new OkHttpCallback<GoodsList>(){
//                        @Override
//                        public void onSuccess(GoodsList goodsResult) {
//                            Log.e("tag",goodsResult.data.toString());
//                        }
//                        @Override
//                        public void onFail(IOException e) {
//
//                        }
//                    });


    }

    /**
     * 准备所有的Fragment
     */
    private void prepareFragments() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CartFragment());
        fragments.add(new MeFragment());
    }

    private void showFragment(int position){
        //显示标题
        switch (position){
            case 0:
                showTitle(false,"首页","");
                break;
            case 1:
                showTitle(false,"购物车","");
                break;
            case 2:
                showTitle(false,"我","");
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //1.先隐藏其他的
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if(i==position){
                if(fragment.isAdded()){
                    transaction.show(fragment);
                }else {
                    //add
                    transaction.add(R.id.fl_container,fragment);
                }
            }else {
                if(fragment.isAdded() ){
                    transaction.hide(fragment);
                }
            }
        }
        //commit
        transaction.commitAllowingStateLoss();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                showFragment(0);
                break;
            case R.id.rb_cart:
                //需要判断当前有没有登录
                if(HeimaMallApp.user!=null){
                    showFragment(1);
                }else {
                    rbHome.setChecked(true);
                    startActivity(new Intent(this,LoginActivity.class));
                }
                break;
            case R.id.rb_me:
                showFragment(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
    }
}
