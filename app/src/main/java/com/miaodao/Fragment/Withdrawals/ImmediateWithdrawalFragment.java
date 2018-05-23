package com.miaodao.Fragment.Withdrawals;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.AppLoanModel;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomSelectorDialog;
import com.miaodao.Sys.Widgets.DialogShowImgCode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现
 * Created by Home_Pc on 2017/3/21.
 */

public class ImmediateWithdrawalFragment extends ContentBaseFragment implements View.OnClickListener {

    private TextView imm_money, imm_message, tv_purpose;
    private View line;
    private EditText imm_Pwd, imm_code;
    private RelativeLayout imm_purpose, imm_setPwd;
    private Button imm_btn_code, imm_submit;

    private boolean isStartTimer = false;
    private GetCodeTime getCodeTime = new GetCodeTime(AppConfig.getInstance().millisInFuture, AppConfig.getInstance().countDownInterval);
    private final int REQUEST_MESSAGE_TAG = 0X4001;
    private final int REQUEST_MESSAGE_TAG_2 = 0X4002;
    private final int REQUEST_MESSAGE_TAG_3 = 0X4003;
    private final int REQUEST_MESSAGE_TAG_4 = 0X4004;
    private CustomSelectorDialog selectorDialog;
    private AppLoanModel appLoanModel = new AppLoanModel();
    private DialogShowImgCode dialogShowImgCode;
    private List<String> dataList = new ArrayList<>();

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_immwith_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        imm_money = (TextView) fgView.findViewById(R.id.imm_money);
        imm_message = (TextView) fgView.findViewById(R.id.imm_message);
        tv_purpose = (TextView) fgView.findViewById(R.id.tv_purpose);
        line = (View) fgView.findViewById(R.id.line);

        imm_purpose = (RelativeLayout) fgView.findViewById(R.id.imm_purpose);
        imm_setPwd = (RelativeLayout) fgView.findViewById(R.id.imm_setPwd);
        imm_Pwd = (EditText) fgView.findViewById(R.id.imm_Pwd);
        imm_code = (EditText) fgView.findViewById(R.id.imm_code);

        imm_btn_code = (Button) fgView.findViewById(R.id.imm_btn_code);
        imm_submit = (Button) fgView.findViewById(R.id.imm_submit);
    }

    @Override
    protected void initEvent() {
        if (!isAdded()) {
            return;
        }
        title_tvShow.setText(R.string.withdrawals_message_9);
        imm_btn_code.setOnClickListener(this);
        imm_submit.setOnClickListener(this);
        imm_purpose.setOnClickListener(this);
        imm_setPwd.setOnClickListener(this);

        Bundle bundle = getArguments();

        if (null == bundle) return;
        appLoanModel = (AppLoanModel) bundle.get("appLoan");
        if (null == appLoanModel) return;
        imm_money.setText(appLoanModel.getAmount() + ".00");
        if (null != appLoanModel.getBankName()) {
            imm_message.setText(getString(R.string.Purpose_message_4) + appLoanModel.getBankName() +
                    getString(R.string.Purpose_message_5) + appLoanModel.getCardNo());
        }

        dataList = appLoanModel.getPurposeList();
        selectorDialog = new CustomSelectorDialog(getActivity(),
                getResources().getString(R.string.withdrawals_message_22), dataList);

        dialogShowImgCode = new DialogShowImgCode(getActivity());
        dialogShowImgCode.setOnDialogOperationclick(new DialogShowImgCode.onDialogOperationclick() {
            @Override
            public void Confirm(String imgCode) {
                if (StringUtils.isBlank(imgCode)) {
                    ToastUtils.shortShow(R.string.reg_inputcodeHint);
                    changeSMSBtn();
                    getCodeTime.cancel();
                    return;
                }

                if (StringUtils.isBlank(SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""))) {
                    ToastUtils.shortShow(R.string.input_account);
                    changeSMSBtn();
                    getCodeTime.cancel();
                    return;
                }

                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("mobile", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""));
                submitMap.put("type", "6");
                submitMap.put("imgCode", imgCode);
                requestForHttp(REQUEST_MESSAGE_TAG_3, AppConfig.getInstance().postMessage, submitMap);
            }

            @Override
            public void cancel() {
                changeSMSBtn();
                getCodeTime.cancel();
            }

            @Override
            public void reGet() {
                startGetSMS();
            }
        });
    }

    public void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, ""));
        map.put("type", "6");
        requestForHttp(REQUEST_MESSAGE_TAG_4, AppConfig.getInstance().getImgCode, map);
    }

    @Override
    public void onStart() {
        super.onStart();
        Boolean userPayPwdStatus = SharedPreferencesUtil.getBoolean(getActivity(), AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG);
        if (userPayPwdStatus) {
            imm_setPwd.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            imm_Pwd.setVisibility(View.VISIBLE);
        } else {
            imm_setPwd.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            imm_Pwd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_2:
                Map<String, Object> resultMap = (Map<String, Object>) result;
                if (resultMap.containsKey("msg") && !resultMap.get("msg").equals(null)) {
                    ToastUtils.shortShow(resultMap.get("msg").toString());
                }
                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                getActivity().finish();
                break;
            case REQUEST_MESSAGE_TAG_3:
                Map<String, Object> messageMap = (Map<String, Object>) result;
                if (messageMap.containsKey("msg") && !messageMap.get("msg").equals(null)) {
                    ToastUtils.shortShow(messageMap.get("msg").toString());
                }
                break;
            case REQUEST_MESSAGE_TAG_4:
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
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_3:
                getCode();
                break;
            case REQUEST_MESSAGE_TAG_2:
                break;
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imm_submit:
                if (appLoanModel.getPurpose() == null || StringUtils.isBlank(appLoanModel.getPurpose().getName())) {
                    ToastUtils.shortShow(R.string.Purpose_message_1);
                    return;
                }
                if (StringUtils.isBlank(imm_Pwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.Purpose_message_2);
                    return;
                }
                if (StringUtils.isBlank(imm_code.getText().toString())) {
                    ToastUtils.shortShow(R.string.Purpose_message_3);
                    return;
                }
                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("userId", appLoanModel.getUserId());
                submitMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                submitMap.put("productId", appLoanModel.getProductId());
                submitMap.put("cycle", appLoanModel.getCycle());
                submitMap.put("amount", appLoanModel.getAmount());
                submitMap.put("loanPurpose", appLoanModel.getPurpose().getName());
                submitMap.put("instId", appLoanModel.getInstId());
                submitMap.put("payPassword", MD5Util.stringMD5(imm_Pwd.getText().toString()));
                submitMap.put("dynamicCode", imm_code.getText().toString());
                showProgressDailog();
                requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().applyLoan, submitMap);
                break;
            case R.id.imm_btn_code:
                startGetSMS();
                break;

            case R.id.imm_purpose:

                if (dataList == null) return;
                if (selectorDialog != null) {
                    selectorDialog.show();
                    selectorDialog.setOnDialogOperationclick(new CustomSelectorDialog.onCustomDialogOperationclick() {
                        @Override
                        public void Confirm(String dateString) {
                            tv_purpose.setText(dateString);
                            appLoanModel.setPurpose(appLoanModel.getPurposeMap().get(dateString));
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
                break;

            case R.id.imm_setPwd:
                ServiceBaseActivity.startActivity(getActivity(), SetWithdrawalsPwdFragment.class.getName());
                break;


            default:
                break;
        }
    }

    private void startGetSMS() {
        getCodeTime.start();
        imm_btn_code.setEnabled(false);
        imm_btn_code.setBackground(getResources().getDrawable(R.drawable.btn_shape_selector_true));
        getCode();
    }

    private class GetCodeTime extends CountDownTimer {
        public GetCodeTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!isAdded()) {
                return;
            }
            imm_btn_code.setText(millisUntilFinished / 1000 + " " + getResources().getString(R.string.RestartGet));
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
        imm_btn_code.setEnabled(true);
        imm_btn_code.setBackground(getResources().getDrawable(R.drawable.btn_shape_backgroup_color));
        imm_btn_code.setText(R.string.RestartGet_1);
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
