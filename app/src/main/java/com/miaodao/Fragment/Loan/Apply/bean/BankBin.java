package com.miaodao.Fragment.Loan.Apply.bean;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/6/9 16:22
 */

public class BankBin {

//     "data": {
//        "cardInfo": {
//            "bankName": "中国建设银行",
//                    "cardType": "D",
//                    "cardTypeName": "借记卡",
//                    "instId": "CCB"
//        },


    private String bankName;
    private String cardType;
    private String cardTypeName;
    private String instId;

    public BankBin() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }
}
