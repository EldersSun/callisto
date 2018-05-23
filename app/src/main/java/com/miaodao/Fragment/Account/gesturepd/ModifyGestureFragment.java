package com.miaodao.Fragment.Account.gesturepd;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
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

/**
 * Created by daixinglong on 2017/3/29.
 * 修改分为三步：
 * 1、确认原手势密码:如果输入验证手势正确，但是返回，原手势密码还是存在;5次机会输错 退出登录 清空手势密码
 * 2、设置新手势密码
 * 3、确认新手势密码
 */

public class ModifyGestureFragment extends ContentBaseFragment implements View.OnClickListener {

    private final int LOGIN_MESSGAE_1 = 0X1001;

    private TextView tvTip;//提示文字
    private FrameLayout loginContainer, modifyContainer, checkContainer;
    private TextView showLoginDialog;//点击显示密码登录对话框
    private GestureContentView loginGesture, modifyGesture, checkGesture;
    private int verifyWrongTime = 5;
    private String gesturePD = "";
    private LoginDialog loginDialog;


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_gesture_modify, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.security_message_8);
        tvTip = (TextView) fgView.findViewById(R.id.tv_tip);
        loginContainer = (FrameLayout) fgView.findViewById(R.id.login_gesture_container);
        modifyContainer = (FrameLayout) fgView.findViewById(R.id.modify_gesture_container);
        checkContainer = (FrameLayout) fgView.findViewById(R.id.modify_check_gesture_container);
        showLoginDialog = (TextView) fgView.findViewById(R.id.show_login_dialog);

        gesturePD = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().GESTURE_PD, "");
        showLoginGesture();
    }


    /**
     * 显示登录手势密码
     */
    private void showLoginGesture() {

        loginContainer.setVisibility(View.VISIBLE);
        modifyContainer.setVisibility(View.GONE);
        checkContainer.setVisibility(View.GONE);
        showLoginDialog.setVisibility(View.VISIBLE);
        loginGesture = new GestureContentView(getActivity(), true, gesturePD, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }

            @Override
            public void checkedSuccess() {
                tvTip.setText(Html.fromHtml("<font color='#666666'>请绘制新手势密码</font>"));
                showModifyGesture();
            }

            @Override
            public void checkedFail() {
                verifyWrongTime--;
                if (verifyWrongTime == 0) {
                    AppConfig.getInstance().clearUserData();
                    SharedPreferencesUtil.removeKey(getActivity(), AppConfig.getInstance().GESTURE_PD);
                    getActivity().finish();
                    return;
                }
                loginGesture.clearDrawlineState(1300L);
                tvTip.setText(Html.fromHtml("<font color='#c70c1e'>手势密码错误,还可以输入" + verifyWrongTime + "次</font>"));
                // 左右移动动画
                Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                tvTip.startAnimation(shakeAnimation);
            }
        });
        loginGesture.setParentView(loginContainer);
    }


    /**
     * 显示修改手势
     */
    private void showModifyGesture() {
        loginContainer.setVisibility(View.GONE);
        modifyContainer.setVisibility(View.VISIBLE);
        checkContainer.setVisibility(View.GONE);
        showLoginDialog.setVisibility(View.GONE);

        modifyGesture = new GestureContentView(getActivity(), false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    tvTip.setText(Html.fromHtml("<font color='#c70c1e'>最少连接6个点, 请重新绘制</font>"));
                    modifyGesture.clearDrawlineState(0L);
                    // 左右移动动画
                    Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    tvTip.startAnimation(shakeAnimation);
                    return;
                }
                tvTip.setText(Html.fromHtml("<font color='#666666'>再次绘制解锁图案</font>"));
                modifyGesture.clearDrawlineState(0L);
                gesturePD = inputCode;
                showCheckGesture(inputCode);
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });

        modifyGesture.setParentView(modifyContainer);
    }


    /**
     * 显示验证手势
     *
     * @param inputCode
     */
    private void showCheckGesture(final String inputCode) {
        loginContainer.setVisibility(View.GONE);
        modifyContainer.setVisibility(View.GONE);
        checkContainer.setVisibility(View.VISIBLE);
        showLoginDialog.setVisibility(View.GONE);

        checkGesture = new GestureContentView(getActivity(), true, inputCode, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }

            @Override
            public void checkedSuccess() {
                checkGesture.clearDrawlineState(0L);
                SharedPreferencesUtil.putString(getActivity(), AppConfig.getInstance().GESTURE_PD, inputCode);
                getActivity().finish();
            }

            @Override
            public void checkedFail() {
                checkGesture.clearDrawlineState(1300L);
                tvTip.setText(Html.fromHtml("<font color='#c70c1e'>图案绘制错误,请重新绘制</font>"));
                // 左右移动动画
                Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                tvTip.startAnimation(shakeAnimation);
            }
        });

        checkGesture.setParentView(checkContainer);

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

        loginDialog = new LoginDialog(getActivity(), new LoginDialog.OnClickListener() {
            @Override
            public void onCancel() {
                hideSoftInput();
                getActivity().finish();
            }

            @Override
            public void onSure(String pwd) {
                hideSoftInput();
                login(pwd);
            }
        });

        loginDialog.setTvPhoneNum(SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""));
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
        map.put("loginName", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""));
        map.put("password", MD5Util.stringMD5(pwd));
        requestForHttp(LOGIN_MESSGAE_1, AppConfig.getInstance().Userlogin, map);
    }


    /**
     * 强制隐藏键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromWindow(loginDialog.getPDView().getWindowToken(), 0); //强制隐藏键盘
            }
        }
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        showModifyGesture();
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }


    /**
     * 判断用输入的验证码是否有效
     *
     * @param inputPassword
     * @return
     */
    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 6) {
            return false;
        }
        return true;
    }

}
