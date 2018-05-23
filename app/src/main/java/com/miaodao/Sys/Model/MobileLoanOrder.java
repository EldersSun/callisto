package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/27.
 */
public class MobileLoanOrder implements Serializable{

    /**
     * 交易流水号
     */
    private String transId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 提现卡号
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
     * 卡姓名
     */
    private String userName;
    /**
     * 贷款金额
     */
    private String amount;
    /**
     * 服务费用
     */
    private String fee;
    /**
     * 实际到账
     */
    private String onAcctAmount;
    /**
     * 贷款状态
     */
    private String loanState;
    /**
     * 贷款状态说明
     */
    private String loanStateDesc;
    /**
     * 申请日期
     */
    private String applyDate;
    /**
     * 申请时间
     */
    private String applyTime;
    /**
     * 确认日期
     */
    private String confirmDate;
    /**
     * 支付日期
     */
    private String payDate;
    /**
     * 拒绝编码
     */
    private String rejectCode;
    /**
     * 拒绝原因
     */
    private String rejectReason;
    /**
     * 用户确认状态
     */
    private String userConfirmState;
    /**
     * 用户确认状态说明
     */
    private String userConfirmStateDesc;
    /**
     * 预计还款日期
     */
    private String refundDate;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanStateDesc() {
        return loanStateDesc;
    }

    public void setLoanStateDesc(String loanStateDesc) {
        this.loanStateDesc = loanStateDesc;
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

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getUserConfirmState() {
        return userConfirmState;
    }

    public void setUserConfirmState(String userConfirmState) {
        this.userConfirmState = userConfirmState;
    }

    public String getUserConfirmStateDesc() {
        return userConfirmStateDesc;
    }

    public void setUserConfirmStateDesc(String userConfirmStateDesc) {
        this.userConfirmStateDesc = userConfirmStateDesc;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
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
        return "MobileLoanOrder{" +
                "transId='" + transId + '\'' +
                ", userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", cardIdxNo='" + cardIdxNo + '\'' +
                ", instId='" + instId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", userName='" + userName + '\'' +
                ", amount='" + amount + '\'' +
                ", fee='" + fee + '\'' +
                ", onAcctAmount='" + onAcctAmount + '\'' +
                ", loanState='" + loanState + '\'' +
                ", loanStateDesc='" + loanStateDesc + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", confirmDate='" + confirmDate + '\'' +
                ", payDate='" + payDate + '\'' +
                ", rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", userConfirmState='" + userConfirmState + '\'' +
                ", userConfirmStateDesc='" + userConfirmStateDesc + '\'' +
                ", refundDate='" + refundDate + '\'' +
                '}';
    }
}
