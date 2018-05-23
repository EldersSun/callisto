package com.miaodao.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/5/17 11:34
 */

public abstract class MainBaseAct extends FragmentActivity{

    protected Unbinder unbinder;
    protected String userId = "";//登录后userId
    protected String tokenId = "";//登录后tokenId

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);

        initParams();
        initActions();
    }


    /**
     * 初始化参数
     */
    protected void initParams() {
        userId = SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_USERID, "");
        tokenId = SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
    }


    protected abstract void initActions();


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
