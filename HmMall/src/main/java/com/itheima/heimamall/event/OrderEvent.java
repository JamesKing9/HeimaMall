package com.itheima.heimamall.event;

/**
 * Created by lxj on 2016/9/18.
 */
public class OrderEvent extends BaseEvent {
    public static final int EnterDetail = 1;

    public OrderEvent(int code, Object data) {
        super(code, data);
    }
}
