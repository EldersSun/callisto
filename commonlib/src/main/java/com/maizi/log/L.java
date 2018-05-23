package com.maizi.log;

import android.util.Log;

import static com.maizi.Constant.ISDEBUG;

/**
 * Created by daixinglong on 2017/5/11.
 */

public class L {

    public static void e(String name, String msg) {
        if (ISDEBUG)
            Log.e(name, msg);
    }


    public static void d(String name, String msg) {
        if (ISDEBUG)
            Log.d(name, msg);
    }


    public static void i(String name, String msg) {
        if (ISDEBUG)
            Log.i(name, msg);
    }


    public static void e(String name, String msg, Exception e) {
        if (ISDEBUG)
            Log.e(name, msg, e);
    }


    public static void d(String name, String msg, Exception e) {
        if (ISDEBUG)
            Log.d(name, msg, e);
    }


    public static void i(String name, String msg, Exception e) {
        if (ISDEBUG)
            Log.i(name, msg, e);
    }
}
