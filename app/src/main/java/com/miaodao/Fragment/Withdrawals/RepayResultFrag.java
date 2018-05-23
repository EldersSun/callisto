package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by daixinglong on 2017/4/18.
 */

public class RepayResultFrag extends ContentBaseFragment implements View.OnClickListener {

    private final String STATE_HANDLE = "P";//处理中
    private final String STATE_SUCC = "S";//成功
    private final String STATE_FAIL = "F";//失败

    private ImageView ivPayStat;//还款状态图标
    private TextView tvPayStat, tvPaySay, tvPayMoney;
    private Button btnPay;

    private Bundle bundle;
    private String state = "";
    private String amount = "";


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_pay_result, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.pay_result_title);
        ivPayStat = (ImageView) fgView.findViewById(R.id.iv_pay_stat);
        tvPayStat = (TextView) fgView.findViewById(R.id.tv_pay_stat);
        tvPaySay = (TextView) fgView.findViewById(R.id.tv_pay_say);
        tvPayMoney = (TextView) fgView.findViewById(R.id.tv_pay_money);
        btnPay = (Button) fgView.findViewById(R.id.btn_pay);

        bundle = getArguments();
        if (bundle == null) return;
        state = bundle.getString("state");
        amount = bundle.getString("amount");

        if (TextUtils.isEmpty(state)) return;
        showInfo();
    }


    /**
     * 根据状态表示显示页面
     */
    private void showInfo() {
        switch (state) {

            case STATE_HANDLE:
                ivPayStat.setImageResource(R.drawable.pay_result_handle);
                tvPayStat.setText(R.string.pay_result_handle);
                tvPayStat.setTextColor(getResources().getColor(R.color.appColor));
                tvPaySay.setText(R.string.pay_result_handle_say);
                tvPayMoney.setText("￥" + amount);
                btnPay.setText(R.string.pay_result_btn);
                break;

            case STATE_SUCC:
                ivPayStat.setImageResource(R.drawable.pay_result_succ);
                tvPayStat.setText(R.string.pay_result_succ);
                tvPayStat.setTextColor(getResources().getColor(R.color.appColor));
                tvPaySay.setText(R.string.pay_result_succ_say);
                tvPayMoney.setText("￥" + amount);
                btnPay.setText(R.string.pay_result_succ_btn);
                break;

            case STATE_FAIL:
                ivPayStat.setImageResource(R.drawable.pay_result_fail);
                tvPayStat.setText(R.string.pay_result_fail);
                tvPayStat.setTextColor(getResources().getColor(R.color.pay_result_gray));
                tvPaySay.setText(R.string.pay_result_fail_say);
//                tvPayMoney.setText(amount);
                btnPay.setText(R.string.pay_result_btn_repay);
                break;

            default:
                break;

        }

    }

    @Override
    protected void initEvent() {

        btnPay.setOnClickListener(this);
        title_back.setOnClickListener(this);


//        btnPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMessage(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1, "");
//                getActivity().finish();
//            }
//        });


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
        goLoanMain();
    }

    private void goLoanMain() {
        EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
        getActivity().finish();
    }


    @Override
    protected boolean OnkeyDownListener(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goLoanMain();
            return false;
        }
        return super.OnkeyDownListener(keyCode, event);
    }
}
