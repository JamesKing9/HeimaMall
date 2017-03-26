package com.itheima.heimamall.bean;


import java.io.Serializable;

/**
 * 收货地址信息
 * id		    唯一标识
 * uid		用户id
 * receiver		收货人
 * phone		手机号
 * area			省市县地区
 * detail		详细地址，街道，楼牌号
 * @author lxj
 *
 */
public class Address implements Serializable{
	public String id;
	public String uid;
	public String receiver;
	public String phone;
	public String area;
	public String detail;

}
