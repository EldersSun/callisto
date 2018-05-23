package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/14.
 */
public class Contact implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 居住城市
     */
    private String liveCity;
    /**
     * 工作城市
     */
    private String workCity;
    /**
     * 工作电话
     */
    private String workPhone;
    /**
     * 住址
     */
    private String liveAddress;
    /**
     * 工作地址
     */
    private String workAddress;
    /**
     * 居住城市编号
     */
    private String liveCityCode;
    /**
     * 居住地址-区
     */
    private String liveDistrict;
    /**
     * 居住地址-省
     */
    private String liveProvince;
    /**
     * 工作城市编号
     */
    private String workCityCode;
    /**
     * 工作地址-区
     */
    private String workDistrict;
    /**
     * 工作地址-省
     */
    private String workProvince;
    /**
     * 居住地址-区编号
     */
    private String liveDistrictCode;
    /**
     * 居住地址-省编号
     */
    private String liveProvinceCode;
    /**
     * 工作地址-区编号
     */
    private String workDistrictCode;
    /**
     * 工作地址-省编号
     */
    private String workProvinceCode;
    /**
     * 公司名称
     */
    private String companyName;


    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLiveCity() {
        return liveCity == null ? "" : liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public String getWorkCity() {
        return workCity == null ? "" : workCity;
    }

    public void setWorkCity(String workCity) {
        this.workCity = workCity;
    }

    public String getWorkPhone() {
        return workPhone == null ? "" : workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getLiveAddress() {
        return liveAddress == null ? "" : liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public String getWorkAddress() {
        return workAddress == null ? "" : workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
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

    public String getLiveProvince() {
        return liveProvince == null ? "" : liveProvince;
    }

    public void setLiveProvince(String liveProvince) {
        this.liveProvince = liveProvince;
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

    public String getWorkProvince() {
        return workProvince == null ? "" : workProvince;
    }

    public void setWorkProvince(String workProvince) {
        this.workProvince = workProvince;
    }

    public String getLiveDistrictCode() {
        return liveDistrictCode == null ? "" : liveDistrictCode;
    }

    public void setLiveDistrictCode(String liveDistrictCode) {
        this.liveDistrictCode = liveDistrictCode;
    }

    public String getLiveProvinceCode() {
        return liveProvinceCode == null ? "" : liveProvinceCode;
    }

    public void setLiveProvinceCode(String liveProvinceCode) {
        this.liveProvinceCode = liveProvinceCode;
    }

    public String getWorkDistrictCode() {
        return workDistrictCode == null ? "" : workDistrictCode;
    }

    public void setWorkDistrictCode(String workDistrictCode) {
        this.workDistrictCode = workDistrictCode;
    }

    public String getWorkProvinceCode() {
        return workProvinceCode == null ? "" : workProvinceCode;
    }

    public void setWorkProvinceCode(String workProvinceCode) {
        this.workProvinceCode = workProvinceCode;
    }

    public String getCompanyName() {
        return companyName == null ? "" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "userId='" + userId + '\'' +
                ", liveCity='" + liveCity + '\'' +
                ", workCity='" + workCity + '\'' +
                ", workPhone='" + workPhone + '\'' +
                ", liveAddress='" + liveAddress + '\'' +
                ", workAddress='" + workAddress + '\'' +
                ", liveCityCode='" + liveCityCode + '\'' +
                ", liveDistrict='" + liveDistrict + '\'' +
                ", liveProvince='" + liveProvince + '\'' +
                ", workCityCode='" + workCityCode + '\'' +
                ", workDistrict='" + workDistrict + '\'' +
                ", workProvince='" + workProvince + '\'' +
                ", liveDistrictCode='" + liveDistrictCode + '\'' +
                ", liveProvinceCode='" + liveProvinceCode + '\'' +
                ", workDistrictCode='" + workDistrictCode + '\'' +
                ", workProvinceCode='" + workProvinceCode + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
