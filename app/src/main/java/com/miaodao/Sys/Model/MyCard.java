package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/3/28.
 */

public class MyCard implements Parcelable{

    private String cardIdxNo;
    private String cardType;
    private String mobile;
    private String cardName;
    private String instId;
    private String instName;

    protected MyCard(Parcel in) {
        cardIdxNo = in.readString();
        cardType = in.readString();
        mobile = in.readString();
        cardName = in.readString();
        instId = in.readString();
        instName = in.readString();
    }

    public static final Creator<MyCard> CREATOR = new Creator<MyCard>() {
        @Override
        public MyCard createFromParcel(Parcel in) {
            return new MyCard(in);
        }

        @Override
        public MyCard[] newArray(int size) {
            return new MyCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardIdxNo);
        dest.writeString(cardType);
        dest.writeString(mobile);
        dest.writeString(cardName);
        dest.writeString(instId);
        dest.writeString(instName);
    }
}
