package com.itheima.heimamall.event;

/**
 * Created by lxj on 2016/9/13.
 */
public class BaseEvent {

    public BaseEvent(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int code;
    public Object data;
}
