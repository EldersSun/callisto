package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by Home_Pc on 2017/3/25.
 */

public class Cycle implements Serializable {
    private String name;
    private int value;
    private int days;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
