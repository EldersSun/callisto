package com.maizi.http.builder;

import java.util.Map;

/**
 * Created by daixinglong on 2017/5/11.
 */

public interface AddParams {

    BaseBuilder params(Map<String, String> params);

    BaseBuilder addParams(String key, String val);
}
