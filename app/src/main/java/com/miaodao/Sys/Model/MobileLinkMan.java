package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/14.
 */
public class MobileLinkMan implements Serializable {

    /**
     * 用户姓名
     */
    private String name;
    /**
     * 联系人类型
     */
    private String type;
    /**
     * 用户手机号
     */
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MobileLinkMan{" +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", mobile='" + mobile + '\'' +
                '}' + super.toString();
    }
}
