package com.miaodao.Sys.Model;

/**
 * Created by daixinglong on 2017/4/13.
 */

public class RepayType {
//
//            "cardNo": "8198",
//          "channelId": "1001",
//          "decisionId": "9d86fa6351764f1a940051a40f0143ba",
//          "instId": "CCB",
//          "instName": "建设银行",
//          "valid": true


    /**
     * channelId 1001:银行卡;  1002 支付宝;  1003 微信
     */

    private int isChoose;
    private String cardNo;
    private String channelId;
    private String deviceId;
    private String instId;
    private String instName;
    private String decisionId;
    private Boolean valid;

    public RepayType() {
    }

    public RepayType(int isChoose, String cardNo, String channelId, String deviceId, String instId, String instName, String decisionId, Boolean valid) {
        this.isChoose = isChoose;
        this.cardNo = cardNo;
        this.channelId = channelId;
        this.deviceId = deviceId;
        this.instId = instId;
        this.instName = instName;
        this.valid = valid;
        this.decisionId = decisionId;
    }

    public int getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(int isChoose) {
        this.isChoose = isChoose;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
    }
}
