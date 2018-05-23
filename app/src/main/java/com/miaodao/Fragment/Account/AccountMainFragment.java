package com.miaodao.Fragment.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Activity.CouponActivity;
import com.miaodao.Activity.LoginActivity;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Account.Car.AccountCarFragment;
import com.miaodao.Fragment.Free.FreeMainFragment;
import com.miaodao.Fragment.WebViewFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.RoundImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户首页
 * Created by Home_Pc on 2017/3/9.
 */

public class AccountMainFragment extends BaseFragment implements View.OnClickListener {

    private RoundImageView account_head;
    private TextView account_Tel, invitedCode, showLogin, tvCouponNumber;
    private RelativeLayout UserInfo_Laytou, account_car, account_bill, account_share, account_ticket, account_star, account_help, account_safe, account_more;
    private Button account_loginOut, account_sign;

    private final int REQUEST_MESSAGE_TAG_1 = 0X2001;
    private final int REQUEST_MESSAGE_TAG_2 = 0X2002;
    private final int GET_COUPON = 0X2003;
    private Context mContext;


    private static AccountMainFragment accountMainFragment = new AccountMainFragment();


    public static AccountMainFragment getInstance(){
        return accountMainFragment;
    }



    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_accountmain_layout, null);
    }


    @Override
    protected void initWidgets(View fgView) {

        account_head = (RoundImageView) fgView.findViewById(R.id.account_head);
        account_Tel = (TextView) fgView.findViewById(R.id.account_Tel);
        invitedCode = (TextView) fgView.findViewById(R.id.tv_invited_code);
        showLogin = (TextView) fgView.findViewById(R.id.tv_show_login);
        tvCouponNumber = (TextView) fgView.findViewById(R.id.tv_coupon_number);

        account_car = (RelativeLayout) fgView.findViewById(R.id.account_car);
        account_bill = (RelativeLayout) fgView.findViewById(R.id.account_bill);
        account_share = (RelativeLayout) fgView.findViewById(R.id.account_share);
        account_ticket = (RelativeLayout) fgView.findViewById(R.id.account_ticket);
        account_help = (RelativeLayout) fgView.findViewById(R.id.account_help);
        account_star = (RelativeLayout) fgView.findViewById(R.id.account_star);
        account_safe = (RelativeLayout) fgView.findViewById(R.id.account_safe);
        account_more = (RelativeLayout) fgView.findViewById(R.id.account_more);

        UserInfo_Laytou = (RelativeLayout) fgView.findViewById(R.id.UserInfo_Laytou);
        account_loginOut = (Button) fgView.findViewById(R.id.account_loginOut);
        account_sign = (Button) fgView.findViewById(R.id.account_sign);
//        invitedCode.setText(Html.fromHtml("您的邀请码:" + "<font color = '#ff9d02'>" +SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_INVITEDCODE, "")) + "</font>");
//        invitedCode.setText(Html.fromHtml("您的邀请码:<font color='#ff9d02'>" + SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_INVITEDCODE, "") + "</font>"));

        account_car.setOnClickListener(this);
        account_bill.setOnClickListener(this);
        account_share.setOnClickListener(this);
        account_ticket.setOnClickListener(this);
        account_help.setOnClickListener(this);
        account_star.setOnClickListener(this);
        account_safe.setOnClickListener(this);
        account_more.setOnClickListener(this);
        UserInfo_Laytou.setOnClickListener(this);
        account_loginOut.setOnClickListener(this);
        account_sign.setOnClickListener(this);

    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppConfig.getInstance().checkUserLoginStaus()) {
            getCoupon();
        }
        showAccountInfo();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * 判断是否登录，显示信息
     */
    private void showAccountInfo() {
        String usetName = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_LOGINNAME, "");
        String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
        if ((!StringUtils.isBlank(userId)) && (!StringUtils.isBlank(usetName))) {
            account_Tel.setText(usetName);
            showLogin.setVisibility(View.GONE);
            invitedCode.setText(Html.fromHtml("您的邀请码:<font color='#ff9d02'>" + SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_INVITEDCODE, "") + "</font>"));
        } else {
            showLogin.setVisibility(View.VISIBLE);
            account_Tel.setText("");
            invitedCode.setText("");
        }
        if (AppConfig.getInstance().checkUserLoginStaus()) {
            account_loginOut.setVisibility(View.VISIBLE);
        } else {
            account_loginOut.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UserInfo_Laytou:
                if (!checkUserLogin()) {
                    return;
                }
                break;
            case R.id.account_car://银行卡
                if (!checkUserLogin()) {
                    return;
                }
                ServiceBaseActivity.startActivity(getActivity(), AccountCarFragment.class.getName());
                break;
            case R.id.account_bill://历史账单
                if (!checkUserLogin()) {
                    return;
                }
                ServiceBaseActivity.startActivity(getActivity(), HistoryBillFragment.class.getName());
                break;
            case R.id.account_share://邀请好友
                if (!checkUserLogin()) {
                    return;
                }
                shareAccount();
                break;
            case R.id.account_ticket://免息券
                if (!checkUserLogin()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CouponActivity.class);
                intent.putExtra("useCoupon", "account");
                startActivity(intent);
                break;

            case R.id.account_star://关注我们
                Bundle starBundle = new Bundle();
//                starBundle.putString("url", "file:///android_asset/followUs.html");
                starBundle.putString("title", getResources().getString(R.string.account_message_5));
                ServiceBaseActivity.startActivity(getActivity(), FollowUsFragment.class.getName(), starBundle);
                break;

            case R.id.account_help://帮助中心
                Bundle helpBundle = new Bundle();
                helpBundle.putString("url", AppConfig.getInstance().HELP_CENTER_URL);
                helpBundle.putString("title", getResources().getString(R.string.account_message_6));
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), helpBundle);
                break;

            case R.id.account_safe://安全设置
                if (!checkUserLogin()) {
                    return;
                }
                ServiceBaseActivity.startActivity(getActivity(), SecurityFragment.class.getName());
                break;

            case R.id.account_more://更多
//                Bundle moreBundle = new Bundle();
//                moreBundle.putString("url", AppConfig.getInstance().MORE_URL);
//                moreBundle.putString("title", getResources().getString(R.string.account_message_8));
//                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), moreBundle);
                ServiceBaseActivity.startActivity(getActivity(), MoreFragment.class.getName());
                break;

            case R.id.account_loginOut:
                DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.loginOut_Prompt, R.string.cancel,
                        R.string.ok, new CustomDialog.OnSureInterface() {
                            @Override
                            public void getOnSure() {
                                if (checkUserLogin()) {
                                    AppConfig.getInstance().requestClearData();
                                    AppConfig.getInstance().clearUserData();
//                                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                                    getActivity().finish();
                                    showAccountInfo();
                                }
                            }

                            @Override
                            public void getOnDesmiss() {

                            }
                        });
                break;
            case R.id.account_sign:
                Bundle signBundle = new Bundle();
                signBundle.putString("url", AppConfig.getInstance().ACCOUNT_SIGN_URL);
                signBundle.putString("title", getResources().getString(R.string.sign));
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), signBundle);
                break;
        }
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
    public void onResponsSuccess(int TAG, Object result) {

        dismissProressDialog();
        Map<String, Object> loginiSucc = (Map<String, Object>) result;
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_1:
                if (loginiSucc.containsKey(AppConfig.getInstance().RESULT_DATA)) {
                    Map<String, String> map = (Map<String, String>) loginiSucc.get(AppConfig.getInstance().RESULT_DATA);
                    if (map.containsKey(SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, null)) && map.containsKey(AppConfig.getInstance().RESULT_USERID)) {
                        String tokenId = map.get(SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, null));
                        String userId = map.get(AppConfig.getInstance().RESULT_USERID);
                        SharedPreferencesUtil.putString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, tokenId);
                        SharedPreferencesUtil.putString(getActivity(), AppConfig.getInstance().RESULT_USERID, userId);
                    }
                }
                break;

            case REQUEST_MESSAGE_TAG_2:
                break;

            case GET_COUPON:
                if (StringUtils.isResponseNull(loginiSucc)) {
                    return;
                }

                Map<String, Object> coupon = (Map<String, Object>) loginiSucc.get("data");
                if (StringUtils.isResponseNull(coupon)) {
                    tvCouponNumber.setText(R.string.account_message_10);
                    return;
                } else {
                    int ticketCount = (int) coupon.get("count");
                    tvCouponNumber.setText("共" + ticketCount + "张免息券可用");
                    SharedPreferencesUtil.putInt(getActivity(), AppConfig.getInstance().RESULT_COUPON_NUMBER, ticketCount);
                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    @Override
    public void onReceiveMessage(String msgkey, Object msgObject) {
        super.onReceiveMessage(msgkey, msgObject);
        if (msgkey.equals(AppConfig.getInstance().VERIFICATION_USER_ISLOGIN)) {
            showAccountInfo();
        }
    }

    /**
     * 判断用户是否登录
     */
    private boolean checkUserLogin() {
        if (!AppConfig.getInstance().checkUserLoginStaus()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("jumpFlag", true);
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 分享代码的实现
     */
    private void shareAccount() {
        Bundle bundle = new Bundle();
        bundle.putString("showTitle", "showTitle");
        ServiceBaseActivity.startActivity(getActivity(), FreeMainFragment.class.getName(), bundle);
    }
}
