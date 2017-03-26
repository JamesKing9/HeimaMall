package com.itheima.heimamall.util;

import java.text.SimpleDateFormat;

/**
 * Created by lxj on 2016/8/30.
 */
public class DateUtil {
    public static String timestamp2ymd(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(timestamp);
    }

    /**
     * 将时间格式化为 "yyyy-MM-dd HH:mm:ss" 形式
     * @param timestamp
     * @return
     */
    public static String to_ymd_hms(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

}
