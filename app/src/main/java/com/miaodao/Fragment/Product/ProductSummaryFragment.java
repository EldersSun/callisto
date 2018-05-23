package com.miaodao.Fragment.Product;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.fcloud.licai.miaodao.R;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Loan.Apply.BasicInfoFragment;
import com.miaodao.Fragment.Loan.Apply.BindCardFragment;
import com.miaodao.Fragment.Loan.Apply.ContactsFragment;
import com.miaodao.Fragment.Loan.Apply.PersonalIdCarFragment;
import com.miaodao.Fragment.Loan.Apply.VideoAuthenticationFragment;
import com.miaodao.Fragment.Product.bean.AddrAuthState;
import com.miaodao.Fragment.Product.bean.AddressContent;
import com.miaodao.Fragment.Product.bean.AuthInfo;
import com.miaodao.Fragment.Product.bean.CardAuthState;
import com.miaodao.Fragment.Product.bean.IdAuthState;
import com.miaodao.Fragment.Product.bean.PhoneAuthState;
import com.miaodao.Fragment.Product.bean.VideoAuthState;
import com.miaodao.Fragment.Product.bean.VideoCompare;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.SummaryPop;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 产品汇总页面
 * Created by Home_Pc on 2017/3/22.
 */

public class ProductSummaryFragment extends ContentBaseFragment implements View.OnClickListener, SummaryPop.ISummary {

    private final String AUTH_INIT = "I";
    private final String AUTH_APPLY = "S";
    private final String AUTH_WAIT = "C";
    private final String AUTH_REJUST = "R";//认证审核拒绝
    private final String AUTH_PASS = "P";


    /**
     * all_id_img 身份证图片
     * all_id_img_status 身份证状态
     * all_bank_imgShow 银行卡类型
     * all_Verification_img 申请开户第五步，图像验证
     * all_Verification_img_status 申请开户第五步，图像验证状态
     * all_Verification_video 申请开户第五步，视频验证
     * all_Verification_video_status 申请开户第五步，视频验证状态
     */
    private ImageView all_id_img, all_id_img_status, all_bank_imgShow, all_Verification_img,
            all_Verification_img_status, all_Verification_video, all_Verification_video_status;

    /**
     * all_Name_TvShow 用户姓名
     * all_IdCard_TvShow 身份证
     * all_bank_CarName 银行卡名
     * all_bank_CarNum 银行卡号
     * all_liveaddrs 家庭住址
     * all_workaddrs 工作地址
     * all_Verification_video_tvShow 是否验证视频
     */
    private TextView all_Name_TvShow, all_IdCard_TvShow, all_bank_CarName, all_bank_CarNum,
            all_liveaddrs, all_workaddrs, all_Verification_img_tvShow, all_Verification_video_tvShow,
            all_bank_type;

    /**
     * all_id_setting 设置身份证
     * all_bank_CarSet 设置银行卡
     * all_addrs_setting 设置地址
     * all_user_setting 设置联系人
     * all_Verification_img_setting 设置图像验证
     * all_Verification_video_setting 设置视频验证
     * all_user_Agreement 用户协议
     * all_submit 提交
     */
    private Button all_id_setting, all_bank_CarSetting, all_addrs_setting, all_user_setting,
            all_Verification_video_setting, all_user_Agreement, all_submit;

    /**
     * all_livecity 居住地址
     * all_workcity 工作地址
     * all_workName 单位名称
     * all_workPhone 单位电话
     * all_Family 家人
     * all_Friend 朋友
     * all_Colleague 同事
     */
    private TextView all_livecity, all_workcity, all_workName, all_workPhone, all_Family, all_Friend, all_Colleague;
    /**
     * 同意协议
     */
    private CheckBox all_user_selector_Agreement;

    /**
     * all_id_layout 身份布局
     * all_bankCard_layout 银行布局
     * all_addrs_layout  地址布局
     * all_info_layout  信息布局
     * all_videoData_layout  验证信息布局
     */
    private LinearLayout all_id_layout, all_bankCard_layout, all_addrs_layout, all_info_layout, all_videoData_layout;
    private RelativeLayout all_confirm_layout;
    private final int REQUEST_MESSAGE_TAG_1 = 0X9001;

    private RelativeLayout all_bankCard_Round_layout;
    private LinearLayout llAddCard;
    private final int REQUEST_MESSAGE_TAG_2 = 0X9002;
    private final int REQUEST_MESSAGE_TAG_3 = 0X9003;

    private String instId = "instId";
    private AuthInfo authInfo;
    private AddressContent addressContent = new AddressContent();

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_productall_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        llAddCard = (LinearLayout) fgView.findViewById(R.id.ll_add_card);
        all_id_img = (ImageView) fgView.findViewById(R.id.all_id_img);
        all_id_img_status = (ImageView) fgView.findViewById(R.id.all_id_img_status);
        all_bank_imgShow = (ImageView) fgView.findViewById(R.id.all_bank_imgShow);
        all_Verification_img = (ImageView) fgView.findViewById(R.id.all_Verification_img);
        all_Verification_img_status = (ImageView) fgView.findViewById(R.id.all_Verification_img_status);
        all_Verification_video = (ImageView) fgView.findViewById(R.id.all_Verification_video);
        all_Verification_video_status = (ImageView) fgView.findViewById(R.id.all_Verification_video_status);

        all_Name_TvShow = (TextView) fgView.findViewById(R.id.all_Name_TvShow);
        all_bank_type = (TextView) fgView.findViewById(R.id.all_bank_type);
        all_IdCard_TvShow = (TextView) fgView.findViewById(R.id.all_IdCard_TvShow);
        all_bank_CarName = (TextView) fgView.findViewById(R.id.all_bank_CarName);
        all_bank_CarNum = (TextView) fgView.findViewById(R.id.all_bank_CarNum);
        all_liveaddrs = (TextView) fgView.findViewById(R.id.all_liveaddrs);
        all_workaddrs = (TextView) fgView.findViewById(R.id.all_workaddrs);
        all_Verification_img_tvShow = (TextView) fgView.findViewById(R.id.all_Verification_img_tvShow);
        all_Verification_video_tvShow = (TextView) fgView.findViewById(R.id.all_Verification_video_tvShow);

        all_id_setting = (Button) fgView.findViewById(R.id.all_id_setting);
        all_bank_CarSetting = (Button) fgView.findViewById(R.id.all_bank_CarSetting);
        all_addrs_setting = (Button) fgView.findViewById(R.id.all_addrs_setting);
        all_user_setting = (Button) fgView.findViewById(R.id.all_user_setting);
        all_Verification_video_setting = (Button) fgView.findViewById(R.id.all_Verification_video_setting);
        all_user_Agreement = (Button) fgView.findViewById(R.id.all_user_Agreement);
        all_submit = (Button) fgView.findViewById(R.id.all_submit);

        all_livecity = (TextView) fgView.findViewById(R.id.all_livecity);
        all_workcity = (TextView) fgView.findViewById(R.id.all_workcity);
        all_workName = (TextView) fgView.findViewById(R.id.all_workName);
        all_workPhone = (TextView) fgView.findViewById(R.id.all_workPhone);
        all_Family = (TextView) fgView.findViewById(R.id.all_Family);
        all_Friend = (TextView) fgView.findViewById(R.id.all_Friend);
        all_Colleague = (TextView) fgView.findViewById(R.id.all_Colleague);

        all_user_selector_Agreement = (CheckBox) fgView.findViewById(R.id.all_user_selector_Agreement);

        all_id_layout = (LinearLayout) fgView.findViewById(R.id.all_id_layout);
        all_bankCard_layout = (LinearLayout) fgView.findViewById(R.id.all_bankCard_layout);
        all_addrs_layout = (LinearLayout) fgView.findViewById(R.id.all_addrs_layout);
        all_info_layout = (LinearLayout) fgView.findViewById(R.id.all_info_layout);
        all_videoData_layout = (LinearLayout) fgView.findViewById(R.id.all_videoData_layout);
        all_confirm_layout = (RelativeLayout) fgView.findViewById(R.id.all_confirm_layout);

        all_bankCard_Round_layout = (RelativeLayout) fgView.findViewById(R.id.all_bankCard_Round_layout);
    }

    @Override
    protected void initEvent() {
        all_id_setting.setOnClickListener(this);
        all_bank_CarSetting.setOnClickListener(this);
        llAddCard.setOnClickListener(this);
        all_addrs_setting.setOnClickListener(this);
        all_user_setting.setOnClickListener(this);
        all_Verification_video_setting.setOnClickListener(this);
        all_user_Agreement.setOnClickListener(this);
        all_submit.setOnClickListener(this);

        setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                getActivity().finish();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getAuthInfo();
    }


    /**
     * 请求author info
     */
    private void getAuthInfo() {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        queryMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().queryUserAuthInfo)).id(REQUEST_MESSAGE_TAG_1)
                .content(StringUtils.toJsonString(queryMap)).build().execute(new StringCallback() {
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
                if (response.equals("")) {
                    showProgressDailog();
                    return;
                }

                String data = StringUtils.getResponseData(response);
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(data);
                    String info = obj.optString("authDetailInfos");
                    authInfo = JSON.parseObject(info, AuthInfo.class);
                    if (authInfo == null) return;
                    showAuthInfo(authInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 展示author info
     * "A100000", "身份证认证"
     * "A101000", "银行卡认证"
     * "A102000", "联系地址认证"
     * "A103000", "联系人认证"
     * "A104000", "图片对比"
     * "A104001", "视频对比"
     *
     * @param authInfo
     */
    private void showAuthInfo(AuthInfo authInfo) {

        //身份证
        IdAuthState idAuthState = authInfo.getA100000();
        IdAuthState.IdContent idContent = idAuthState.getContent();
        all_Name_TvShow.setText(idContent.getName());
        all_IdCard_TvShow.setText(idContent.getCertNo());
        if (AUTH_REJUST.equals(idAuthState.getAuthState()) || (TextUtils.isEmpty(idAuthState.getAuthState()))) {
            all_id_img_status.setImageResource(R.drawable.icon_failed);
            all_id_setting.setVisibility(View.VISIBLE);
            all_id_setting.setText(R.string.upload_file);
        } else {
            all_id_img_status.setImageResource(R.drawable.icon_fixed);
            all_id_setting.setVisibility(View.VISIBLE);
            all_id_setting.setText(R.string.setting);
        }


        //银行卡
        CardAuthState a101000 = authInfo.getA101000();
        CardAuthState.CardContent cardContent = a101000.getContent();
        if (TextUtils.isEmpty(cardContent.getInstName())) {
            all_bank_imgShow.setImageResource(R.color.transparentColor);
            all_bankCard_Round_layout.setBackground(getResources().getDrawable(R.drawable.product_card_bg));
            all_bank_CarName.setText("");
            llAddCard.setVisibility(View.VISIBLE);
            all_bank_CarSetting.setVisibility(View.GONE);
        } else {
            all_bank_CarName.setText(cardContent.getInstName());
            all_bank_CarNum.setText(cardContent.getCardIdxNo());
            instId = cardContent.getInstId();
            llAddCard.setVisibility(View.GONE);
            all_bank_CarSetting.setVisibility(View.GONE);
            all_id_setting.setVisibility(View.GONE);
            getBankList();
        }

        //联系地址
        AddrAuthState a102000 = authInfo.getA102000();
        AddrAuthState.AddrContent addrContent = a102000.getContent();
        if (TextUtils.isEmpty(addrContent.getLiveAddress())) {
            all_addrs_setting.setText(R.string.upload_file);
        } else {
            all_addrs_setting.setText(R.string.setting);
        }
        all_livecity.setText(addrContent.getLiveProvince() + addrContent.getLiveCity() + addrContent.getLiveDistrict());
        all_liveaddrs.setText(addrContent.getLiveAddress());
        all_workcity.setText(addrContent.getWorkProvince() + addrContent.getWorkCity() + addrContent.getWorkDistrict());
        all_workaddrs.setText(addrContent.getWorkAddress());
        all_workName.setText(addrContent.getCompanyName());
        all_workPhone.setText(addrContent.getWorkPhone());

        addressContent.setLiveProvince(addrContent.getLiveProvince());
        addressContent.setLiveCity(addrContent.getLiveCity());
        addressContent.setLiveDistrict(addrContent.getLiveDistrict());
        addressContent.setLiveAddress(addrContent.getLiveAddress());
        addressContent.setWorkProvince(addrContent.getWorkProvince());
        addressContent.setWorkCity(addrContent.getWorkCity());
        addressContent.setWorkDistrict(addrContent.getWorkDistrict());
        addressContent.setWorkAddress(addrContent.getWorkAddress());
        addressContent.setCompanyName(addrContent.getCompanyName());
        addressContent.setWorkPhone(addrContent.getWorkPhone());
        addressContent.setLiveProvinceCode(addrContent.getLiveProvinceCode());
        addressContent.setLiveCityCode(addrContent.getLiveCityCode());
        addressContent.setLiveDistrictCode(addrContent.getLiveDistrictCode());
        addressContent.setWorkProvinceCode(addrContent.getWorkProvinceCode());
        addressContent.setWorkCityCode(addrContent.getWorkCityCode());
        addressContent.setWorkDistrictCode(addrContent.getWorkDistrictCode());

        //紧急联系人
        //“01”----  家人
        //“02”----  朋友
        //“03”----  同事
        PhoneAuthState a103000 = authInfo.getA103000();
        List<PhoneAuthState.PhoneContent> phoneContents = a103000.getContent();

        if (phoneContents.isEmpty()) {
            if (TextUtils.isEmpty(addrContent.getLiveAddress())) {
                all_addrs_setting.setText(R.string.upload_file);
            } else {
                all_addrs_setting.setText(R.string.setting);
            }
        }

        for (PhoneAuthState.PhoneContent c : phoneContents) {
            if ("01".equals(c.getType())) {
                all_Family.setText(c.getName() + " " + c.getMobile());
                addressContent.setFamilyName(c.getName());
                addressContent.setFamilyPhone(c.getMobile());
            }

            if ("02".equals(c.getType())) {
                all_Friend.setText(c.getName() + " " + c.getMobile());
                addressContent.setFriendName(c.getName());
                addressContent.setFriendPhone(c.getMobile());
            }

            if ("03".equals(c.getType())) {
                all_Colleague.setText(c.getName() + " " + c.getMobile());
                addressContent.setColleagueName(c.getName());
                addressContent.setColleaguePhone(c.getMobile());
            }
        }

        //图片对比
        //A104000
        VideoAuthState videoAuthState = authInfo.getA104000();
        String photoState = videoAuthState.getAuthState();
        if (AUTH_REJUST.equals(photoState) || TextUtils.isEmpty(photoState)) {
            all_Verification_img_status.setImageResource(R.drawable.icon_failed);
        } else {
            all_Verification_img_status.setImageResource(R.drawable.icon_fixed);
        }

        all_Verification_video_setting.setText(R.string.setting);
        if (TextUtils.isEmpty(photoState)) {
            all_Verification_img_tvShow.setText(R.string.all_Product_message_11);
            all_Verification_video_setting.setText(R.string.upload_file);
        } else if (photoState.equals("P")) {
            all_Verification_img_tvShow.setText(R.string.all_Product_message_14);
        } else if (photoState.equals("R")) {
            all_Verification_img_tvShow.setText(R.string.all_Product_message_15);
        } else {
            all_Verification_img_tvShow.setText(R.string.all_Product_message_13);
        }

        //视频对比
        //A104001
        //"I", "初始化"
        //"S", "提交申请"
        //"C", "待审核"
        //"R", "拒绝"
        //"P", "审核通过"


        VideoCompare a104001 = authInfo.getA104001();
        String videoState = a104001.getAuthState();
        if (videoState.equals(AUTH_REJUST) || TextUtils.isEmpty(videoState)) {
            all_Verification_video_status.setImageResource(R.drawable.icon_failed);
        } else {
            all_Verification_video_status.setImageResource(R.drawable.icon_fixed);
        }

        all_Verification_video_setting.setText(R.string.setting);
        if (TextUtils.isEmpty(videoState)) {
            all_Verification_video_tvShow.setText(R.string.all_Product_message_12);
            all_Verification_video_setting.setText(R.string.upload_file);
        } else if ("P".equals(videoState)) {
            all_Verification_video_tvShow.setText(R.string.all_Product_message_17);
        } else if ("R".equals(videoState)) {
            all_Verification_video_tvShow.setText(R.string.all_Product_message_18);
        } else {
            all_Verification_video_tvShow.setText(R.string.all_Product_message_16);
        }
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


                    if (!StringUtils.isBlank(instId)) {
                        Glide.with(ProductSummaryFragment.this).load(AppConfig.getInstance().BASE_BANK + "/" + instId + ".png").into(all_bank_imgShow);
                        setViewRoundBack(object.optString(instId));
                    }
                } catch (JSONException e) {
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
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.all_id_setting:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_1);
                ServiceBaseActivity.startActivity(getActivity(), PersonalIdCarFragment.class.getName(), bundle);
                break;

            case R.id.all_bank_CarSetting:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_2);
                ServiceBaseActivity.startActivity(getActivity(), BindCardFragment.class.getName(), bundle);
                break;

            case R.id.ll_add_card:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_2);
                ServiceBaseActivity.startActivity(getActivity(), BindCardFragment.class.getName(), bundle);
                break;

            case R.id.all_addrs_setting:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_3);
                bundle.putParcelable("value", addressContent);
                ServiceBaseActivity.startActivity(getActivity(), BasicInfoFragment.class.getName(), bundle);
                break;

            case R.id.all_user_setting:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_4);
                bundle.putParcelable("value", addressContent);
                ServiceBaseActivity.startActivity(getActivity(), ContactsFragment.class.getName(), bundle);
                break;

            case R.id.all_Verification_video_setting:
                bundle.putString("tag", AppConfig.getInstance().JUMP_MESSAGE_TAG_5);
                ServiceBaseActivity.startActivity(getActivity(), VideoAuthenticationFragment.class.getName(), bundle);
                break;

            case R.id.all_user_Agreement:
                SummaryPop summaryPop = new SummaryPop(getActivity());
                summaryPop.showAtLocation(all_confirm_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                summaryPop.setChecked(all_user_selector_Agreement.isChecked());
                summaryPop.setISummary(this);
//                bundle.putString("url", AppConfig.getInstance().LOAN_URL);
//                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), bundle);
                break;

            case R.id.all_submit:
                showDialog();
                break;
        }
    }

    /**
     * 设置银行卡圆角背景
     */
    private void setViewRoundBack(String color) {
        /**
         * 外部矩形弧度
         */
        float[] outerRadian = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadian, null, null);
        ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
        /**
         * 指定填充颜色
         */
        drawable.getPaint().setColor(Color.parseColor(color));
        /**
         * 指定填充模式
         */
        drawable.getPaint().setStyle(Paint.Style.FILL);
        all_bankCard_Round_layout.setBackground(drawable);
    }


    /**
     * 显示对话框
     */
    private void showDialog(){
        if (!all_user_selector_Agreement.isChecked()) {
            ToastUtils.shortShow(R.string.all_Product_message_19);
            return;
        }
        DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.all_Product_message_20,
                R.string.all_Product_message_21, R.string.all_Product_message_22, new CustomDialog.OnSureInterface() {
                    @Override
                    public void getOnSure() {
                        upLoadInfo();
                    }

                    @Override
                    public void getOnDesmiss() {

                    }
                });
    }



    /**
     * 提交开户
     */
    private void upLoadInfo() {
        showProgressDailog();
        Map<String, Object> submitMap = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
        String tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
        submitMap.put("userId", userId);
        submitMap.put("tokenId", tokenId);
        requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().submitAuthInfo, submitMap);
    }


    @Override
    public void upLoad() {
        showDialog();
    }

    @Override
    public void setChecked(boolean isChecked) {
        all_user_selector_Agreement.setChecked(isChecked);
    }
}
