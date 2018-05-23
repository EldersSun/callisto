package com.maizi.cache;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.List;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class DataBaseCache extends BaseCache {

    private static DataBaseCache instance;

    private DataBaseCache() {
    }

    public static DataBaseCache getInstance() {

        if (instance == null) {
            synchronized (DataBaseCache.class) {
                if (instance == null) {
                    instance = new DataBaseCache();
                }
            }
        }

        return instance;
    }


    @Override
    public void addOrUpdate(String key, String value) {
        List<CacheModel> cacheModels = DataSupport.where("cachekey = ?", key).find(CacheModel.class);
        CacheModel cacheModel;
        if (cacheModels.isEmpty()) {
            cacheModel = new CacheModel();
            cacheModel.setCacheKey(key);
            cacheModel.setCacheValue(value);
            cacheModel.save();
        } else {
            cacheModel = cacheModels.get(0);
            cacheModel.setCacheValue(value);
            cacheModel.update(cacheModel.getId());
        }
    }

    @Override
    public void addOrUpdate(String key, byte[] value) {

    }

    @Override
    public void addOrUpdate(String key, InputStream value) {

    }

    @Override
    public String getString(String key) {
        List<CacheModel> cacheModels = DataSupport.where("cachekey = ?", key).find(CacheModel.class);
        return cacheModels.isEmpty() ? "" : cacheModels.get(0).getCacheValue();
    }

    @Override
    public byte[] getByteArray(String key) {
        return new byte[0];
    }

    @Override
    public InputStream getStream(String key) {
        return null;
    }

    @Override
    public void clear() {
        DataSupport.deleteAll(CacheModel.class);
    }

    @Override
    public void remove(String key) {
        DataSupport.deleteAll(CacheModel.class, "cachekey = ?", key);
    }

    @Override
    public boolean containsKey(String key) {
        List<CacheModel> cacheModels = DataSupport.where("cachekey = ?", key).find(CacheModel.class);
        return cacheModels.isEmpty() ? true : false;
    }
}
