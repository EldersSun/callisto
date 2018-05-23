package com.miaodao.Fragment.Loan.Apply;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Loan.Apply.bean.BindCard;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Channels;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ArrayUtils;
import com.miaodao.Sys.Utils.BankCardWatcherGetInfo;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.Citypickerview.widget.CityPicker;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.CustomSelectorDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.DialogShowImgCode;
import com.miaodao.Utils.BindCardAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 申请-绑卡页面
 * Created by Home_Pc on 2017/3/13.
 */

public class BindCardFragment extends ContentBaseFragment implements View.OnClickListener {

    private Button bind_BankCode, bind_submit;
    private TextView bind_Bank, bind_City, tv_scale_num;
    private EditText bind_BankCard, bind_BankPhone, bind_BankInputCode;
    private RelativeLayout show_bind_city;
    private RecyclerView rvBank;

    private GetCode getCode = new GetCode(AppConfig.getInstance().millisInFuture, AppConfig.getInstance().countDownInterval);
    private boolean isTimer = false;
    private CityPicker cityPicker;
    private List<String> bankList = ArrayUtils.arrayToListForString(AppConfig.getInstance().Banks);

    private CustomSelectorDialog customSelectorDialog;
    private DialogShowImgCode dialogShowImgCode;
    private BindCardAdapter bindCardAdapter;

    private final int REQUEST_MESSAGE_TAG_1 = 0X1021;
    private final int REQUEST_MESSAGE_TAG_2 = 0X1022;
    private final int REQUEST_MESSAGE_TAG_3 = 0X1023;
    private final int REQUEST_MESSAGE_TAG_4 = 0X1024;
    private final int REQUEST_MESSAGE_TAG_5 = 0X1025;

    private String transId = "";
    private String decisionId = "";
    private String tag = "";
    private List<Channels> bank = new ArrayList<>();
    private BindCard bindCard;//用于保存绑定卡信息

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_bindcard_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        bindCard = DataSupport.findFirst(BindCard.class);
        title_menu.setBackgroundResource(R.color.transparentColor);
        title_menu.setText(R.string.bind_card_message_13);
        title_tvShow.setText(R.string.bind_card_message_14);

        bind_BankCard = (EditText) fgView.findViewById(R.id.bind_BankCard);
        bind_Bank = (TextView) fgView.findViewById(R.id.bind_Bank);
        bind_City = (TextView) fgView.findViewById(R.id.bind_City);
        tv_scale_num = (TextView) fgView.findViewById(R.id.tv_scale_num);
        show_bind_city = (RelativeLayout) fgView.findViewById(R.id.show_bind_city);
        bind_BankPhone = (EditText) fgView.findViewById(R.id.bind_BankPhone);
        bind_BankInputCode = (EditText) fgView.findViewById(R.id.bind_BankInputCode);
        rvBank = (RecyclerView) fgView.findViewById(R.id.rv_bank);

        bind_City.setText(WheatFinanceApplication.getInstance().getProvince() + "  " +
                WheatFinanceApplication.getInstance().getCity());

        showBindInfo();

        bind_BankCode = (Button) fgView.findViewById(R.id.bind_BankCode);
        bind_submit = (Button) fgView.findViewById(R.id.bind_submit);

//        bind_BankCard.addTextChangedListener(new BankCardWatcher(bind_BankCard));
//        bind_BankCard.addTextChangedListener(new BankCardWatcherGetInfo(getActivity(), bind_BankCard, bind_Bank, 6));
        bind_BankCard.addTextChangedListener(new BankCardWatcherGetInfo(getActivity(), bind_BankCard, bind_Bank, tv_scale_num, 6));

        bindCardAdapter = new BindCardAdapter(getActivity());
        rvBank.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvBank.setAdapter(bindCardAdapter);
        getBindBankList();
    }

    /**
     * 展示保存的信息
     */
    private void showBindInfo() {

        if (bindCard == null) return;
        if (!TextUtils.isEmpty(bindCard.getCardNo())) {
            bind_BankCard.setText(bindCard.getCardNo());
            bind_BankCard.setSelection(bindCard.getCardNo().length());
            tv_scale_num.setText(bindCard.getCardNo());
            tv_scale_num.setVisibility(View.VISIBLE);
        }
        bind_Bank.setText(bindCard.getBankName());
        bind_City.setText(bindCard.getBankCity());
        bind_BankPhone.setText(bindCard.getPhoneNo());
    }


    /**
     * 网络请求支持的银行卡列表
     */
    private void getBindBankList() {

        Map<String, Object> bankList = new HashMap<>();

        String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
        String tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.shortShow(getString(R.string.relogin));
            return;
        }
        bankList.put("userId", userId);
        bankList.put("tokenId", tokenId);
//        requestForHttp(REQUEST_MESSAGE_TAG_5, AppConfig.getInstance().BIND_CARD_BANK_LIST, bankList);

        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().BIND_CARD_BANK_LIST))
                .content(StringUtils.toJsonString(bankList)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response))
                    return;
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.optString("data");
                    if (TextUtils.isEmpty(data))
                        return;
                    JSONObject obj = new JSONObject(data);
                    String channels = obj.optString("channels");
                    if (TextUtils.isEmpty(channels))
                        return;
                    bank = JSON.parseArray(channels, Channels.class);
                    bindCardAdapter.setChannelsList(bank);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        bind_Bank.setOnClickListener(this);
        show_bind_city.setOnClickListener(this);
//        bind_City.setOnClickListener(this);
        bind_BankCode.setOnClickListener(this);
        bind_submit.setOnClickListener(this);


        Bundle bundle = getArguments();
        if (bundle.containsKey("tag")) {
            tag = bundle.getString("tag");
        }

        setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.backMessage,
                        R.string.cancel, R.string.ok, new CustomDialog.OnSureInterface() {
                            @Override
                            public void getOnSure() {
                                if (StringUtils.isBlank(tag)) {
                                    ServiceBaseActivity.startActivity(getActivity(), ProductSummaryFragment.class.getName());
                                }
                                getActivity().finish();
                            }

                            @Override
                            public void getOnDesmiss() {

                            }
                        });
            }
        });

        customSelectorDialog = new CustomSelectorDialog(getActivity(), getResources().getString(R.string.selectorBank), bankList);

        cityPicker = new CityPicker.Builder(getActivity()).textSize(20)
                .title(getResources().getString(R.string.bind_card_message_15))
                .titleBackgroundColor(AppConfig.getInstance().PickerTitleBackColor)
                .titleTextColor(AppConfig.getInstance().black)
                .confirTextColor(AppConfig.getInstance().black)
                .cancelTextColor(AppConfig.getInstance().black)
                .onlyShowProvinceAndCity(true)
                .province(AppConfig.getInstance().defauleProvince)
                .city(AppConfig.getInstance().defauleCity)
                .district(AppConfig.getInstance().defauleDistrict)
                .textColor(Color.parseColor(AppConfig.getInstance().black))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();

        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                bind_City.setText(citySelected[0] + "  " + citySelected[1]
                        + "  " + citySelected[2]);
            }

            @Override
            public void onSelectedCode(String... cityCode) {

            }
        });


        customSelectorDialog.setOnDialogOperationclick(new CustomSelectorDialog.onCustomDialogOperationclick() {
            @Override
            public void Confirm(String dateString) {
                bind_Bank.setText(dateString);
            }

            @Override
            public void cancel() {

            }
        });

        bind_BankCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tv_scale_num.setVisibility(View.GONE);
                    getBank();
                } else {
                    tv_scale_num.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogShowImgCode = new DialogShowImgCode(getActivity());

        dialogShowImgCode.setOnDialogOperationclick(new DialogShowImgCode.onDialogOperationclick() {
            @Override
            public void Confirm(String imgCode) {

                if (StringUtils.isBlank(imgCode)) {
                    ToastUtils.shortShow(R.string.reg_inputcodeHint);
                    changeSMSBtn();
                    getCode.cancel();
                    return;
                }

                if (StringUtils.isBlank(bind_BankCard.getText().toString())) {
                    ToastUtils.shortShow(R.string.bind_card_message_7);
                    changeSMSBtn();
                    getCode.cancel();
                    return;
                }
                if (StringUtils.isBlank(bind_BankPhone.getText().toString())) {
                    ToastUtils.shortShow(R.string.bind_card_message_10);
                    changeSMSBtn();
                    getCode.cancel();
                    return;
                }

                Map<String, Object> codeMap = new HashMap<>();
                codeMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                codeMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                codeMap.put("cardMobile", bind_BankPhone.getText().toString());
                codeMap.put("cardNo", bind_BankCard.getText().toString());
                codeMap.put("imgCode", imgCode);
                requestForHttp(REQUEST_MESSAGE_TAG_3, AppConfig.getInstance().sendBindCardSmsCode, codeMap);
            }

            @Override
            public void cancel() {
                changeSMSBtn();
                getCode.cancel();
            }

            @Override
            public void reGet() {
                startGetSMS();
            }
        });
    }


    /**
     * 获取银行
     */
    private void getBank() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        map.put("cardNo", bind_BankCard.getText().toString().replaceAll(" ", ""));
        requestForHttp(REQUEST_MESSAGE_TAG_1, AppConfig.getInstance().queryBankByNo, map);

//        OkHttpUtils.post().content(StringUtils.toJsonString(map)).url(StringUtils.toUtl(AppConfig.getInstance().queryBankByNo))
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(String msg, Call call, Exception e, int id) {
//                if (!TextUtils.isEmpty(msg)) {
//                    ToastUtils.shortShow(msg);
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                if (TextUtils.isEmpty(response))
//                    return;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String data = object.optString("data");
//                    if (data != null) {
//                        JSONObject dataObject = new JSONObject(data);
//                        String info = dataObject.optString("cardInfo");
//                        BankBin card = JSON.parseObject(info, BankBin.class);
//                        if (card != null) {
//                            String instId = card.getInstId();
//                            for (Channels channel : bank) {
//                                if (channel.getInstId().equals(instId)) {
//                                    bind_Bank.setText(card.getBankName());
//                                    break;
//                                }
//                            }
//                            if (TextUtils.isEmpty(bind_Bank.getText().toString())) {
//                                ToastUtils.shortShow("不支持的银行卡");
//                            }
//                        } else {
//                            if (TextUtils.isEmpty(bind_Bank.getText().toString())) {
//                                ToastUtils.shortShow("不支持的银行卡");
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });


    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {

            case REQUEST_MESSAGE_TAG_1:
                Map<String, Object> resultMap = (Map<String, Object>) result;
                if (resultMap.containsKey("data")) {
                    Map<String, Map<String, String>> dataMap = (Map<String, Map<String, String>>) resultMap.get("data");
                    if (dataMap.containsKey("cardInfo")) {
                        Map<String, String> cardInfo = dataMap.get("cardInfo");
                        if (cardInfo.containsKey("bankName")) {

                            for (Channels channel : bank) {
                                if (channel.getInstId().equals(cardInfo.get("instId"))) {
                                    bind_Bank.setText(cardInfo.get("bankName"));
                                    break;
                                }
                            }

                            if (TextUtils.isEmpty(bind_Bank.getText().toString())) {
                                ToastUtils.shortShow("不支持的银行卡");
                            }

                        }
                    }
                }
                break;

            case REQUEST_MESSAGE_TAG_2:
                Map<String, Object> getCodeMap = (Map<String, Object>) result;
                if (getCodeMap != null && !StringUtils.isBlank(getCodeMap.get("code").toString())
                        && getCodeMap.get("code").toString().equals(AppConfig.getInstance().REQUEST_SUCC_CODE)) {
                    Map<String, String> imgMap = (Map<String, String>) getCodeMap.get("data");
                    String str = imgMap.get("imgCode");
                    if (!StringUtils.isBlank(str)) {
                        Bitmap bitmap = ImgUtils.base64ToBitmap(str);
                        if (bitmap != null) {
                            dialogShowImgCode.showImgDialog(bitmap);
                        }
                    }
                }
                break;

            case REQUEST_MESSAGE_TAG_3:
                Map<String, Object> regResultMap = (Map<String, Object>) result;
                if (regResultMap.containsKey("msg")) {
                    ToastUtils.shortShow(regResultMap.get("msg").toString());
                }
                if (regResultMap.containsKey("data")) {
                    Map<String, String> dataMap = (Map<String, String>) regResultMap.get("data");
                    if (dataMap.containsKey("transId")) {
                        transId = dataMap.get("transId");
                    }

                    if (dataMap.containsKey("decisionId")) {
                        decisionId = dataMap.get("decisionId");
                    }
                }
                break;

            case REQUEST_MESSAGE_TAG_4:
                dismissProressDialog();
                ToastUtils.shortShow(R.string.submitSucc);

                //提交成功后，删除缓存
                DataSupport.deleteAll(BindCard.class);

                if (StringUtils.isBlank(tag)) {
                    ServiceBaseActivity.startActivity(getActivity(), BasicInfoFragment.class.getName());
                } else {
                    sendMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_2, "");
                }
                getActivity().finish();
                break;

            case REQUEST_MESSAGE_TAG_5:
//                Map<String, Object> bank = (Map<String, Object>) result;
//                if (bank.get("data").equals(null)) return;
//                Map<String, Object> contant = (Map<String, Object>) bank.get("data");
//                if (contant.get("channels").equals(null)) return;
//                showBankList(bank);

                break;

        }
    }


//    /**
//     * 显示支持的银行列表
//     *
//     * @param bank
//     */
//    private void showBankList(Map<String, Object> bank) {
//
//        Map<String, Object> data = (Map<String, Object>) bank.get("data");
//        List<Map<String, Object>> banks = (List<Map<String, Object>>) data.get("channels");
//        List<Channels> channelsList = new ArrayList<>();
//
//        for (Map<String, Object> b : banks) {
//            try {
//                Channels channels = MapUtils.mapToBean(b, Channels.class);
//                channelsList.add(channels);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (channelsList.isEmpty()) return;
//        bindCardAdapter.setChannelsList(channelsList);
//    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_1:
                break;

            case REQUEST_MESSAGE_TAG_3:
                ToastUtils.shortShow(result == null ? "" : result);
                if (getCode != null) {
                    getCode.cancel();
                }
                changeSMSBtn();
                break;

            default:
                ToastUtils.shortShow(result == null ? "" : result);
                break;
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

//    @Override
//    public void onContentClick(View v) {
//        switch (v.getId()) {
//            case R.id.bind_Bank:
////                customSelectorDialog.show();
//                break;
//            case R.id.bind_City:
//                cityPicker.show();
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_BankCode:
                startGetSMS();
                break;

            case R.id.show_bind_city:
                cityPicker.show();
                tv_scale_num.setVisibility(View.GONE);
                getBank();
                break;

            case R.id.bind_submit:
                if (StringUtils.isBlank(bind_BankCard.getText().toString())) {
                    ToastUtils.shortShow(R.string.bind_card_message_7);
                    return;
                }
                if (StringUtils.isBlank(bind_City.getText().toString().replaceAll("  ", ""))) {
                    ToastUtils.shortShow(R.string.bind_card_message_15);
                    return;
                }
                if (StringUtils.isBlank(bind_BankPhone.getText().toString())) {
                    ToastUtils.shortShow(R.string.bind_card_message_10);
                    return;
                }
                if (StringUtils.isBlank(bind_BankInputCode.getText().toString())) {
                    ToastUtils.shortShow(R.string.bind_card_message_11);
                    return;
                }
                Map<String, Object> submitMap = new HashMap<>();
                submitMap.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                submitMap.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                submitMap.put("cardMobile", bind_BankPhone.getText().toString());
                submitMap.put("cardNo", bind_BankCard.getText().toString());
                submitMap.put("decisionId", decisionId);
                submitMap.put("transId", transId);
                submitMap.put("verifyCode", bind_BankInputCode.getText().toString());
                requestForHttp(REQUEST_MESSAGE_TAG_4, AppConfig.getInstance().bindCardAuth, submitMap);
                showProgressDailog();
                break;
        }
    }


    /**
     * 保存信息
     */
    private void saveBindCardInfo() {

        if (bindCard == null) {
            bindCard = new BindCard();
            bindCard.setCardNo(bind_BankCard.getText().toString());
            bindCard.setBankCity(bind_City.getText().toString());
            bindCard.setBankName(bind_Bank.getText().toString());
            bindCard.setPhoneNo(bind_BankPhone.getText().toString());
            bindCard.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("cardNo", bind_BankCard.getText().toString());
            values.put("bankCity", bind_City.getText().toString());
            values.put("bankName", bind_Bank.getText().toString());
            values.put("phoneNo", bind_BankPhone.getText().toString());
            DataSupport.updateAll(BindCard.class, values);
        }
    }


    /**
     * 开始发送短信
     */
    private void startGetSMS() {
        bind_BankCode.setEnabled(false);
        bind_BankCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_selector_true));

        getCode();
    }

    private void getCode() {
        if (StringUtils.isBlank(bind_BankPhone.getText().toString())) {
            ToastUtils.shortShow(R.string.bind_card_message_10);
            return;
        }
//        saveBindCardInfo();
        getCode.start();
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", bind_BankPhone.getText().toString());
        map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        map.put("type", "5");
        requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().getImgCode, map);
    }

    private class GetCode extends CountDownTimer {
        public GetCode(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bind_BankCode.setText(millisUntilFinished / 1000 + getResources().getString(R.string.RestartGet));
        }

        @Override
        public void onFinish() {
            changeSMSBtn();
        }
    }


    /**
     * 改变button状态
     */
    private void changeSMSBtn() {
        bind_BankCode.setEnabled(true);
        bind_BankCode.setBackground(getResources().getDrawable(R.drawable.btn_shape_backgroup_color));
        bind_BankCode.setText(R.string.RestartGet_1);
    }


    @Override
    public void onStop() {
        super.onStop();
        saveBindCardInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getCode != null) {
            getCode.cancel();
        }
    }

    @Override
    protected boolean OnkeyDownListener(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.backMessage,
                    R.string.cancel, R.string.ok, new CustomDialog.OnSureInterface() {
                        @Override
                        public void getOnSure() {
                            if (StringUtils.isBlank(tag)) {
                                ServiceBaseActivity.startActivity(getActivity(), ProductSummaryFragment.class.getName());
                            }
                            getActivity().finish();
                        }

                        @Override
                        public void getOnDesmiss() {

                        }
                    });
            return true;
        }
        return super.OnkeyDownListener(keyCode, event);
    }
}
