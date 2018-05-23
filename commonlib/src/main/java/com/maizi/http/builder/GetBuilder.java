package com.maizi.http.builder;

import android.net.Uri;

import com.maizi.http.RequestCall;
import com.maizi.http.request.GetRequest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class GetBuilder extends BaseBuilder<GetBuilder> implements AddParams {


    @Override
    public RequestCall build() {

        if (url != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, tag, params, headers, requestId, invokeType, cache, hasResponseCode, null).build();
    }


    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }


    /**
     * get请求的时候将params拼接到url后面
     *
     * @param url
     * @param params
     * @return
     */
    private String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) return url;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
