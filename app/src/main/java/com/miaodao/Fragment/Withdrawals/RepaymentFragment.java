package com.miaodao.Fragment.Withdrawals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.miaodao.Activity.CouponActivity;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Free.FreeMainFragment;
import com.miaodao.Fragment.Loan.RateCalculationFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Coupon;
import com.miaodao.Sys.Model.Repay;
import com.miaodao.Sys.Model.RepayType;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MapUtils;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Utils.alipay.PayResult;
import com.miaodao.Sys.Widgets.RepayTypePop;
import com.miaodao.Sys.Widgets.ShowShareDialog;
import com.miaodao.Sys.Widgets.UiContentView.UiContentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 立即还款
 * Created by Home_Pc on 2017/3/21.
 */

public class RepaymentFragment extends BaseFragment implements View.OnClickListener, ShowShareDialog.IShare, RepayTypePop.IActionListener {

    private static final int SDK_PAY_FLAG = 1;

    private final int REQUEST_PAY_CHANNEL = 0x001;
    private final int REQUEST_PAY_LOAN = 0x002;
    private final int REQUEST_PAY = 0x003;
    private final int REQUEST_ORDER_STATUS = 0x004;
    private final int GET_COUPON = 0x005;

    private TextView repaymentMoney;//贷款金额
    private TextView tvTip;//提示信息
    private TextView tvCouponShow;//显示免息券信息
    private UiContentView tvMoney;//贷款金额
    private UiContentView tvCost;//贷款手续费
    private UiContentView tvRepayDate;//到期还款日
    private UiContentView tvRepayLeftDate;//剩余还款日
    private RelativeLayout useCoupon;//使用优惠券
    private Button btnRate;//费率计算
    private Button btnRepay;//还款
    private Bundle bundle;
    private Repay repay;
    private ShowShareDialog showShareDialog;
    private List<RepayType> repayTypeList;//支付渠道
    private RepayTypePop repayTypePop;//支付popWindow
    private Map<String, Object> dataMap;//主要用于保存支付订单信息
    private Map<String, Object> repayInfo;
    private String payChannelId = "";//支付渠道ID
    private String decisionId = "";//支付渠道ID
    private RepayType repayType;//支付方式
    private int getOrderNum = 0;

    private MyHandler myHandler;
    private int couponNumber;
    private Object couponId = "";//抵扣卷ID
    private Coupon coupon;
    private Context mContext;


    private static class MyHandler extends Handler {

        private WeakReference<RepaymentFragment> repaymentFragment;

        public MyHandler(RepaymentFragment repaymentFragment) {
            this.repaymentFragment = new WeakReference<RepaymentFragment>(repaymentFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SDK_PAY_FLAG:
                    //轮询后台结果
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        repaymentFragment.get().startGetOrderStatus();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.shortShow("支付失败");
                    }

                    break;

                default:
                    break;

            }


        }
    }


    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_repayment_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        EventBus.getDefault().register(this);
        repaymentMoney = (TextView) fgView.findViewById(R.id.repayment_money);
        tvTip = (TextView) fgView.findViewById(R.id.tv_tip);
        tvCouponShow = (TextView) fgView.findViewById(R.id.tv_coupon_show);
        tvMoney = (UiContentView) fgView.findViewById(R.id.tv_money);
        tvCost = (UiContentView) fgView.findViewById(R.id.tv_cost);
        tvRepayDate = (UiContentView) fgView.findViewById(R.id.tv_repay_date);
        tvRepayLeftDate = (UiContentView) fgView.findViewById(R.id.tv_repay_left_date);
        useCoupon = (RelativeLayout) fgView.findViewById(R.id.use_coupon);
        btnRate = (Button) fgView.findViewById(R.id.btn_rate);
        btnRepay = (Button) fgView.findViewById(R.id.btn_repay);

        bundle = getArguments();
        if (bundle == null) return;
        repay = bundle.getParcelable("content");
        coupon = bundle.getParcelable("coupon");
        if (repay == null) return;
        showInfo();
        showShareDialog = new ShowShareDialog(getActivity());
        showShareDialog.setListener(this);
        myHandler = new MyHandler(this);

        getCoupon();
    }


    @Override
    protected void initEvent() {
        btnRate.setOnClickListener(this);
        btnRepay.setOnClickListener(this);
        useCoupon.setOnClickListener(this);
    }

    /**
     * 获取免息券数量
     */
    private void getCoupon() {
        Map<String, Object> coupon = new HashMap<>();
        coupon.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        coupon.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        requestForHttp(GET_COUPON, AppConfig.getInstance().GET_COUPON_NUMBER, coupon);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * 显示信息
     */
    private void showInfo() {
        repaymentMoney.setText("￥" + repay.getUnRepayedAmount());
        tvMoney.setContentVaule(repay.getAmount() + "元");
        tvCost.setContentVaule(" " + repay.getIntegrateFee() + "元");
        tvRepayDate.setContentVaule(repay.getRepayDate());

        if (repay.getOverdueDays() == 0) {
            tvRepayLeftDate.setTitleVaule(R.string.repayment_message_4);
            tvRepayLeftDate.setContentVaule(Html.fromHtml("还有" + "<font color='#3098fd'>" + repay.getRepayDays() + "</font>天"));
        } else {
            tvRepayLeftDate.setTitleVaule(R.string.repayment_message_7);
            tvRepayLeftDate.setContentVaule(Html.fromHtml("逾期" + "<font color='#3098fd'>" + repay.getOverdueDays() + "</font>天"));
        }
        tvTip.setText("请将应还金额存入您的" + repay.getBankName() + "(尾号" + repay.getCardIdxNo().replaceAll("\\*", "").replaceAll(" ", "") + ")然后点击立即还款");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_rate:
                Bundle bundle = new Bundle();
                bundle.putInt("minVaule", SharedPreferencesUtil.getInt(getActivity(), "minVaule", 0));
                bundle.putInt("maxVaule", SharedPreferencesUtil.getInt(getActivity(), "maxVaule", 0));
                bundle.putInt("day", SharedPreferencesUtil.getInt(getActivity(), "day", 0));
                bundle.putString("interestFormula", SharedPreferencesUtil.getString(getActivity(), "interestFormula", ""));
                ServiceBaseActivity.startActivity(getActivity(), RateCalculationFragment.class.getName(), bundle);
                break;

            case R.id.btn_repay:
                //1.点击立即还款，请求服务器获取支付渠道信息；popwindow展示
                //2.如果银行卡 是否需要输入密码
                //3.如果微信、支付宝 点击后popWindow消失 请求服务器获取订单信息，成功回调后调用sdk
                //4.微信支付宝需要轮询结果

//                getPayChannel();
                //请求支付信息
                requestRepayLoan();
                break;

            case R.id.use_coupon:
                if (tvCouponShow.getText().toString().equals("立即邀请")) {
                    showShareDialog.show();
                } else {
                    Intent intent = new Intent(getActivity(), CouponActivity.class);
                    intent.putExtra("useCoupon", "show");
                    intent.putExtra("transId", repay.getTransId());
                    intent.putExtra("productId", repay.getProductId());
                    startActivity(intent);
                }
                break;

            default:
                break;
        }

    }


    /**
     * 获取支付渠道
     */
    private void getPayChannel() {

        Map<String, Object> payChannel = new HashMap<>();
        payChannel.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        payChannel.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        requestForHttp(REQUEST_PAY_CHANNEL, AppConfig.getInstance().PAY_CHANNEL, payChannel);
    }


    /**
     * 显示请求的支付渠道
     *
     * @param channels
     */
    private void showPayType(List<Map<String, Object>> channels) {

        repayTypeList = new ArrayList<>();
        for (Map<String, Object> b : channels) {
            try {
                RepayType repayType = MapUtils.mapToBean(b, RepayType.class);
                repayTypeList.add(repayType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (repayTypeList.isEmpty()) return;
        repayTypePop.setListener(this);
        if (repayTypeList.size() == 1) {
            repayTypeList.get(0).setIsChoose(1);
            payChannelId = repayTypeList.get(0).getChannelId();
            decisionId = repayTypeList.get(0).getDecisionId();
        }
        repayTypePop.setRepayTypeList(repayTypeList);
        repayTypePop.showAtLocation(btnRepay, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @Override
    public void share() {
        Bundle bundle = new Bundle();
        bundle.putString("showTitle", "showTitle");
        ServiceBaseActivity.startActivity(getActivity(), FreeMainFragment.class.getName(), bundle);
    }

    @Override
    public void cancelPay() {
        payChannelId = "";
    }

    @Override
    public void payNow(ImageView iv, ProgressBar bar, View clickView) {
        getOrderNum = 0;
        if (repay == null) return;
        if (TextUtils.isEmpty(payChannelId)) {
            ToastUtils.shortShow("请选择支付的方式");
            return;
        }

        clickView.setEnabled(false);
        iv.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        Map<String, Object> pay = new HashMap<>();
        pay.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        pay.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        pay.put("transId", repayInfo.get("transId"));
        pay.put("refTransId", repayInfo.get("refTransId"));
        pay.put("merchantId", repayInfo.get("merchantId"));
        pay.put("amount", repayInfo.get("amount"));
        pay.put("discount", repayInfo.get("discount"));
        pay.put("realityAmount", repayInfo.get("realityAmount"));
        pay.put("productId", repayInfo.get("productId"));
        pay.put("payChannelId", payChannelId);
        pay.put("decisionId", decisionId);
        pay.put("secSignature", repayInfo.get("secSignature"));
        pay.put("instId", repayType.getInstId());
        pay.put("cardNo", repayType.getCardNo());
        pay.put("couponId", couponId);
        requestForHttp(REQUEST_PAY, AppConfig.getInstance().PAY_REPAY, pay);
    }

    @Override
    public void payItem(int position) {
        repayType = repayTypeList.get(position);

        if (repayType.getValid() == false) {
            ToastUtils.shortShow("暂不支持");
            return;
        }

        if (repayType.getValid() == false) return;
        for (RepayType r : repayTypeList) {
            r.setIsChoose(0);
        }
        repayType.setIsChoose(1);
        payChannelId = repayType.getChannelId();
        decisionId = repayType.getDecisionId();
        repayTypePop.setRepayTypeList(repayTypeList);
    }

    /**
     * 请求支付订单信息
     */
    private void requestRepayLoan() {
        if (repay == null) return;
        if ("1".equals(repay.getExtFields().getRepayOrderState())) {
            ToastUtils.shortShow("您尚有一笔订单处于在处理状态");
            return;
        }

        showProgressDailog();
        Map<String, Object> payLoan = new HashMap<>();
        payLoan.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        payLoan.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        payLoan.put("transId", repay.getTransId());
        payLoan.put("couponId", couponId);
        requestForHttp(REQUEST_PAY_LOAN, AppConfig.getInstance().PAY_LOAN_INFO, payLoan);
    }


    /**
     * 轮询订单结果
     */
    private void startGetOrderStatus() {
        getOrderNum++;
        if (getOrderNum < 3) {
            getOrderSta();
        } else {
            // TODO: 2017/4/18 跳转到支付订单列表
        }
    }


    /**
     * 获取订单信息
     */
    private void getOrderSta() {
        showProgressDailog();
        Map<String, Object> orderSta = new HashMap<>();
        orderSta.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        orderSta.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        orderSta.put("outTransId", dataMap.get("outTransId"));
        requestForHttp(REQUEST_ORDER_STATUS, AppConfig.getInstance().QUERY_PAY, orderSta);
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {


        Map<String, Object> resultMap = (Map<String, Object>) result;
        if (StringUtils.isResponseNull(resultMap)) return;
        dataMap = (Map<String, Object>) resultMap.get("data");
        if (StringUtils.isResponseNull(dataMap)) return;
        switch (TAG) {
            //返回支付渠道
            case REQUEST_PAY_CHANNEL:
                dismissProressDialog();
                List<Map<String, Object>> channels = (List<Map<String, Object>>) dataMap.get("channels");
                showPayType(channels);
                //请求支付信息
//                requestRepayLoan();
                break;

            //返回支付信息
            case REQUEST_PAY_LOAN:
                repayTypePop = new RepayTypePop(mContext);
                repayInfo = dataMap;
                repayTypePop.setRepayMoney("￥" + repayInfo.get("realityAmount") + "元");
//                (总额:500元,已抵扣5元)
                repayTypePop.setDetail(Html.fromHtml("(总金额:" + repayInfo.get("amount") + "元," + "<font color='#3098fd'>已抵扣" + dataMap.get("discount") + "元</font>)"));
                getPayChannel();
                break;

            //开始支付
            //银行卡直接就支付了，并返回了支付结果
            case REQUEST_PAY:
                showPayNow(dataMap);
                break;

            //轮询支付结果
            case REQUEST_ORDER_STATUS:
                getOrderNum = 0;
                dismissProressDialog();
                Bundle bundle = new Bundle();
                bundle.putString("state", (String) dataMap.get("state"));
                bundle.putString("amount", (String) dataMap.get("amount"));
                ServiceBaseActivity.startActivity(getActivity(), RepayResultFrag.class.getName(), bundle);
                break;

            //获取优惠券
            case GET_COUPON:
                couponNumber = (int) dataMap.get("count");
                if (couponNumber == 0) {
                    tvCouponShow.setText("立即邀请");
                } else {
                    tvCouponShow.setText("已有" + couponNumber + "张券可使用");
                }

                showCouponInfo(coupon);

                break;

            default:
                break;
        }
    }


    /**
     * 根据渠道ID选择支付方式
     */
    private void showPayNow(Map<String, Object> dataMap) {

        //dimiss popwindow
        repayTypePop.backgroundAlpha(1);
        repayTypePop.dismiss();


        //配置沙箱模式
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        //支付宝支付
        if (AppConfig.getInstance().ALIPAY_PAY.equals(payChannelId)) {
            doAlipay();
        } else if (AppConfig.getInstance().WECHAT_PAY.equals(payChannelId)) {

        } else if (AppConfig.getInstance().BANK_CARD_PAY.equals(payChannelId)) {
            Bundle bundle = new Bundle();
            //如果没有返回状态信息,跳转处理中
            if (!dataMap.containsKey("state")) {
                bundle.putString("state", "P");
                bundle.putString("amount", (String) dataMap.get("amount"));
                ServiceBaseActivity.startActivity(getActivity(), RepayResultFrag.class.getName(), bundle);
            }
            //如果是P处理状态的话，就轮询，其他的状态直接显示
            if ("P".equalsIgnoreCase((String) dataMap.get("state"))) {
                //直接轮询结果
                startGetOrderStatus();
            } else {
                bundle.putString("state", (String) dataMap.get("state"));
                bundle.putString("amount", (String) dataMap.get("amount"));
                ServiceBaseActivity.startActivity(getActivity(), RepayResultFrag.class.getName(), bundle);
            }
        }
    }


    /**
     * 唤起支付宝支付
     */
    private void doAlipay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2((String) dataMap.get("form"), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


//    /**
//     * 银行卡还款
//     */
//    private void doBackPay(){
//
//        Map<String, Object> repayMap = new HashMap<>();
//
//        repayMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
//        repayMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
//        repayMap.put("transId", repay.getTransId());
//        repayMap.put("amount", dataMap.get("amount"));
//        repayMap.put("amount", dataMap.get("amount"));
//
//    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);

        //网络不好的情况下再再次发送请求
        if (WheatFinanceApplication.getInstance().getResources().getString(R.string.newwork_timeout).equals(result)) {
            if (REQUEST_ORDER_STATUS == TAG) {
                startGetOrderStatus();
            }
        }

//        if (REQUEST_PAY == TAG || REQUEST_PAY_LOAN == TAG) {
//            repayTypePop.backgroundAlpha(1);
//            repayTypePop.dismiss();
//        }

        if (REQUEST_PAY == TAG) {
            repayTypePop.backgroundAlpha(1);
            repayTypePop.dismiss();
        }


    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();

        if (REQUEST_PAY == TAG) {
            repayTypePop.backgroundAlpha(1);
            repayTypePop.dismiss();
        }
    }

    /**
     * 显示优惠券信息
     *
     * @param event
     */
    public void showCouponInfo(Coupon event) {
        if (event == null) return;
        if (TextUtils.isEmpty(event.getId())) {
            couponId = "";
            tvCouponShow.setText("已有" + couponNumber + "张券可使用");
            return;
        }
        String couponType = event.getCouponType();
        if ("001".equals(couponType)) {
            tvCouponShow.setText(Html.fromHtml("<font color='#3098fd'>已选中" + event.getValue() + "元抵扣券</font>"));
        } else {
            tvCouponShow.setText(Html.fromHtml("<font color='#3098fd'>已选中" + event.getValue() + "天免息券</font>"));
        }
        couponId = event.getId();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String msg) {
        if ("repay".equals(msg)) {
            requestRepayLoan();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
