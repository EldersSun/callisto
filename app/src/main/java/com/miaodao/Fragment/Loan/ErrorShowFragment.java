package com.miaodao.Fragment.Loan;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;

/**
 * 网络连接失败
 * Created by Home_Pc on 2017/3/27.
 */

public class ErrorShowFragment extends BaseFragment implements View.OnClickListener{
    private ImageView error_layout;

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_errorshow_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        error_layout = (ImageView) fgView.findViewById(R.id.error_imgShow);
    }

    @Override
    protected void initEvent() {
        error_layout.setOnClickListener(this);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error_imgShow:
                sendMessage(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1,"");
                break;
        }
    }
}
