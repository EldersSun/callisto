package com.miaodao.Fragment.Loan.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/5/15.
 */

public class WaitPay implements Parcelable{

//    "amount": "2010.00",
//            "cardIdxNo": "8198",
//            "confirmAmount": null,
//            "extFields": {},
//            "fee": null,
//            "integrateFee": "12.00",
//            "overdueDays": 0,
//            "payTime": "2017-05-06",
//            "productId": "100001",
//            "refTransId": "10120170508114855000000000191201",
//            "repayDate": "2017-05-15",
//            "transId": "10220170508160425000000000193201",
//            "transState": "初始化",
//            "unRepayedAmount": "0.00",
//            "userId": null


    private String amount;
    private String cardIdxNo;
    private String confirmAmount;
    private Object extFields;
    private String fee;
    private String integrateFee;
    private int overdueDays;
    private String payTime;
    private String productId;
    private String refTransId;
    private String repayDate;
    private String transId;
    private String transState;
    private String unRepayedAmount;
    private String userId;

    public WaitPay() {
    }

    protected WaitPay(Parcel in) {
        amount = in.readString();
        cardIdxNo = in.readString();
        confirmAmount = in.readString();
        fee = in.readString();
        integrateFee = in.readString();
        overdueDays = in.readInt();
        payTime = in.readString();
        productId = in.readString();
        refTransId = in.readString();
        repayDate = in.readString();
        transId = in.readString();
        transState = in.readString();
        unRepayedAmount = in.readString();
        userId = in.readString();
    }

    public static final Creator<WaitPay> CREATOR = new Creator<WaitPay>() {
        @Override
        public WaitPay createFromParcel(Parcel in) {
            return new WaitPay(in);
        }

        @Override
        public WaitPay[] newArray(int size) {
            return new WaitPay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(cardIdxNo);
        dest.writeString(confirmAmount);
        dest.writeString(fee);
        dest.writeString(integrateFee);
        dest.writeInt(overdueDays);
        dest.writeString(payTime);
        dest.writeString(productId);
        dest.writeString(refTransId);
        dest.writeString(repayDate);
        dest.writeString(transId);
        dest.writeString(transState);
        dest.writeString(unRepayedAmount);
        dest.writeString(userId);
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(String confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public Object getExtFields() {
        return extFields;
    }

    public void setExtFields(Object extFields) {
        this.extFields = extFields;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getIntegrateFee() {
        return integrateFee;
    }

    public void setIntegrateFee(String integrateFee) {
        this.integrateFee = integrateFee;
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

    public String getRefTransId() {
        return refTransId;
    }

    public void setRefTransId(String refTransId) {
        this.refTransId = refTransId;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
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
}
