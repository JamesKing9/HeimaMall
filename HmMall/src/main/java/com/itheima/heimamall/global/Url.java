package com.itheima.heimamall.global;

import com.itheima.heimamall.util.SharePrefUtil;

/**
 * url常量定义
 * Created by lxj on 2016/8/8.
 */
public class Url {
    public static String ServerHost = "http://172.16.17.120:8080/heimamall/";

    static {
        String serverhost = SharePrefUtil.create_SP_Instance(HeimaMallApp.context).getString("serverhost", null);
        ServerHost = String.format("http://%s:8080/heimamall/",serverhost);;
    }

//   public static  public static String ServerHost = "http://192.168.2.108:8080/heimamall/";

//    public static String ServerHost = "http://192.168.81.82:8080/heimamall/";

    //图片的url前缀
    public static String ImagePrefix = ServerHost + "image/";


    //获取商品列表
    public static String getGoodsList = ServerHost + "goodslist/%s?page=%d";

    //注册
    public static String register = ServerHost + "register";
    //登录
    public static String login = ServerHost + "login";
    //更新用户信息
    public static String updateUser = ServerHost + "updateuser";
    public static String updateAvatar = ServerHost + "updateavatar";
    //购物车
    public static String addCart = ServerHost + "addcart";
    public static String cartList = ServerHost + "cartlist";
    public static String deleteCart = ServerHost + "deletecart";
    //收货地址
    public static String getAddress = ServerHost + "getaddress";
    public static String delAddress = ServerHost + "deleteaddress";
    public static String addAddress = ServerHost + "addaddress";
    //订单
    public static String submitOrder = ServerHost + "submitorder";
    public static String updateOrder = ServerHost + "updateorder";
    public static String getOrderList = ServerHost + "getorders";
    public static String genSign = ServerHost + "gensign";
    //检查更新
    public static String checkUpdate = ServerHost + "checkupdate";
}
