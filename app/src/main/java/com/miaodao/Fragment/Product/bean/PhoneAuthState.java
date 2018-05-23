package com.miaodao.Fragment.Product.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class PhoneAuthState {

//
//   "authState": "S",
//           "authStateName": "Submit",
//           "content": [
//    {
//        "mobile": "17025446465",
//            "name": "丁丁猫",
//            "type": "01"
//    },
//    {
//        "mobile": "18436555255",
//            "name": "默契",
//            "type": "02"
//    },
//    {
//        "mobile": "18284545843",
//            "name": "张盼",
//            "type": "03"
//    }
//                ],
//                        "errorCode": null,
//                        "errorMsg": null,
//                        "subAuthCode": "103000",
//                        "subAuthName": "联系人认证",
//                        "userId": "00120170420000000214"


    private String authState;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;
    private String authStateName;
    private List<PhoneContent> content = new ArrayList<>();

    public PhoneAuthState() {
    }

    public String getAuthState() {
        return authState == null ? "" : authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
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

    public String getAuthStateName() {
        return authStateName == null ? "" : authStateName;
    }

    public void setAuthStateName(String authStateName) {
        this.authStateName = authStateName;
    }

    public List<PhoneContent> getContent() {
        return content == null ? new ArrayList<PhoneContent>() : content;
    }

    public void setContent(List<PhoneContent> content) {
        this.content = content;
    }

    public class PhoneContent {

//          "mobile": "18436555255",
//                  "name": "默契",
//                  "type": "02"


        private String mobile;
        private String name;
        private String type;

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type == null ? "" : type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
