package com.miaodao.Fragment.Loan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Free.FreeMainFragment;
import com.miaodao.Fragment.Loan.Apply.LoanFailFragment;
import com.miaodao.Fragment.Loan.bean.WaitPay;
import com.miaodao.Fragment.WebViewFragment;
import com.miaodao.Fragment.Withdrawals.PendingAuditFragment;
import com.miaodao.Fragment.Withdrawals.PerfectInfoFragment;
import com.miaodao.Fragment.Withdrawals.QuestionnaireMainFragment;
import com.miaodao.Fragment.Withdrawals.RepaymentFragment;
import com.miaodao.Fragment.Withdrawals.WaitRepayFrag;
import com.miaodao.Fragment.Withdrawals.WithdrawalMainFragment;
import com.miaodao.Fragment.Withdrawals.WithdrawalsOperationFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Coupon;
import com.miaodao.Sys.Model.LoanStatusContent;
import com.miaodao.Sys.Model.PictureURL;
import com.miaodao.Sys.Model.Repay;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MapUtils;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Widgets.VerticalTextView;
import com.miaodao.Sys.Widgets.slidview.FlowIndicator;
import com.miaodao.Sys.Widgets.slidview.SlideShowView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 贷款
 * Created by Home_Pc on 2017/3/8.
 */

public class LoanMainFragment extends BaseFragment implements View.OnClickListener, SlideShowView.IOnRedBagClickListener {

    private final int REQUEST_MESSAGE_TAG = 0X1002;
    private final int REQUEST_BANNER_TAG = 0X1003;
    private final int REQUEST_ROLL_TAG = 0X1004;
    private VerticalTextView tvLoanAd;
    private SlideShowView showImage;
    private FlowIndicator indicator;
    private ArrayList<PictureURL> images = new ArrayList<PictureURL>();
    //    private ArrayList<Integer> images = new ArrayList<Integer>();
    private ArrayList<String> ad = new ArrayList<String>();
    private LinearLayout llNetError;//网络连接错误页面
    private ImageView errorImgShow;//点击重新加载
    private LinearLayout llSysMaintain;//显示系统设计维护图片
    private FrameLayout loanContentLayout;
    private LinearLayout llScrollTextLoan;

    //还款失败
    private LinearLayout llFailTip;
    private TextView tvShowFailDetail;
    public String stateCode = "";
    private Coupon coupon;
    private ArrayList<WaitPay> waitPays = new ArrayList<>();

    private static LoanMainFragment loanMainFragment = new LoanMainFragment();

    public static LoanMainFragment getInstance() {
        return loanMainFragment;
    }


    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_loan_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        EventBus.getDefault().register(this);
        sendMessage(AppConfig.getInstance().LOAN_MESSAGE_1, View.GONE);
        tvLoanAd = (VerticalTextView) fgView.findViewById(R.id.tv_loan_ad);
        showImage = (SlideShowView) fgView.findViewById(R.id.show_image);
        indicator = (FlowIndicator) fgView.findViewById(R.id.indicator);
        llNetError = (LinearLayout) fgView.findViewById(R.id.ll_net_error);
        llSysMaintain = (LinearLayout) fgView.findViewById(R.id.ll_sys_maintain);
        errorImgShow = (ImageView) fgView.findViewById(R.id.error_imgShow);
        loanContentLayout = (FrameLayout) fgView.findViewById(R.id.Loan_ContentLayout);
        llScrollTextLoan = (LinearLayout) fgView.findViewById(R.id.ll_scroll_text_loan);
        llFailTip = (LinearLayout) fgView.findViewById(R.id.ll_fail_tip);
        tvShowFailDetail = (TextView) fgView.findViewById(R.id.tv_show_fail_detail);

        showImage.setRedBagClickListener(this);
        tvLoanAd.setTextList(ad);
        tvLoanAd.setText(14, 5, getResources().getColor(R.color.loan_bank_text));//设置属性
        tvLoanAd.setTextStillTime(3000);//设置停留时长间隔
        tvLoanAd.setAnimTime(800);//设置进入和退出的时间间隔

        getHomeStatus();

        //请求首页banner图
        getBanner();

        //请求首页轮播文字
        getRollText();
    }


    /**
     * 获取首页轮播图
     */
    private void getBanner() {
        OkHttpUtils.get().getCode(false).url(AppConfig.getInstance().GET_BANNER)
                .build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response))
                    return;

                images = (ArrayList<PictureURL>) JSON.parseArray(response, PictureURL.class);
                if (images == null || images.isEmpty()) return;
                showImage.initData(indicator, images);
                showImage.setCurrentItem(images.size() * 50);
                showImage.setAutoPlay(true);
                showImage.playLoop();
            }
        });


    }


    /**
     * 获取首页轮播文字
     */
    private void getRollText() {
        Map<String, Object> rollStr = new HashMap<>();
        rollStr.put("msgType", "H");
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().GET_ROLL_STR))
                .content(StringUtils.toJsonString(rollStr)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {

                String tr = response;

                if (TextUtils.isEmpty(response))
                    return;
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.optString("data");
                    if (TextUtils.isEmpty(data))
                        return;
                    JSONObject obj = new JSONObject(data);
                    ArrayList<String> datas = (ArrayList<String>) JSON.parseArray(obj.optString("scrollbarMsg"), String.class);
                    llScrollTextLoan.setVisibility(View.VISIBLE);
                    tvLoanAd.setTextList(datas);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (showImage != null) {
            showImage.playLoop();
        }
        tvLoanAd.startAutoScroll();
    }


    /**
     * 查询处理中的订单
     */
    private void getPendingTrade(String transId) {
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        statusMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        statusMap.put("refTransId", transId);
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().GET_PENDING_TRADE)).content(StringUtils.toJsonString(statusMap)).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(String msg, Call call, Exception e, int id) {
//                        if (!TextUtils.isEmpty(msg)) {
//                            ToastUtils.shortShow(msg);
//                        }
                        llFailTip.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String st = response;
                        if (TextUtils.isEmpty(response))
                            return;
                        try {
                            JSONObject object = new JSONObject(response);
                            String data = object.optString("data");
                            if (TextUtils.isEmpty(data)) {
                                llFailTip.setVisibility(View.GONE);
                                return;
                            }
                            JSONObject obj = new JSONObject(data);
                            String orderList = obj.optString("orderList");
//                            if (orderList.equals("null")) {
//                                llFailTip.setVisibility(View.GONE);
//                                return;
//                            }
                            waitPays = (ArrayList<WaitPay>) JSON.parseArray(orderList, WaitPay.class);
                            if (waitPays == null || waitPays.isEmpty()) {
                                llFailTip.setVisibility(View.GONE);
                                return;
                            }
                            showRepayFail();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }


    @Override
    public void onPause() {
        super.onPause();
        if (showImage != null) {
            showImage.stopPlay();
        }
        tvLoanAd.stopAutoScroll();
    }

    /**
     * 判断首页状态
     */
    private void getHomeStatus() {
        showProgressDailog();
        llFailTip.setVisibility(View.GONE);
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        statusMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().homeInitState, statusMap);
    }

    @Override
    protected void initEvent() {
        observeMessage(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
        errorImgShow.setOnClickListener(this);
        tvShowFailDetail.setOnClickListener(this);
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        llNetError.setVisibility(View.GONE);
        llSysMaintain.setVisibility(View.GONE);
        loanContentLayout.setVisibility(View.VISIBLE);

        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                Map<String, Object> resultMap = (Map<String, Object>) result;

                if (resultMap.containsKey("data")) {
                    Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                    /**
                     * NoLogin	100100	未登录    登陆后 跳转产品页面
                     * NoAuth	100200	未认证    跳转产品页面走认证
                     * AuthFail	100201	认证未通过    完善资料 先跳到未认证的页面，然后跳转到汇总
                     * AuthAudit	100202	认证审核中 首页显示正在审核
                     * NoSubmit	100203	认证资料未提交
                     * NoQuestionnaire	100300	未填写调查问卷
                     * NoBorrow	100400	未借款 跳转产品页面
                     * NoRefund	100401	待还款 待还款页面
                     */
                    if (!dataMap.equals(null) && dataMap.containsKey("stateCode")) {
                        if (!dataMap.get("stateCode").equals(null) && !StringUtils.isBlank((String) dataMap.get("stateCode"))) {
                            stateCode = (String) dataMap.get("stateCode");
                            switch (stateCode) {
                                case "100100": //跳转登陆页面
                                    AppConfig.getInstance().clearUserData();
                                    LoanMainHomeFragment loanFragment = new LoanMainHomeFragment();
                                    pushChildFragment(R.id.Loan_ContentLayout, loanFragment, null, false);
//                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    break;

                                case "100201"://认证未通过    完善资料 先跳到未认证的页面，然后跳转到汇总 --审核不通过
                                case "100203"://认证资料未提交
                                    AppConfig.getInstance().PerfectInfoMap = dataMap;
                                    pushChildFragment(R.id.Loan_ContentLayout, new PerfectInfoFragment(), null, false);
                                    break;

                                case "100202"://首页显示正在审核，即资料已经提交，请等待审核
                                    PendingAuditFragment pendingAuditFragment = new PendingAuditFragment();
                                    pushChildFragment(R.id.Loan_ContentLayout, pendingAuditFragment, null, false);
                                    break;

                                case "100300":// 填写调查问卷
                                    QuestionnaireMainFragment questionnaireMainFragment = new QuestionnaireMainFragment();
                                    pushChildFragment(R.id.Loan_ContentLayout, questionnaireMainFragment, null, false);
                                    break;

                                case "100401"://待还款 待还款页面
                                    if (!dataMap.get("content").equals(null)) {
                                        showRepay((Map<String, Object>) dataMap.get("content"));
                                    }
                                    break;

                                case "100400"://未借款 跳转产品页面
                                    WithdrawalMainFragment withdrawalMainFragment = new WithdrawalMainFragment();
                                    pushChildFragment(R.id.Loan_ContentLayout, withdrawalMainFragment, null, false);
                                    break;

                                case "100410"://提现中
                                    //显示提现中状态
                                    if (dataMap.containsKey("content") && !dataMap.get("content").equals(null)) {
                                        showLoanWait((Map<String, Object>) dataMap.get("content"), "100410");
                                    }
                                    break;

                                case "100411"://已到账
                                    if (dataMap.containsKey("content") && !dataMap.get("content").equals(null)) {
                                        showLoanWait((Map<String, Object>) dataMap.get("content"), "100411");
                                    }
                                    break;

                                case "100412"://贷款失败
                                    if (dataMap.containsKey("content") && !dataMap.get("content").equals(null)) {
                                        Map<String, Object> content = (Map<String, Object>) dataMap.get("content");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("transId", (String) content.get("transId"));
                                        pushChildFragment(R.id.Loan_ContentLayout, new LoanFailFragment(), bundle, false);
                                    }
                                    break;

                                case "100200"://未认证    跳转产品页面走认证
                                default:
                                    LoanMainHomeFragment loanMainHomeFragment = new LoanMainHomeFragment();
                                    pushChildFragment(R.id.Loan_ContentLayout, loanMainHomeFragment, null, false);
                                    break;
                            }
                        } else {
                            llNetError.setVisibility(View.VISIBLE);
                            llSysMaintain.setVisibility(View.GONE);
                            loanContentLayout.setVisibility(View.GONE);
                        }
                    }
                }
                break;

            default:
                break;


        }
    }


    /**
     * 显示提现中状态
     */
    private void showLoanWait(Map<String, Object> content, String status) {

        Bundle bundle = new Bundle();
        try {
            LoanStatusContent loanStatusContent = MapUtils.mapToBean(content, LoanStatusContent.class);
            bundle.putParcelable("content", loanStatusContent);
            bundle.putString("status", status);
            pushChildFragment(R.id.Loan_ContentLayout, new WithdrawalsOperationFragment(), bundle, false);
        } catch (Exception e) {
        }
    }


    /**
     * 显示待还款
     *
     * @param content
     */
    private void showRepay(Map<String, Object> content) {
        Bundle bundle = new Bundle();
        try {

            String str = JSON.toJSONString(content);
            Repay repay = JSON.parseObject(str, Repay.class);

//            Repay repay = MapUtils.mapToBean(content, Repay.class);
            getPendingTrade(repay.getTransId());
            bundle.putParcelable("content", repay);
            bundle.putParcelable("coupon", coupon);
            RepaymentFragment repaymentFragment = new RepaymentFragment();
            pushChildFragment(R.id.Loan_ContentLayout, repaymentFragment, bundle, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showRepayFail() {
        tvShowFailDetail.setText(Html.fromHtml("<u>" + "点击查看详情" + "</u>"));
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(llFailTip, "translationY", -30.0f, 0.0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(llFailTip, "alpha", 0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveIn).with(fadeIn);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
                llFailTip.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();
    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        if (WheatFinanceApplication.getInstance().getString(R.string.newwork_timeout).equals(result) && TAG == REQUEST_MESSAGE_TAG
                || (TAG == REQUEST_MESSAGE_TAG && TextUtils.isEmpty(result))) {
            llNetError.setVisibility(View.VISIBLE);
            llSysMaintain.setVisibility(View.GONE);
            loanContentLayout.setVisibility(View.GONE);
        }

        if (AppConfig.getInstance().REQUEST_SYS_MAINTAIN_CODE.equals(result) && TAG == REQUEST_MESSAGE_TAG) {
            llNetError.setVisibility(View.GONE);
            llSysMaintain.setVisibility(View.VISIBLE);
            loanContentLayout.setVisibility(View.GONE);
        }

        if (TAG == REQUEST_ROLL_TAG) {
            llScrollTextLoan.setVisibility(View.GONE);
        }


    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        if (TAG == REQUEST_MESSAGE_TAG) {
            llNetError.setVisibility(View.VISIBLE);
            llSysMaintain.setVisibility(View.GONE);
            loanContentLayout.setVisibility(View.GONE);
        }

        if (TAG == REQUEST_ROLL_TAG) {
            llScrollTextLoan.setVisibility(View.GONE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String event) {
        if (AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1.equals(event)) {
            getHomeStatus();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Coupon event) {
        coupon = event;
        getHomeStatus();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_fail_detail:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("waitPays", waitPays);
                ServiceBaseActivity.startActivity(getActivity(), WaitRepayFrag.class.getName(), bundle);
                break;

            case R.id.error_imgShow:
                llNetError.setVisibility(View.GONE);
                llSysMaintain.setVisibility(View.GONE);
                getHomeStatus();
                break;

            default:
                break;
        }
    }


    @Override
    public void gotoQuestionPage(String linkUrl, PictureURL pictureURL) {
        Bundle bundle = new Bundle();
        if (AppConfig.getInstance().ACCOUNT_ACTIVITY_URL.contains(linkUrl)) {
            bundle.putString("showTitle", "showTitle");
            bundle.putString("title", pictureURL.getTitle());
            ServiceBaseActivity.startActivity(getActivity(), FreeMainFragment.class.getName(), bundle);
        } else {
            bundle.putString("url", AppConfig.getInstance().DATABASE_URL + linkUrl);
            bundle.putString("title", pictureURL.getTitle());
            ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), bundle);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
