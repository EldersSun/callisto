package com.miaodao.Fragment.Quota;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.maizi.http.HttpType;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.WebViewFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.response.ZhimaResp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/5/19 10:33
 */

public class ZhimaAuthorFrag extends ContentBaseFragment implements View.OnClickListener {


    EditText etName;
    EditText etIdCard;
    Button zimaAuthor;


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_zhima, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText("芝麻信用授权");
        etName = (EditText) fgView.findViewById(R.id.et_name);
        etIdCard = (EditText) fgView.findViewById(R.id.et_id_card);
        zimaAuthor = (Button) fgView.findViewById(R.id.btn_zima_author);
    }

    @Override
    protected void initEvent() {
        zimaAuthor.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startAuthor();
    }


    public void startAuthor() {
        String name = etName.getText().toString();
        String idCard = etIdCard.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.shortShow("请输入您的姓名");
            return;
        }

        if (TextUtils.isEmpty(idCard)) {
            ToastUtils.shortShow("请输入您的身份证号");
            return;
        }

        if (!StringUtils.isCard(idCard)) {
            ToastUtils.shortShow("请输入正确的身份证号");
            return;
        }

        showProgressDailog();
        Map<String, Object> author = new HashMap<>();
        author.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        author.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        author.put("name", name);
        author.put("certNo", idCard.toUpperCase());

        OkHttpUtils.post().content(StringUtils.toJsonString(author)).url(StringUtils.toUtl(AppConfig.getInstance().ZHIMAAUTHOR))
                .invokeType(HttpType.SERVER_ONLY).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                dismissProressDialog();
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtils.shortShow(msg);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                dismissProressDialog();
                if (TextUtils.isEmpty(response))
                    return;
                ZhimaResp zhimaResp = JSON.parseObject(response, ZhimaResp.class);
                ZhimaResp.ZhimaUrl zhimaUrl = zhimaResp.getData();
                if (zhimaUrl != null) {
                    goAuthorWeb(zhimaUrl.getAntifraudUrl());
                }
            }
        });
    }


    private void goAuthorWeb(String url) {
        Bundle starBundle = new Bundle();
        starBundle.putString("url", url);
        starBundle.putString("title", "芝麻信用授权");
        ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), starBundle);
        getActivity().finish();
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
