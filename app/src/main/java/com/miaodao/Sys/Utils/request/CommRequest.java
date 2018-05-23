package com.miaodao.Sys.Utils.request;

import android.content.Context;
import android.content.Intent;

import com.miaodao.Activity.LoginActivity;
import com.miaodao.Sys.Config.AppConfig;

/**
 * Created by daixinglong on 2017/4/11.
 */

public class CommRequest {

    public static void loginAction(Context context) {
        AppConfig.getInstance().clearUserData();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
