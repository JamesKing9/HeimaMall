package com.itheima.heimamall.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.itheima.heimamall.R;

/**
 * Created by lxj on 2016/8/8.
 */
public class StateLayout extends FrameLayout {

    private View loadingView;
    private View emptyView;
    private View errorView;
    private View contentView;

    public StateLayout(Context context) {
        this(context,null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化3种布局
     */
    private void init() {
        loadingView = View.inflate(getContext(), R.layout.layout_loading,null);
        addView(loadingView);

        errorView = View.inflate(getContext(), R.layout.layout_error,null);
        TextView textView = (TextView) errorView.findViewById(R.id.btn_reload);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onReload();
                }
            }
        });
        addView(errorView);

        emptyView = View.inflate(getContext(),R.layout.layout_empty,null);
        addView(emptyView);

        hideAllView();
    }

    /**
     * 设置ContentView
     * @param view
     */
    public void setContentView(View view){
        this.contentView = view;
        this.contentView.setVisibility(View.INVISIBLE);
        addView(this.contentView);
    }

    private void hideAllView(){
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);

        if(this.contentView!=null){
            this.contentView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示loadingView
     *
     */
    public void showLoadingView(){
        hideAllView();
        loadingView.setVisibility(View.VISIBLE);
    }
    /**
     * 显示errorView
     *
     */
    public void showErrorView(){
        hideAllView();
        errorView.setVisibility(View.VISIBLE);
    }

    public void showEmptyView(){
        hideAllView();
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 显示contentView
     *
     */
    public void showContentView(){
        hideAllView();
        contentView.setVisibility(View.VISIBLE);
    }

    private OnReloadListener listener;
    public void setOnReloadListener(OnReloadListener listener){
        this.listener = listener;
    }
    public interface OnReloadListener{
        void onReload();
    }

}
