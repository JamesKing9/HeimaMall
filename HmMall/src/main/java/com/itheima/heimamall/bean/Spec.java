package com.itheima.heimamall.bean;

import java.io.Serializable;

/**
 * Created by lxj on 2016/8/12.
 */
public class Spec implements Serializable{
    private String name;
    private String options;//规格的选项

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Spec{" +
                "name='" + name + '\'' +
                ", options='" + options + '\'' +
                '}';
    }
}
