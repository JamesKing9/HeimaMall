package com.itheima.heimamall.bean;

import java.io.Serializable;

/**
 * 购物车实体类
 * @author lxj
 * 字段：
 * id	
 * goods	商品
 * num		商品数量
 * uid		用户id
 */
public class CartItem implements Serializable{
	private String id;
	private Goods goods;
	private int num;
	private String uid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
