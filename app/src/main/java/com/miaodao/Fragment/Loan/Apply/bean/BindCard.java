package com.miaodao.Fragment.Loan.Apply.bean;

import org.litepal.crud.DataSupport;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/6/11 09:27
 */

public class BindCard extends DataSupport {

    private String cardNo;
    private String bankName;
    private String bankCity;
    private String phoneNo;

    public String getCardNo() {
        return cardNo == null ? "" : cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName == null ? "" : bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCity() {
        return bankCity == null ? "" : bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getPhoneNo() {
        return phoneNo == null ? "" : phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
