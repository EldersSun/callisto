package com.miaodao.Fragment.Loan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Activity.LoginActivity;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Loan.Apply.PersonalIdCarFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Cycle;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.Money;
import com.miaodao.Sys.Utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页申请开户
 * Created by Home_Pc on 2017/3/10.
 */

public class LoanMainHomeFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView Loan_QuotaTvShow, loan_QuotaMinVaule, loan_QuotaMaxVaule, Loan_repaymentTvShow;
    private SeekBar Loan_QuotaSeekBar;
    private Button Loan_submit;
    private LinearLayout LoanRateCalculation;
    private TextView loan_Quota_time_one, loan_Quota_time_two, loan_Quota_time_three;

    private int minVaule = 5;
    private int maxVaule = 50;
    //滑动条的最终数据
    private int seekBarVaule = 0;

    private final int REQUEST_MESSAGE_TAG = 0X1005;
    private String interestFormula = "0.0";
    private int days = 0;
    private int blueColor;
    private int grayColor;

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_loanhome_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        Loan_QuotaTvShow = (TextView) fgView.findViewById(R.id.Loan_QuotaTvShow);
        loan_QuotaMinVaule = (TextView) fgView.findViewById(R.id.loan_QuotaMinVaule);
        loan_QuotaMaxVaule = (TextView) fgView.findViewById(R.id.loan_QuotaMaxVaule);
        Loan_repaymentTvShow = (TextView) fgView.findViewById(R.id.Loan_repaymentTvShow);

        Loan_QuotaSeekBar = (SeekBar) fgView.findViewById(R.id.Loan_QuotaSeekBar);
        Loan_submit = (Button) fgView.findViewById(R.id.Loan_submit);
        LoanRateCalculation = (LinearLayout) fgView.findViewById(R.id.LoanRateCalculation);

        loan_Quota_time_one = (TextView) fgView.findViewById(R.id.loan_Quota_time_one);
        loan_Quota_time_two = (TextView) fgView.findViewById(R.id.loan_Quota_time_two);
        loan_Quota_time_three = (TextView) fgView.findViewById(R.id.loan_Quota_time_three);

        blueColor = getResources().getColor(R.color.appColor);
        grayColor = getResources().getColor(R.color.loan_bank_text);
    }

    @Override
    protected void initEvent() {
        Loan_submit.setOnClickListener(this);
        LoanRateCalculation.setOnClickListener(this);
        loan_Quota_time_one.setOnClickListener(this);
        loan_Quota_time_two.setOnClickListener(this);
        loan_Quota_time_three.setOnClickListener(this);
        Loan_QuotaSeekBar.setOnSeekBarChangeListener(this);

        Loan_QuotaSeekBar.setMax(maxVaule - minVaule);
        Loan_QuotaSeekBar.setProgress(maxVaule / 2);
        requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().queryLoanProduct, new HashMap<String, Object>());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Loan_submit:
                if(!AppConfig.getInstance().checkUserLoginStaus()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("jumpFlag", true);
                    startActivity(intent);
                    return;
                }
                ServiceBaseActivity.startActivity(getActivity(), PersonalIdCarFragment.class.getName());
                break;
            case R.id.LoanRateCalculation:
                Bundle bundle = new Bundle();
                bundle.putInt("minVaule",minVaule);
                bundle.putInt("maxVaule",maxVaule);
                bundle.putInt("day",days);
                bundle.putString("interestFormula",interestFormula);
                ServiceBaseActivity.startActivity(getActivity(), RateCalculationFragment.class.getName(),bundle);
                break;

            case R.id.loan_Quota_time_one:
                showChange(R.id.loan_Quota_time_one);
                break;

            case R.id.loan_Quota_time_two:
                showChange(R.id.loan_Quota_time_two);
                break;

            case R.id.loan_Quota_time_three:
                showChange(R.id.loan_Quota_time_three);
                break;

            default:
                break;
        }
    }


    /**
     * 响应点击事件变化界面
     *
     * @param id
     */
    private void showChange(int id) {
        Cycle cycle;
        switch (id) {
            case R.id.loan_Quota_time_one:
                loan_Quota_time_one.setTextColor(blueColor);
                loan_Quota_time_two.setTextColor(grayColor);
                loan_Quota_time_three.setTextColor(grayColor);
                cycle = (Cycle) loan_Quota_time_one.getTag();
                getRate(cycle);
                break;

            case R.id.loan_Quota_time_two:
                loan_Quota_time_one.setTextColor(grayColor);
                loan_Quota_time_two.setTextColor(blueColor);
                loan_Quota_time_three.setTextColor(grayColor);
                cycle = (Cycle) loan_Quota_time_two.getTag();
                getRate(cycle);
                break;

            case R.id.loan_Quota_time_three:
                loan_Quota_time_one.setTextColor(grayColor);
                loan_Quota_time_two.setTextColor(grayColor);
                loan_Quota_time_three.setTextColor(blueColor);
                cycle = (Cycle) loan_Quota_time_three.getTag();
                getRate(cycle);
                break;
        }

    }

    /**
     * 计算利率
     *
     * @param cycle
     */
    private void getRate(Cycle cycle) {
        if (cycle == null) return;
        //本金
        Money capital = new Money(String.valueOf(seekBarVaule));
        //利息
        Money interestDays = capital.multiply(Double.valueOf(interestFormula));
        days = cycle.getDays();
        Money amountTotal = interestDays.multiply(days);
        //本息
        Money v1 = capital.add(amountTotal);
        Loan_repaymentTvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
    }

    // 滚动时
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarVaule = (progress + minVaule / 100) * 100;
        Loan_QuotaTvShow.setText(seekBarVaule + ".00");
        showChange(R.id.loan_Quota_time_one);
    }

    // 开始滚动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    // 停止滚动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                Map<String, Object> resultMap = (Map<String, Object>) result;
                if (resultMap.containsKey("data") && !resultMap.get("data").equals(null)) {
                    Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                    if (dataMap.containsKey("products") && !dataMap.get("products").equals(null)) {
                        List<Map<String, Object>> products = (List<Map<String, Object>>) dataMap.get("products");
                        for (int i = 0; i < products.size(); i++) {
                            Map<String, Object> productMap = products.get(i);
                            if (productMap.containsKey("cycles") && !productMap.get("cycles").equals(null)) {
                                List<Map<String, Object>> cycles = (List<Map<String, Object>>) productMap.get("cycles");
                                getCycle(cycles);
                            }

                            if (productMap.containsKey("interestFormula") && !productMap.get("interestFormula").equals(null)) {
                                interestFormula = (String) productMap.get("interestFormula");
                            }

                            if (productMap.containsKey("minLimit") && !productMap.get("minLimit").equals(null)) {
                                minVaule = (Integer) productMap.get("minLimit");
                                loan_QuotaMinVaule.setText(String.valueOf(minVaule));
                            }

                            if (productMap.containsKey("maxLimit") && !productMap.get("maxLimit").equals(null)) {
                                maxVaule = (Integer) productMap.get("maxLimit");
                                loan_QuotaMaxVaule.setText(String.valueOf(maxVaule));
                                Loan_QuotaSeekBar.setMax(maxVaule / 100 - minVaule / 100);
                                Loan_QuotaSeekBar.setProgress((maxVaule / 100) / 2);
                            }


                            saveRateInfo();

                        }
                    }
                }
                break;
        }
    }


    /**
     * 保存费率计算
     */
    private void saveRateInfo() {

//        bundle.putInt("minVaule",minVaule);
//        bundle.putInt("maxVaule",maxVaule);
//        bundle.putInt("day",days);
//        bundle.putString("interestFormula",interestFormula);
        SharedPreferencesUtil.putInt(getActivity(), "minVaule", minVaule);
        SharedPreferencesUtil.putInt(getActivity(), "maxVaule", maxVaule);
        SharedPreferencesUtil.putInt(getActivity(), "day", days);
        SharedPreferencesUtil.putString(getActivity(), "interestFormula", interestFormula);
    }

    /**
     * 获取周期
     *
     * @param cycles
     */
    private void getCycle(List<Map<String, Object>> cycles) {
        for (int i = 0; i < cycles.size(); i++) {
            Map<String, Object> cycleMap = cycles.get(i);
            if (i == 0) {
                Cycle cycle = new Cycle();
                if (cycleMap.containsKey("name") && !cycleMap.get("name").equals(null)) {
                    cycle.setName((String) cycleMap.get("name"));
                }
                if (cycleMap.containsKey("value") && !cycleMap.get("value").equals(null)) {
                    cycle.setValue((Integer) cycleMap.get("value"));
                }
                if (cycleMap.containsKey("days") && !cycleMap.get("days").equals(null)) {
                    cycle.setDays((Integer) cycleMap.get("days"));
                }
                loan_Quota_time_one.setText(cycle.getName());
                loan_Quota_time_one.setTag(cycle);
            } else if (i == 1) {
                Cycle cycle = new Cycle();
                if (cycleMap.containsKey("name") && !cycleMap.get("name").equals(null)) {
                    cycle.setName((String) cycleMap.get("name"));
                }
                if (cycleMap.containsKey("value") && !cycleMap.get("value").equals(null)) {
                    cycle.setValue((Integer) cycleMap.get("value"));
                }
                if (cycleMap.containsKey("days") && !cycleMap.get("days").equals(null)) {
                    cycle.setDays((Integer) cycleMap.get("days"));
                }
                loan_Quota_time_two.setText(cycle.getName());
                loan_Quota_time_two.setTag(cycle);
            } else if (i == 2) {
                Cycle cycle = new Cycle();
                if (cycleMap.containsKey("name") && !cycleMap.get("name").equals(null)) {
                    cycle.setName((String) cycleMap.get("name"));
                }
                if (cycleMap.containsKey("value") && !cycleMap.get("value").equals(null)) {
                    cycle.setValue((Integer) cycleMap.get("value"));
                }
                if (cycleMap.containsKey("days") && !cycleMap.get("days").equals(null)) {
                    cycle.setDays((Integer) cycleMap.get("days"));
                }
                loan_Quota_time_three.setText(cycle.getName());
                loan_Quota_time_three.setTag(cycle);
            }
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }
}
