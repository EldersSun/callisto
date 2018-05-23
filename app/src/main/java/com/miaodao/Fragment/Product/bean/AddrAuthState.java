package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class AddrAuthState {

//      "authState": "S",
//              "authStateName": "Submit",
//              "content": {
//        "companyName": "bffnd",
//                "liveAddress": "bdnxnxbxxn401",
//                "liveCity": "上海市",
//                "liveCityCode": "310100",
//                "liveDistrict": "浦东新区",
//                "liveDistrictCode": "310115",
//                "liveProvince": "上海市",
//                "liveProvinceCode": "310000",
//                "userId": "00120170420000000214",
//                "workAddress": "上海市上海市浦东新区",
//                "workCity": "上海市",
//                "workCityCode": "310100",
//                "workDistrict": "浦东新区",
//                "workDistrictCode": "310115",
//                "workPhone": "021534554554",
//                "workProvince": "上海市",
//                "workProvinceCode": "310000"
//    },
//            "errorCode": null,
//            "errorMsg": null,
//            "subAuthCode": "102000",
//            "subAuthName": "联系地址认证",
//            "userId": "00120170420000000214"


    private String authState;
    private String authStateName;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;
    private AddrContent content;

    public AddrAuthState() {
    }

    public String getAuthState() {
        return authState == null ? "" : authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getAuthStateName() {
        return authStateName == null ? "" : authStateName;
    }

    public void setAuthStateName(String authStateName) {
        this.authStateName = authStateName;
    }

    public String getErrorCode() {
        return errorCode == null ? "" : errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSubAuthCode() {
        return subAuthCode == null ? "" : subAuthCode;
    }

    public void setSubAuthCode(String subAuthCode) {
        this.subAuthCode = subAuthCode;
    }

    public String getSubAuthName() {
        return subAuthName == null ? "" : subAuthName;
    }

    public void setSubAuthName(String subAuthName) {
        this.subAuthName = subAuthName;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AddrContent getContent() {
        return content == null ? new AddrContent() : content;
    }

    public void setContent(AddrContent content) {
        this.content = content;
    }

    public class AddrContent {

//          "companyName": "bffnd",
//                  "liveAddress": "bdnxnxbxxn401",
//                  "liveCity": "上海市",
//                  "liveCityCode": "310100",
//                  "liveDistrict": "浦东新区",
//                  "liveDistrictCode": "310115",
//                  "liveProvince": "上海市",
//                  "liveProvinceCode": "310000",
//                  "userId": "00120170420000000214",
//                  "workAddress": "上海市上海市浦东新区",
//                  "workCity": "上海市",
//                  "workCityCode": "310100",
//                  "workDistrict": "浦东新区",
//                  "workDistrictCode": "310115",
//                  "workPhone": "021534554554",
//                  "workProvince": "上海市",
//                  "workProvinceCode": "310000"


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
    }

}
