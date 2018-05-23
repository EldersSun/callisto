package com.miaodao.Fragment.Account.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class Card {
//
//     "cardIdxNo": "**** **** **** 8194",
//             "cardType": "储蓄卡",
//             "instId": "CCB",
//             "instName": "建设银行",
//             "signId": "20170420000000000031"


    private String cardIdxNo;
    private String cardType;
    private String instId;
    private String instName;
    private String signId;

    public Card() {
    }

    public String getCardIdxNo() {
        return cardIdxNo == null ? "" : cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getCardType() {
        return cardType == null ? "" : cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getInstId() {
        return instId == null ? "" : instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getInstName() {
        return instName == null ? "" : instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getSignId() {
        return signId == null ? "" : signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
