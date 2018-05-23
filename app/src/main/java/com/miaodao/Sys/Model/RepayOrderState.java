package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/5/16.
 */

public class RepayOrderState implements Parcelable{

//    repayOrderState	0	没有还款记录
//    repayOrderState	1	有正在处理中的还款订单
//    repayOrderState	2	有失败的还款订单

    private String repayOrderState;

    public RepayOrderState() {
    }

    protected RepayOrderState(Parcel in) {
        repayOrderState = in.readString();
    }

    public static final Creator<RepayOrderState> CREATOR = new Creator<RepayOrderState>() {
        @Override
        public RepayOrderState createFromParcel(Parcel in) {
            return new RepayOrderState(in);
        }

        @Override
        public RepayOrderState[] newArray(int size) {
            return new RepayOrderState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(repayOrderState);
    }


    public String getRepayOrderState() {
        return repayOrderState;
    }

    public void setRepayOrderState(String repayOrderState) {
        this.repayOrderState = repayOrderState;
    }
}
