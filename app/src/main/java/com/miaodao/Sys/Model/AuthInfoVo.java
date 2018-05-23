package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by zhangshihang on 2017/3/16.
 */
public class AuthInfoVo implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 认证状态
     */
    private String authState;

    /**
     * 认证状态说明
     */
    private String authStateName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
