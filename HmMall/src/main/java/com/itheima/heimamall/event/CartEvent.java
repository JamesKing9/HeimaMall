package com.itheima.heimamall.event;

/**
 * Created by lxj on 2016/9/18.
 */
public class CartEvent extends BaseEvent{
    public static final int Update = 1;//更新

    public CartEvent(int code, Object data) {
        super(code, data);
    }
}
