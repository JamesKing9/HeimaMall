package com.itheima.heimamall.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.User;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.ui.activity.AboutActivity;
import com.itheima.heimamall.ui.activity.AddressManagerActivity;
import com.itheima.heimamall.ui.activity.EditProfileActivity;
import com.itheima.heimamall.ui.activity.LoginActivity;
import com.itheima.heimamall.ui.activity.ShowOrderActivity;
import com.itheima.heimamall.util.SharePrefUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.itheima.heimamall.util.UpdateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/8/1.
 */
public class MeFragment extends BaseFragment {


    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.tv_show_order)
    TextView tvShowOrder;
    @BindView(R.id.tv_manager_address)
    TextView tvManagerAddress;
    @BindView(R.id.tv_clear_cache)
    TextView tvClearCache;
    @BindView(R.id.tv_check_update)
    TextView tvCheckUpdate;
    @BindView(R.id.tv_about_mall)
    TextView tvAboutMall;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);
        stateLayout.showContentView();
    }

    @Override
    public void loadData() {
        //检查有没有登录
        if(HeimaMallApp.user!=null){
            //显示用户信息
            showUserInfo();

        }else{
            showNeedLogin();
        }
    }

    public void showUserInfo() {

        Glide.with(getActivity()).load(Url.ImagePrefix+HeimaMallApp.user.avatar)
                .placeholder(R.mipmap.avatar_default)
                .crossFade(1000)
                .into(ivAvatar);
        if(TextUtils.isEmpty(HeimaMallApp.user.nickname)){
            tvName.setText("匿名用户");
        }else {
            tvName.setText(HeimaMallApp.user.nickname);
        }

        //显示退出登录按钮
        btnLogout.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void refreshUserInfo(User user){
        showUserInfo();
    }

    private void showNeedLogin() {
        ivAvatar.setImageResource(R.mipmap.avatar_default);
        tvName.setText("点击登录");
        //隐藏退出登录按钮
        btnLogout.setVisibility(View.INVISIBLE);
    }


    @OnClick({ R.id.ll_profile, R.id.tv_show_order, R.id.tv_manager_address, R.id.tv_clear_cache, R.id.tv_check_update, R.id.tv_about_mall, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_profile:
                if(HeimaMallApp.user==null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    //去编辑个人信息界面
                    startActivity(new Intent(getActivity(),EditProfileActivity.class));
                }
                break;
            case R.id.tv_show_order:
                if(HeimaMallApp.user==null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), ShowOrderActivity.class));
                }
                break;
            case R.id.tv_manager_address:
                if(HeimaMallApp.user==null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), AddressManagerActivity.class));
                }
                break;
            case R.id.tv_check_update:
                UpdateUtil.checkUpdate(getContext());
                break;
            case R.id.tv_about_mall:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        SharePrefUtil.create_SP_Instance(getActivity()).remove(Constant.User);
        HeimaMallApp.user = null;
        showNeedLogin();
        ToastUtil.showToast("注销成功！");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
