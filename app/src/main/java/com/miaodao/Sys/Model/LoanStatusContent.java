package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/3/30.
 */

public class LoanStatusContent implements Parcelable{

    private String userId;
    private String transId;
    private String productId;
    private String productName;
    private String certName;
    private String cardIdxNo;
    private String instId;
    private String bankName;
    private String applyDate;
    private String applyTime;
    private String amount;
    private String fee;
    private String onAcctAmount;
    private String loanState;
    private String loanStateDesc;
    private String confirmDate;
    private String payDate;
    private String payTime;
    private String rejectCode;
    private String rejectReason;
    private String userConfirmState;
    private String userConfirmStateDesc;
    private String refundDate;

    public LoanStatusContent() {
    }

    protected LoanStatusContent(Parcel in) {
        userId = in.readString();
        transId = in.readString();
        productId = in.readString();
        productName = in.readString();
        certName = in.readString();
        cardIdxNo = in.readString();
        instId = in.readString();
        bankName = in.readString();
        applyDate = in.readString();
        applyTime = in.readString();
        amount = in.readString();
        fee = in.readString();
        onAcctAmount = in.readString();
        loanState = in.readString();
        loanStateDesc = in.readString();
        confirmDate = in.readString();
        payDate = in.readString();
        payTime = in.readString();
        rejectCode = in.readString();
        rejectReason = in.readString();
        userConfirmState = in.readString();
        userConfirmStateDesc = in.readString();
        refundDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(transId);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(certName);
        dest.writeString(cardIdxNo);
        dest.writeString(instId);
        dest.writeString(bankName);
        dest.writeString(applyDate);
        dest.writeString(applyTime);
        dest.writeString(amount);
        dest.writeString(fee);
        dest.writeString(onAcctAmount);
        dest.writeString(loanState);
        dest.writeString(loanStateDesc);
        dest.writeString(confirmDate);
        dest.writeString(payDate);
        dest.writeString(payTime);
        dest.writeString(rejectCode);
        dest.writeString(rejectReason);
        dest.writeString(userConfirmState);
        dest.writeString(userConfirmStateDesc);
        dest.writeString(refundDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoanStatusContent> CREATOR = new Creator<LoanStatusContent>() {
        @Override
        public LoanStatusContent createFromParcel(Parcel in) {
            return new LoanStatusContent(in);
        }

        @Override
        public LoanStatusContent[] newArray(int size) {
            return new LoanStatusContent[size];
        }
    };

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
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
}
