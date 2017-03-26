package com.itheima.heimamall.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.Button;

import com.itheima.heimamall.R;
import com.itheima.heimamall.util.SharePrefUtil;
import com.itheima.heimamall.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置 主机ip
 * ip 保存到 heimamall.cfg.xml 中，键为serverhost；
 */
public class SetServerActivity extends BaseActivity {
    @BindView(R.id.et_server)
    TextInputEditText etServer;
    @BindView(R.id.btn)
    Button btn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_server;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        showTitle(false, "设置服务器的ip", "");

        String serverhost = SharePrefUtil.create_SP_Instance(this).getString("serverhost", null);
        if(!TextUtils.isEmpty(serverhost)){
//            startActivity(new Intent(this,SplashActivity.class));
//            finish();
            etServer.setText(serverhost);
        }
    }


    @OnClick(R.id.btn)
    public void onClick() {
        String string = etServer.getText().toString();
        if(!TextUtils.isEmpty(string)){
            SharePrefUtil.create_SP_Instance(this).saveString("serverhost",string);
            ToastUtil.showToast("保存成功！");
            startActivity(new Intent(this,SplashActivity.class));
            finish();
        }
    }
}
