package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Free.FreeMainFragment;
import com.miaodao.Fragment.Loan.RateCalculationFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.LoanStatusContent;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.ShowShareDialog;
import com.miaodao.Sys.Widgets.UiContentView.UiContentView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现处理中---申请提现
 * Created by Home_Pc on 2017/3/21.
 */

public class WithdrawalsOperationFragment extends BaseFragment implements UiContentView.OnContentClick, View.OnClickListener, ShowShareDialog.IShare {

    private final int LOAN_CONFIRM = 0x0000;

    private TextView loanStatus;//标记贷款状态
    private TextView loanMoney;//贷款金额
    private TextView bankName;//银行名
    private TextView loanStatus1Date;//状态1的日期
    private TextView loanStatus1Time;//状态1的时间
    private TextView loanStatus2Date;//状态2的日期
    private TextView loanStatus2Time;//状态2的时间
    private TextView loanStatus3Date;//状态3的日期
    private TextView loanStatus3Time;//状态3的时间
    private ImageView bankImg;//银行ICON
    private ImageView loanTimeImhShow;//贷款图标状态条
    private TextView loanRealMoney;//实际到账金额
    private TextView loanCard;//实际到账金额
    private TextView loanPayDate;//实际到账金额
    private RelativeLayout withTimeShare;//分享
    private Button withTimeSubmit;//知道了按钮
    private Bundle bundle;
    private String loanStatusFlag = "";//贷款状态表示
    private LoanStatusContent loanStatusContent;
    private ImageView ivRate;//点击进去费率计算

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_withdrawals_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {

        bundle = getArguments();

        loanStatus = (TextView) fgView.findViewById(R.id.loan_status);
        loanMoney = (TextView) fgView.findViewById(R.id.loan_money);
        bankName = (TextView) fgView.findViewById(R.id.loan_bank_name);
        loanStatus1Date = (TextView) fgView.findViewById(R.id.loan_status1_date);
        loanStatus1Time = (TextView) fgView.findViewById(R.id.loan_status1_time);
        loanStatus2Date = (TextView) fgView.findViewById(R.id.loan_status2_date);
        loanStatus2Time = (TextView) fgView.findViewById(R.id.loan_status2_time);
        loanStatus3Date = (TextView) fgView.findViewById(R.id.loan_status3_date);
        loanStatus3Time = (TextView) fgView.findViewById(R.id.loan_status3_time);
        bankImg = (ImageView) fgView.findViewById(R.id.loan_bank_img);
        loanTimeImhShow = (ImageView) fgView.findViewById(R.id.loan_time_imhShow);
        loanRealMoney = (TextView) fgView.findViewById(R.id.loan_real_money);
        loanCard = (TextView) fgView.findViewById(R.id.loan_card);
        loanPayDate = (TextView) fgView.findViewById(R.id.loan_pay_date);
        withTimeShare = (RelativeLayout) fgView.findViewById(R.id.with_time_share);
        withTimeSubmit = (Button) fgView.findViewById(R.id.with_time_submit);
        ivRate = (ImageView) fgView.findViewById(R.id.iv_rate);
        showContent();
    }


    /**
     *
     */
    private void showContent() {
        loanRealMoney.setText("400" + "元" + "(已扣除服务费:35元");

        if (bundle == null) return;
        loanStatusContent = bundle.getParcelable("content");
        if (loanStatusContent == null) return;
        loanStatusFlag = bundle.getString("status");
        if ("100410".equalsIgnoreCase(loanStatusFlag)) {
            withTimeSubmit.setVisibility(View.GONE);
            loanStatus.setText(R.string.withdrawals_message_1);
            loanTimeImhShow.setImageResource(R.drawable.withd_status_2);
            loanStatus3Date.setText("预计" + loanStatusContent.getPayDate());

        } else {
            withTimeSubmit.setVisibility(View.VISIBLE);
            loanStatus.setText(R.string.withdrawals_message_2);
            loanTimeImhShow.setImageResource(R.drawable.withd_status_3);
            loanStatus3Date.setText(loanStatusContent.getPayDate());
            String payTime = loanStatusContent.getPayTime();
            if (!TextUtils.isEmpty(payTime)) {
                loanStatus3Time.setText(payTime);
            }
        }
//        ic_add_bank_phone_question_normal
//        Glide.with(this).load(MResource.getResource(loanStatusContent.getInstId().toLowerCase(), getActivity())).into(bankImg);
        Glide.with(this).load(AppConfig.getInstance().BASE_BANK + "/" + loanStatusContent.getInstId() + ".png").into(bankImg);
        loanMoney.setText("￥" + loanStatusContent.getAmount());
        bankName.setText(loanStatusContent.getBankName() + "(" + "尾号" + loanStatusContent.getCardIdxNo() + ")");
        loanStatus1Date.setText(loanStatusContent.getApplyDate());
        loanStatus1Time.setText(loanStatusContent.getApplyTime());
        loanStatus2Date.setText(loanStatusContent.getApplyDate());
        loanStatus2Time.setText(loanStatusContent.getApplyTime());
//        loanStatus3Time.setText(loanStatusContent);
        loanRealMoney.setText(loanStatusContent.getOnAcctAmount() + "元" + "  (已扣除服务费:" + loanStatusContent.getFee() + "元");
        loanCard.setText(loanStatusContent.getBankName() + "(" + "尾号" + loanStatusContent.getCardIdxNo() + ")" + "   " + loanStatusContent.getCertName());
        loanPayDate.setText(loanStatusContent.getRefundDate());
    }


    @Override
    protected void initEvent() {
        withTimeShare.setOnClickListener(this);
        withTimeSubmit.setOnClickListener(this);
        ivRate.setOnClickListener(this);
    }


    @Override
    public void onContentClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.with_time_share:

                showShareDialog();

                break;

            case R.id.with_time_submit:
                confirmLoan();
                break;

            case R.id.iv_rate:
                Bundle bundle = new Bundle();
                bundle.putInt("minVaule", SharedPreferencesUtil.getInt(getActivity(), "minVaule", 0));
                bundle.putInt("maxVaule", SharedPreferencesUtil.getInt(getActivity(), "maxVaule", 0));
                bundle.putInt("day", SharedPreferencesUtil.getInt(getActivity(), "day", 0));
                bundle.putString("interestFormula", SharedPreferencesUtil.getString(getActivity(), "interestFormula", ""));
                ServiceBaseActivity.startActivity(getActivity(), RateCalculationFragment.class.getName(), bundle);
                break;

            default:
                break;

        }
    }


    /**
     * 显示分享对话框
     */
    private void showShareDialog() {
        ShowShareDialog shareDialog = new ShowShareDialog(getActivity());
        shareDialog.setListener(this);
        shareDialog.show();
    }


    @Override
    public void share() {
        Bundle bundle = new Bundle();
        bundle.putString("showTitle", "showTitle");
        ServiceBaseActivity.startActivity(getActivity(), FreeMainFragment.class.getName(), bundle);
    }


    /**
     * 向服务器确认
     */
    private void confirmLoan() {
        if (loanStatusContent == null) return;
        Map<String, Object> confirmMap = new HashMap<>();
        confirmMap.put("transId", loanStatusContent.getTransId());
        showProgressDailog();
        requestForHttp(LOAN_CONFIRM, AppConfig.getInstance().applyLoanConfirm, confirmMap);
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
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

}
