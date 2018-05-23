package com.maizi.cache;

import org.litepal.crud.DataSupport;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class CacheModel extends DataSupport{

    private int id;
    private String cacheKey;
    private String cacheValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }
}
