package com.miaodao.Fragment.Account;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.DialogShowImgCode;

import java.util.HashMap;
import java.util.Map;


/**
 * 找回密码
 * Created by Home_Pc on 2017/3/9.
 */

public class BackUserPwdFragment extends ContentBaseFragment implements View.OnClickListener {

    private EditText backUserPwd_Tel, backUserPwd_inputCode, backUserPwd_IdCode, BackserPwd_NewPwd;
    private Button backUserPwd_getCode, backUserPwd_submit;
    private LinearLayout backUserPwd_IdCarLayout;

    private GetCodeTime getCodeTime = new GetCodeTime(AppConfig.getInstance().millisInFuture, AppConfig.getInstance().countDownInterval);
    private boolean isStartTimer = false;

    private final int MESSAGE_TAG_1 = 0X1021;
    private final int MESSAGE_TAG_2 = 0X1022;
    private final int MESSAGE_TAG_3 = 0X1023;

    private boolean isShowIdCard = false;

    private DialogShowImgCode dialogShowImgCode;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_backuserpwd_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.setloginPwd);
        backUserPwd_Tel = (EditText) fgView.findViewById(R.id.backUserPwd_Tel);
        backUserPwd_inputCode = (EditText) fgView.findViewById(R.id.backUserPwd_inputcode);
        backUserPwd_IdCode = (EditText) fgView.findViewById(R.id.backUserPwd_IdCode);
        BackserPwd_NewPwd = (EditText) fgView.findViewById(R.id.BackserPwd_NewPwd);
        backUserPwd_getCode = (Button) fgView.findViewById(R.id.backUserPwd_getCode);
        backUserPwd_submit = (Button) fgView.findViewById(R.id.backUserPwd_submit);
        backUserPwd_IdCarLayout = (LinearLayout) fgView.findViewById(R.id.backUserPwd_IdCarLayout);

        backUserPwd_Tel.setText(SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""));
//        backUserPwd_Tel.setEnabled(false);

        backUserPwd_getCode.setOnClickListener(this);
        backUserPwd_submit.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        dialogShowImgCode = new DialogShowImgCode(getActivity());
        dialogShowImgCode.setOnDialogOperationclick(new DialogShowImgCode.onDialogOperationclick() {
            @Override
            public void Confirm(String imgCode) {
                if (StringUtils.isBlank(imgCode)) {
                    ToastUtils.shortShow(R.string.reg_inputcodeHint);
                    getCodeTime.cancel();
                    changeSMSBtn();
                    return;
                }

                if (StringUtils.isBlank(backUserPwd_Tel.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_account);
                    getCodeTime.cancel();
                    changeSMSBtn();
                    return;
                }
//                if (StringUtils.isBlank(BackserPwd_NewPwd.getText().toString())) {
//                    ToastUtils.shortShow(R.string.input_pwd);
//                    return;
//                }

//                if (BackserPwd_NewPwd.getText().toString().length() < 6 || BackserPwd_NewPwd.getText().toString().length() > 20) {
//                    ToastUtils.shortShow(R.string.reg_inputPwdMessage);
//                    return;
//                }

                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("mobile", backUserPwd_Tel.getText().toString());
                submitMap.put("type", "2");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backUserPwd_getCode:
                startGetSMS();
                break;
            case R.id.backUserPwd_submit:
                if (StringUtils.isBlank(backUserPwd_Tel.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_account);
                    return;
                }
                if (!StringUtils.isPassword(BackserPwd_NewPwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.reg_inputPwdMessage);
                    return;
                }
                if (StringUtils.isBlank(backUserPwd_inputCode.getText().toString())) {
                    ToastUtils.shortShow(R.string.input_code);
                    return;
                }

                if (isShowIdCard &&
                        StringUtils.isBlank(backUserPwd_IdCode.getText().toString())) {
                    ToastUtils.shortShow(R.string.backUserPwd_message_2);
                    return;
                }

                if (BackserPwd_NewPwd.getText().toString().length() < 6 ||
                        BackserPwd_NewPwd.getText().toString().length() > 20) {
                    ToastUtils.shortShow(R.string.reg_inputPwdMessage);
                    return;
                }
                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("mobile", backUserPwd_Tel.getText().toString());
                submitMap.put("newPassword", MD5Util.stringMD5(BackserPwd_NewPwd.getText().toString()));
                submitMap.put("passwordType", "L");
                submitMap.put("dynamicCode", backUserPwd_inputCode.getText().toString());
                if (isShowIdCard) {
                    submitMap.put("certNo", backUserPwd_IdCode.getText().toString());
                }
                showProgressDailog();
                requestForHttp(MESSAGE_TAG_3, AppConfig.getInstance().UserPwdRegSetting, submitMap);
                break;
        }
    }


    /**
     * 点击后开始获取验证码
     */
    private void startGetSMS() {
        if (StringUtils.isBlank(backUserPwd_Tel.getText().toString())) {
            ToastUtils.shortShow(R.string.backUserPwd_message_1);
            return;
        }
        getCodeTime.start();
        backUserPwd_getCode.setEnabled(false);
        backUserPwd_getCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_selector_true));
        getCode();
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
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
                Map<String, Object> messageMap = (Map<String, Object>) result;
                Map<String, String> imgMap = (Map<String, String>) messageMap.get("data");
                String status = imgMap.get("status");
                if (status.equals("N")) {
                    isShowIdCard = true;
                    backUserPwd_IdCarLayout.setVisibility(View.VISIBLE);
                }

                break;
            case MESSAGE_TAG_3:
                ToastUtils.shortShow(R.string.operationSucc);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {

        dismissProressDialog();

        ToastUtils.shortShow(result == null ? "" : result);
        switch (TAG) {
            case MESSAGE_TAG_2:
                getCode();
                break;
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    public void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", backUserPwd_Tel.getText().toString());
        map.put("type", "2");
        requestForHttp(MESSAGE_TAG_1, AppConfig.getInstance().getImgCode, map);
    }


    private class GetCodeTime extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public GetCodeTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            backUserPwd_getCode.setText(millisUntilFinished / 1000 + getResources().getString(R.string.RestartGet));
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
        backUserPwd_getCode.setEnabled(true);
        backUserPwd_getCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_backgroup_color));
        backUserPwd_getCode.setText(R.string.RestartGet_1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getCodeTime != null) {
            getCodeTime.cancel();
            getCodeTime = null;
        }
    }
}
