package com.maizi.http.callback;


import com.maizi.cache.BaseCache;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by daixinglong on 2017/5/11.
 */

public abstract class BaseCallBack<T> {

    public void inProgress(float progress, long total, int id) {
    }

    public boolean validateReponse(Response response, int id) {
        return response.isSuccessful();
    }

    public void saveData(String key, String response, BaseCache cache){
        cache.addOrUpdate(key, response);
    }

    public abstract T parseNetworkResponse(Response response, int id, BaseCache cache, String key) throws Exception;

    public abstract void onError(String msg, Call call, Exception e, int id);

    public abstract void onResponse(T response, int id);


    public static BaseCallBack DEFAULT_CALLBACK = new BaseCallBack() {
        @Override
        public Object parseNetworkResponse(Response response, int id, BaseCache cache, String key) throws Exception {
            return null;
        }

        @Override
        public void onError(String msg, Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };
}
