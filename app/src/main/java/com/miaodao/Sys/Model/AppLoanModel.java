package com.miaodao.Sys.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 贷款提交
 * Created by Home_Pc on 2017/3/27.
 */

public class AppLoanModel implements Serializable {
    private String userId;
    private String productId;
    private String cycle;
    private String amount;
    private String bankName;
    private String cardNo;
    private Map<String,Purpose> purposeMap;
    private List<String> purposeList;
    private Purpose purpose;
    private String instId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Map<String, Purpose> getPurposeMap() {
        return purposeMap;
    }

    public void setPurposeMap(Map<String, Purpose> purposeMap) {
        this.purposeMap = purposeMap;
    }

    public List<String> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<String> purposeList) {
        this.purposeList = purposeList;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }
}
