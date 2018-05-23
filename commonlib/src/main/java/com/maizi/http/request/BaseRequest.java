package com.maizi.http.request;

import com.alibaba.fastjson.JSON;
import com.maizi.MD5;
import com.maizi.cache.BaseCache;
import com.maizi.cache.DataBaseCache;
import com.maizi.http.RequestCall;
import com.maizi.http.callback.BaseCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by daixinglong on 2017/5/11.
 */

public abstract class BaseRequest {

    protected String url;
    protected Object tag;
    protected boolean hasResponseCode = true;//返回是否包含返回码，默认情况下是有的
    protected Map<String, String> params;
    protected Map<String, String> headers;
    private String content;
    protected int requestId;
    protected int invokeType;//标记请求使用的缓存策略
    protected BaseCache cache;//缓存策略
    protected String cacheMD5Key;
    protected Request.Builder builder = new Request.Builder();


    public BaseRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int requestId, int invokeType, BaseCache cache, boolean hasResponseCode, String content) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.requestId = requestId;
        this.invokeType = invokeType;
        this.cache = cache;
        this.hasResponseCode = hasResponseCode;
        this.content = content;
        this.cacheMD5Key = calculateCacheMD5Key();
        initBuilder();
    }

    /**
     * 真正的request builder初始化参数
     */
    private void initBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }


    /**
     * 添加请求头信息
     */
    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * 添加请求体 request body
     *
     * @param requestBody
     * @param callback
     * @return
     */
    protected RequestBody wrapRequestBody(RequestBody requestBody, final BaseCallBack callback) {
        return requestBody;
    }

    /**
     * 通过request 返回执行请求的 call
     *
     * @return
     */
    public RequestCall build() {
        return new RequestCall(this);
    }

    /**
     * 根据requestBody生成request
     *
     * @param callback
     * @return
     */
    public Request generateRequest(BaseCallBack callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, callback);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    public int getInvokeType() {
        return invokeType;
    }

    public BaseCache getCache() {
        //默认使用数据库缓存
        if (cache == null) {
            cache = DataBaseCache.getInstance();
        }
        return cache;
    }

    public int getRequestId() {
        return requestId;
    }

    /**
     * 缓存的key
     *
     * @return
     */
    public String getCacheKey() {
        return this.cacheMD5Key;
    }

    /**
     * 缓存的key
     *
     * @return
     */
    private String calculateCacheMD5Key() {
        if (content == null && params == null) {
            return MD5.calculateMD5(url);
        } else {
            if (content == null && params != null) {
                params.remove("uuid");
                params.remove("tokenId");
                params.remove("geogX");
                params.remove("geogY");
                return MD5.calculateMD5(url + "" + JSON.toJSONString(params));
            } else if (content != null && params == null) {
                try {
                    JSONObject object = new JSONObject(content);
                    String uuidStr = object.optString("uuid");
                    String tokenIdStr = object.optString("tokenId");
                    String geogYStr = object.optString("geogX");
                    String geogXStr = object.optString("geogY");
                    content = content.replaceAll(uuidStr, "");
                    content = content.replaceAll(tokenIdStr, "");
                    content = content.replaceAll(geogYStr, "");
                    content = content.replaceAll(geogXStr, "");
                    return MD5.calculateMD5(url + "" + content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public boolean isHasResponseCode() {
        return hasResponseCode;
    }

    //提供的抽象方法
    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);


}
