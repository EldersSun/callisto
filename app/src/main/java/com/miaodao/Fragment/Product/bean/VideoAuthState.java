package com.miaodao.Fragment.Product.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class VideoAuthState {

//  "authState": "S",
//          "authStateName": "Submit",
//          "content": [
//    {
//        "authState": "S",
//            "photoPath": "/opt/files/user/photo/pic/20170427092829-00120170420000000214-001.jpg",
//            "photoType": "001",
//            "rejectReason": null
//    },
//    {
//        "authState": "S",
//            "photoPath": "/opt/files/user/photo/video/20170427092829-00120170420000000214-002.mp4",
//            "photoType": "002",
//            "rejectReason": null
//    }
//                ],
//                        "errorCode": null,
//                        "errorMsg": null,
//                        "subAuthCode": "104000",
//                        "subAuthName": "图片对比",
//                        "userId": "00120170420000000214"


    private String authState;
    private String authStateName;
    private String errorCode;
    private String errorMsg;
    private String subAuthCode;
    private String subAuthName;
    private String userId;
    private List<VideoContent> content;

    public VideoAuthState() {
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

    public List<VideoContent> getContent() {
        return content == null ? new ArrayList<VideoContent>() : content;
    }

    public void setContent(List<VideoContent> content) {
        this.content = content;
    }

    public class VideoContent {

//         "authState": "S",
//                 "photoPath": "/opt/files/user/photo/video/20170427092829-00120170420000000214-002.mp4",
//                 "photoType": "002",
//                 "rejectReason": null


        private String authState;
        private String photoPath;
        private String photoType;
        private String rejectReason;

        public String getAuthState() {
            return authState == null ? "" : authState;
        }

        public void setAuthState(String authState) {
            this.authState = authState;
        }

        public String getPhotoPath() {
            return photoPath == null ? "" : photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public String getPhotoType() {
            return photoType == null ? "" : photoType;
        }

        public void setPhotoType(String photoType) {
            this.photoType = photoType;
        }

        public String getRejectReason() {
            return rejectReason == null ? "" : rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }
    }


}
