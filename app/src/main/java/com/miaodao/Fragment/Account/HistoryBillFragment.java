package com.miaodao.Fragment.Account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Account.bean.Bill;
import com.miaodao.Fragment.SetNetFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.MobileUserLoanInfo;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.MapUtils;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Utils.HistoryBillAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * 历史账单页面
 * Created by Home_Pc on 2017/3/24.
 */

public class HistoryBillFragment extends ContentBaseFragment implements View.OnClickListener {
    private RecyclerView AccountCatLvShow;
    private HistoryBillAdapter historyBillAdapter;

    private RelativeLayout netRequestErrorParent;//网络连接失败，显示失败页面
    private ImageView ivNetError;//点击重新加载
    private TextView tvSetNet;//点击跳转到网络设置页面
    private TextView tvNoData;//页面没有数据


    private final int REQUEST_MESSAGE_TAG = 0X4021;
    private List<MobileUserLoanInfo> mobileUserLoanInfos = new ArrayList<>();

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_accountcar_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.account_message_2);
        AccountCatLvShow = (RecyclerView) fgView.findViewById(R.id.AccountCatLvShow);
        netRequestErrorParent = (RelativeLayout) fgView.findViewById(R.id.net_request_error_parent);
        ivNetError = (ImageView) fgView.findViewById(R.id.iv_net_error);
        tvSetNet = (TextView) fgView.findViewById(R.id.tv_set_net);
        tvNoData = (TextView) fgView.findViewById(R.id.tv_no_data);
        historyBillAdapter = new HistoryBillAdapter(getActivity());

        getBill();
    }

    /**
     * 请求获取账单
     */
    private void getBill() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        map.put("pageNo", "1");
        map.put("pageSize", "10");
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().queryUserLoanInfo))
                .content(StringUtils.toJsonString(map)).id(REQUEST_MESSAGE_TAG).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                dismissProressDialog();
                if ((WheatFinanceApplication.getInstance().getString(R.string.newwork_timeout).equals(msg) || WheatFinanceApplication.getInstance().getString(R.string.newwork_error).equals(msg)) && historyBillAdapter.getItemCount() == 0) {
                    netRequestErrorParent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                dismissProressDialog();
                netRequestErrorParent.setVisibility(View.GONE);
                if (TextUtils.isEmpty(response)) {
                    showProgressDailog();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.optString("data");
                    JSONObject obj = new JSONObject(data);
                    if (obj.optString("loanInfo").equals("null")) {
                        showMsgTip();
                        return;
                    }
                    List<Bill> bills = JSON.parseArray(obj.optString("loanInfo"), Bill.class);
                    if (bills == null || bills.isEmpty()) {
                        showMsgTip();
                        return;
                    }
                    tvNoData.setVisibility(View.GONE);
                    historyBillAdapter.setMobileUserLoanInfos(bills);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showMsgTip();
                }
            }
        });


    }


    @Override
    protected void initEvent() {
        AccountCatLvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        AccountCatLvShow.setAdapter(historyBillAdapter);

        ivNetError.setOnClickListener(this);
        tvSetNet.setOnClickListener(this);
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        netRequestErrorParent.setVisibility(View.GONE);

        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                try {
                    Map<String, Object> resultMap = (Map<String, Object>) result;
                    if (resultMap.containsKey("data")) {
                        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                        if (dataMap.containsKey("loanInfo") && !dataMap.get("loanInfo").equals(null)) {
                            List<Object> loans = (List<Object>) dataMap.get("loanInfo");
                            if (loans.isEmpty()) {
                                showMsgTip();
                                return;
                            }
                            mobileUserLoanInfos.clear();
                            for (int i = 0; i < loans.size(); i++) {
                                Map<String, Object> objectMap = (Map<String, Object>) loans.get(i);
                                MobileUserLoanInfo mobileUserLoanInfo = new MobileUserLoanInfo();
                                mobileUserLoanInfo = MapUtils.map2Bean(objectMap, MobileUserLoanInfo.class);
                                mobileUserLoanInfos.add(mobileUserLoanInfo);
                            }
//                            historyBillAdapter.setMobileUserLoanInfos(mobileUserLoanInfos);
                        } else {
                            showMsgTip();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * 显示没有数据
     */
    private void showMsgTip() {
        tvNoData.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        if (WheatFinanceApplication.getInstance().getString(R.string.newwork_timeout).equals(result)) {
            netRequestErrorParent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        netRequestErrorParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_net_error:
                getBill();
                break;

            case R.id.tv_set_net:
                goSetNet();
                break;

            default:
                break;
        }
    }

    /**
     * 跳转到网络设置页面
     */
    private void goSetNet() {
        Bundle moreBundle = new Bundle();
        moreBundle.putString("title", getResources().getString(R.string.net_set));
        ServiceBaseActivity.startActivity(getActivity(), SetNetFragment.class.getName());
    }

}
