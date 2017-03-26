package com.itheima.heimamall.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.UserResult;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.sms.SMSHeaderFactory;
import com.itheima.heimamall.sms.SmsApi;
import com.itheima.heimamall.sms.SmsResult;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.Logger;
import com.itheima.heimamall.util.MD5Util;
import com.itheima.heimamall.util.PhoneUtil;
import com.itheima.heimamall.util.SharePrefUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * “注册”新用户
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_register_username)
    TextInputEditText etRegisterUsername;
    @BindView(R.id.et_register_password)
    TextInputEditText etRegisterPassword;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.btn_obtain_code)
    Button btnSendCode;
    @BindView(R.id.et_valide_code)
    TextInputEditText etValideCode;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }

    @Override
    protected void setListener() {
    }
    @Override
    public void loadData() {
        stateLayout.showContentView();
    }

    @OnClick({R.id.btn_obtain_code, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_obtain_code: /*获取验证码*/
                if (checkPhone()) {
                    //发送验证码
                    sendSms();
                    btnSendCode.setEnabled(false);
                    btnSendCode.setText("正在发送");
                }
                break;
            case R.id.btn_register:
                if (checkInput()) {
                    //1.先让服务器验证一下验证码的有效性
                    verifySmsCode();
                }
                break;
        }
    }

    /**
     * 将验证码提交到服务器进行验证是否正确
     */
    private void verifySmsCode() {
        DialogUtil.showProgressDialog(getActivity(), "register", "正在提交验证码");
        PostParams params = new PostParams();
        params.add("mobile",etPhone.getText().toString());
        params.add("code",etValideCode.getText().toString());
        OkHttpEngine.create()
                .setHeaders(SMSHeaderFactory.genSmsHeaders())
                .post(SmsApi.VerifySms, params, new OkHttpCallback<SmsResult>() {
                    @Override
                    public void onSuccess(SmsResult result) {
                        Logger.e("tag","sms: "+result);
                        if(result.isSuccess()){
                            //验证成功,执行注册操作！
                            executeRegister();

                        }else {
                            ToastUtil.showToast(result.msg);
                        }
                    }
                    @Override
                    public void onFail(IOException e) {
                        ToastUtil.showToast("验证失败");
                        btnSendCode.setEnabled(true);
                        btnSendCode.setText("请重新发送");
                    }
                });
    }

    /**
     * 发送短信验证码
     */
    private void sendSms() {
        PostParams params = new PostParams();
        params.add("mobile",etPhone.getText().toString());
        OkHttpEngine.create()
                .setHeaders(SMSHeaderFactory.genSmsHeaders())
                .post(SmsApi.SendSms, params, new OkHttpCallback<SmsResult>() {
                    @Override
                    public void onSuccess(SmsResult result) {
                        Logger.e("tag","sms: "+result);
                        Logger.e("tag","headers: "+SMSHeaderFactory.genSmsHeaders());
                        if(result.isSuccess()){
                            ToastUtil.showToast("发送成功!");
                            //开始倒计时
                            countDown();
                        }else {
                            ToastUtil.showToast(result.msg);
                            btnSendCode.setEnabled(true);
                            btnSendCode.setText("重新发送");
                        }
                    }
                    @Override
                    public void onFail(IOException e) {
                        ToastUtil.showToast("验证码发送失败");
                        btnSendCode.setEnabled(true);
                        btnSendCode.setText("重新发送");
                    }
                });
    }

    /**
     * 倒计时
     */
    private void countDown() {
        ValueAnimator animator = ValueAnimator.ofInt(60, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                btnSendCode.setText(animatedValue + "");
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                btnSendCode.setEnabled(true);
                btnSendCode.setText("重新发送");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                btnSendCode.setEnabled(false);
            }
        });
        animator.setDuration(60 * 1000).start();
    }

    private boolean checkInput() {
        if (!checkUsernameAndPassword(etRegisterUsername, etRegisterPassword)) {
            return false;
        }

        if (!checkPhone()) {
            return false;
        }

        if (TextUtils.isEmpty(etValideCode.getText().toString())) {
            ToastUtil.showToast("验证码不能为空！");
            return false;
        }

        return true;
    }
    private boolean checkUsernameAndPassword(TextInputEditText etUser, TextInputEditText etPass) {
        final String username = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        //1.检查用户名是否为空
        if (TextUtils.isEmpty(username)) {
            etUser.setError("用户名不能为空！");
            return false;
        }
        //2.检查用户名是否合法
        if (!isRightInput(username)) {
            etUser.setError("用户名必须是5-15位数字和字母组合");
            return false;
        }
        //3.检查密码是否为空
        if (TextUtils.isEmpty(password)) {
            etPass.setError("密码不能为空！");
            return false;
        }
        //4.检查密码是否合法
        if (!isRightInput(password)) {
            etPass.setError("密码必须是5-15位数字和字母组合");
            return false;
        }
        return true;
    }


    /**
     * 检查Phone
     *
     * @return
     */
    private boolean checkPhone() {
        String phone = etPhone.getText().toString().trim();
        //检查手机号是否为空
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("手机号码不能为空！");
            return false;
        }
        if (!PhoneUtil.isPhoneNumber(phone)) {
            etPhone.setError("手机号不正确！");
            return false;
        }
        return true;
    }



    private void executeRegister() {
        DialogUtil.showProgressDialog(getActivity(), "register", "正在注册");
        final String username = etRegisterUsername.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        //提交注册
        PostParams postParams = new PostParams();
        postParams.add("username", username);
        postParams.add("password", MD5Util.md5(password));
        postParams.add("phone", phone);
        OkHttpEngine.create().post(Url.register, postParams, new OkHttpCallback<UserResult>(){
            @Override
            public void onSuccess(UserResult userBaseBean) {
                if(userBaseBean.isSuccess()){
                    Logger.e("tag", userBaseBean.data.toString());
                    //1.保存最新的用户数据
                    SharePrefUtil.create_SP_Instance(getActivity())
                            .saveObj(Constant.User, userBaseBean.data);
                    HeimaMallApp.user = userBaseBean.data;

                    //关闭对话框
                    DialogUtil.dismissDialog("register");
                    //通知界面更新
                    EventBus.getDefault().post(userBaseBean.data);
                    ToastUtil.showToast("注册成功");
                    getActivity().finish();
                }else {
                    ToastUtil.showToast(userBaseBean.msg);
                    DialogUtil.dismissDialog("register");
                }
            }
            @Override
            public void onFail(IOException e) {
                ToastUtil.showToast("请求失败");
                DialogUtil.dismissDialog("register");
            }
        });
    }

    /**
     * 是否是合法的用户名
     *
     * @param username
     * @return
     */
    private boolean isRightInput(String username) {
        String rg = "^[0-9a-zA-Z]{5,15}$";
        return username.matches(rg);
    }
}
