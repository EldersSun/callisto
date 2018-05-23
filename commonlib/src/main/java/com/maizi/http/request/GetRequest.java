package com.maizi.http.request;


import com.maizi.cache.BaseCache;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class GetRequest extends BaseRequest {


    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int requestId, int invokeType, BaseCache cache, boolean hasResponseCode, String content) {
        super(url, tag, params, headers, requestId, invokeType, cache, hasResponseCode, content);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }

}
