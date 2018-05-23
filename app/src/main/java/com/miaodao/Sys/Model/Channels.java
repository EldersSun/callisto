package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/4/5.
 */

public class Channels implements Parcelable{

    private String instId;
    private String instName;

    public Channels() {
    }

    public Channels(String instId, String instName) {
        this.instId = instId;
        this.instName = instName;
    }

    protected Channels(Parcel in) {
        instId = in.readString();
        instName = in.readString();
    }

    public static final Creator<Channels> CREATOR = new Creator<Channels>() {
        @Override
        public Channels createFromParcel(Parcel in) {
            return new Channels(in);
        }

        @Override
        public Channels[] newArray(int size) {
            return new Channels[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(instId);
        dest.writeString(instName);
    }
}
