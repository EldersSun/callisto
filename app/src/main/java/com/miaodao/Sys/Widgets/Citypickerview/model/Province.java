package com.miaodao.Sys.Widgets.Citypickerview.model;

import java.io.Serializable;

/**
 * @version $Id: Province.java, v 0.0.1 2017/3/9 10:23 callie Exp $
 */
public class Province implements Serializable {

    /**
     * code CODE.
     */
    private String code;
    /**
     * name NAME.
     */
    private String name;

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


}
