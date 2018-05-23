package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/4/1.
 */

public class Repay implements Parcelable {

    private RepayOrderState extFields;// {"repayOrderState": "0"}
    private String amount;
    private String applyDate; //"20170320",
    private String applyTime; //"095048",
    private String bankName; //null,
    private String cardIdxNo;//"**** **** **** 6976",
    private String confirmTime; //"Tue Mar 21 17:07:04 CST 2017",
    private String cycle; //"7",
    private String integrateFee; //"50.00",
    private String instId; //null,
    private String onAcctAmount; //"950.00",
    private String overdueAmount; //"0.00",
    private int overdueDays; //0,
    private String payTime; //"Tue Mar 21 17:07:13 CST 2017",
    private String productId; //"100001",
    private String repayDate; //"Tue Mar 21 00:00:00 CST 2017",
    private String repayedAmount; //"0.00",
    private String repayedTime; //"Tue Mar 21 17:07:21 CST 2017",
    private String subTransCode; //"现金贷",
    private String subTransName; //"现金贷",
    private String transId; //"10120170324173922000000000042031",
    private String unRepayedAmount; //"0.00",
    private String userId; //"00120170301000000031"
    private String repayDays;//"7",


    public Repay() {
    }


    protected Repay(Parcel in) {
        extFields = in.readParcelable(RepayOrderState.class.getClassLoader());
        amount = in.readString();
        applyDate = in.readString();
        applyTime = in.readString();
        bankName = in.readString();
        cardIdxNo = in.readString();
        confirmTime = in.readString();
        cycle = in.readString();
        integrateFee = in.readString();
        instId = in.readString();
        onAcctAmount = in.readString();
        overdueAmount = in.readString();
        overdueDays = in.readInt();
        payTime = in.readString();
        productId = in.readString();
        repayDate = in.readString();
        repayedAmount = in.readString();
        repayedTime = in.readString();
        subTransCode = in.readString();
        subTransName = in.readString();
        transId = in.readString();
        unRepayedAmount = in.readString();
        userId = in.readString();
        repayDays = in.readString();
    }

    public static final Creator<Repay> CREATOR = new Creator<Repay>() {
        @Override
        public Repay createFromParcel(Parcel in) {
            return new Repay(in);
        }

        @Override
        public Repay[] newArray(int size) {
            return new Repay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(extFields, flags);
        dest.writeString(amount);
        dest.writeString(applyDate);
        dest.writeString(applyTime);
        dest.writeString(bankName);
        dest.writeString(cardIdxNo);
        dest.writeString(confirmTime);
        dest.writeString(cycle);
        dest.writeString(integrateFee);
        dest.writeString(instId);
        dest.writeString(onAcctAmount);
        dest.writeString(overdueAmount);
        dest.writeInt(overdueDays);
        dest.writeString(payTime);
        dest.writeString(productId);
        dest.writeString(repayDate);
        dest.writeString(repayedAmount);
        dest.writeString(repayedTime);
        dest.writeString(subTransCode);
        dest.writeString(subTransName);
        dest.writeString(transId);
        dest.writeString(unRepayedAmount);
        dest.writeString(userId);
        dest.writeString(repayDays);
    }


    public RepayOrderState getExtFields() {
        return extFields == null ? new RepayOrderState() : extFields;
    }

    public void setExtFields(RepayOrderState extFields) {
        this.extFields = extFields;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getIntegrateFee() {
        return integrateFee;
    }

    public void setIntegrateFee(String integrateFee) {
        this.integrateFee = integrateFee;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getOnAcctAmount() {
        return onAcctAmount;
    }

    public void setOnAcctAmount(String onAcctAmount) {
        this.onAcctAmount = onAcctAmount;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayedAmount() {
        return repayedAmount;
    }

    public void setRepayedAmount(String repayedAmount) {
        this.repayedAmount = repayedAmount;
    }

    public String getRepayedTime() {
        return repayedTime;
    }

    public void setRepayedTime(String repayedTime) {
        this.repayedTime = repayedTime;
    }

    public String getSubTransCode() {
        return subTransCode;
    }

    public void setSubTransCode(String subTransCode) {
        this.subTransCode = subTransCode;
    }

    public String getSubTransName() {
        return subTransName;
    }

    public void setSubTransName(String subTransName) {
        this.subTransName = subTransName;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getUnRepayedAmount() {
        return unRepayedAmount;
    }

    public void setUnRepayedAmount(String unRepayedAmount) {
        this.unRepayedAmount = unRepayedAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRepayDays() {
        return repayDays;
    }

    public void setRepayDays(String repayDays) {
        this.repayDays = repayDays;
    }
}
