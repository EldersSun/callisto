package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * 用途
 * Created by Home_Pc on 2017/3/27.
 */

public class Purpose implements Serializable{
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
