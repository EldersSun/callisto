package com.miaodao.Sys.Widgets.Citypickerview.model;

import java.io.Serializable;

/**
 * @version $Id: City.java, v 0.0.1 2017/3/9 10:24 callie Exp $
 */
public class City implements Serializable{

    /**
     * code CODE.
     */
    private String code;
    /**
     * name NAME.
     */
    private String name;
    /**
     * provinceCode PROVINCE_CODE.
     */
    private String provinceCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
