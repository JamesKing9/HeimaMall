package com.itheima.heimamall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.itheima.heimamall.R;

import butterknife.ButterKnife;

/**
 * Created by lxj on 2016/9/15.
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        this(context, R.style.BottomDialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(getLayoutId());
        ButterKnife.bind(this);

        //宽度全屏
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = width;
        getWindow().setAttributes(attributes);

    }

    public abstract int getLayoutId();
}
