package com.miaodao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.githang.statusbar.StatusBarCompat;
import com.miaodao.Fragment.Account.AccountMainFragment;
import com.miaodao.Fragment.Free.FreeMainFragment;
import com.miaodao.Fragment.Loan.LoanMainFragment;
import com.miaodao.Fragment.Quota.QuotaMainFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.LBSUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/5/17 10:40
 */

public class MainAct extends FragmentActivity implements View.OnClickListener {

    private LinearLayout llLoan, llFree, llQuota, llMe;
    private TextView tvLoan, tvFree, tvQuota, tvMe;
    private ImageView ivLoan, ivFree, ivQuota, ivMe;
    private int blueColor, grayColor;

    private LoanMainFragment loanMainFragment;//贷款
    private QuotaMainFragment quotaMainFragment;//提额
    private FreeMainFragment freeMainFragment;//免息
    private AccountMainFragment accountMainFragment;//账户

    //标题
    private ImageView titleBack;
    private Button titleMenu;
    private TextView tvTitle, titleMenuNum;
    private RelativeLayout layoutTitle;


    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private FragmentManager fragmentManager;

    private LBSUtil lbsUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main_layout);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.appColor));
        initParams(savedInstanceState);
        initActions();
    }


    /**
     * 初始化参数
     */
    private void initParams(Bundle savedInstanceState) {

        llLoan = (LinearLayout) findViewById(R.id.ll_loan);
        llFree = (LinearLayout) findViewById(R.id.ll_free);
        llQuota = (LinearLayout) findViewById(R.id.ll_quota);
        llMe = (LinearLayout) findViewById(R.id.ll_me);

        tvLoan = (TextView) findViewById(R.id.tv_loan);
        tvFree = (TextView) findViewById(R.id.tv_free);
        tvQuota = (TextView) findViewById(R.id.tv_quota);
        tvMe = (TextView) findViewById(R.id.tv_me);

        ivLoan = (ImageView) findViewById(R.id.iv_loan);
        ivFree = (ImageView) findViewById(R.id.iv_free);
        ivQuota = (ImageView) findViewById(R.id.iv_quota);
        ivMe = (ImageView) findViewById(R.id.iv_me);

        blueColor = getResources().getColor(R.color.liji_material_blue_500);
        grayColor = getResources().getColor(R.color.loan_time_text);

        //开启百度地图定位
        lbsUtil = new LBSUtil();
        lbsUtil.startLBS();

        //标题
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleMenu = (Button) findViewById(R.id.title_menu);
        tvTitle = (TextView) findViewById(R.id.title_tvShow);
        titleMenuNum = (TextView) findViewById(R.id.title_menuNum);


        showChild(savedInstanceState);
    }


    /**
     * 显示fragment逻辑
     *
     * @param savedInstanceState
     */
    private void showChild(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
//        if (savedInstanceState != null) {
//            //获取“内存重启”时保存的索引下标
//            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
//
//            //注意，添加顺序要跟下面添加的顺序一样！！！！
//            fragments.removeAll(fragments);
//
//            loanMainFragment = (LoanMainFragment) fragmentManager.findFragmentByTag(0 + "");
//            freeMainFragment = (FreeMainFragment) fragmentManager.findFragmentByTag(1 + "");
//            quotaMainFragment = (QuotaMainFragment) fragmentManager.findFragmentByTag(2 + "");
//            accountMainFragment = (AccountMainFragment) fragmentManager.findFragmentByTag(3 + "");
//
//            fragments.add(loanMainFragment);
//            fragments.add(freeMainFragment);
//            fragments.add(quotaMainFragment);
//            fragments.add(accountMainFragment);
//
//            //恢复fragment页面
//            restoreFragment();
//        } else {
//        loanMainFragment = new LoanMainFragment();
//        freeMainFragment = new FreeMainFragment();
//        quotaMainFragment = new QuotaMainFragment();
//        accountMainFragment = new AccountMainFragment();


        loanMainFragment = LoanMainFragment.getInstance();
        freeMainFragment = FreeMainFragment.getInstance();
        quotaMainFragment = QuotaMainFragment.getInstance();
        accountMainFragment = AccountMainFragment.getInstance();

        fragments.add(loanMainFragment);
        fragments.add(freeMainFragment);
        fragments.add(quotaMainFragment);
        fragments.add(accountMainFragment);

        showFragment();
//        }
    }

    private void initActions() {
        llLoan.setOnClickListener(this);
        llFree.setOnClickListener(this);
        llQuota.setOnClickListener(this);
        llMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_loan:
                if (loanMainFragment.isVisible())
                    return;
                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                currentIndex = 0;
                changeStatus(currentIndex);
                showFragment();
                break;

            case R.id.ll_free:
                if (!AppConfig.getInstance().checkUserLoginStaus()) {
                    goLogin();
                    return;
                }
                if (freeMainFragment.isVisible())
                    return;
                EventBus.getDefault().post(AppConfig.getInstance().MESSAGE_GET_COUPON);
                currentIndex = 1;
                changeStatus(currentIndex);
                showFragment();
                break;

            case R.id.ll_quota:
                if (!AppConfig.getInstance().checkUserLoginStaus()) {
                    goLogin();
                    return;
                }
                if (quotaMainFragment.isVisible())
                    return;
                currentIndex = 2;
                changeStatus(currentIndex);
                showFragment();

//                Intent intent = new Intent(this, ActIdCamera.class);
//                Intent intent = new Intent(this, ActFaceCame.class);
//                startActivity(intent);

//                ServiceBaseActivity.startActivity(this, ZhimaAuthorFrag.class.getName());
//                ServiceBaseActivity.startActivity(this, BindCardFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, PersonalIdCarFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, BasicInfoFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, ContactsFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, WithdrawalMainFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, VideoAuthenticationFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, WithdrawalsOperationFragment.class.getName());
//                startActivity(new Intent(MainActivity.this, WXPayActivity.class));
//                ServiceBaseActivity.startActivity(this, BackUserPwdFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, BackUserPayPwdFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, RegisterUserFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, ProductSummaryFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, RepaymentFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, ImmediateWithdrawalFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, QuestionFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, QuestionnaireMainFragment.class.getName());
//                ServiceBaseActivity.startActivity(this, TestFrag.class.getName());
//                ServiceBaseActivity.startActivity(this, PerfectInfoFragment.class.getName());


//                Bundle bundle = new Bundle();
//                bundle.putString("state", "F");
//                bundle.putString("amount", "5000.00");
//                ServiceBaseActivity.startActivity(this, RepayResultFrag.class.getName(), bundle);


//                Intent intent = new Intent(this, ActIdCamera.class);
//                intent.putExtra(ActIdCamera.ID_TAG, ActIdCamera.ID_FRONT);
//                startActivity(intent);


                break;

            case R.id.ll_me:
                if (accountMainFragment.isVisible())
                    return;
                currentIndex = 3;
                changeStatus(currentIndex);
                showFragment();
//
//                Intent intent1 = new Intent(this, ActFaceCame.class);
//                startActivity(intent1);


                break;

            default:
                break;
        }
    }


    /**
     * @param parentId
     */
    private void changeStatus(int parentId) {

        switch (parentId) {
            case 0:
                tvLoan.setTextColor(blueColor);
                tvFree.setTextColor(grayColor);
                tvQuota.setTextColor(grayColor);
                tvMe.setTextColor(grayColor);
                ivLoan.setImageResource(R.drawable.loan_selector_true);
                ivFree.setImageResource(R.drawable.free_selector_false);
                ivQuota.setImageResource(R.drawable.quota_selector_false);
                ivMe.setImageResource(R.drawable.me_selector_false);

                layoutTitle.setVisibility(View.VISIBLE);
                titleMenu.setVisibility(View.VISIBLE);
                titleBack.setVisibility(View.GONE);
                tvTitle.setText(R.string.app_name);
                break;

            case 1:
                tvLoan.setTextColor(grayColor);
                tvFree.setTextColor(blueColor);
                tvQuota.setTextColor(grayColor);
                tvMe.setTextColor(grayColor);
                ivLoan.setImageResource(R.drawable.loan_selector_false);
                ivFree.setImageResource(R.drawable.free_selector_true);
                ivQuota.setImageResource(R.drawable.quota_selector_false);
                ivMe.setImageResource(R.drawable.me_selector_false);

                layoutTitle.setVisibility(View.VISIBLE);
                titleMenu.setVisibility(View.GONE);
                titleBack.setVisibility(View.GONE);
                tvTitle.setText(R.string.title_invite);
                break;

            case 2:
                tvLoan.setTextColor(grayColor);
                tvFree.setTextColor(grayColor);
                tvQuota.setTextColor(blueColor);
                tvMe.setTextColor(grayColor);
                ivLoan.setImageResource(R.drawable.loan_selector_false);
                ivFree.setImageResource(R.drawable.free_selector_false);
                ivQuota.setImageResource(R.drawable.quota_selector_true);
                ivMe.setImageResource(R.drawable.me_selector_false);

                layoutTitle.setVisibility(View.VISIBLE);
                titleMenu.setVisibility(View.GONE);
                titleBack.setVisibility(View.GONE);
                tvTitle.setText(R.string.title_tie);
                break;

            case 3:
                tvLoan.setTextColor(grayColor);
                tvFree.setTextColor(grayColor);
                tvQuota.setTextColor(grayColor);
                tvMe.setTextColor(blueColor);
                ivLoan.setImageResource(R.drawable.loan_selector_false);
                ivFree.setImageResource(R.drawable.free_selector_false);
                ivQuota.setImageResource(R.drawable.quota_selector_false);
                ivMe.setImageResource(R.drawable.me_selector_true);

                titleMenu.setVisibility(View.GONE);
                titleBack.setVisibility(View.GONE);
                tvTitle.setText("");
                break;

            default:
                break;

        }

    }


    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentIndex == 0) {
            titleBack.setVisibility(View.GONE);
        }

        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.hide(currentFragment);
            transaction.add(R.id.main_container, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
            transaction.isAddToBackStackAllowed();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 没有登录，跳转到登录页面
     */
    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("jumpFlag", true);
        startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbsUtil.stopLBS();
    }
}
