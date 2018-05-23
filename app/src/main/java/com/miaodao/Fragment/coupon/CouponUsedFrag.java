package com.miaodao.Fragment.coupon;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Coupon;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MapUtils;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Utils.CouponAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.miaodao.Activity.CouponActivity.USED;

/**
 * Created by daixinglong on 2017/5/6.
 */

public class CouponUsedFrag extends BaseFragment {

    private final int GET_COUPON = 0x001;

    private RecyclerView rvCoupon;
    private ImageView ivEmpty;
    private CouponAdapter couponAdapter;


    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_coupon, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        rvCoupon = (RecyclerView) fgView.findViewById(R.id.rv_coupon);
        ivEmpty = (ImageView) fgView.findViewById(R.id.iv_empty);
        couponAdapter = new CouponAdapter(getActivity(), "");
        rvCoupon.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCoupon.setAdapter(couponAdapter);
        getCoupon();
    }

    /**
     * 获取免息券数量
     */
    private void getCoupon() {
        showProgressDailog();
        Map<String, Object> coupon = new HashMap<>();
        coupon.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        coupon.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        coupon.put("state", USED);
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().GET_COUPON))
                .content(StringUtils.toJsonString(coupon)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                dismissProressDialog();

                if (!TextUtils.isEmpty(msg))
                    ToastUtils.shortShow(msg);
                if (couponAdapter.getItemCount() == 0)
                    ivEmpty.setVisibility(View.VISIBLE);
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
                    ivEmpty.setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    JSONObject object = new JSONObject(data);
                    List<Coupon> couponList = JSON.parseArray(object.optString("couponList"), Coupon.class);
                    if (couponList == null || couponList.isEmpty()) {
                        ivEmpty.setVisibility(View.VISIBLE);
                    } else {
                        couponAdapter.setCouponList(couponList);
                    }
                } catch (JSONException e) {
                    ivEmpty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void initEvent() {

    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        ivEmpty.setVisibility(View.GONE);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        if (StringUtils.isResponseNull(resultMap)) {
            ivEmpty.setVisibility(View.VISIBLE);
            return;
        }

        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        if (StringUtils.isResponseNull(dataMap)) {
            ivEmpty.setVisibility(View.VISIBLE);
            return;
        }

        if (dataMap.get("couponList").equals(null)) {
            ivEmpty.setVisibility(View.VISIBLE);
            return;
        }

        List<Map<String, Object>> couponList = (List<Map<String, Object>>) dataMap.get("couponList");
        if (couponList.isEmpty()) {
            ivEmpty.setVisibility(View.VISIBLE);
            return;
        }
        showCoupon(couponList);
    }


    /**
     * 显示列表
     *
     * @param couponList
     */
    private void showCoupon(List<Map<String, Object>> couponList) {


        try {
            List<Coupon> couponList1 = new ArrayList<>();
            for (Map<String, Object> obj : couponList) {
                couponList1.add(MapUtils.mapToBean(obj, Coupon.class));
            }
            couponAdapter.setCouponList(couponList1);
        } catch (Exception e) {
            e.printStackTrace();
            ivEmpty.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ivEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ivEmpty.setVisibility(View.VISIBLE);
    }

}
