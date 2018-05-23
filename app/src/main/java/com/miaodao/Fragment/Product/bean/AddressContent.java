package com.miaodao.Fragment.Product.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/6/8 13:51
 */

public class AddressContent extends DataSupport implements Parcelable {

    private String companyName;
    private String liveAddress;
    private String liveCity;
    private String liveCityCode;
    private String liveDistrict;
    private String liveDistrictCode;
    private String liveProvince;
    private String liveProvinceCode;
    private String userId;
    private String workAddress;
    private String workCity;
    private String workCityCode;
    private String workDistrict;
    private String workDistrictCode;
    private String workPhone;
    private String workProvince;
    private String workProvinceCode;

    private String familyName;
    private String familyPhone;
    private String friendName;
    private String friendPhone;
    private String colleagueName;
    private String colleaguePhone;


    public AddressContent() {
    }

    protected AddressContent(Parcel in) {
        companyName = in.readString();
        liveAddress = in.readString();
        liveCity = in.readString();
        liveCityCode = in.readString();
        liveDistrict = in.readString();
        liveDistrictCode = in.readString();
        liveProvince = in.readString();
        liveProvinceCode = in.readString();
        userId = in.readString();
        workAddress = in.readString();
        workCity = in.readString();
        workCityCode = in.readString();
        workDistrict = in.readString();
        workDistrictCode = in.readString();
        workPhone = in.readString();
        workProvince = in.readString();
        workProvinceCode = in.readString();
        familyName = in.readString();
        familyPhone = in.readString();
        friendName = in.readString();
        friendPhone = in.readString();
        colleagueName = in.readString();
        colleaguePhone = in.readString();
    }

    public static final Creator<AddressContent> CREATOR = new Creator<AddressContent>() {
        @Override
        public AddressContent createFromParcel(Parcel in) {
            return new AddressContent(in);
        }

        @Override
        public AddressContent[] newArray(int size) {
            return new AddressContent[size];
        }
    };

    public String getCompanyName() {
        return companyName == null ? "" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLiveAddress() {
        return liveAddress == null ? "" : liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public String getLiveCity() {
        return liveCity == null ? "" : liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public String getLiveCityCode() {
        return liveCityCode == null ? "" : liveCityCode;
    }

    public void setLiveCityCode(String liveCityCode) {
        this.liveCityCode = liveCityCode;
    }

    public String getLiveDistrict() {
        return liveDistrict == null ? "" : liveDistrict;
    }

    public void setLiveDistrict(String liveDistrict) {
        this.liveDistrict = liveDistrict;
    }

    public String getLiveDistrictCode() {
        return liveDistrictCode == null ? "" : liveDistrictCode;
    }

    public void setLiveDistrictCode(String liveDistrictCode) {
        this.liveDistrictCode = liveDistrictCode;
    }

    public String getLiveProvince() {
        return liveProvince == null ? "" : liveProvince;
    }

    public void setLiveProvince(String liveProvince) {
        this.liveProvince = liveProvince;
    }

    public String getLiveProvinceCode() {
        return liveProvinceCode == null ? "" : liveProvinceCode;
    }

    public void setLiveProvinceCode(String liveProvinceCode) {
        this.liveProvinceCode = liveProvinceCode;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkAddress() {
        return workAddress == null ? "" : workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getWorkCity() {
        return workCity == null ? "" : workCity;
    }

    public void setWorkCity(String workCity) {
        this.workCity = workCity;
    }

    public String getWorkCityCode() {
        return workCityCode == null ? "" : workCityCode;
    }

    public void setWorkCityCode(String workCityCode) {
        this.workCityCode = workCityCode;
    }

    public String getWorkDistrict() {
        return workDistrict == null ? "" : workDistrict;
    }

    public void setWorkDistrict(String workDistrict) {
        this.workDistrict = workDistrict;
    }

    public String getWorkDistrictCode() {
        return workDistrictCode == null ? "" : workDistrictCode;
    }

    public void setWorkDistrictCode(String workDistrictCode) {
        this.workDistrictCode = workDistrictCode;
    }

    public String getWorkPhone() {
        return workPhone == null ? "" : workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getWorkProvince() {
        return workProvince == null ? "" : workProvince;
    }

    public void setWorkProvince(String workProvince) {
        this.workProvince = workProvince;
    }

    public String getWorkProvinceCode() {
        return workProvinceCode == null ? "" : workProvinceCode;
    }

    public void setWorkProvinceCode(String workProvinceCode) {
        this.workProvinceCode = workProvinceCode;
    }

    public String getFamilyName() {
        return familyName == null ? "" : familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyPhone() {
        return familyPhone == null ? "" : familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getFriendName() {
        return friendName == null ? "" : friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendPhone() {
        return friendPhone == null ? "" : friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getColleagueName() {
        return colleagueName == null ? "" : colleagueName;
    }

    public void setColleagueName(String colleagueName) {
        this.colleagueName = colleagueName;
    }

    public String getColleaguePhone() {
        return colleaguePhone == null ? "" : colleaguePhone;
    }

    public void setColleaguePhone(String colleaguePhone) {
        this.colleaguePhone = colleaguePhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyName);
        dest.writeString(liveAddress);
        dest.writeString(liveCity);
        dest.writeString(liveCityCode);
        dest.writeString(liveDistrict);
        dest.writeString(liveDistrictCode);
        dest.writeString(liveProvince);
        dest.writeString(liveProvinceCode);
        dest.writeString(userId);
        dest.writeString(workAddress);
        dest.writeString(workCity);
        dest.writeString(workCityCode);
        dest.writeString(workDistrict);
        dest.writeString(workDistrictCode);
        dest.writeString(workPhone);
        dest.writeString(workProvince);
        dest.writeString(workProvinceCode);
        dest.writeString(familyName);
        dest.writeString(familyPhone);
        dest.writeString(friendName);
        dest.writeString(friendPhone);
        dest.writeString(colleagueName);
        dest.writeString(colleaguePhone);
    }
}
