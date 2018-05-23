package com.miaodao.Fragment.Account.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class Bill {

//      "amount": "2000.00",
//              "applyDate": "2017年05月06日",
//              "applyTime": null,
//              "bankName": null,
//              "cardIdxNo": "8194",
//              "confirmTime": "2017-05-07 00:00:00",
//              "cycle": "7",
//              "instId": null,
//              "integrateFee": "0.00",
//              "onAcctAmount": "1900.00",
//              "overdue": true,
//              "overdueAmount": "0.00",
//              "overdueDays": 0,
//              "payTime": "2017年05月06日",
//              "productId": "100001",
//              "repayDate": "2017年05月13日",
//              "repayDays": "0",
//              "repayedAmount": "0.00",
//              "repayedTime": null,
//              "subTransCode": "现金贷",
//              "subTransName": "现金贷",
//              "transId": "10120170506095826000000000182214",
//              "unRepayedAmount": "2000.00",
//              "userId": "00120170420000000214"


    private String amount;
    private String applyDate;
    private String applyTime;
    private String bankName;
    private String cardIdxNo;
    private String confirmTime;
    private String cycle;
    private String instId;
    private String integrateFee;
    private String onAcctAmount;
    private boolean overdue;
    private String overdueAmount;
    private String overdueDays;
    private String payTime;
    private String productId;
    private String repayDate;
    private String repayDays;
    private String repayedAmount;
    private String repayedTime;
    private String subTransCode;
    private String subTransName;
    private String transId;
    private String unRepayedAmount;
    private String userId;

    public Bill() {
    }

    public String getAmount() {
        return amount == null ? "" : amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplyDate() {
        return applyDate == null ? "" : applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyTime() {
        return applyTime == null ? "" : applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getBankName() {
        return bankName == null ? "" : bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardIdxNo() {
        return cardIdxNo == null ? "" : cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getConfirmTime() {
        return confirmTime == null ? "" : confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getCycle() {
        return cycle == null ? "" : cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getInstId() {
        return instId == null ? "" : instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getIntegrateFee() {
        return integrateFee == null ? "" : integrateFee;
    }

    public void setIntegrateFee(String integrateFee) {
        this.integrateFee = integrateFee;
    }

    public String getOnAcctAmount() {
        return onAcctAmount == null ? "" : onAcctAmount;
    }

    public void setOnAcctAmount(String onAcctAmount) {
        this.onAcctAmount = onAcctAmount;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public String getOverdueAmount() {
        return overdueAmount == null ? "" : overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getOverdueDays() {
        return overdueDays == null ? "" : overdueDays;
    }

    public void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getPayTime() {
        return payTime == null ? "" : payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getProductId() {
        return productId == null ? "" : productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRepayDate() {
        return repayDate == null ? "" : repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayDays() {
        return repayDays == null ? "" : repayDays;
    }

    public void setRepayDays(String repayDays) {
        this.repayDays = repayDays;
    }

    public String getRepayedAmount() {
        return repayedAmount == null ? "" : repayedAmount;
    }

    public void setRepayedAmount(String repayedAmount) {
        this.repayedAmount = repayedAmount;
    }

    public String getRepayedTime() {
        return repayedTime == null ? "" : repayedTime;
    }

    public void setRepayedTime(String repayedTime) {
        this.repayedTime = repayedTime;
    }

    public String getSubTransCode() {
        return subTransCode == null ? "" : subTransCode;
    }

    public void setSubTransCode(String subTransCode) {
        this.subTransCode = subTransCode;
    }

    public String getSubTransName() {
        return subTransName == null ? "" : subTransName;
    }

    public void setSubTransName(String subTransName) {
        this.subTransName = subTransName;
    }

    public String getTransId() {
        return transId == null ? "" : transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getUnRepayedAmount() {
        return unRepayedAmount == null ? "" : unRepayedAmount;
    }

    public void setUnRepayedAmount(String unRepayedAmount) {
        this.unRepayedAmount = unRepayedAmount;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
