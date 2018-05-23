package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class IdAuthState {

//     "authState":"S",
//             "authStateName":"Submit",
//             "content":
//
//    {
//        "backPath":"/opt/files/user/card/20170420122124-00120170420000000214-2.jpg",
//            "certNo":"320********410",
//            "certType":{
//    },
//        "frontPath":"/opt/files/user/card/20170420122124-00120170420000000214-1.jpg",
//            "id":"20170420000000000122",
//            "legality":"1.0",
//            "name":"戴**"
//    },
//            "errorCode":null,
//            "errorMsg":null,
//            "subAuthCode":"100000",
//            "subAuthName":"身份证认证",
//            "userId":"00120170420000000214"


    private String authState;
    private String authStateName;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;
    private IdContent content;

    public IdAuthState() {
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

    public IdContent getContent() {
        return content == null ? new IdContent() : content;
    }

    public void setContent(IdContent content) {
        this.content = content;
    }

    public class IdContent {

//                "backPath": "/opt/files/user/card/20170420122124-00120170420000000214-2.jpg",
//                 "certNo": "320********410",
//                 "certType": {},
//                "frontPath": "/opt/files/user/card/20170420122124-00120170420000000214-1.jpg",
//                "id": "20170420000000000122",
//                "legality": "1.0",
//                "name": "戴**"


        private String backPath;
        private String certNo;
        private String frontPath;
        private String id;
        private String legality;
        private String name;
        private Object certType;

        public String getBackPath() {
            return backPath == null ? "" : backPath;
        }

        public void setBackPath(String backPath) {
            this.backPath = backPath;
        }

        public String getCertNo() {
            return certNo == null ? "" : certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public String getFrontPath() {
            return frontPath == null ? "" : frontPath;
        }

        public void setFrontPath(String frontPath) {
            this.frontPath = frontPath;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLegality() {
            return legality == null ? "" : legality;
        }

        public void setLegality(String legality) {
            this.legality = legality;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getCertType() {
            return certType == null ? "" : certType;
        }

        public void setCertType(Object certType) {
            this.certType = certType;
        }
    }

}
