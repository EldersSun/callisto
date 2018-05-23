package com.maizi.http.request;


import com.maizi.cache.BaseCache;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by daixinglong on 2017/5/12.
 */

public class PostStringRequest extends BaseRequest {

    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");

    private String content;
    private MediaType mediaType;

    public PostStringRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int requestId, int invokeType, BaseCache cache, boolean hasResponseCode, String content, MediaType mediaType) {
        super(url, tag, params, headers, requestId, invokeType, cache, hasResponseCode, content);

        this.content = content;
        this.mediaType = mediaType;

        if (this.mediaType == null)
            this.mediaType = MEDIA_TYPE_PLAIN;
    }


    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
