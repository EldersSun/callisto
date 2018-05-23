package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.miaodao.Fragment.Product.bean.AddressContent;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.MobileLinkMan;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.CustomSelectorDialog;
import com.miaodao.Sys.Widgets.DialogHelp;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * 紧急联系人
 * Created by Home_Pc on 2017/3/14.
 */

public class ContactsFragment extends ContentBaseFragment implements View.OnClickListener {

    /**
     * 家人 朋友 同事
     */
    private EditText contacts_Family_name, contacts_Friend_name, contacts_Colleague_name;
    private TextView contacts_Family_phone, contacts_Friend_phone, contacts_Colleague_phone;
    private Button contacts_Family_selector, contacts_Friend_selector, contacts_Colleague_selector, contacts_submit;

    private String tag = "";

    /**
     * 库 phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, Phone.CONTACT_ID};

    /**
     * 家人
     */
    private final int GET_MAIL_MESSAGE_1 = 01;
    /**
     * 朋友
     */
    private final int GET_MAIL_MESSAGE_2 = 02;
    /**
     * 同事
     */
    private final int GET_MAIL_MESSAGE_3 = 03;

    private final int REQUEST_MESSAGE_TAG = 0X1041;

    private AddressContent addressContent;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_contacts_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_menu.setBackgroundResource(R.color.transparentColor);
        title_menu.setText(R.string.contacts_message_4);
        title_tvShow.setText(R.string.contacts_message_3);

        contacts_Family_name = (EditText) fgView.findViewById(R.id.contacts_Family_name);
        contacts_Friend_name = (EditText) fgView.findViewById(R.id.contacts_Friend_name);
        contacts_Colleague_name = (EditText) fgView.findViewById(R.id.contacts_Colleague_name);

        contacts_Family_selector = (Button) fgView.findViewById(R.id.contacts_Family_selector);
        contacts_Friend_selector = (Button) fgView.findViewById(R.id.contacts_Friend_selector);
        contacts_Colleague_selector = (Button) fgView.findViewById(R.id.contacts_Colleague_selector);
        contacts_submit = (Button) fgView.findViewById(R.id.contacts_submit);

        contacts_Family_phone = (TextView) fgView.findViewById(R.id.contacts_Family_phone);
        contacts_Friend_phone = (TextView) fgView.findViewById(R.id.contacts_Friend_phone);
        contacts_Colleague_phone = (TextView) fgView.findViewById(R.id.contacts_Colleague_phone);

        contacts_Family_selector.setOnClickListener(this);
        contacts_Friend_selector.setOnClickListener(this);
        contacts_Colleague_selector.setOnClickListener(this);
        contacts_submit.setOnClickListener(this);

        Bundle bundle = getArguments();

        if (bundle != null) {
            tag = bundle.getString("tag");
            addressContent = bundle.getParcelable("value");
            showInfo();
        }

        addressContent = DataSupport.findFirst(AddressContent.class);
        showInfo();
    }

    /**
     *
     */
    private void showInfo() {
        if (addressContent == null)
            return;

        if (!TextUtils.isEmpty(addressContent.getFamilyName())){
            contacts_Family_name.setText(addressContent.getFamilyName());
            contacts_Family_phone.setText(addressContent.getFamilyPhone());
        }

        if (!TextUtils.isEmpty(addressContent.getFriendName())){
            contacts_Friend_name.setText(addressContent.getFriendName());
            contacts_Friend_phone.setText(addressContent.getFriendPhone());
        }

        if (!TextUtils.isEmpty(addressContent.getColleagueName())){
            contacts_Colleague_name.setText(addressContent.getColleagueName());
            contacts_Colleague_phone.setText(addressContent.getColleaguePhone());
        }
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
    }

    @Override
    public void onStart() {
        super.onStart();
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                DialogHelp.getInstance(getActivity()).showDialog(R.string.lead_message, R.string.lead_message_2, R.string.cancel,
                        R.string.lead_message_3, new CustomDialog.OnSureInterface() {
                            @Override
                            public void getOnSure() {
                                goToAppSetting();
                            }

                            @Override
                            public void getOnDesmiss() {
                                getActivity().finish();
                            }
                        });
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                dismissProressDialog();
                ToastUtils.shortShow(R.string.submitSucc);
                DataSupport.deleteAll(AddressContent.class);
                if (StringUtils.isBlank(tag)) {
                    ServiceBaseActivity.startActivity(getActivity(), VideoAuthenticationFragment.class.getName());
                } else {
                    sendMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_2, "");
                }
                getActivity().finish();
                break;
        }
    }


    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 0x1001);
    }

    @Override
    public void onClick(View v) {
        phoneList.clear();
        switch (v.getId()) {
            case R.id.contacts_Family_selector:
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), GET_MAIL_MESSAGE_1);
                break;
            case R.id.contacts_Friend_selector:
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), GET_MAIL_MESSAGE_2);
                break;
            case R.id.contacts_Colleague_selector:
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), GET_MAIL_MESSAGE_3);
                break;
            case R.id.contacts_submit:
                List<MobileLinkMan> linkMans = new ArrayList<>();
                if (StringUtils.isBlank(contacts_Family_name.getText().toString())
                        || StringUtils.isBlank(contacts_Family_phone.getText().toString())) {
                    ToastUtils.shortShow(R.string.contacts_message_8);
                    return;
                } else {
                    MobileLinkMan mobileLinkMan = new MobileLinkMan();
                    mobileLinkMan.setName(contacts_Family_name.getText().toString());
                    mobileLinkMan.setMobile(contacts_Family_phone.getText().toString().replace(" ", ""));
                    mobileLinkMan.setType("01");
                    linkMans.add(mobileLinkMan);
                }
                if (StringUtils.isBlank(contacts_Friend_name.getText().toString())
                        || StringUtils.isBlank(contacts_Friend_phone.getText().toString())) {
                    ToastUtils.shortShow(R.string.contacts_message_9);
                    return;
                } else {
                    MobileLinkMan mobileLinkMan = new MobileLinkMan();
                    mobileLinkMan.setName(contacts_Friend_name.getText().toString());
                    mobileLinkMan.setMobile(contacts_Friend_phone.getText().toString().replace(" ", ""));
                    mobileLinkMan.setType("02");
                    linkMans.add(mobileLinkMan);
                }
                if (StringUtils.isBlank(contacts_Colleague_name.getText().toString())
                        || StringUtils.isBlank(contacts_Colleague_phone.getText().toString())) {
                    ToastUtils.shortShow(R.string.contacts_message_10);
                    return;
                } else {
                    MobileLinkMan mobileLinkMan = new MobileLinkMan();
                    mobileLinkMan.setName(contacts_Colleague_name.getText().toString());
                    mobileLinkMan.setMobile(contacts_Colleague_phone.getText().toString().replace(" ", ""));
                    mobileLinkMan.setType("03");
                    linkMans.add(mobileLinkMan);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("linkMans", linkMans);
                map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                requestForHttp(REQUEST_MESSAGE_TAG, AppConfig.getInstance().linkManAuth, map);
                showProgressDailog();
                break;
        }
    }

    private List<String> phoneList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getPhoneContacts(requestCode, data);
        }
    }

    private void getPhoneContacts(final int requestCode, Intent data) {
        // ContentProvider展示数据类似一个单个数据库表
        // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
        ContentResolver reContentResolverol = getActivity().getContentResolver();
        // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
        Uri contactData = data.getData();
        // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
        Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
        cursor.moveToFirst();
        // 获得DATA表中的名字
        final String username = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        // 条件为联系人ID
        String contactId = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts._ID));
        // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
        Cursor phone = reContentResolverol.query(Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                        + contactId, null, null);

        while (phone.moveToNext()) {
            String usernumber = phone
                    .getString(phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneList.add(usernumber);
        }
        if (phoneList != null) {
            if (phoneList.size() > 1) {
                CustomSelectorDialog customSelectorDialog = new CustomSelectorDialog(getActivity(),
                        getString(R.string.selectorContacts), phoneList);
                customSelectorDialog.show();
                customSelectorDialog.setOnDialogOperationclick(new CustomSelectorDialog.onCustomDialogOperationclick() {
                    @Override
                    public void Confirm(String dateString) {
                        switch (requestCode) {
                            case GET_MAIL_MESSAGE_1:
                                contacts_Family_name.setText(username);
                                contacts_Family_phone.setText(dateString);
                                break;
                            case GET_MAIL_MESSAGE_2:
                                contacts_Friend_name.setText(username);
                                contacts_Friend_phone.setText(dateString);
                                break;
                            case GET_MAIL_MESSAGE_3:
                                contacts_Colleague_name.setText(username);
                                contacts_Colleague_phone.setText(dateString);
                                break;
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            } else {
                switch (requestCode) {
                    case GET_MAIL_MESSAGE_1:
                        contacts_Family_name.setText(username);
                        contacts_Family_phone.setText(phoneList.get(0));
                        break;
                    case GET_MAIL_MESSAGE_2:
                        contacts_Friend_name.setText(username);
                        contacts_Friend_phone.setText(phoneList.get(0));
                        break;
                    case GET_MAIL_MESSAGE_3:
                        contacts_Colleague_name.setText(username);
                        contacts_Colleague_phone.setText(phoneList.get(0));
                        break;
                }
            }
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
        saveContacts();
    }


    /**
     * 保存信息
     */
    private void saveContacts() {

        if (addressContent == null) {
            addressContent = new AddressContent();
            addressContent.setFamilyName(contacts_Family_name.getText().toString());
            addressContent.setFamilyPhone(contacts_Family_phone.getText().toString());
            addressContent.setFriendName(contacts_Friend_name.getText().toString());
            addressContent.setFriendPhone(contacts_Friend_phone.getText().toString());
            addressContent.setColleagueName(contacts_Colleague_name.getText().toString());
            addressContent.setColleaguePhone(contacts_Colleague_phone.getText().toString());
            addressContent.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("familyName", contacts_Family_name.getText().toString());
            values.put("familyPhone", contacts_Family_phone.getText().toString());
            values.put("friendName", contacts_Friend_name.getText().toString());
            values.put("friendPhone", contacts_Friend_phone.getText().toString());
            values.put("colleagueName", contacts_Colleague_name.getText().toString());
            values.put("colleaguePhone", contacts_Colleague_phone.getText().toString());
            DataSupport.updateAll(AddressContent.class, values);
        }

    }
}
