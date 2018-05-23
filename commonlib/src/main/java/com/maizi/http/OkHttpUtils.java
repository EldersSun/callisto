package com.maizi.http;

import android.text.TextUtils;

import com.maizi.cache.BaseCache;
import com.maizi.http.builder.GetBuilder;
import com.maizi.http.builder.PostFormBuilder;
import com.maizi.http.builder.PostStringBuilder;
import com.maizi.http.callback.BaseCallBack;
import com.maizi.http.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class OkHttpUtils {

    private final String SUCC_CODE = "000000";
    public final String REQUEST_SYS_MAINTAIN_CODE = "900001";//系统升级码
    public final String REQUEST_LOGIN_CODE = "010399";//跳转登录页

    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private PostDataHandler mHandler;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mHandler = PostDataHandler.getInstance();
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Executor getDelivery() {
        return mHandler.defaultCallbackExecutor();
    }


    /**
     * get请求
     *
     * @return
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * post请求
     *
     * @return
     */
    public static PostStringBuilder post() {
        return new PostStringBuilder();
    }


    /**
     * 含有文件的post提交
     *
     * @return
     */
    public static PostFormBuilder postForm() {
        return new PostFormBuilder();
    }


    /**
     * 真正的执行网络请求并返回信息的地方
     *
     * @param requestCall
     * @param callBack
     */
    public void execute(final RequestCall requestCall, BaseCallBack callBack) {
        if (callBack == null)
            callBack = BaseCallBack.DEFAULT_CALLBACK;
        final BaseCallBack finalCallback = callBack;
        final BaseRequest request = requestCall.getOkHttpRequest();
        final int id = request.getRequestId();
        final BaseCache cache = request.getCache();

        requestCall.getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback("网络连接超时!", call, e, finalCallback, id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback("", call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback("", call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }
                    Object o = finalCallback.parseNetworkResponse(response, id, cache, request.getCacheKey());
                    parseResult(call, o, finalCallback, id, request.isHasResponseCode());
                } catch (Exception e) {
                    sendFailResultCallback("", call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });

    }


    /**
     * 解析返回成功后的数据
     *
     * @param o
     */
    private void parseResult(final Call call, Object o, final BaseCallBack finalCallback, final int id, boolean isHasResponseCode) {

        String result = (String) o;
        //返回的信息是空
        if (TextUtils.isEmpty(result)) {
            sendFailResultCallback("", call, new Exception("empty"), finalCallback, id);
            return;
        }

        //有的请求是没有返回码的，直接返回一段信息，这个时候就直接返回信息
        if (!isHasResponseCode) {
            sendSuccessResultCallback(o, finalCallback, id);
            return;
        }


        //开始解释返回信息code
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = (String) jsonObject.get("code");
            String msg = (String) jsonObject.get("msg");

            //"000000"表示成功
            if (SUCC_CODE.equals(code)) {
                sendSuccessResultCallback(o, finalCallback, id);

                //系统升级提示
            } else if (REQUEST_SYS_MAINTAIN_CODE.equals(code)) {
                sendFailResultCallback(REQUEST_SYS_MAINTAIN_CODE, call, new Exception(), finalCallback, id);

                //需要跳转到登录页面
            } else if (REQUEST_LOGIN_CODE.equals(code)) {
                sendFailResultCallback(REQUEST_LOGIN_CODE, call, new Exception(), finalCallback, id);

                //失败的时候将失败信息返回到业务层显示
            } else {
                sendFailResultCallback(msg, call, new Exception(), finalCallback, id);
            }
        } catch (JSONException e) {
            finalCallback.onError("", call, e, id);
            e.printStackTrace();
        }
    }


    /**
     * 返回成功
     *
     * @param o
     * @param finalCallback
     * @param id
     */
    private void sendSuccessResultCallback(final Object o, final BaseCallBack finalCallback, final int id) {
        if (finalCallback == null) return;
        mHandler.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onResponse(o, id);
            }
        });
    }


    /**
     * 返回失败
     *
     * @param call
     * @param e
     * @param finalCallback
     * @param id
     */
    private void sendFailResultCallback(final String msg, final Call call, final Exception e, final BaseCallBack finalCallback, final int id) {
        if (finalCallback == null) return;
        mHandler.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onError(msg, call, e, id);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }



}
