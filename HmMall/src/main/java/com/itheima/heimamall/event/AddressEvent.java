package com.itheima.heimamall.event;

/**
 * Created by lxj on 2016/9/13.
 */
public class AddressEvent extends BaseEvent{
    public static final int Delete = 1;
    public static final int Update = 2;
    public static final int Set = 3;
    public static final int BeDeleted = 4;

    public AddressEvent(int code, Object data) {
        super(code, data);
    }
}
