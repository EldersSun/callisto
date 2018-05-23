package com.maizi.http.callback;


import android.text.TextUtils;

import com.maizi.cache.BaseCache;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by daixinglong on 2017/5/11.
 */

public abstract class StringCallback extends BaseCallBack<String> {

    @Override
    public String parseNetworkResponse(Response response, int id, BaseCache cache, String key) throws IOException {
        String result = response.body().string();
        if (!TextUtils.isEmpty(result)) {
            saveData(key, result, cache);
        }
        return result;
    }
}
