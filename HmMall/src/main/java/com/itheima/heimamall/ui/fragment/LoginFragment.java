package com.itheima.heimamall.ui.fragment;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.UserResult;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.Logger;
import com.itheima.heimamall.util.MD5Util;
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
 * Created by lxj on 2016/10/13.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_username)
    TextInputEditText etUsername;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    @Override
    public int getLayoutId() {
        return R.layout.layout_login;
    }

    @Override
    protected void setListener() {

    }

    // 点击登录 按钮
    @OnClick({R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (checkUsernameAndPassword(etUsername, etPassword)) {
                    executeLogin();
                }
                break;
        }
    }

    @Override
    public void loadData() {
        stateLayout.showContentView();
    }

    /**
     * 执行登录请求
     */
    private void executeLogin() {
        DialogUtil.showProgressDialog(getActivity(), "register", "正在登录");
        PostParams postParams = new PostParams();
        postParams.add("username", etUsername.getText().toString());
        /*
        Message Digest Algorithm MD5（中文名为消息摘要算法第五版）
        为计算机安全领域广泛使用的一种散列函数，用以提供消息的完整性保护。
        该算法的文件号为RFC 1321（R.Rivest,MIT Laboratory for Computer Science and RSA Data Security Inc. April 1992）。
        MD5即Message-Digest Algorithm 5（信息-摘要算法5），用于确保信息传输完整一致。
        是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法），主流编程语言普遍已有MD5实现。
        将数据（如汉字）运算为另一固定长度值，是杂凑算法的基础原理，MD5的前身有MD2、MD3和MD4。
        MD5算法具有以下特点：
        1、压缩性：任意长度的数据，算出的MD5值长度都是固定的。
        2、容易计算：从原数据计算出MD5值很容易。
        3、抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
        4、强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
        MD5的作用是让大容量信息在用数字签名软件签署私人密钥前被"压缩"成一种保密的格式（就是
        把一个任意长度的字节串变换成一定长的十六进制数字串）。
        除了MD5以外，其中比较有名的还有sha-1、RIPEMD以及Haval等。
         */
        postParams.add("password", MD5Util.md5(etPassword.getText().toString())); /* 对密码进行 md5签名 */
        /* compile 'com.github.li-xiaojun:OkHttpEngine:1.2' */
        OkHttpEngine.create()
                .post( /* 使用 post 请求服务器*/
                        Url.login,
                        postParams,
                        new OkHttpCallback<UserResult>(){
            @Override
            public void onSuccess(UserResult userBaseBean) {
                DialogUtil.dismissDialog("register");
                if(userBaseBean.isSuccess()){
                    Logger.e("tag", userBaseBean.data.toString());
                    //1.保存最新的用户数据
                    SharePrefUtil.create_SP_Instance(getActivity()).saveObj(Constant.User, userBaseBean.data);
                    HeimaMallApp.user = userBaseBean.data;

                    /* compile 'org.greenrobot:eventbus:3.0.0' */
                    //通知界面更新
                    EventBus.getDefault().post(userBaseBean.data); /*Posts the given event to the event bus.*/
                    ToastUtil.showToast("登录成功");
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
     * 检查用户名和密码
     *
     * @return
     */
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
