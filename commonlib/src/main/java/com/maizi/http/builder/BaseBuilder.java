package com.maizi.http.builder;

import android.util.ArrayMap;

import com.maizi.cache.BaseCache;
import com.maizi.http.RequestCall;

import java.util.Map;

/**
 * Created by daixinglong on 2017/5/11.
 * <p>
 * request builder的基础父类，主要用于向request传递基础参数
 * 如：url、id、params
 */

public abstract class BaseBuilder<T extends BaseBuilder> {

    protected int requestId;//请求id
    protected String url;//请求的url
    protected Object tag;//请求的tag
    protected boolean hasResponseCode = true;//返回是否包含返回码，默认情况下是有的
    protected Map<String, String> params;//请求的参数
    protected Map<String, String> headers;//添加的请求头
    protected int invokeType;//标记请求使用的缓存策略
    protected BaseCache cache;//缓存策略

    public T id(int requestId) {
        this.requestId = requestId;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T heanders(ArrayMap<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T invokeType(int invokeType) {
        this.invokeType = invokeType;
        return (T) this;
    }

    public T cache(BaseCache cache) {
        this.cache = cache;
        return (T) this;
    }

    public T getCode(boolean hasResponseCode) {
        this.hasResponseCode = hasResponseCode;
        return (T) this;
    }

    public abstract RequestCall build();
}
