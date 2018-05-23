package com.miaodao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.miaodao.Sys.Config.AppConfig;

/**
 * Created by daixinglong on 2017/3/28.
 */

public class WXRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AppConfig appConfig = AppConfig.getInstance();
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        msgApi.registerApp(appConfig.WX_APP_ID);

    }
}
