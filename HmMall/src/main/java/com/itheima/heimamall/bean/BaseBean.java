package com.itheima.heimamall.bean;

import java.io.Serializable;

/**
 * Created by lxj on 2016/10/30.
 */

public class BaseBean<T> implements Serializable {
    /** code为服务器处理的状态，0是逻辑成功，-1是逻辑错误*/
    public int code;//
    public String msg;
    /** 返回的真实的数据，类型不确定 */
    public T data;//


    public boolean isSuccess(){
        return code==0;
    }


}
