package com.miaodao.Activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miaodao.Base.BaseActivity;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Account.BackUserPwdFragment;
import com.miaodao.Fragment.Account.RegisterUserFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.loadingView.LoadingView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆
 * Created by Home_Pc on 2017/3/8.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private EditText login_account;
    private EditText login_pwd;
    private Button login_submit;//提交
    private Button login_reg;//注册
    private Button login_backPwd;//找回

    private final int LOGIN_MESSGAE_1 = 0X1001;
    private final int LOGIN_MESSGAE_2 = 0X1002;

    private boolean jumpFlag = false;
    private String isFinish = "";//手势密码来的时候不需要跳转到main

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.ac_login_layout, null);
    }

    @Override
    protected void initWidgets() {
        title_tvShow.setText(R.string.userLogin);
        login_account = (EditText) findViewById(R.id.login_account);
        login_pwd = (EditText) findViewById(R.id.login_pwd);

        login_submit = (Button) findViewById(R.id.login_submit);
        login_reg = (Button) findViewById(R.id.login_reg);
        login_backPwd = (Button) findViewById(R.id.login_backPwd);

        login_submit.setOnClickListener(this);
        login_reg.setOnClickListener(this);
        login_backPwd.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

//        if (!StringUtils.isBlank(SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_TOKRNID, ""))
//                && !StringUtils.isBlank(SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_USERID, ""))) {
//            String tokenId = SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_TOKRNID, "");
//            String userId = SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_USERID, "");
//
//            Map<String, String> map = new HashMap<>();
//            map.put("tokenId", tokenId);
//            map.put("userId", userId);
//            requestForHttp(LOGIN_MESSGAE_2, AppConfig.getInstance().UserTokenlogin, map);
//        }
        login_account.setText(SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_LOGINNAME, ""));
//        login_pwd.setText("123456");

        Intent intent = getIntent();
        jumpFlag = intent.getBooleanExtra("jumpFlag", false);
        isFinish = intent.getStringExtra(AppConfig.getInstance().GESTURE_LOGIN_FINISH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit:
                if (!StringUtils.isPhoneNumer(login_account.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_account);
                    return;
                }
                if (StringUtils.isBlank(login_pwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_pwd);
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("loginName", login_account.getText().toString());
                map.put("password", MD5Util.stringMD5(login_pwd.getText().toString()));
                LoadingView.show(this, this);
                requestForHttp(LOGIN_MESSGAE_1, AppConfig.getInstance().Userlogin, map);
//                startActivity(new Intent(this, MainActivity.class));
//                this.finish();
                break;

            case R.id.login_reg:
                ServiceBaseActivity.startActivity(this, RegisterUserFragment.class.getName());
                break;

            case R.id.login_backPwd:
                ServiceBaseActivity.startActivity(this, BackUserPwdFragment.class.getName());
                break;
        }
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        LoadingView.dismiss();
        //手势密码登录成功的时候不跳转到mian
        if ("gestureFinish".equalsIgnoreCase(isFinish)) {
            SharedPreferencesUtil.removeKey(LoginActivity.this, AppConfig.getInstance().GESTURE_PD);
            finish();
            return;
        }

        switch (TAG) {
            case LOGIN_MESSGAE_1:
                Map<String, Object> loginiSucc = (Map<String, Object>) result;
                if (loginiSucc.containsKey(AppConfig.getInstance().RESULT_DATA)) {
                    Map<String, Object> map = (Map<String, Object>) loginiSucc.get(AppConfig.getInstance().RESULT_DATA);
                    if (map.containsKey(AppConfig.getInstance().RESULT_TOKRNID_TAG) && map.containsKey(AppConfig.getInstance().RESULT_USERID)) {
                        String tokenId = (String) map.get(AppConfig.getInstance().RESULT_TOKRNID_TAG);
                        String userId = (String) map.get(AppConfig.getInstance().RESULT_USERID);
                        String invitedCode = (String) map.get(AppConfig.getInstance().RESULT_INVITEDCODE);
                        SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_USERID, userId);
                        SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_LOGINNAME, login_account.getText().toString());
                        SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_TOKRNID_TAG, tokenId);
                        SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_INVITEDCODE, invitedCode);
                    }

                    if (map.containsKey(AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG)) {
                        boolean userIsPayPwdStatus = (boolean) map.get(AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG);
                        SharedPreferencesUtil.putBoolean(this, AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG, userIsPayPwdStatus);
                    }
                }
                if (!jumpFlag) {
                    startActivity(new Intent(this, MainAct.class));
                }
                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                this.finish();
                break;

            case LOGIN_MESSGAE_2:
                if (!jumpFlag) {
                    startActivity(new Intent(this, MainAct.class));
                }
                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                this.finish();
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        LoadingView.dismiss();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        LoadingView.dismiss();
    }

}
