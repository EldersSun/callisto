package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class CardAuthState {

//     "authState": "S",
//             "authStateName": "Submit",
//             "content": {
//        "branchId": null,
//                "cardIdxNo": "**** **** **** 8194",
//                "cardName": "戴兴龙",
//                "cardType": {},
//        "instId": "CCB",
//                "instName": "建设银行",
//                "mobile": "18202169937",
//                "signId": "20170420000000000031",
//                "userId": "00120170420000000214"
//    },
//            "errorCode": null,
//            "errorMsg": null,
//            "subAuthCode": "101000",
//            "subAuthName": "银行卡认证",
//            "userId": "00120170420000000214"


    private String authState;
    private String authStateName;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;
    private CardContent content;

    public CardAuthState() {
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

    public CardContent getContent() {
        return content == null ? new CardContent() : content;
    }

    public void setContent(CardContent content) {
        this.content = content;
    }

    public class CardContent {

//
//          "branchId": null,
//                  "cardIdxNo": "**** **** **** 8194",
//                  "cardName": "戴兴龙",
//                  "cardType": {},
//                "instId": "CCB",
//                "instName": "建设银行",
//                "mobile": "18202169937",
//                "signId": "20170420000000000031",
//                "userId": "00120170420000000214"

        private String branchId;
        private String cardIdxNo;
        private String cardName;
        private String cardType;
        private String instId;
        private String instName;
        private String mobile;
        private String signId;
        private String userId;

        public String getBranchId() {
            return branchId == null ? "" : branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getCardIdxNo() {
            return cardIdxNo == null ? "" : cardIdxNo;
        }

        public void setCardIdxNo(String cardIdxNo) {
            this.cardIdxNo = cardIdxNo;
        }

        public String getCardName() {
            return cardName == null ? "" : cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getCardType() {
            return cardType == null ? "" : cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getInstId() {
            return instId == null ? "" : instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }

        public String getInstName() {
            return instName == null ? "" : instName;
        }

        public void setInstName(String instName) {
            this.instName = instName;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSignId() {
            return signId == null ? "" : signId;
        }

        public void setSignId(String signId) {
            this.signId = signId;
        }

        public String getUserId() {
            return userId == null ? "" : userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


}
