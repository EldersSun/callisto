package com.miaodao.Fragment.Loan.Apply;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.miaodao.Fragment.Product.bean.AddressContent;
import com.miaodao.Fragment.WebViewFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Contact;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.Citypickerview.widget.CityPicker;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本信息
 * Created by Home_Pc on 2017/3/14.
 */

public class BasicInfoFragment extends ContentBaseFragment implements View.OnClickListener {
    private RelativeLayout show_address, work_address;
    private TextView basic_city, basic_workAddrs;
    private EditText basic_infoAddrs, basic_workName, basic_workPhone;
    private Button basic_submit, basic_inputStandard;

    private CityPicker liveCityPicker;
    private CityPicker workCityPicker;

    private Contact contact = new Contact();
    private final int REQUEST_MESSAGE_TAG = 0X1031;
    private String tag = "";
    private AddressContent addressContent;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_basicinfo_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_menu.setBackgroundResource(R.color.transparentColor);
        title_menu.setText(R.string.basic_message_2);
        title_tvShow.setText(R.string.basic_message_1);

        basic_city = (TextView) fgView.findViewById(R.id.basic_city);
        show_address = (RelativeLayout) fgView.findViewById(R.id.show_address);
        work_address = (RelativeLayout) fgView.findViewById(R.id.work_address);
        basic_workAddrs = (TextView) fgView.findViewById(R.id.basic_workAddrs);
        basic_workName = (EditText) fgView.findViewById(R.id.basic_workName);
        basic_workPhone = (EditText) fgView.findViewById(R.id.basic_workPhone);

        basic_infoAddrs = (EditText) fgView.findViewById(R.id.basic_infoAddrs);
        basic_submit = (Button) fgView.findViewById(R.id.basic_submit);
        basic_inputStandard = (Button) fgView.findViewById(R.id.basic_inputStandard);


        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = bundle.getString("tag");
            addressContent = bundle.getParcelable("value");
            showInfo();
        }

        addressContent = DataSupport.findFirst(AddressContent.class);
        showInfo();
    }

    private void showInfo() {
        if (addressContent == null)
            return;

        basic_city.setText(addressContent.getLiveProvince() + "  " + addressContent.getLiveCity()
                + "  " + addressContent.getLiveDistrict());
        basic_infoAddrs.setText(addressContent.getLiveAddress());

        basic_workAddrs.setText(addressContent.getWorkProvince() + "  " + addressContent.getWorkCity()
                + "  " + addressContent.getWorkDistrict());

        basic_workName.setText(addressContent.getCompanyName());
        basic_workPhone.setText(addressContent.getWorkPhone());

        contact.setLiveProvince(addressContent.getLiveProvince());
        contact.setLiveCity(addressContent.getLiveCity());
        contact.setLiveDistrict(addressContent.getLiveDistrict());
        contact.setLiveDistrictCode(addressContent.getLiveDistrictCode());
        contact.setLiveCityCode(addressContent.getLiveDistrictCode());
        contact.setLiveProvinceCode(addressContent.getLiveProvinceCode());
        contact.setWorkDistrictCode(addressContent.getWorkDistrictCode());
        contact.setWorkCityCode(addressContent.getWorkDistrictCode());
        contact.setWorkProvinceCode(addressContent.getWorkProvinceCode());
        contact.setWorkProvince(addressContent.getWorkProvince());
        contact.setWorkCity(addressContent.getWorkCity());
        contact.setWorkDistrict(addressContent.getWorkDistrict());
        contact.setWorkAddress(addressContent.getWorkProvince() + addressContent.getWorkCity() + addressContent.getWorkDistrict());

    }


    @Override
    protected void initEvent() {

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

        basic_submit.setOnClickListener(this);
        basic_inputStandard.setOnClickListener(this);
        show_address.setOnClickListener(this);
        work_address.setOnClickListener(this);


        liveCityPicker = new CityPicker.Builder(getActivity()).textSize(20)
                .title(getResources().getString(R.string.basic_message_14))
                .titleBackgroundColor(AppConfig.getInstance().PickerTitleBackColor)
                .titleTextColor(AppConfig.getInstance().black)
                .confirTextColor(AppConfig.getInstance().black)
                .cancelTextColor(AppConfig.getInstance().black)
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

        workCityPicker = new CityPicker.Builder(getActivity()).textSize(20)
                .title(getResources().getString(R.string.basic_message_15))
                .titleBackgroundColor(AppConfig.getInstance().PickerTitleBackColor)
                .titleTextColor(AppConfig.getInstance().black)
                .confirTextColor(AppConfig.getInstance().black)
                .cancelTextColor(AppConfig.getInstance().black)
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

        liveCityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                basic_city.setText(citySelected[0] + "  " + citySelected[1]
                        + "  " + citySelected[2]);

                contact.setLiveProvince(citySelected[0]);
                contact.setLiveCity(citySelected[1]);
                contact.setLiveDistrict(citySelected[2]);
            }

            @Override
            public void onSelectedCode(String... cityCode) {
                contact.setLiveProvinceCode(cityCode[0]);
                contact.setLiveCityCode(cityCode[1]);
                contact.setLiveDistrictCode(cityCode[2]);

            }
        });

        workCityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                basic_workAddrs.setText(citySelected[0] + "  " + citySelected[1]
                        + "  " + citySelected[2]);
                contact.setWorkProvince(citySelected[0]);
                contact.setWorkCity(citySelected[1]);
                contact.setWorkDistrict(citySelected[2]);
                contact.setWorkAddress(citySelected[0] + citySelected[1]
                        + citySelected[2]);
            }

            @Override
            public void onSelectedCode(String... cityCode) {
                contact.setWorkProvinceCode(cityCode[0]);
                contact.setWorkCityCode(cityCode[1]);
                contact.setWorkDistrictCode(cityCode[2]);
            }
        });
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                dismissProressDialog();
                ToastUtils.shortShow(R.string.submitSucc);
                if (StringUtils.isBlank(tag)) {
                    ServiceBaseActivity.startActivity(getActivity(), ContactsFragment.class.getName());
                } else {
                    sendMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_2, "");
                }
                getActivity().finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basic_submit:
                if (StringUtils.isBlank(basic_city.getText().toString().replaceAll(" ", ""))) {//居住地址
                    ToastUtils.shortShow(R.string.basic_message_14);
                    return;
                }

                if (StringUtils.isBlank(basic_infoAddrs.getText().toString())) {//详细居住地址
                    ToastUtils.shortShow(R.string.basic_message_16);
                    return;
                }

                if (StringUtils.isBlank(basic_workAddrs.getText().toString().replaceAll(" ", ""))) {//工作地址
                    ToastUtils.shortShow(R.string.basic_message_15);
                    return;
                }
                if (StringUtils.isBlank(basic_workName.getText().toString())) {//单位名称
                    ToastUtils.shortShow(R.string.basic_message_17);
                    return;
                }
                if (StringUtils.isBlank(basic_workPhone.getText().toString())) {//单位电话
                    ToastUtils.shortShow(R.string.basic_message_18);
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
                String tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
                map.put("userId", userId);
                map.put("tokenId", tokenId);
                contact.setUserId(userId);
                contact.setLiveAddress(basic_infoAddrs.getText().toString());
                contact.setCompanyName(basic_workName.getText().toString());
                contact.setWorkPhone(basic_workPhone.getText().toString());
                map.put("contact", contact);
                this.requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().contactsAuth, map);
                showProgressDailog();
                break;
            case R.id.basic_inputStandard:
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConfig.getInstance().input_Standard_url);
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), bundle);
                break;
            case R.id.show_address:
                liveCityPicker.show();
                break;
            case R.id.work_address:
                workCityPicker.show();
                break;

            default:
                break;
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


    @Override
    public void onStop() {
        super.onStop();
        saveAddress();
    }


    /**
     * 保存编辑信息
     */
    private void saveAddress() {

        if (addressContent == null) {
            addressContent = new AddressContent();
            addressContent.setLiveProvince(contact.getLiveProvince());
            addressContent.setLiveProvinceCode(contact.getLiveProvinceCode());
            addressContent.setLiveCity(contact.getLiveCity());
            addressContent.setLiveCityCode(contact.getLiveCityCode());
            addressContent.setLiveDistrict(contact.getLiveDistrict());
            addressContent.setLiveDistrictCode(contact.getLiveDistrictCode());

            addressContent.setWorkProvince(contact.getWorkProvince());
            addressContent.setWorkProvinceCode(contact.getWorkProvinceCode());
            addressContent.setWorkCity(contact.getWorkCity());
            addressContent.setWorkCityCode(contact.getWorkCityCode());
            addressContent.setWorkDistrict(contact.getWorkDistrict());
            addressContent.setWorkDistrictCode(contact.getWorkDistrictCode());

            addressContent.setCompanyName(basic_workName.getText().toString());
            addressContent.setLiveAddress(basic_infoAddrs.getText().toString());
            addressContent.setWorkPhone(basic_workPhone.getText().toString());
            addressContent.setWorkAddress(contact.getWorkAddress());

            addressContent.save();
        } else {
            ContentValues values = new ContentValues();

            values.put("liveProvince", contact.getLiveProvince());
            values.put("liveProvinceCode", contact.getLiveProvinceCode());
            values.put("liveCity", contact.getLiveCity());
            values.put("liveCityCode", contact.getLiveCityCode());
            values.put("liveDistrict", contact.getLiveDistrict());
            values.put("liveDistrictCode", contact.getLiveDistrictCode());
            values.put("workProvince", contact.getWorkProvince());
            values.put("workProvinceCode", contact.getWorkProvinceCode());
            values.put("workCity", contact.getWorkCity());
            values.put("workCityCode", contact.getWorkCityCode());
            values.put("workDistrict", contact.getWorkDistrict());
            values.put("workDistrictCode", contact.getWorkDistrictCode());
            values.put("workAddress", contact.getWorkAddress());
            values.put("companyName", basic_workName.getText().toString());
            values.put("liveAddress", basic_infoAddrs.getText().toString());
            values.put("workPhone", basic_workPhone.getText().toString());
            DataSupport.updateAll(AddressContent.class, values);
        }

    }
}
