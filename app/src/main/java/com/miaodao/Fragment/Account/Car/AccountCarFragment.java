package com.miaodao.Fragment.Account.Car;

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
import com.miaodao.Fragment.Account.bean.Card;
import com.miaodao.Fragment.SetNetFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Communication.OnItemRecyclerViewClickListener;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Utils.AccountCarShowAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Home_Pc on 2017/3/10.
 */

public class AccountCarFragment extends ContentBaseFragment implements View.OnClickListener {

    private RecyclerView AccountCatLvShow;
    private AccountCarShowAdapter adapter;
    private RelativeLayout netRequestErrorParent;//网络连接失败，显示失败页面
    private ImageView ivNetError;//点击重新加载
    private TextView tvSetNet;//点击跳转到网络设置页面
    private TextView tvNoData;//页面没有数据

    private final int QUERYCARDLIST_MESSAGE_TAG = 0x0001;
    private final int BANK_LIST = 0x0002;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_accountcar_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.account_message_1);
        AccountCatLvShow = (RecyclerView) fgView.findViewById(R.id.AccountCatLvShow);
        netRequestErrorParent = (RelativeLayout) fgView.findViewById(R.id.net_request_error_parent);
        ivNetError = (ImageView) fgView.findViewById(R.id.iv_net_error);
        tvSetNet = (TextView) fgView.findViewById(R.id.tv_set_net);
        tvNoData = (TextView) fgView.findViewById(R.id.tv_no_data);
        adapter = new AccountCarShowAdapter(getActivity());
        adapter.setOnItemRecyclerViewClickListener(new OnItemRecyclerViewClickListener() {
            @Override
            public void onItemClickListener(View view, int postition) {

            }
        });

        queryCardList();
        getBankList();
    }

    @Override
    protected void initEvent() {
        AccountCatLvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        AccountCatLvShow.setAdapter(adapter);
        ivNetError.setOnClickListener(this);
        tvSetNet.setOnClickListener(this);
    }


    /**
     * 网络请求查询用户银行卡列表
     */
    private void queryCardList() {

        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        statusMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().queryMyCard)).id(QUERYCARDLIST_MESSAGE_TAG)
                .content(StringUtils.toJsonString(statusMap)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                dismissProressDialog();
                if ((WheatFinanceApplication.getInstance().getString(R.string.newwork_timeout).equals(msg) || WheatFinanceApplication.getInstance().getString(R.string.newwork_error).equals(msg)) && adapter.getItemCount() == 0) {
                    netRequestErrorParent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                dismissProressDialog();
                netRequestErrorParent.setVisibility(View.GONE);
                if (response == "") {
                    showProgressDailog();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.optString("data");
                    Card card = JSON.parseObject(data, Card.class);

                    if (card == null){
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    tvNoData.setVisibility(View.GONE);
                    adapter.setCardList(card);
//                    requestUrlForData(BANK_LIST, AppConfig.getInstance().bankColorUrl);
                } catch (JSONException e) {
                    tvNoData.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取银行的颜色
     */
    private void getBankList() {

        OkHttpUtils.get().url(AppConfig.getInstance().bankColorUrl)
                .getCode(false).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response))
                    return;
                try {
                    JSONObject object = new JSONObject(response);
                    adapter.setBackGround(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        netRequestErrorParent.setVisibility(View.GONE);
        switch (TAG) {

            case BANK_LIST:
                Map<String, Object> colorMap = (Map<String, Object>) result;

                if (null == colorMap) return;
                if (colorMap.equals(null)) return;
//                adapter.setBackGround(colorMap);

//                if (!StringUtils.isBlank(instId)) {
//                    if (colorMap.containsKey(instId) && !colorMap.get(instId).equals(null)) {
//                        int drawableRes = MResource.getIdByNameForDrawable(getActivity(),
//                                instId.toLowerCase());
//                        all_bank_imgShow.setImageResource(drawableRes);
//                        setViewRoundBack(colorMap.get(instId).toString());
//                    }
//                }
                break;
            default:
                break;

        }
    }

    private void noMsgTip() {
//        DialogHelp.getInstance(getActivity()).showSuccDialog("提示", "暂无数据", new CustomDialog.OnSureInterface() {
//            @Override
//            public void getOnSure() {
//                getActivity().finish();
//            }
//
//            @Override
//            public void getOnDesmiss() {
//
//            }
//        });
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_net_error:
                queryCardList();
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
