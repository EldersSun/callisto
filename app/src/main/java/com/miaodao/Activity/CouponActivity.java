package com.miaodao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Base.MainBaseActivity;
import com.miaodao.Fragment.coupon.CouponCanCanFrag;
import com.miaodao.Fragment.coupon.CouponCanNotFrag;
import com.miaodao.Fragment.coupon.CouponCanUseFrag;
import com.miaodao.Fragment.coupon.CouponDelayFrag;
import com.miaodao.Fragment.coupon.CouponUsedFrag;

/**
 * Created by daixinglong on 2017/5/6.
 */

public class CouponActivity extends MainBaseActivity implements View.OnClickListener {

    public static final int CAN_USE = 0;//未使用
    public static final int USED = 1;//已使用
    public static final int DELAY = 2;//已过期

    private TextView tvCouponCan, tvCouponUsed, tvCouponDelay, tvCouponCanCan, tvCouponCanNot;
    private LinearLayout llTwo, llThree;
    private int blueColor, whiteColor;
    private FrameLayout couponContainer;

    private CouponCanUseFrag couponCanUseFrag;
    private CouponUsedFrag couponUsedFrag;
    private CouponDelayFrag couponDelayFrag;
    private CouponCanCanFrag couponCanCanFrag;
    private CouponCanNotFrag couponCanNotFrag;
    private Intent intent;
    private String useCouponFrom = "";

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.ac_coupon, null);
    }

    @Override
    protected void initWidgets() {
        title_tvShow.setText(R.string.account_message_4);
        tv_title_right.setVisibility(View.VISIBLE);
        title_menu.setVisibility(View.GONE);

        blueColor = getResources().getColor(R.color.appColor);
        whiteColor = getResources().getColor(R.color.whiteColor);
        tvCouponCan = (TextView) findViewById(R.id.tv_coupon_can);
        tvCouponUsed = (TextView) findViewById(R.id.tv_coupon_used);
        tvCouponDelay = (TextView) findViewById(R.id.tv_coupon_delay);
        tvCouponCanCan = (TextView) findViewById(R.id.tv_coupon_can_can);
        tvCouponCanNot = (TextView) findViewById(R.id.tv_coupon_can_not);
        llTwo = (LinearLayout) findViewById(R.id.ll_two);
        llThree = (LinearLayout) findViewById(R.id.ll_three);
        couponContainer = (FrameLayout) findViewById(R.id.coupon_container);

        intent = getIntent();
        if (intent != null) {
            useCouponFrom = intent.getStringExtra("useCoupon");
            Bundle bundle = new Bundle();
            if ("account".equals(useCouponFrom)) {
                llThree.setVisibility(View.VISIBLE);
                bundle.putString("useCouponFrom", useCouponFrom);
                couponCanUseFrag = new CouponCanUseFrag();
                pushFragmentController(R.id.coupon_container, couponCanUseFrag, bundle, false);
            } else {
                llTwo.setVisibility(View.VISIBLE);
                bundle.putString("useCouponFrom", useCouponFrom);
                bundle.putString("transId", intent.getStringExtra("transId"));
                bundle.putString("productId", intent.getStringExtra("productId"));
                couponCanCanFrag = new CouponCanCanFrag();
                pushFragmentController(R.id.coupon_container, couponCanCanFrag, bundle, false);
            }
        }
    }

    @Override
    protected void initEvent() {
        tvCouponCan.setOnClickListener(this);
        tvCouponUsed.setOnClickListener(this);
        tvCouponDelay.setOnClickListener(this);
        tvCouponCanCan.setOnClickListener(this);
        tvCouponCanNot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_coupon_can:
                changeTv(R.id.tv_coupon_can);
                if (couponCanUseFrag == null)
                    couponCanUseFrag = new CouponCanUseFrag();
                if (couponCanUseFrag.isVisible())
                    return;
                Bundle bundle = new Bundle();
                bundle.putString("useCouponFrom", useCouponFrom);
                pushFragmentController(R.id.coupon_container, couponCanUseFrag, bundle, false);
                break;

            case R.id.tv_coupon_used:
                changeTv(R.id.tv_coupon_used);
                if (couponUsedFrag == null)
                    couponUsedFrag = new CouponUsedFrag();
                if (couponUsedFrag.isVisible())
                    return;
                pushFragmentController(R.id.coupon_container, couponUsedFrag, null, false);
                break;

            case R.id.tv_coupon_delay:
                changeTv(R.id.tv_coupon_delay);
                if (couponDelayFrag == null)
                    couponDelayFrag = new CouponDelayFrag();
                if (couponDelayFrag.isVisible())
                    return;
                pushFragmentController(R.id.coupon_container, couponDelayFrag, null, false);
                break;

            case R.id.tv_coupon_can_can:
                changeCanTv(R.id.tv_coupon_can_can);
                if (couponCanCanFrag == null)
                    couponCanCanFrag = new CouponCanCanFrag();
                if (couponCanCanFrag.isVisible())
                    return;
                pushFragmentController(R.id.coupon_container, couponCanCanFrag, null, false);
                break;

            case R.id.tv_coupon_can_not:
                changeCanTv(R.id.tv_coupon_can_not);
                if (couponCanNotFrag == null)
                    couponCanNotFrag = new CouponCanNotFrag();
                if (couponCanNotFrag.isVisible())
                    return;
                Bundle bundle1 = new Bundle();
                bundle1.putString("useCouponFrom", "account");
                pushFragmentController(R.id.coupon_container, couponCanNotFrag, bundle1, false);
                break;

            default:
                break;

        }


    }


    /**
     * 改变textview的显示
     */
    private void changeTv(int id) {

        switch (id) {
            case R.id.tv_coupon_can:
                tvCouponCan.setBackgroundResource(R.drawable.coupon_can_use_select);
                tvCouponUsed.setBackgroundResource(R.drawable.coupon_used_unselect);
                tvCouponDelay.setBackgroundResource(R.drawable.coupon_delay_unselect);
                tvCouponCan.setTextColor(whiteColor);
                tvCouponUsed.setTextColor(blueColor);
                tvCouponDelay.setTextColor(blueColor);
                break;

            case R.id.tv_coupon_used:
                tvCouponCan.setBackgroundResource(R.drawable.coupon_can_use_unselect);
                tvCouponUsed.setBackgroundResource(R.drawable.coupon_used_select);
                tvCouponDelay.setBackgroundResource(R.drawable.coupon_delay_unselect);
                tvCouponCan.setTextColor(blueColor);
                tvCouponUsed.setTextColor(whiteColor);
                tvCouponDelay.setTextColor(blueColor);
                break;

            case R.id.tv_coupon_delay:
                tvCouponCan.setBackgroundResource(R.drawable.coupon_can_use_unselect);
                tvCouponUsed.setBackgroundResource(R.drawable.coupon_used_unselect);
                tvCouponDelay.setBackgroundResource(R.drawable.coupon_delay_select);
                tvCouponCan.setTextColor(blueColor);
                tvCouponUsed.setTextColor(blueColor);
                tvCouponDelay.setTextColor(whiteColor);
                break;

            default:
                break;

        }


    }

    /**
     * 改变textview的显示
     */
    private void changeCanTv(int id) {

        switch (id) {
            case R.id.tv_coupon_can_can:
                tvCouponCanCan.setBackgroundResource(R.drawable.coupon_can_use_select);
                tvCouponCanNot.setBackgroundResource(R.drawable.coupon_delay_unselect);
                tvCouponCanCan.setTextColor(whiteColor);
                tvCouponCanNot.setTextColor(blueColor);
                break;

            case R.id.tv_coupon_can_not:
                tvCouponCanCan.setBackgroundResource(R.drawable.coupon_can_use_unselect);
                tvCouponCanNot.setBackgroundResource(R.drawable.coupon_delay_select);
                tvCouponCanCan.setTextColor(blueColor);
                tvCouponCanNot.setTextColor(whiteColor);
                break;
            default:
                break;

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
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
