package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/25.
 */
public class MobileUserLoanInfo implements Serializable {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 交易流水号
     */
    private String transId;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 周期
     */
    private String cycle;
    /**
     * 产品类型
     */
    private String subTransName;
    /**
     * 提现索引卡号(后4位）
     */
    private String cardIdxNo;
    /**
     * 银行编号
     */
    private String instId;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 申请日期
     */
    private String applyDate;
    /**
     * 申请时间
     */
    private String applyTime;
    /**
     * 金额
     */
    private String amount;
    /**
     * 费用
     */
    private String fee;
    /**
     * 实际到账金额
     */
    private String onAcctAmount;
    /**
     * 确认时间
     */
    private String confirmTime;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 应还款日期
     */
    private String repayDate;
    /**
     * 实际还款时间
     */
    private String repayedTime;
    /**
     * 逾期天数
     */
    private String overdueDays;
    /**
     * 逾期金额
     */
    private String overdueAmount;
    /**
     * 已还款金额
     */
    private String repayedAmount;
    /**
     * 未还款金额
     */
    private String unRepayedAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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

    public String getSubTransCode() {
        return subTransName;
    }

    public void setSubTransCode(String subTransName) {
        this.subTransName = subTransName;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOnAcctAmount() {
        return onAcctAmount;
    }

    public void setOnAcctAmount(String onAcctAmount) {
        this.onAcctAmount = onAcctAmount;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayedTime() {
        return repayedTime;
    }

    public void setRepayedTime(String repayedTime) {
        this.repayedTime = repayedTime;
    }

    public String getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getRepayedAmount() {
        return repayedAmount;
    }

    public void setRepayedAmount(String repayedAmount) {
        this.repayedAmount = repayedAmount;
    }

    public String getUnRepayedAmount() {
        return unRepayedAmount;
    }

    public void setUnRepayedAmount(String unRepayedAmount) {
        this.unRepayedAmount = unRepayedAmount;
    }

    public String getSubTransName() {
        return subTransName;
    }

    public void setSubTransName(String subTransName) {
        this.subTransName = subTransName;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "MobileUserLoanInfo{" +
                "userId='" + userId + '\'' +
                ", transId='" + transId + '\'' +
                ", productId='" + productId + '\'' +
                ", cycle='" + cycle + '\'' +
                ", subTransName='" + subTransName + '\'' +
                ", cardIdxNo='" + cardIdxNo + '\'' +
                ", instId='" + instId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", amount='" + amount + '\'' +
                ", fee='" + fee + '\'' +
                ", onAcctAmount='" + onAcctAmount + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", repayDate='" + repayDate + '\'' +
                ", repayedTime='" + repayedTime + '\'' +
                ", overdueDays=" + overdueDays +
                ", overdueAmount='" + overdueAmount + '\'' +
                ", repayedAmount='" + repayedAmount + '\'' +
                ", unRepayedAmount='" + unRepayedAmount + '\'' +
                '}';
    }
}
