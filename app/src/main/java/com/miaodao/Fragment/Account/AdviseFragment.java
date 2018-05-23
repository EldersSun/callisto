package com.miaodao.Fragment.Account;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fcloud.licai.miaodao.R;
import com.maizi.http.HttpType;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/6/20 10:38
 */

public class AdviseFragment extends ContentBaseFragment {

    private EditText etAdvise;
    private Button btnAdvise;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_advise, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText("意见反馈");
        etAdvise = (EditText) fgView.findViewById(R.id.et_advise);
        btnAdvise = (Button) fgView.findViewById(R.id.btn_advise);
    }

    @Override
    protected void initEvent() {
        btnAdvise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adivseUpload();
            }
        });
    }


    /**
     * 上传意见反馈
     */
    private void adivseUpload() {

        String advise = etAdvise.getText().toString().trim();
        if (advise.isEmpty()) {
            ToastUtils.shortShow("请先填写您的建议");
            return;
        }

        if (!AppConfig.getInstance().checkUserLoginStaus()) {
            ToastUtils.shortShow("请先登录");
            return;
        }


        showProgressDailog();
        Map<String, Object> map = new HashMap<>();
        map.put("content", advise);
        map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));

        OkHttpUtils.post().content(StringUtils.toJsonString(map)).url(StringUtils.toUtl(AppConfig.getInstance().FEEDBACK))
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
                if (!TextUtils.isEmpty(response)){
                    ToastUtils.shortShow("提交成功,感谢您的反馈");
                    getActivity().finish();
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

}
