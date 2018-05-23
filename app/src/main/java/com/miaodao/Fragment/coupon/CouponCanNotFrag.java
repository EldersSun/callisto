package com.miaodao.Fragment.coupon;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Coupon;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Utils.CouponAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.miaodao.Activity.CouponActivity.CAN_USE;

/**
 * Created by daixinglong on 2017/5/6.
 */

public class CouponCanNotFrag extends BaseFragment {

    private final int GET_COUPON = 0x001;

    private RecyclerView rvCoupon;
    private ImageView ivEmpty;
    private Button btnNotUse;
    private CouponAdapter couponAdapter;
    private Coupon coupon = new Coupon();
    private Bundle bundle;
    private String useCouponFrom = "";


    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_coupon, null);
    }

    @Override
    protected void initWidgets(View fgView) {

        bundle = getArguments();
        if (bundle != null) {
            useCouponFrom = bundle.getString("useCouponFrom");
        }

        rvCoupon = (RecyclerView) fgView.findViewById(R.id.rv_coupon);
        ivEmpty = (ImageView) fgView.findViewById(R.id.iv_empty);
        btnNotUse = (Button) fgView.findViewById(R.id.btn_not_use);
        couponAdapter = new CouponAdapter(getActivity(), useCouponFrom);
        rvCoupon.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCoupon.setAdapter(couponAdapter);
        getCoupon();
    }

    @Override
    protected void initEvent() {
    }

    /**
     * 获取免息券数量
     */
    private void getCoupon() {
        showProgressDailog();
        Map<String, Object> coupon = new HashMap<>();
        coupon.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        coupon.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        coupon.put("transId", bundle.getString("transId"));
        coupon.put("state", CAN_USE);
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().GET_COUPON))
                .content(StringUtils.toJsonString(coupon)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                dismissProressDialog();
                if (!TextUtils.isEmpty(msg))
                    ToastUtils.shortShow(msg);

                if (couponAdapter.getItemCount() == 0)
                    showEmpty();
            }

            @Override
            public void onResponse(String response, int id) {
                dismissProressDialog();
                if (response.equals("")) {
                    showProgressDailog();
                    return;
                }
                String data = StringUtils.getResponseData(response);
                if (TextUtils.isEmpty(data)) {
                    showEmpty();
                    return;
                }

                try {
                    JSONObject object = new JSONObject(data);
                    List<Coupon> couponList = JSON.parseArray(object.optString("couponList"), Coupon.class);
                    if (couponList == null || couponList.isEmpty()) {
                        showEmpty();
                    } else {
                        couponAdapter.setCouponList(couponList);
                    }
                } catch (JSONException e) {
                    showEmpty();
                    e.printStackTrace();
                }
            }
        });
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


    private void showEmpty() {
        ivEmpty.setVisibility(View.VISIBLE);
        btnNotUse.setVisibility(View.GONE);
    }

}
