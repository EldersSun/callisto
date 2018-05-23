package com.miaodao.Sys.Widgets.Citypickerview.model;

import java.io.Serializable;

/**
 * @version $Id: Area.java, v 0.0.1 2017/3/9 10:26 callie Exp $
 */
public class Area implements Serializable {

    /**
     * code CODE.
     */
    private String code;
    /**
     * name NAME.
     */
    private String name;
    /**
     * cityCode CITY_CODE.
     */
    private String cityCode;

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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
