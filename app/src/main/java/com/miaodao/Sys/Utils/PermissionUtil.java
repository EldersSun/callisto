package com.miaodao.Sys.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by daixinglong on 2017/4/20.
 */

public class PermissionUtil {

    /**
     * 判断是否有权限
     */
    public static boolean hasPermission(Context context, String... permissions) {

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != (ContextCompat.checkSelfPermission(context, permission))) {
                return false;
            }
        }
        return true;
    }


    public static void requestPermission(Activity activity, int code, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, code);
    }


}
