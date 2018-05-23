package com.miaodao.Fragment.Loan.Apply;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.ShapedImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人身份证拍摄
 * Created by Home_Pc on 2017/3/10.
 */

public class PersonalIdCarFragment extends ContentBaseFragment implements View.OnClickListener {

    private ShapedImageView idFront;//身份证前面照
    private ImageView frontRetake;//点击重拍身份证前面照
    private ShapedImageView idBack;//身份证后面照
    private ImageView backRetake;//点击重拍身份证后面照
    private TextView tvFrontAlready, tvBackAlready;//显示已拍摄
    private Button personal_submit;
    private Boolean isFront = false, isBack = false;
    private EditText personal_Name, personal_idCarNum;

    private final int REQUEST_MESSAGE_TAG_1 = 0X1011;
    private final int REQUEST_MESSAGE_TAG_2 = 0X1012;
    private final int REQUEST_MESSAGE_TAG_3 = 0X1013;
    private LinearLayout personal_tvShowLayout;

    private String tag = "";

    private final int JUMP_MESSAGE_TAG_1 = 0x1001;

    private File frontFile;//身份证前面照片文件
    private File backFile;//身份证反面照文件


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_personalidcar_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        idFront = (ShapedImageView) fgView.findViewById(R.id.id_front);
        frontRetake = (ImageView) fgView.findViewById(R.id.front_retake);
        idBack = (ShapedImageView) fgView.findViewById(R.id.id_back);
        backRetake = (ImageView) fgView.findViewById(R.id.back_retake);
        tvFrontAlready = (TextView) fgView.findViewById(R.id.tv_front_already);
        tvBackAlready = (TextView) fgView.findViewById(R.id.tv_back_already);
        personal_submit = (Button) fgView.findViewById(R.id.personal_submit);

        personal_Name = (EditText) fgView.findViewById(R.id.personal_Name);
        personal_idCarNum = (EditText) fgView.findViewById(R.id.personal_idCarNum);
        personal_tvShowLayout = (LinearLayout) fgView.findViewById(R.id.personal_tvShowLayout);

        title_menu.setBackgroundResource(R.color.transparentColor);
        title_menu.setText(R.string.personal_message_4);
        title_tvShow.setText(R.string.personal_message_14);
    }

    @Override
    protected void initEvent() {

        idFront.setOnClickListener(this);
        frontRetake.setOnClickListener(this);
        idBack.setOnClickListener(this);
        backRetake.setOnClickListener(this);
        personal_submit.setOnClickListener(this);

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
    }


    @Override
    public void onResume() {
        super.onResume();
        showPhoto();
    }


    /**
     * 显示拍摄的照片
     * 如果本地存在，显示，并显示重拍按钮
     * 如果不存在，不显示重拍按钮
     */
    private void showPhoto() {
        frontFile = new File(AppConfig.getInstance().VERIFICATION_ID_FRONT_PHONE_PATH);
        backFile = new File(AppConfig.getInstance().VERIFICATION_ID_BACK_PHONE_PATH);

        //显示身份证前面照片
        if (frontFile.exists()) {
            //需要不适用内存缓存，不然，拍的照片不变
            Glide.with(this).load(frontFile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(idFront);
            frontRetake.setVisibility(View.VISIBLE);
            tvFrontAlready.setVisibility(View.VISIBLE);
        } else {
            Glide.with(this).load(R.drawable.ic_idcard_img_default_front_selector).into(idFront);
            frontRetake.setVisibility(View.GONE);
            tvFrontAlready.setVisibility(View.GONE);
        }

        //显示身份证反面照
        if (backFile.exists()) {
            isBack = true;
            Glide.with(this).load(backFile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(idBack);
            backRetake.setVisibility(View.VISIBLE);
            tvBackAlready.setVisibility(View.VISIBLE);
        } else {
            Glide.with(this).load(R.drawable.ic_idcard_img_default_back).into(idBack);
            backRetake.setVisibility(View.GONE);
            tvBackAlready.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.id_front://相机拍摄正面
            case R.id.front_retake://相机拍摄正面

//                if (!PermissionUtil.hasPermission(getActivity(), Manifest.permission.CAMERA)) {
//                    PermissionUtil.requestPermission(getActivity(), 0x000, Manifest.permission.CAMERA,
//                            Manifest.permission.RECORD_AUDIO);
//                    return;
//                }
                intent = new Intent(getActivity(), ActIdCamera.class);
                intent.putExtra(ActIdCamera.ID_TAG, ActIdCamera.ID_FRONT);
                startActivity(intent);
//                ServiceBaseActivity.startActivity(getActivity(), CameraIdFrontFragment.class.getName());
                break;

            case R.id.id_back://相机拍摄背面
            case R.id.back_retake://相机拍摄背面
                if (!frontFile.exists()) {
                    ToastUtils.shortShow(R.string.personal_message_5);
                    return;
                }
//                if (!PermissionUtil.hasPermission(getActivity(), Manifest.permission.CAMERA)) {
//                    PermissionUtil.requestPermission(getActivity(), 0x001, Manifest.permission.CAMERA,
//                            Manifest.permission.RECORD_AUDIO);
//                    return;
//                }
                intent = new Intent(getActivity(), ActIdCamera.class);
                intent.putExtra(ActIdCamera.ID_TAG, ActIdCamera.ID_BACK);
                startActivity(intent);
//                ServiceBaseActivity.startActivity(getActivity(), CameraIdBackFragment.class.getName());
                break;

            case R.id.personal_submit:
                if (personal_tvShowLayout.getVisibility() == View.GONE) {
                    if (!isBack) {
                        ToastUtils.shortShow(R.string.personal_message_13);
                        return;
                    }
                    Map<String, File> fileMap = new HashMap<>();
                    fileMap.put("frontPic", frontFile);
                    fileMap.put("backPic", backFile);
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                    map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
                    requestForHttpFile(REQUEST_MESSAGE_TAG_1, AppConfig.getInstance().uploadIdCard, fileMap, map);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
                    map.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));

                    String name = personal_Name.getText().toString();
                    String no = personal_idCarNum.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(no)){
                        ToastUtils.shortShow("姓名和身份证号不能为空");
                        return;
                    }

                    map.put("certNo", no);
                    map.put("certName", name);
                    requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().commitCustomerAuth, map);
                }
                showProgressDailog();
                break;
        }
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_1:
                Map<String, Map<String, String>> resultMap = (Map<String, Map<String, String>>) result;
                if (personal_tvShowLayout.getVisibility() == View.GONE) {
                    personal_tvShowLayout.setVisibility(View.VISIBLE);
                    if (resultMap.containsKey("data")) {
                        Map<String, String> map = resultMap.get("data");
                        if (map.containsKey("userName")) {
                            personal_Name.setText(map.get("userName"));
                        }
                        if (map.containsKey("certNo")) {
                            personal_idCarNum.setText(map.get("certNo"));
                        }
//                        personal_front.setEnabled(false);
//                        personal_back.setEnabled(false);
//                        personal_front.setImageResource(R.color.transparentColor);
//                        personal_back.setImageResource(R.color.transparentColor);
                    }
                }
                break;
            case REQUEST_MESSAGE_TAG_2:
                ToastUtils.shortShow(R.string.submitSucc);
                if (StringUtils.isBlank(tag)) {
                    ServiceBaseActivity.startActivity(getActivity(), BindCardFragment.class.getName());
                } else {
                    sendMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_1, "");
                }
                getActivity().finish();
                break;
            case REQUEST_MESSAGE_TAG_3:
                Map<String, Map<String, String>> queryResultMap = (Map<String, Map<String, String>>) result;
                if (queryResultMap.containsKey("data")) {
                    Map<String, String> map = queryResultMap.get("data");
                    if (map.containsKey("authState")) {
                        String status = map.get("authState");
                        if (status == null) {
//                            personal_front.setEnabled(true);
//                            personal_back.setEnabled(true);
//                            personal_front.setImageResource(R.drawable.btn_idcarimgshow_selector);
//                            personal_back.setImageResource(R.drawable.btn_idcarimgshow_selector);
                        } else if (status.equals("S") || status.equals("C") || status.equals("P")) {
//                            personal_front.setEnabled(false);
//                            personal_back.setEnabled(false);
//                            personal_front.setImageResource(R.color.transparentColor);
//                            personal_back.setImageResource(R.color.transparentColor);
                        } else {
//                            personal_front.setEnabled(true);
//                            personal_back.setEnabled(true);
//                            personal_front.setImageResource(R.drawable.btn_idcarimgshow_selector);
//                            personal_back.setImageResource(R.drawable.btn_idcarimgshow_selector);
                        }
                    }
                }
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
