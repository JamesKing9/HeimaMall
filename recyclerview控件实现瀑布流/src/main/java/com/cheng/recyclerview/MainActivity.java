package com.cheng.recyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>使用RecyclerView控件实现瀑布流</p>
 * http://www.jianshu.com/p/402466ae7b15
 */
public class MainActivity extends AppCompatActivity {

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // 初始化RecyclerView
        initRecyclerView((RecyclerView) findViewById(R.id.recyclerview));
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView 主控件
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true); // 设置固定大小
        initRecyclerLayoutManager(recyclerView); // 初始化LayoutManager
        initRecyclerAdapter(recyclerView); // 初始化Adapter
        initItemDecoration(recyclerView); // 初始化边界装饰
        initItemAnimator(recyclerView); // 初始化动画效果
    }

    /**
     * 初始化RecyclerView的LayoutManager
     *
     * @param recyclerView 主控件
     */
    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        // 错列网格布局
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
    }

    /**
     * 初始化RecyclerView的Adapter
     *
     * @param recyclerView 主控件
     */
    private void initRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new MyAdapter(getData());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化RecyclerView的(ItemDecoration)项目装饰
     *
     * @param recyclerView 主控件
     */
    private void initItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new MyItemDecoration(this));
    }

    /**
     * 初始化RecyclerView的(ItemAnimator)项目动画
     *
     * @param recyclerView 主控件
     */
    private void initItemAnimator(RecyclerView recyclerView) {
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // 默认动画
    }

    /**
     * 模拟的数据
     *
     * @return 数据
     */
    private ArrayList<DataModel> getData() {
        int count = 57;
        ArrayList<DataModel> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DataModel model = new DataModel();

            model.setDateTime(getBeforeDay(new Date(), i));
            model.setLabel("No. " + i);

            data.add(model);
        }

        return data;
    }

    /**
     * 获取日期的前一天
     *
     * @param date 日期
     * @param i    偏离
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
