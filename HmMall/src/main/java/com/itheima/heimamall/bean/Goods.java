package com.itheima.heimamall.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lxj on 2016/8/12.
 */
public class Goods implements Serializable{
    private String id;
    private String name;
    private String category;
    private String desc;
    private String merchant;
    private String service;
    private float price;//价格
    private float oprice;//原价
    private int sales;//销量
    private ArrayList<String> imgs;//图片
    private ArrayList<Spec> specs;//商品规格

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getOprice() {
        return oprice;
    }

    public void setOprice(float oprice) {
        this.oprice = oprice;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(ArrayList<Spec> specs) {
        this.specs = specs;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", desc='" + desc + '\'' +
                ", merchant='" + merchant + '\'' +
                ", service='" + service + '\'' +
                ", price=" + price +
                ", oprice=" + oprice +
                ", sales=" + sales +
                ", imgs=" + imgs +
                ", specs=" + specs +
                '}';
    }
}
