package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class VideoCompare {

//     "authState": "S",
//             "authStateName": "Submit",
//             "content": null,
//             "errorCode": null,
//             "errorMsg": null,
//             "subAuthCode": "104001",
//             "subAuthName": "视频对比",
//             "userId": "00120170420000000214"


    private String authState;
    private String authStateName;
    private String content;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;

    public VideoCompare() {
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

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
