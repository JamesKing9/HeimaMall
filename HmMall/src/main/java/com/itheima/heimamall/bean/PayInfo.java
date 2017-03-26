package com.itheima.heimamall.bean;

import java.io.Serializable;

/**
 * 付款渠道
 * channelnum	渠道号
 * channelname  渠道名称，如支付宝，微信
 * @author lxj
 *
 */
public class PayInfo implements Serializable{
	
	private int channelnum;//渠道号,1是微信，2是支付宝
	private String channelname;//渠道名称
	public int getChannelnum() {
		return channelnum;
	}
	public void setChannelnum(int channelnum) {
		this.channelnum = channelnum;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	
	
}
