package com.miaodao.Fragment.Withdrawals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Loan.Apply.BasicInfoFragment;
import com.miaodao.Fragment.Loan.Apply.BindCardFragment;
import com.miaodao.Fragment.Loan.Apply.ContactsFragment;
import com.miaodao.Fragment.Loan.Apply.PersonalIdCarFragment;
import com.miaodao.Fragment.Loan.Apply.VideoAuthenticationFragment;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;

import java.util.List;
import java.util.Map;

/**
 * 完善资料
 * Created by Home_Pc on 2017/3/23.
 */

public class PerfectInfoFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout perfectInfo_idInfo_layout, perfectInfo_idCard_layout,
            perfectInfo_basic_layout, perfectInfo_Contacts_layout, perfectInfo_video_layout;

    private TextView perfectInfo_idInfo_MsgTvShow, perfectInfo_idCard_MsgTvShow,
            perfectInfo_basic_MsgTvShow, perfectInfo_Contacts_MsgTvShow, perfectInfo_video_MsgTvShow;

    private Button perfectInfo_idInfo_setting, perfectInfo_idCard_setting, perfectInfo_basic_setting,
            perfectInfo_Contacts_setting, perfectInfo_video_setting, perfectInfo_submit;

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_perfectinfo_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        perfectInfo_idInfo_layout = (LinearLayout) fgView.findViewById(R.id.perfectInfo_idInfo_layout);
        perfectInfo_idCard_layout = (LinearLayout) fgView.findViewById(R.id.perfectInfo_idCard_layout);
        perfectInfo_basic_layout = (LinearLayout) fgView.findViewById(R.id.perfectInfo_basic_layout);
        perfectInfo_Contacts_layout = (LinearLayout) fgView.findViewById(R.id.perfectInfo_Contacts_layout);
        perfectInfo_video_layout = (LinearLayout) fgView.findViewById(R.id.perfectInfo_video_layout);

        perfectInfo_idInfo_MsgTvShow = (TextView) fgView.findViewById(R.id.perfectInfo_idInfo_MsgTvShow);
        perfectInfo_idCard_MsgTvShow = (TextView) fgView.findViewById(R.id.perfectInfo_idCard_MsgTvShow);
        perfectInfo_basic_MsgTvShow = (TextView) fgView.findViewById(R.id.perfectInfo_basic_MsgTvShow);
        perfectInfo_Contacts_MsgTvShow = (TextView) fgView.findViewById(R.id.perfectInfo_Contacts_MsgTvShow);
        perfectInfo_video_MsgTvShow = (TextView) fgView.findViewById(R.id.perfectInfo_video_MsgTvShow);

        perfectInfo_idInfo_setting = (Button) fgView.findViewById(R.id.perfectInfo_idInfo_setting);
        perfectInfo_idCard_setting = (Button) fgView.findViewById(R.id.perfectInfo_idCard_setting);
        perfectInfo_basic_setting = (Button) fgView.findViewById(R.id.perfectInfo_basic_setting);
        perfectInfo_Contacts_setting = (Button) fgView.findViewById(R.id.perfectInfo_Contacts_setting);
        perfectInfo_video_setting = (Button) fgView.findViewById(R.id.perfectInfo_video_setting);
        perfectInfo_submit = (Button) fgView.findViewById(R.id.perfectInfo_submit);
    }

    @Override
    protected void initEvent() {
        perfectInfo_idInfo_setting.setOnClickListener(this);
        perfectInfo_idCard_setting.setOnClickListener(this);
        perfectInfo_basic_setting.setOnClickListener(this);
        perfectInfo_Contacts_setting.setOnClickListener(this);
        perfectInfo_video_setting.setOnClickListener(this);
        perfectInfo_submit.setOnClickListener(this);

//        observeMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_1);
//        observeMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_2);
//        observeMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_3);
//        observeMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_4);
//        observeMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_5);

        Map<String, Object> map = AppConfig.getInstance().PerfectInfoMap;
        if (map.containsKey("stateCode") && !StringUtils.isBlank((String) map.get("stateCode"))
                && ((String) map.get("stateCode")).equals("100203")) {
            perfectInfo_submit.setVisibility(View.VISIBLE);
        }
        if (map.containsKey("content") && !map.get("content").toString().equals("null")) {
            List<Map<String, Object>> contentMap = (List<Map<String, Object>>) map.get("content");
            if (!contentMap.isEmpty()) {
                setCotentData(contentMap);
            }
        }
    }

    /**
     * 设置内容资料
     * "A100000", "身份证认证"
     * "A101000", "银行卡认证"
     * "A102000", "联系地址认证"
     * "A103000", "联系人认证"
     * "A104000", "图片对比"
     * "A104001", "视频对比"
     */
    private void setCotentData(List<Map<String, Object>> contentMap) {
        for (int i = 0; i < contentMap.size(); i++) {
            Map<String, Object> infoMap = contentMap.get(i);
            //"subAuthCode" -> "103000"
            //"authState" -> "null"
            /**
             * 身份证
             */
            if (infoMap.containsKey("subAuthCode") && infoMap.get("subAuthCode").toString().equals("100000")) {
                if (infoMap.get("authState").equals(null) || infoMap.get("authState") == null
                        || infoMap.get("authState").toString().equals("R")) {
                    perfectInfo_idInfo_layout.setVisibility(View.VISIBLE);
//                    if ("I".equals(infoMap.get("authState").toString())) {
//                        perfectInfo_idInfo_setting.setText(R.string.setting);
//                    } else {
                        perfectInfo_idInfo_setting.setText(R.string.upload_file);
//                    }
                    perfectInfo_idInfo_MsgTvShow.setText(infoMap.get("errorMsg").toString());

                } else {
                    perfectInfo_idInfo_layout.setVisibility(View.GONE);
                }
            }
            /**
             * 银行卡
             */
            if (infoMap.containsKey("subAuthCode") && infoMap.get("subAuthCode").toString().equals("101000")) {
                if (infoMap.get("authState").equals(null) || infoMap.get("authState") == null
                        || infoMap.get("authState").toString().equals("R")) {
                    perfectInfo_idCard_layout.setVisibility(View.VISIBLE);
//                    if ("I".equals(infoMap.get("authState").toString())) {
//                        perfectInfo_idCard_setting.setText(R.string.setting);
//                    } else {
                        perfectInfo_idCard_setting.setText(R.string.upload_file);
//                    }
                    perfectInfo_idCard_MsgTvShow.setText(infoMap.get("errorMsg").toString());

                } else {
                    perfectInfo_idCard_layout.setVisibility(View.GONE);
                }
            }

            /**
             * 联系地址
             */
            if (infoMap.containsKey("subAuthCode") && infoMap.get("subAuthCode").toString().equals("102000")) {
                if (infoMap.get("authState").equals(null) || infoMap.get("authState") == null
                        || infoMap.get("authState").toString().equals("R")) {
                    perfectInfo_basic_layout.setVisibility(View.VISIBLE);
//                    if ("I".equals(infoMap.get("authState").toString())) {
//                        perfectInfo_basic_setting.setText(R.string.setting);
//                    } else {
                        perfectInfo_basic_setting.setText(R.string.upload_file);
//                    }
                    perfectInfo_basic_MsgTvShow.setText(infoMap.get("errorMsg").toString());
                } else {
                    perfectInfo_basic_layout.setVisibility(View.GONE);
                }
            }

            /**
             * 紧急联系人
             * “01”----  家人
             * “02”----  朋友
             * “03”----  同事
             */
            if (infoMap.containsKey("subAuthCode") && infoMap.get("subAuthCode").toString().equals("103000")) {
                if (infoMap.get("authState").equals(null) || infoMap.get("authState") == null
                        || infoMap.get("authState").toString().equals("R")) {
                    perfectInfo_Contacts_layout.setVisibility(View.VISIBLE);
//                    if ("I".equals(infoMap.get("authState").toString())) {
//                        perfectInfo_Contacts_setting.setText(R.string.setting);
//                    } else {
                        perfectInfo_Contacts_setting.setText(R.string.upload_file);
//                    }
                    perfectInfo_Contacts_MsgTvShow.setText(infoMap.get("errorMsg").toString());
                } else {
                    perfectInfo_Contacts_layout.setVisibility(View.GONE);
                }
            }

            /**
             *图片对比
             */
            if (infoMap.containsKey("subAuthCode") && infoMap.get("subAuthCode").toString().equals("104000")
                    || infoMap.get("subAuthCode").toString().equals("104001")) {
                if (infoMap.get("authState").equals(null) || infoMap.get("authState") == null
                        || infoMap.get("authState").toString().equals("R")) {
                    perfectInfo_video_layout.setVisibility(View.VISIBLE);
//                    if ("I".equals(infoMap.get("authState").toString())) {
//                        perfectInfo_video_setting.setText(R.string.setting);
//                    } else {
                        perfectInfo_video_setting.setText(R.string.upload_file);
//                    }
                    perfectInfo_video_MsgTvShow.setText(infoMap.get("errorMsg").toString());
                } else {
                    perfectInfo_video_layout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
//                dismissProressDialog();
//                sendMessage(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1,"");
//                Map<String, Object> resultMap = (Map<String, Object>) result;
//                if (resultMap.containsKey("data")) {
//                    Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
//                    if (dataMap.containsKey("content") && dataMap.get("content") != null && !dataMap.get("content").equals(null)) {
//                        List<Map<String, Object>> contentMap = (List<Map<String, Object>>) dataMap.get("content");
//                        setCotentData(contentMap);
//                    }
//                }
                break;
            case REQUEST_MESSAGE_TAG_2:
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    private final int REQUEST_MESSAGE_TAG = 0x1006;
    private final int REQUEST_MESSAGE_TAG_2 = 0x1007;

    /**
     * "100000", "身份证认证"
     * "101000", "银行卡认证"
     * "102000", "联系地址认证"
     * "103000", "联系人认证"
     * "104000", "图片对比"
     * "104001", "视频对比"
     */
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.perfectInfo_idInfo_setting:
//                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_1);
                ServiceBaseActivity.startActivity(getActivity(), PersonalIdCarFragment.class.getName(), bundle);
                break;
            case R.id.perfectInfo_idCard_setting:
//                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_2);
                ServiceBaseActivity.startActivity(getActivity(), BindCardFragment.class.getName(), bundle);
                break;
            case R.id.perfectInfo_basic_setting:
//                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_3);
                ServiceBaseActivity.startActivity(getActivity(), BasicInfoFragment.class.getName(), bundle);
                break;
            case R.id.perfectInfo_Contacts_setting:
//                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_4);
                ServiceBaseActivity.startActivity(getActivity(), ContactsFragment.class.getName(), bundle);
                break;
            case R.id.perfectInfo_video_setting:
//                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_5);
                ServiceBaseActivity.startActivity(getActivity(), VideoAuthenticationFragment.class.getName(), bundle);
                break;
            case R.id.perfectInfo_submit:
//                Map<String, Object> submitMap = new HashMap<>();
//                String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
//                submitMap.put("userId", userId);
//                requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().submitAuthInfo, submitMap);
                ServiceBaseActivity.startActivity(getActivity(), ProductSummaryFragment.class.getName());
                break;
        }
    }

    @Override
    public void onReceiveMessage(String msgkey, Object msgObject) {
        super.onReceiveMessage(msgkey, msgObject);
//        if (msgkey.equals(AppConfig.getInstance().JUMP_MESSAGE_TAG_1)
//                || msgkey.equals(AppConfig.getInstance().JUMP_MESSAGE_TAG_2)
//                || msgkey.equals(AppConfig.getInstance().JUMP_MESSAGE_TAG_3)
//                || msgkey.equals(AppConfig.getInstance().JUMP_MESSAGE_TAG_4)
//                || msgkey.equals(AppConfig.getInstance().JUMP_MESSAGE_TAG_5)) {
//            perfectInfo_idInfo_layout.setVisibility(View.GONE);
//            perfectInfo_idCard_layout.setVisibility(View.GONE);
//            perfectInfo_basic_layout.setVisibility(View.GONE);
//            perfectInfo_Contacts_layout.setVisibility(View.GONE);
//            perfectInfo_video_layout.setVisibility(View.GONE);
//            checkStatus();
//        }
    }

//    private void checkStatus() {
////        showProgressDailog();
//        /**
//         * 判断首页状态
//         */
//        Map<String, Object> statusMap = new HashMap<>();
//        statusMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
//        statusMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
//        requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().homeInitState, statusMap);
//    }


    @Override
    public void onResume() {
        super.onResume();
//        checkStatus();
    }
}
