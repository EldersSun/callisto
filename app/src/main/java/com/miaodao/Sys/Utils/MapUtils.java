package com.miaodao.Sys.Utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created by Home_Pc on 2017/3/29.
 */

public class MapUtils {

    /**
     * map转换object
     *
     * @param map
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> obj) throws Exception {
        if (map == null) {
            return null;
        }
        Set<Map.Entry<String, Object>> sets = map.entrySet();
        T t = obj.newInstance();
        Method[] methods = obj.getDeclaredMethods();
        for (Map.Entry<String, Object> entry : sets) {
            String str = entry.getKey();
            String setMethod = "set" + str.substring(0, 1).toUpperCase() + str.substring(1);
            for (Method method : methods) {
                if (method.getName().equals(setMethod)) {
                    if (entry.getValue().toString().equals("null")) {
                        method.invoke(t, "");
                    } else {
                        method.invoke(t, entry.getValue());
                    }

                }
            }
        }
        return t;
    }



    /**
     * map转换object
     *
     * @param map
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> obj) throws Exception {
        if (map == null) {
            return null;
        }
        Set<Map.Entry<String, Object>> sets = map.entrySet();
        T t = obj.newInstance();
        Method[] methods = obj.getDeclaredMethods();
        for (Map.Entry<String, Object> entry : sets) {
            String str = entry.getKey();
            String setMethod = "set" + str.substring(0, 1).toUpperCase() + str.substring(1);
            for (Method method : methods) {
                if (method.getName().equals(setMethod)) {
                    if (entry.getValue().toString().equals("null")) {
                        method.invoke(t, "");
                    } else {
                        method.invoke(t, entry.getValue() + "");
                    }

                }
            }
        }
        return t;
    }










}
