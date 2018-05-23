package com.miaodao.Fragment.Loan.Apply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 贷款申请失败页面
 * Created by Home_Pc on 2017/3/31.
 */

public class LoanFailFragment extends BaseFragment implements View.OnClickListener{

    private final int LOAN_CONFIRM = 0x0000;
    private Button loan_fail_Submit;

    private Bundle bundle;
    private String transId;


    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_loanfail_layout,null);
    }

    @Override
    protected void initWidgets(View fgView) {
        loan_fail_Submit = (Button) fgView.findViewById(R.id.loan_fail_Submit);
        bundle = getArguments();

        if (bundle != null){
            transId = bundle.getString("transId");
        }
    }

    @Override
    protected void initEvent() {
        loan_fail_Submit.setOnClickListener(this);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
        dismissProressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loan_fail_Submit:
                showProgressDailog();
                Map<String, Object> confirmMap = new HashMap<>();
                confirmMap.put("transId", transId);
                showProgressDailog();
                requestForHttp(LOAN_CONFIRM, AppConfig.getInstance().applyLoanConfirm, confirmMap);
                break;
        }
    }
}
