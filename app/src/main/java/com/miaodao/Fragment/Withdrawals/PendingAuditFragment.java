package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.WebViewFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.ToastUtils;

/**
 * 填写完成等待审核
 * Created by Home_Pc on 2017/3/21.
 */

public class PendingAuditFragment extends BaseFragment implements View.OnClickListener{
    private Button pendingAudit_Submit;

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_pendingaudit_layout,null);
    }

    @Override
    protected void initWidgets(View fgView) {
        pendingAudit_Submit = (Button) fgView.findViewById(R.id.pendingAudit_Submit);
    }

    @Override
    protected void initEvent() {
        pendingAudit_Submit.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.pendingAudit_Submit:
                Bundle helpBundle = new Bundle();
                helpBundle.putString("url", AppConfig.getInstance().HELP_CENTER_URL);
                helpBundle.putString("title", getResources().getString(R.string.account_message_6));
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), helpBundle);
                break;
        }
    }
}
