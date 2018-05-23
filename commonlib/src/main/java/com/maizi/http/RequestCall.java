package com.maizi.http;


import com.maizi.cache.BaseCache;
import com.maizi.http.callback.BaseCallBack;
import com.maizi.http.request.BaseRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 * 对OkHttpRequest的封装，对外提供更多的接口：cancel(),readTimeOut()...
 */
public class RequestCall {
    private BaseRequest okHttpRequest;
    private Request request;
    private Call call;
    private BaseCache cache;
    private int invokeType;
    private int requestId;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient clone;

    public RequestCall(BaseRequest request) {
        this.okHttpRequest = request;
        this.cache = request.getCache();
        this.invokeType = request.getInvokeType();
        this.requestId = request.getRequestId();
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public void execute(final BaseCallBack callback) {
        buildCall(callback);

        //使用了缓存
        if (invokeType == HttpType.CACHE_AND_SERVER) {
            final String response = cache.getString(okHttpRequest.getCacheKey());
            OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onResponse(response, requestId);
                }
            });
        }
        OkHttpUtils.getInstance().execute(this, callback);

//        15921140236
    }


    /**
     * @param callback
     * @return
     */
    public Call buildCall(BaseCallBack callback) {
        request = generateRequest(callback);
        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(BaseCallBack callback) {
        return okHttpRequest.generateRequest(callback);
    }


    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public BaseRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() throws IOException {
        buildCall(null);
        return call.execute();
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


}
