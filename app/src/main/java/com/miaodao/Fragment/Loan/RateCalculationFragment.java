package com.miaodao.Fragment.Loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.Money;
import com.miaodao.Sys.Utils.NetWorkUtil;
import com.miaodao.Sys.Utils.StringUtils;

/**
 * 费率计算页面
 * Created by Home_Pc on 2017/3/9.
 */

public class RateCalculationFragment extends ContentBaseFragment implements SeekBar.OnSeekBarChangeListener {

    private TextView lation_amount_TvShow, lation_day_TvShow, lation_returnAmount_TvShow;
    private SeekBar lation_amount_SeekBar, lation_amount_Day;
    private WebView lation_amount_webView;

    private int minVaule = 5;
    private int maxVaule = 50;
    //滑动条的最终数据
    private int seekBarAmountVaule = 0;

    private int seekBarDay = 1;

    private int minDay = 1;
    private int maxDay = 1;

    private final int REQUEST_MESSAGE_TAG = 0X1005;
    private String interestFormula = "0.0";

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_ratecalculation_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        lation_amount_TvShow = (TextView) fgView.findViewById(R.id.lation_amount_TvShow);
        lation_day_TvShow = (TextView) fgView.findViewById(R.id.lation_day_TvShow);
        lation_returnAmount_TvShow = (TextView) fgView.findViewById(R.id.lation_returnAmount_TvShow);

        lation_amount_SeekBar = (SeekBar) fgView.findViewById(R.id.lation_amount_SeekBar);
        lation_amount_Day = (SeekBar) fgView.findViewById(R.id.lation_amount_Day);

        lation_amount_webView = (WebView) fgView.findViewById(R.id.lation_amount_webView);

        WebSettings webSettings = lation_amount_webView.getSettings();
        /**
         * 禁止缩放
         */
        webSettings.setSupportZoom(false);
        /**
         * 适应手机屏幕
         */
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        lation_amount_webView.loadUrl(AppConfig.getInstance().lation_amount_url);

        lation_amount_SeekBar.setOnSeekBarChangeListener(this);
        lation_amount_Day.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void initEvent() {
        Bundle bundle = getArguments();
        if (bundle.containsKey("minVaule")) {
            minVaule = bundle.getInt("minVaule");
//            lation_amount_TvShow.setText((minVaule + "") == null ? "" : (minVaule + "元"));
        }

        if (bundle.containsKey("maxVaule")) {
            maxVaule = bundle.getInt("maxVaule");
//            lation_returnAmount_TvShow.setText((maxVaule + "") == null ? "" : (maxVaule + "元"));
            lation_amount_SeekBar.setMax(maxVaule / 100 - minVaule / 100);
        }

        if (bundle.containsKey("day")) {
            maxDay = bundle.getInt("day");
//            lation_day_TvShow.setText((maxDay + "") == null ? "" : (maxDay + "天"));
            lation_amount_Day.setMax(maxDay - minDay);
        }

        if (bundle.containsKey("interestFormula") && !StringUtils.isBlank(bundle.getString("interestFormula"))) {
            interestFormula = bundle.getString("interestFormula");
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.lation_amount_SeekBar:
                seekBarAmountVaule = (progress + minVaule / 100) * 100;
                lation_amount_TvShow.setText(seekBarAmountVaule + "元");
                CalculateInterest();
                break;
            case R.id.lation_amount_Day:
                maxDay = progress + seekBarDay;
                lation_day_TvShow.setText(progress + minDay + "天");
                CalculateInterest();
                break;
        }
    }

    /**
     * 计算利息
     */
    private void CalculateInterest() {
        //本金
        Money capital = new Money(String.valueOf(seekBarAmountVaule));
        //利息
        Money interestDays = capital.multiply(Double.valueOf(interestFormula));
        //30天
        Money amountTotal = interestDays.multiply(maxDay);
        //本息
        Money v1 = capital.add(amountTotal);
        lation_returnAmount_TvShow.setText(v1.getAmount().setScale(1).toString() + getResources().getString(R.string.money_Company));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
}
