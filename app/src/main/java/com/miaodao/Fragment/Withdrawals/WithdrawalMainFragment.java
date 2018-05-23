package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Loan.RateCalculationFragment;
import com.miaodao.Fragment.Quota.ZhimaAuthorFrag;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.AppLoanModel;
import com.miaodao.Sys.Model.Cycle;
import com.miaodao.Sys.Model.Purpose;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.Money;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fcloud.licai.miaodao.R.color.loan_bank_text;

/**
 * 提现首页
 * Created by Home_Pc on 2017/3/21.
 */

public class WithdrawalMainFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView with_main_money, with_main_MinVaule, with_main_MaxVaule, with_main_TvShow, with_main_bank;
    private TextView with_main_time_one, with_main_time_two, with_main_time_three;
    private Button with_main_submit;
    private LinearLayout with_main_RateCalculation;
    private SeekBar with_main_seekBar;

    private int minVaule = 5;
    private int maxVaule = 50;
    //滑动条的最终数据
    private int seekBarVaule = 0;

    private final int REQUEST_MESSAGE_TAG = 0X3001;
    private final int REQUEST_MESSAGE_TAG_2 = 0X3002;
    private String interestFormula = "0.0";
    private int days = 1;
    private AppLoanModel appLoanModel = new AppLoanModel();

    private Map<String, Purpose> purposeMap = new HashMap<>();
    private List<String> purposeName = new ArrayList<>();
    private int blueColor, grayColor;
    private boolean hasZhimaAuthor;//是否有芝麻授权

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_withdrawalsmain_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        blueColor = getResources().getColor(R.color.appColor);
        grayColor = getResources().getColor(loan_bank_text);
        with_main_money = (TextView) fgView.findViewById(R.id.with_main_money);
        with_main_MinVaule = (TextView) fgView.findViewById(R.id.with_main_MinVaule);
        with_main_MaxVaule = (TextView) fgView.findViewById(R.id.with_main_MaxVaule);
        with_main_TvShow = (TextView) fgView.findViewById(R.id.with_main_TvShow);
        with_main_bank = (TextView) fgView.findViewById(R.id.with_main_bank);

        with_main_time_one = (TextView) fgView.findViewById(R.id.with_main_time_one);
        with_main_time_two = (TextView) fgView.findViewById(R.id.with_main_time_two);
        with_main_time_three = (TextView) fgView.findViewById(R.id.with_main_time_three);

        with_main_RateCalculation = (LinearLayout) fgView.findViewById(R.id.with_main_RateCalculation);
        with_main_submit = (Button) fgView.findViewById(R.id.with_main_submit);
        with_main_seekBar = (SeekBar) fgView.findViewById(R.id.with_main_seekBar);
    }

    @Override
    protected void initEvent() {
        with_main_RateCalculation.setOnClickListener(this);
        with_main_submit.setOnClickListener(this);
        with_main_time_one.setOnClickListener(this);
        with_main_time_two.setOnClickListener(this);
        with_main_time_three.setOnClickListener(this);
        with_main_seekBar.setOnSeekBarChangeListener(this);
        showProgressDailog();
        requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().queryLoanProduct, new HashMap<String, Object>());
        String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, null);
        appLoanModel.setUserId(userId);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();

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
                                with_main_MinVaule.setText(String.valueOf(minVaule));
                            }

                            if (productMap.containsKey("maxLimit") && !productMap.get("maxLimit").equals(null)) {
                                maxVaule = (Integer) productMap.get("maxLimit");
                                with_main_MaxVaule.setText(String.valueOf(maxVaule));
                                with_main_seekBar.setMax(maxVaule / 100 - minVaule / 100);
                                with_main_seekBar.setProgress((maxVaule / 100) / 2);
                            }
                            if (productMap.containsKey("productId") && !productMap.get("productId").equals(null)) {
                                appLoanModel.setProductId(productMap.get("productId").toString());
                            }
                        }
                    }
                }
                Map<String, Object> applyLoanMap = new HashMap<>();
                applyLoanMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, null));
                applyLoanMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, null));
                requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().queryPageData, applyLoanMap);
                break;

            case REQUEST_MESSAGE_TAG_2:
                Map<String, Object> resultContentMap = (Map<String, Object>) result;
                if (resultContentMap.containsKey("data")) {
                    Map<String, Object> dataMap = (Map<String, Object>) resultContentMap.get("data");
                    if (dataMap.containsKey("purpose") && !dataMap.get("purpose").equals(null)) {
                        List<Map<String, Object>> purposes = (List<Map<String, Object>>) dataMap.get("purpose");
                        purposeMap.clear();
                        purposeName.clear();
                        hasZhimaAuthor = true;
                        for (int i = 0; i < purposes.size(); i++) {
                            Map<String, Object> purposeDataMap = purposes.get(i);
                            Purpose purpose = new Purpose();
                            if (purposeDataMap.containsKey("name") && !purposeDataMap.get("name").equals(null)) {
                                purpose.setName(purposeDataMap.get("name").toString());
                            }
                            if (purposeDataMap.containsKey("desc") && !purposeDataMap.get("desc").equals(null)) {
                                purpose.setDesc(purposeDataMap.get("desc").toString());
                                purposeName.add(purposeDataMap.get("desc").toString());
                            }
                            purposeMap.put(purpose.getDesc(), purpose);
                            appLoanModel.setPurposeList(purposeName);
                            appLoanModel.setPurposeMap(purposeMap);
                            if (!dataMap.get("instId").equals(null)) {
                                appLoanModel.setInstId((String) dataMap.get("instId"));
                            } else {
                                appLoanModel.setInstId("");
                            }

                        }
                    }
                    /**
                     * 2 = {HashMap$HashMapEntry@4914} "bankName" -> "建设银行"
                     1 = {HashMap$HashMapEntry@4913} "cardNo" -> "0005"
                     */
                    if (dataMap.containsKey("bankName") && !dataMap.get("bankName").equals(null) &&
                            dataMap.containsKey("cardNo") && !dataMap.get("cardNo").equals(null)) {
                        if (isAdded()) {
                            with_main_bank.setText(getString(R.string.Purpose_message_4) + dataMap.get("bankName") +
                                    getString(R.string.Purpose_message_5) + dataMap.get("cardNo"));
                            appLoanModel.setBankName(dataMap.get("bankName").toString());
                            appLoanModel.setCardNo(dataMap.get("cardNo").toString());
                        }
                    }
                }
                break;
        }
    }


    /**
     * 显示提示框
     */
    private void goZhima(String msg) {
        DialogHelp.getInstance(getActivity()).showDialog("温馨提示", msg, "再等等", "立即授权", new CustomDialog.OnSureInterface() {
            @Override
            public void getOnSure() {
                ServiceBaseActivity.startActivity(getActivity(), ZhimaAuthorFrag.class.getName());
            }

            @Override
            public void getOnDesmiss() {
//                Bundle starBundle = new Bundle();
//                starBundle.putString("url", "http://120.27.49.79/vendor/view/verifySuccess.html");
//                starBundle.putString("title", "芝麻信用授权");
//                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), starBundle);
            }
        });
    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();

//        if (AppConfig.getInstance().REQUEST_ZHIMA_CODE.equals(result)) {
//            goZhima("离拥有资金就差一步了\n是否立即去进行芝麻信用授权");
//        }

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.with_main_RateCalculation:
                Bundle bundle = new Bundle();
                bundle.putInt("minVaule", SharedPreferencesUtil.getInt(getActivity(), "minVaule", 0));
                bundle.putInt("maxVaule", SharedPreferencesUtil.getInt(getActivity(), "maxVaule", 0));
                bundle.putInt("day", SharedPreferencesUtil.getInt(getActivity(), "day", 0));
                bundle.putString("interestFormula", SharedPreferencesUtil.getString(getActivity(), "interestFormula", ""));
                ServiceBaseActivity.startActivity(getActivity(), RateCalculationFragment.class.getName(), bundle);
                break;

            case R.id.with_main_submit:
                if (hasZhimaAuthor) {
                    appLoanModel.setAmount(seekBarVaule + "");
                    Bundle WithBundle = new Bundle();
                    WithBundle.putSerializable("appLoan", appLoanModel);
                    ServiceBaseActivity.startActivity(getActivity(), ImmediateWithdrawalFragment.class.getName(), WithBundle);
                } else {
                    goZhima("离拥有资金就差一步了\n是否立即去进行芝麻信用授权");
                }
                break;

            case R.id.with_main_time_one:
                showChange(R.id.with_main_time_one);
                break;

            case R.id.with_main_time_two:
                showChange(R.id.with_main_time_two);
                break;

            case R.id.with_main_time_three:
                showChange(R.id.with_main_time_three);
                break;
//            case R.id.with_main_time_one:
//            case R.id.with_main_time_two:
//            case R.id.with_main_time_three:
//                CalculateInterest();
//                break;


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
            case R.id.with_main_time_one:
                with_main_time_one.setTextColor(blueColor);
                with_main_time_two.setTextColor(grayColor);
                with_main_time_three.setTextColor(grayColor);
                cycle = (Cycle) with_main_time_one.getTag();
                getRate(cycle);
                break;

            case R.id.with_main_time_two:
                with_main_time_one.setTextColor(grayColor);
                with_main_time_two.setTextColor(blueColor);
                with_main_time_three.setTextColor(grayColor);
                cycle = (Cycle) with_main_time_two.getTag();
                getRate(cycle);
                break;

            case R.id.with_main_time_three:
                with_main_time_one.setTextColor(grayColor);
                with_main_time_two.setTextColor(grayColor);
                with_main_time_three.setTextColor(blueColor);
                cycle = (Cycle) with_main_time_three.getTag();
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
        System.out.println(v1);
        with_main_TvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
        appLoanModel.setCycle(cycle.getValue() + "");

    }


    /**
     * 计算利息
     */
    private void CalculateInterest() {
//        if (with_main_time_one.isChecked()) {
//            if (with_main_time_one.getTag() != null) {
//                Cycle cycle = (Cycle) with_main_time_one.getTag();
//                //本金
//                Money capital = new Money(String.valueOf(seekBarVaule));
//                //利息
//                Money interestDays = capital.multiply(Double.valueOf(interestFormula));
//                days = cycle.getDays();
//                Money amountTotal = interestDays.multiply(days);
//                //本息
//                Money v1 = capital.add(amountTotal);
//                System.out.println(v1);
//                with_main_TvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
//                appLoanModel.setCycle(cycle.getValue() + "");
//            }
//        } else if (with_main_time_two.isChecked()) {
//            if (with_main_time_two.getTag() != null) {
//                Cycle cycle = (Cycle) with_main_time_two.getTag();
//                //本金
//                Money capital = new Money(String.valueOf(seekBarVaule));
//                //利息
//                Money interestDays = capital.multiply(Double.valueOf(interestFormula));
//                days = cycle.getDays();
//                Money amountTotal = interestDays.multiply(days);
//                //本息
//                Money v1 = capital.add(amountTotal);
//                System.out.println(v1);
//                with_main_TvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
//                appLoanModel.setCycle(cycle.getValue() + "");
//            }
//        } else if (with_main_time_three.isChecked()) {
//            if (with_main_time_three.getTag() != null) {
//                Cycle cycle = (Cycle) with_main_time_three.getTag();
//                //本金
//                Money capital = new Money(String.valueOf(seekBarVaule));
//                //利息
//                Money interestDays = capital.multiply(Double.valueOf(interestFormula));
//                days = cycle.getDays();
//                Money amountTotal = interestDays.multiply(days);
//                //本息
//                Money v1 = capital.add(amountTotal);
//                System.out.println(v1);
//                with_main_TvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
//                appLoanModel.setCycle(cycle.getValue() + "");
//            }
//        }
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
                with_main_time_one.setText(cycle.getName());
                with_main_time_one.setTag(cycle);
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
                with_main_time_two.setText(cycle.getName());
                with_main_time_two.setTag(cycle);
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
                with_main_time_three.setText(cycle.getName());
                with_main_time_three.setTag(cycle);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarVaule = (progress + minVaule / 100) * 100;
        with_main_money.setText(seekBarVaule + ".00");
//        CalculateInterest();

        showChange(R.id.with_main_time_one);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
