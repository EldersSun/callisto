package com.maizi.cache;


import com.maizi.log.L;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class MemoryCache extends BaseCache {

    private Map<String, byte[]> cacheMap = Collections.synchronizedMap(new LinkedHashMap<String, byte[]>(10, 1.5f, true));

    // current allocated size
    private long size = 0;

    // default max memory in bytes
    private long totalLimit = 1000000;

    private long oneValueMaxBytes;

    private static MemoryCache instance;

    private MemoryCache(long oneValueMaxBytes) {
        this.oneValueMaxBytes = oneValueMaxBytes;
        // use 25% of available heap size
        setTotalLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public static MemoryCache getInstance(long oneValueMaxBytes) {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache(oneValueMaxBytes);
                }
            }
        }
        return instance;
    }

    public void setTotalLimit(long totalLimit) {
        this.totalLimit = totalLimit;
    }

    @Override
    public void addOrUpdate(String key, String value) {
        throw new RuntimeException("Not support string type value.");
    }

    @Override
    public void addOrUpdate(String key, byte[] value) {
        if (value.length > oneValueMaxBytes) {
            return;
        }
        try {
            if (cacheMap.containsKey(key)) {
                size -= cacheMap.get(key).length;
            }
            cacheMap.put(key, value);
            size += value.length;
            checkSize();
        } catch (Exception e) {
            L.e("MemoryCache.addOrUpdate", "Failed to add or update");
        }

    }

    @Override
    public void addOrUpdate(String key, InputStream value) {
        throw new RuntimeException("Not support InputStream type value.");
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public byte[] getByteArray(String key) {
        try {
            if (!cacheMap.containsKey(key)) {
                return null;
            }
            // NullPointerException sometimes happen here
            // http://code.google.com/p/osmdroid/issues/detail?id=78
            return cacheMap.get(key);
        } catch (NullPointerException e) {
            L.e("MemoryCache", "Failed to get cache", e);
        }
        return null;
    }

    @Override
    public InputStream getStream(String key) {
        return null;
    }

    @Override
    public void clear() {
        try {
            // NullPointerException sometimes happen here
            // http://code.google.com/p/osmdroid/issues/detail?id=78
            cacheMap.clear();
            size = 0;
        } catch (NullPointerException e) {
            L.e("MemoryCache", "Failed to clear cache", e);
        }
    }

    @Override
    public void remove(String key) {
        size -= cacheMap.get(key).length;
        cacheMap.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        return cacheMap.containsKey(key);
    }

    private void checkSize() {
        if (size > totalLimit) {
            // least recently accessed item will be the first one iterated
            Iterator<Map.Entry<String, byte[]>> iter = cacheMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, byte[]> entry = iter.next();
                size -= entry.getValue().length;
                iter.remove();
                if (size <= totalLimit) {
                    break;
                }
            }
        }
    }
}
