package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Fragment.Loan.bean.WaitPay;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Utils.RepayAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by daixinglong on 2017/5/5.
 */

public class WaitRepayFrag extends ContentBaseFragment implements RepayAdapter.IRePay {

    private TextView tvRepayMoney, tvLoanMoney, tvLoanRate, tvFailRepay, tvLoanDate;
    private RecyclerView rvRepay;
    private RepayAdapter repayAdapter;
    private ArrayList<WaitPay> waitPays;


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_wait_repay, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText("待支付");
        tvRepayMoney = (TextView) fgView.findViewById(R.id.tv_repay_money);
        tvLoanMoney = (TextView) fgView.findViewById(R.id.tv_loan_money);
        tvLoanRate = (TextView) fgView.findViewById(R.id.tv_loan_rate);
        tvFailRepay = (TextView) fgView.findViewById(R.id.tv_fail_repay);
        tvLoanDate = (TextView) fgView.findViewById(R.id.tv_loan_date);
        rvRepay = (RecyclerView) fgView.findViewById(R.id.rv_repay);
        rvRepay.setLayoutManager(new LinearLayoutManager(getActivity()));
        repayAdapter = new RepayAdapter(getActivity());
        rvRepay.setAdapter(repayAdapter);
        repayAdapter.setListener(this);
        Bundle arguments = getArguments();
        if (arguments == null) return;
        waitPays = arguments.getParcelableArrayList("waitPays");
        if (waitPays == null) return;
        showWaitPays();
    }


    /**
     * 显示待还款订单
     */
    private void showWaitPays() {
        repayAdapter.setList(waitPays);
    }

    @Override
    protected void initEvent() {

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
    public void repay() {
        EventBus.getDefault().post("repay");
        getActivity().finish();
    }
}
