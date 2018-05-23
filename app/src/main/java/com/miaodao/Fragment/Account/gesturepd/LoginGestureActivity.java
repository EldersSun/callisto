package com.miaodao.Fragment.Account.gesturepd;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miaodao.Activity.LoginActivity;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Base.BaseActivity;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.LoginDialog;
import com.miaodao.Sys.Widgets.getsture.GestureContentView;
import com.miaodao.Sys.Widgets.getsture.GestureDrawline;

import java.util.HashMap;
import java.util.Map;

import static com.miaodao.Fragment.Account.gesturepd.GestureMainFragment.GESTURE_WORK;

/**
 * Created by daixinglong on 2017/3/29.
 */

public class LoginGestureActivity extends BaseActivity implements View.OnClickListener {

    private final int LOGIN_MESSGAE_1 = 0X1001;
    private TextView tvTip;//手势密码提示文字
    private FrameLayout loginGestureContainer;//显示手势密码的容器
    private TextView showLoginDialog;//点击出现密码登录对话框
    private int verifyWrongTime = 5;//允许手势错误次数
    private GestureContentView gestureContentView;//手势密码view
    private String gesturePd = "";
    private LoginDialog loginDialog;

    private Intent intent;
    private String getstureWork;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_gestuer_verify, null);
    }

    @Override
    protected void initWidgets() {

        intent = getIntent();
        if (intent == null) return;
        getstureWork = intent.getStringExtra(GESTURE_WORK);

        title_layout.setVisibility(View.GONE);
        gesturePd = SharedPreferencesUtil.getString(this, AppConfig.getInstance().GESTURE_PD, "");
        tvTip = (TextView) findViewById(R.id.tv_tip);
        loginGestureContainer = (FrameLayout) findViewById(R.id.login_gesture_container);
        showLoginDialog = (TextView) findViewById(R.id.show_login_dialog);
        gestureContentView = new GestureContentView(this, true, gesturePd, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }

            @Override
            public void checkedSuccess() {
                if ("shutGesture".equals(getstureWork)) {
                    SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().GESTURE_PD);
                }
                LoginGestureActivity.this.finish();
            }

            @Override
            public void checkedFail() {
                verifyWrongTime--;
                if (verifyWrongTime == 0) {
                    // TODO: 2017/3/29 跳转到登录页面
                    Intent intent = new Intent(LoginGestureActivity.this, LoginActivity.class);
                    intent.putExtra(AppConfig.getInstance().GESTURE_LOGIN_FINISH, "gestureFinish");
                    startActivity(intent);
                    finish();
                    return;
                }
                gestureContentView.clearDrawlineState(1300L);
                tvTip.setText(Html.fromHtml("<font color='#c70c1e'>手势密码错误,还可以输入" + verifyWrongTime + "次</font>"));
                // 左右移动动画
                Animation shakeAnimation = AnimationUtils.loadAnimation(LoginGestureActivity.this, R.anim.shake);
                tvTip.startAnimation(shakeAnimation);
            }
        });

        gestureContentView.setParentView(loginGestureContainer);
    }

    @Override
    protected void initEvent() {
        showLoginDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.show_login_dialog:
                showLoginDialog();
                break;

            default:
                break;

        }

    }

    /**
     * 弹出登录框
     */
    private void showLoginDialog() {

        loginDialog = new LoginDialog(this, new LoginDialog.OnClickListener() {
            @Override
            public void onCancel() {
                hideSoftInput();
//                finish();
            }

            @Override
            public void onSure(String pwd) {
                hideSoftInput();
                login(pwd);
            }
        });

        loginDialog.setTvPhoneNum(SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_LOGINNAME, ""));
        loginDialog.show();
    }


    /**
     * 登录验证
     *
     * @param pwd
     */
    private void login(String pwd) {
        if (StringUtils.isBlank(pwd)) {
            ToastUtils.shortShow(R.string.input_pwd);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", SharedPreferencesUtil.getString(this, AppConfig.getInstance().RESULT_LOGINNAME, ""));
        map.put("password", MD5Util.stringMD5(pwd));
        requestForHttp(LOGIN_MESSGAE_1, AppConfig.getInstance().Userlogin, map);
    }


    /**
     * 强制隐藏键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromWindow(loginDialog.getPDView().getWindowToken(), 0); //强制隐藏键盘
            }
        }
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {


        Map<String, Object> loginiSucc = (Map<String, Object>) result;
        if (loginiSucc.containsKey(AppConfig.getInstance().RESULT_DATA)) {
            Map<String, Object> map = (Map<String, Object>) loginiSucc.get(AppConfig.getInstance().RESULT_DATA);
            if (map.containsKey(AppConfig.getInstance().RESULT_TOKRNID_TAG) && map.containsKey(AppConfig.getInstance().RESULT_USERID)) {
                String tokenId = (String) map.get(AppConfig.getInstance().RESULT_TOKRNID_TAG);
                String userId = (String) map.get(AppConfig.getInstance().RESULT_USERID);
                String invitedCode = (String) map.get(AppConfig.getInstance().RESULT_INVITEDCODE);
                String phoneNum = (String) map.get("userName");
                SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_USERID, userId);
                SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_LOGINNAME, phoneNum);
                SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_TOKRNID_TAG, tokenId);
                SharedPreferencesUtil.putString(this, AppConfig.getInstance().RESULT_INVITEDCODE, invitedCode);

                SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().GESTURE_PD);
            }

            if (map.containsKey(AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG)) {
                boolean userIsPayPwdStatus = (boolean) map.get(AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG);
                SharedPreferencesUtil.putBoolean(this, AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG, userIsPayPwdStatus);
            }
        }


        finish();
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
