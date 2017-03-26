package com.itheima.heimamall.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.itheima.heimamall.R;
import com.itheima.heimamall.ui.fragment.LoginFragment;
import com.itheima.heimamall.ui.fragment.RegisterFragment;

public class LoginActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setListener() {
    }
    @Override
    protected void initData() {

        showTitle(true, "登录", "注册");
        replaceFragment(new LoginFragment());
    }
    private boolean isShowLogin = true;
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment).commitAllowingStateLoss();
    }

    @Override
    public void onRightTitleClick() {
        super.onRightTitleClick();
        if (isShowLogin) {
            showTitle(true, "注册", "登录");
            replaceFragment(new RegisterFragment());
        } else {
            showTitle(true, "登录", "注册");
            replaceFragment(new LoginFragment());
        }
        isShowLogin = !isShowLogin;
    }
}
