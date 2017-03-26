package com.itheima.heimamall.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 一个订单是针对同一个商家的一次购买行为，会包含多个不同商品
 * 订单实体类
 * 字段：
 * id  
 * ordernum		订单号
 * merchant		商家信息
 * goodsinfos	订购的商品信息,会包含多个商品信息
 * uid			下单的用户id
 * address		收货地址信息
 * payinfo		付款信息,包括付款渠道
 * state		订单状态, 1:待付款    2：已付款，待发货     3：已发货，待收货
 * createtime	订单创建时间
 * @author lxj
 *
 */
public class Order implements Serializable{
	private String id;
	private String ordernum;

	public ArrayList<CartItem> getCartinfos() {
		return cartinfos;
	}

	public void setCartinfos(ArrayList<CartItem> cartinfos) {
		this.cartinfos = cartinfos;
	}

	private ArrayList<CartItem> cartinfos;
	private String uid;
	private Address address;
	private PayInfo payinfo;
	private int state;
	/**订单创建的时间 */
	private long createtime;//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public PayInfo getPayinfo() {
		return payinfo;
	}
	public void setPayinfo(PayInfo payinfo) {
		this.payinfo = payinfo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	/** 获取：订单创建的时间*/
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	
	
}
