package com.itheima.heimamall.sms;

/**
 * Created by lxj on 2016/9/21.
 */

public class SmsResult {
    public String code;//操作吗
    public String msg;//操作结果信息
    public String obj;//验证码数据

    public boolean isSuccess(){
        return code.equals("200");
    }
}
