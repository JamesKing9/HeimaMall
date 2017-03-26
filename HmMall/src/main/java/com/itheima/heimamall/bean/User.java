package com.itheima.heimamall.bean;

import java.io.Serializable;

/**
 * Created by lxj on 2016/8/21.
 */
public class User implements Serializable{
    public String uid;
    public String username;
    public String nickname;
    public String password;
    public String phone;
    public int gender;//2:男性， 1：女性
    public long birthday;//出生日期
    public String avatar;//头像图片


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
