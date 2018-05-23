package com.miaodao.Fragment.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.miaodao.Activity.LoginActivity;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.WebViewFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.DialogShowImgCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面
 * Created by Home_Pc on 2017/3/8.
 */

public class RegisterUserFragment extends ContentBaseFragment implements View.OnClickListener {

    private EditText reg_account, reg_pwd, reg_inputcode, invitedCode;
    private Button reg_getCode, reg_submit, reg_login, reg_RegistrationProtocol;
    private CheckBox reg_selectorShowPwd;

    private GetCodeTime getCodeTime = new GetCodeTime(AppConfig.getInstance().millisInFuture, AppConfig.getInstance().countDownInterval);
    private boolean isStartTimer = false;

    private DialogShowImgCode dialogShowImgCode;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_reguser_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {

        reg_account = (EditText) fgView.findViewById(R.id.reg_account);
        reg_pwd = (EditText) fgView.findViewById(R.id.reg_pwd);
        reg_inputcode = (EditText) fgView.findViewById(R.id.reg_inputcode);
        invitedCode = (EditText) fgView.findViewById(R.id.et_invited_code);

        reg_getCode = (Button) fgView.findViewById(R.id.reg_getCode);
        reg_submit = (Button) fgView.findViewById(R.id.reg_submit);
        reg_login = (Button) fgView.findViewById(R.id.reg_login);
        reg_RegistrationProtocol = (Button) fgView.findViewById(R.id.reg_RegistrationProtocol);
        reg_selectorShowPwd = (CheckBox) fgView.findViewById(R.id.reg_selectorShowPwd);

        reg_getCode.setOnClickListener(this);
        reg_submit.setOnClickListener(this);
        reg_login.setOnClickListener(this);
        reg_RegistrationProtocol.setOnClickListener(this);

        reg_selectorShowPwd.setOnClickListener(this);

    }

    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", reg_account.getText().toString());
        map.put("type", "1");
        requestForHttp(MESSAGE_TAG_1, AppConfig.getInstance().getImgCode, map);
    }


    @Override
    protected void initEvent() {

        dialogShowImgCode = new DialogShowImgCode(getActivity());
        dialogShowImgCode.setOnDialogOperationclick(new DialogShowImgCode.onDialogOperationclick() {
            @Override
            public void Confirm(String imgCode) {
                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("mobile", reg_account.getText().toString());
                submitMap.put("type", "1");
                submitMap.put("imgCode", imgCode);
                requestForHttp(MESSAGE_TAG_2, AppConfig.getInstance().postMessage, submitMap);
            }

            @Override
            public void cancel() {
                getCodeTime.cancel();
                changeSMSBtn();
            }

            @Override
            public void reGet() {
                startGetSMS();
            }
        });

    }

    private final int MESSAGE_TAG_1 = 0X1001;
    private final int MESSAGE_TAG_2 = 0X1002;
    private final int MESSAGE_TAG_3 = 0X1003;

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.reg_getCode:
                startGetSMS();
                break;

            case R.id.reg_submit:
                if (!StringUtils.isPhoneNumer(reg_account.getText().toString())) {
                    ToastUtils.shortShow(R.string.reg_inputAccountHint);
                    return;
                }

                if (!StringUtils.isPassword(reg_pwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.reg_inputPwdMessage);
                    return;
                }
                if (StringUtils.isBlank(reg_inputcode.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_code);
                    return;
                }

                String inviteCode = invitedCode.getText().toString();

                if (!TextUtils.isEmpty(inviteCode)) {
                    if (inviteCode.length() != 6) {
                        ToastUtils.shortShow(R.string.reg_inputInviteMessage);
                        return;
                    }
                }

                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("loginName", reg_account.getText().toString());
                submitMap.put("password", MD5Util.stringMD5(reg_pwd.getText().toString()));
                submitMap.put("dynamicCode", reg_inputcode.getText().toString());
                if (!TextUtils.isEmpty(inviteCode)) {
                    submitMap.put("invitedCode", invitedCode.getText().toString());
                }
                requestForHttp(MESSAGE_TAG_3, AppConfig.getInstance().UserReg, submitMap);
                break;

            case R.id.reg_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            case R.id.reg_RegistrationProtocol:
                bundle.putString("url", AppConfig.getInstance().REGIST_URL);
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), bundle);
                break;

            case R.id.reg_selectorShowPwd:
                if (reg_selectorShowPwd.isChecked()) {
                    reg_pwd.setTransformationMethod(null);
                } else {
                    reg_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                if (!StringUtils.isBlank(reg_pwd.getText().toString())) {
                    reg_pwd.setSelection(reg_pwd.getText().toString().length());
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void startGetSMS() {
        if (!StringUtils.isPhoneNumer(reg_account.getText().toString())) {
            ToastUtils.shortShow(R.string.reg_inputAccountHint);
            return;
        }

        if (!StringUtils.isPassword(reg_pwd.getText().toString())) {
            ToastUtils.shortShow(R.string.reg_inputPwdMessage);
            return;
        }

        getCodeTime.start();
        reg_getCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_selector_true));
        reg_getCode.setEnabled(false);
        getCode();
    }

    private class GetCodeTime extends CountDownTimer {
        public GetCodeTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            reg_getCode.setText(millisUntilFinished / 1000 + " " + getResources().getString(R.string.RestartGet));
        }

        @Override
        public void onFinish() {
            changeSMSBtn();
        }
    }

    /**
     * 改变button状态
     */
    private void changeSMSBtn() {
        reg_getCode.setEnabled(true);
        reg_getCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_backgroup_color));
        reg_getCode.setText(R.string.RestartGet_1);
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case MESSAGE_TAG_1:
                Map<String, Object> getCodeMap = (Map<String, Object>) result;
                if (getCodeMap != null && !StringUtils.isBlank(getCodeMap.get("code").toString())
                        && getCodeMap.get("code").toString().equals(AppConfig.getInstance().REQUEST_SUCC_CODE)) {
                    Map<String, String> imgMap = (Map<String, String>) getCodeMap.get("data");
                    String str = imgMap.get("imgCode");
                    if (!StringUtils.isBlank(str)) {
                        Bitmap bitmap = ImgUtils.base64ToBitmap(str);
                        if (bitmap != null) {
                            dialogShowImgCode.showImgDialog(bitmap);
                        }
                    }
                }
                break;
            case MESSAGE_TAG_2:
                Map<String, Object> regResultMap = (Map<String, Object>) result;
                if (regResultMap.containsKey("msg")) {
                    ToastUtils.shortShow(regResultMap.get("msg").toString());
                }
                break;
            case MESSAGE_TAG_3:
                Map<String, Object> regSuccMap = (Map<String, Object>) result;
                if (regSuccMap.containsKey("msg") && !StringUtils.isBlank(regSuccMap.get("msg").toString())) {
                    ToastUtils.shortShow(regSuccMap.get("msg").toString());
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
        switch (TAG) {
            case MESSAGE_TAG_2:
                getCode();
                break;
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (getCodeTime != null) {
            getCodeTime.cancel();
            getCodeTime = null;
        }
    }
}
