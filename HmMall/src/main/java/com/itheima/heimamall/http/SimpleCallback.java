package com.itheima.heimamall.http;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lxj.okhttpengine.callback.OkHttpCallback;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 对OkhttpCallback进行统一处理
 * Created by lxj on 2016/8/23.
 */
public abstract class SimpleCallback extends OkHttpCallback<String> {
    @Override
    public void onSuccess(String result) {
        if (result!=null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.getInt("code");
                if(code==0){
                    //传递data数据
                    onLogicSuccess(jsonObject.getString("data"));
                }else if(code==-1){
                    onLogicError(code,jsonObject.getString("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求成功并且操作成功调用的方法
     *
     * @param t
     */
    public abstract void onLogicSuccess(String t);

    /**
     * 请求成功并且操作失败调用的方法
     *
     * @param code
     * @param msg
     */
    public abstract void onLogicError(int code, String msg);

}
