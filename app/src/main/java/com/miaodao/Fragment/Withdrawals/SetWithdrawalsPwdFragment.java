package com.miaodao.Fragment.Withdrawals;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MD5Util;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置提现密码
 * Created by Home_Pc on 2017/3/21.
 */
public class SetWithdrawalsPwdFragment extends ContentBaseFragment implements View.OnClickListener {
    private EditText setwith_pwd, setwith_rePwd, setwith_loginPwd, setwith_idCard;
    private Button setwith_submit;

    private final int REQUEST_MESSAGE_TAG = 0X9001;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_setwith_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        setwith_pwd = (EditText) fgView.findViewById(R.id.setwith_pwd);
        setwith_rePwd = (EditText) fgView.findViewById(R.id.setwith_rePwd);
        setwith_loginPwd = (EditText) fgView.findViewById(R.id.setwith_loginPwd);
        setwith_idCard = (EditText) fgView.findViewById(R.id.setwith_idCard);
        setwith_submit = (Button) fgView.findViewById(R.id.setwith_submit);
    }

    @Override
    protected void initEvent() {
        title_tvShow.setText(R.string.withdrawals_message_21);
        setwith_submit.setOnClickListener(this);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                ToastUtils.shortShow(R.string.operationSucc);
                SharedPreferencesUtil.putBoolean(getActivity(), AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG, true);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setwith_submit:
                if (!StringUtils.isPassword(setwith_pwd.getText().toString())) {
//                    ToastUtils.shortShow(R.string.reg_inputPwdMessage);
                    ToastUtils.longShow("提现密码设置有误(6-20位密码，必须含大小写字母、数字)");
                    return;
                }
                if (!StringUtils.isPassword(setwith_rePwd.getText().toString())) {
                    ToastUtils.longShow("确认提现密码设置有误(6-20位密码，必须含大小写字母、数字)");
                    return;
                }
                if (!setwith_pwd.getText().toString().equals(setwith_rePwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.setWith_message_6);
                    return;
                }
                if (StringUtils.isBlank(setwith_loginPwd.getText().toString())) {
                    ToastUtils.shortShow(R.string.setWith_message_4);
                    return;
                }
                if (StringUtils.isBlank(setwith_idCard.getText().toString())) {
                    ToastUtils.shortShow(R.string.setWith_message_5);
                    return;
                }
                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                submitMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                submitMap.put("loginPassword", MD5Util.stringMD5(setwith_loginPwd.getText().toString()));
                submitMap.put("newPassword", MD5Util.stringMD5(setwith_rePwd.getText().toString()));
                submitMap.put("certNo", setwith_idCard.getText().toString());
                showProgressDailog();
                requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().setPayPassword, submitMap);
                break;
        }
    }
}
