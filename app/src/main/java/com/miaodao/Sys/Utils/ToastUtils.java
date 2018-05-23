package com.miaodao.Sys.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.miaodao.Application.WheatFinanceApplication;


/**
 * 简单提示Toast工具类
 */
public class ToastUtils {

    private static Toast mToast;


    public static void shortShow(Context context, String msg) {
        if (null != msg && msg.length() > 0 && null != context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }

    public static void shortShow(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    str,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void shortShow(int source) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    source,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(source);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


    public static void longShow(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    str,
                    Toast.LENGTH_LONG);
        } else {
            mToast.setText(str);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void longShow(int source) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    source,
                    Toast.LENGTH_LONG);
        } else {
            mToast.setText(source);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


    public static void showWithTime(int source, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    source,
                    time);
        } else {
            mToast.setText(source);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


    public static void showWithTime(String str, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(WheatFinanceApplication.getInstance(),
                    str,
                    time);
        } else {
            mToast.setText(str);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
