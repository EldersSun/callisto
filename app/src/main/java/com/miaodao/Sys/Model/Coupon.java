package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/5/6.
 */

public class Coupon implements Parcelable{

//     "couponCode": "100002",
//             "couponName": "免息券",
//             "couponType": "002",
//             "expiredDate": "2017年07月01日",
//             "extFields": {},
//            "id": "e891d9002fe811e78498342387005fa8",
//            "state": "0",
//            "value": "7"

    private String couponCode;
    private String couponName;
    private String couponType;
    private String expiredDate;
//    private String extFields;
    private String id;
    private String state;
    private String value;
    private boolean available;

    public Coupon(){}

    protected Coupon(Parcel in) {
        couponCode = in.readString();
        couponName = in.readString();
        couponType = in.readString();
        expiredDate = in.readString();
        id = in.readString();
        state = in.readString();
        value = in.readString();
        available = in.readByte() != 0;
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(couponCode);
        dest.writeString(couponName);
        dest.writeString(couponType);
        dest.writeString(expiredDate);
        dest.writeString(id);
        dest.writeString(state);
        dest.writeString(value);
        dest.writeByte((byte) (available ? 1 : 0));
    }


    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
