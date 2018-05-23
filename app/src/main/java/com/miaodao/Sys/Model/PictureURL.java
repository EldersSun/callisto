package com.miaodao.Sys.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daixinglong on 2017/4/5.
 */

public class PictureURL implements Parcelable{

// "bannerUrl": "http://120.27.49.79/images/banner/banner_invite.png",
//         "action": true,
//         "code": 1,
//         "title":"閭€璇峰ソ鍙嬮€佸厤鎭埜",
//         "actionUrl": "/appStatic/view/activity.html"


    private String bannerUrl;
    private boolean action;
    private String code;
    private String actionUrl;
    private String title;

    public PictureURL() {
    }


    protected PictureURL(Parcel in) {
        bannerUrl = in.readString();
        action = in.readByte() != 0;
        code = in.readString();
        actionUrl = in.readString();
        title = in.readString();
    }

    public static final Creator<PictureURL> CREATOR = new Creator<PictureURL>() {
        @Override
        public PictureURL createFromParcel(Parcel in) {
            return new PictureURL(in);
        }

        @Override
        public PictureURL[] newArray(int size) {
            return new PictureURL[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bannerUrl);
        dest.writeByte((byte) (action ? 1 : 0));
        dest.writeString(code);
        dest.writeString(actionUrl);
        dest.writeString(title);
    }

    public PictureURL(String bannerUrl, boolean action, String code, String actionUrl, String title) {
        this.bannerUrl = bannerUrl;
        this.action = action;
        this.code = code;
        this.actionUrl = actionUrl;
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
