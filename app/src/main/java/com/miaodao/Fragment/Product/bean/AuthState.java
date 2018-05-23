package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class AuthState {
//
//     "authInfo": {
//        "authState": "P",
//                "authStateName": "Pass",
//                "userId": "00120170420000000214"
//    }


    private String authState;
    private String authStateName;
    private String userId;

    public AuthState() {
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

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
