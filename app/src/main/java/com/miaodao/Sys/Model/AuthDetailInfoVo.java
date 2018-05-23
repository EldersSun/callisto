package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/16.
 */
public class AuthDetailInfoVo  implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户认证项
     */
    private String subAuthCode;

    /**
     * 认证项名称
     */
    private String subAuthName;

    /**
     * 用户认证状态
     */
    private String authState;

    /**
     * 用户认证状态说明
     */
    private String authStateName;

    /**
     * 错误编号
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 认证项详细内容
     */
    private Object content;

    public AuthDetailInfoVo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubAuthCode() {
        return subAuthCode;
    }

    public void setSubAuthCode(String subAuthCode) {
        this.subAuthCode = subAuthCode;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getAuthStateName() {
        return authStateName;
    }

    public void setAuthStateName(String authStateName) {
        this.authStateName = authStateName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getSubAuthName() {
        return subAuthName;
    }

    public void setSubAuthName(String subAuthName) {
        this.subAuthName = subAuthName;
    }
}
