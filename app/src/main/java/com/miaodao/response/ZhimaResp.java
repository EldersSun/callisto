package com.miaodao.response;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/5/19 11:40
 */

public class ZhimaResp {

    private String code;
    private ZhimaUrl data;
    private String msg;

    public ZhimaResp() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZhimaUrl getData() {
        return data;
    }

    public void setData(ZhimaUrl data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public class ZhimaUrl{
       private String antifraudUrl;

        public ZhimaUrl() {
        }


        public String getAntifraudUrl() {
            return antifraudUrl;
        }

        public void setAntifraudUrl(String antifraudUrl) {
            this.antifraudUrl = antifraudUrl;
        }
    }



}
