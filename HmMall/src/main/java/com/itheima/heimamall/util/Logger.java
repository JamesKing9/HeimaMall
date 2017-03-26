package com.itheima.heimamall.util;


import android.util.Log;

import com.itheima.heimamall.BuildConfig;

/**
 * Created by lxj on 2016/8/5.
 */
public class Logger {
    public static void e(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void e(Object object,String msg){
        if(BuildConfig.DEBUG){
            Log.e(object.getClass().getSimpleName(),msg);
        }
    }

    public static void d(Object object,String msg){
        if(BuildConfig.DEBUG){
            Log.d(object.getClass().getSimpleName(),msg);
        }
    }
}
