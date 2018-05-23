package com.maizi.http.builder;


import com.maizi.http.RequestCall;
import com.maizi.http.request.PostStringRequest;

import okhttp3.MediaType;

/**
 * Created by daixinglong on 2017/5/12.
 */

public class PostStringBuilder extends BaseBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }


    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, requestId, invokeType, cache, hasResponseCode, content, mediaType).build();
    }
}
