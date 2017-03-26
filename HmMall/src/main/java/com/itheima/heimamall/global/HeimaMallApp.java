package com.itheima.heimamall.global;

import android.app.Application;
import android.content.Context;

import com.itheima.heimamall.bean.User;
import com.itheima.heimamall.util.SharePrefUtil;
import com.lxj.okhttpengine.OkHttpEngine;

import java.io.File;
import java.util.concurrent.TimeUnit;


/**
 * 自定义的 Application 类
 */
public class HeimaMallApp extends Application {

    public static Context context;
    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        /* com.github.li-xiaojun:OkHttpEngine:1.2 */
        //初始化OkHttpEngine配置
        OkHttpEngine.create()
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .cache(new File(getCacheDir(),"cache"),Constant.Cache.Size);

        //获取登录的user
        user = (User) SharePrefUtil.create_SP_Instance(this).getObj(Constant.User);
    }

}
