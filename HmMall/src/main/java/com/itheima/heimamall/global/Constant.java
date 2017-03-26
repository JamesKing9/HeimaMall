package com.itheima.heimamall.global;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 * Created by lxj on 2016/8/5.
 */
public interface Constant {
    /**商品分类常量**/
    interface GoodsCategory{
       String Women = "women"; //潮流女装
       String Man = "man"; //品牌男装
       String Child = "child"; //母婴童装
       String Phone = "phone"; //手机数码
       String Book = "book"; //图书音像
    }

    /**文件目录定义**/
    interface Dir{
        String BaseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + HeimaMallApp.context.getPackageName();
    }

    /**定义缓存文件的大小**/
    interface Cache{
        long Size = 1024*1024*100;//100M
    }


    interface OrderState{
        int StateUnpay = 1;//待付款
        int StateUnsend = 2;//已经付款，待发货
        int StateUnreceived = 3;//已经发货，待确认收货
    }

    //当前登录用户
    String User = "User";

}
