package com.itheima.heimamall.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.itheima.heimamall.bean.BaseBean;
import com.itheima.heimamall.bean.UpdateInfo;
import com.itheima.heimamall.bean.UpdateInfoResult;
import com.itheima.heimamall.global.Url;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import java.io.IOException;

/**
 * Created by lxj on 2016/10/6.
 */

public class UpdateUtil {
    public static void checkUpdate(Context context){
        PostParams params = new PostParams();
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            params.add("versioncode", info.versionCode+"");
            OkHttpEngine.create().post(Url.checkUpdate, params, new OkHttpCallback<UpdateInfoResult>(){
                @Override
                public void onSuccess(UpdateInfoResult updateInfoBaseBean) {
                    if(updateInfoBaseBean.isSuccess()){
                        if(updateInfoBaseBean.data!=null && updateInfoBaseBean.data.isNeedUpdate){
                            //...do something
                        }else {
                            ToastUtil.showToast("当前已经是最新版本！");
                        }
                    }
                }
                @Override
                public void onFail(IOException e) {

                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
