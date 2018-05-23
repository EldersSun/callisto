package com.miaodao.Fragment.Account;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.miaodao.Activity.LoginActivity;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Account.gesturepd.GestureMainFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ToastUtils;

/**
 * 安全设置首页
 * Created by Home_Pc on 2017/3/24.
 */

public class SecurityFragment extends ContentBaseFragment implements View.OnClickListener {

    private Button security_setUserLoginPwd, security_setGesture, security_setAccountPwd;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_securityhome_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.account_message_7);
        Boolean userPayPwdStatus = SharedPreferencesUtil.getBoolean(getActivity(), AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG);
        security_setUserLoginPwd = (Button) fgView.findViewById(R.id.security_setUserLoginPwd);
        security_setGesture = (Button) fgView.findViewById(R.id.security_setGesture);
        security_setAccountPwd = (Button) fgView.findViewById(R.id.security_setAccountPwd);
        if (userPayPwdStatus){
            security_setAccountPwd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {
        security_setUserLoginPwd.setOnClickListener(this);
        security_setGesture.setOnClickListener(this);
        security_setAccountPwd.setOnClickListener(this);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_setUserLoginPwd://登录密码
                ServiceBaseActivity.startActivity(getActivity(), BackUserPwdFragment.class.getName());
                break;
            case R.id.security_setGesture:
                if (checkUserLogin()) {
                    ServiceBaseActivity.startActivity(getActivity(), GestureMainFragment.class.getName());
                }
                break;
            case R.id.security_setAccountPwd://支付密码
                ServiceBaseActivity.startActivity(getActivity(), BackUserPayPwdFragment.class.getName());
                break;
        }
    }


    /**
     * 判断用户是否登录
     */
    private boolean checkUserLogin() {
        if (!AppConfig.getInstance().checkUserLoginStaus()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("jumpFlag", true);
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }


}
