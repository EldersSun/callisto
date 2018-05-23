package com.maizi.cache;

import java.io.InputStream;

/**
 * Created by daixinglong on 2017/5/11.
 */

public abstract class BaseCache {

    public abstract void addOrUpdate(String key, String value);

    public abstract void addOrUpdate(String key, byte[] value);

    public abstract void addOrUpdate(String key, InputStream value);

    public abstract String getString(String key);

    public abstract byte[] getByteArray(String key);

    public abstract InputStream getStream(String key);

    public abstract void clear();

    public abstract void remove(String key);

    public abstract boolean containsKey(String key);

}
