package com.miaodao.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动页
 * Created by Home_Pc on 2017/3/13.
 */
public class LaunchActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_PHONE = 0;
    private JumpLogin jumpLogin = new JumpLogin(1000 * 1, 1000);
    private TextView lead_TimeShow;
    private Boolean isJumpFlag;
    private Boolean isFirst = false;

    /**
     * 配置手机需要的权限
     */
    private final String[] PERMISSIONS = {
            //Manifest.permission.CAMERA,
            //Manifest.permission.RECORD_AUDIO,
            //Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 默认已经赋予
     *
     * @param permission
     * @return
     */
    private boolean checkSelfPermissions(String permission) {
        boolean isPermission = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        return isPermission;
    }

    private Map<String, Boolean> isPermission() {
        Map<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < PERMISSIONS.length; i++) {
            if (checkSelfPermissions(PERMISSIONS[i])) {
                map.put("true", true);
            } else {
                map.put("false", false);
            }
        }
        return map;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.whiteColor));
        setContentView(R.layout.ac_lead_layout);
        iniWidgets();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lead_TimeShow:
                if (isPermission().containsKey("false")) {
                    DialogHelp.getInstance(this).showDialog(R.string.lead_message, R.string.lead_message_2, R.string.cancel,
                            R.string.lead_message_3, new CustomDialog.OnSureInterface() {
                                @Override
                                public void getOnSure() {
                                    goToAppSetting();
                                }

                                @Override
                                public void getOnDesmiss() {
                                    finish();
                                }
                            });
                } else {
                    if (isJumpFlag) {
                        jumpLogin.cancel();
                    }
                    goNextAct();
                }
                break;
        }
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(PACKAGE, getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, jumpResultCode);
    }

    private final String PACKAGE = "package";
    private final int jumpResultCode = 0x1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case jumpResultCode:
                if (isPermission().containsKey("true")) {
                    isJumpFlag = true;
                    jumpLogin.start();
                }
                break;
        }
    }

    private class JumpLogin extends CountDownTimer {
        public JumpLogin(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            lead_TimeShow.setText(String.valueOf(millisUntilFinished / 1000) + "秒后跳转");
        }

        @Override
        public void onFinish() {
            if (isPermission().containsKey("false")) {
                DialogHelp.getInstance(LaunchActivity.this).showDialog(R.string.lead_message, R.string.lead_message_2, R.string.cancel,
                        R.string.lead_message_3, new CustomDialog.OnSureInterface() {
                            @Override
                            public void getOnSure() {
                                goToAppSetting();
                            }

                            @Override
                            public void getOnDesmiss() {
                                finish();
                            }
                        });
            } else {
//                if (isJumpFlag) {
//                    jumpLogin.cancel();
//                }
                goNextAct();
            }


//            goNextAct();
        }
    }


    /**
     * 如果是第一次启动,跳转到引导页面
     * <p>
     * 如果不是,跳转到首页
     */
    private void goNextAct() {
        Intent intent;
        if (!isFirst) {
            SharedPreferencesUtil.putBoolean(this, AppConfig.getInstance().IS_FIRST_LAUNCH, true);
            intent = new Intent(LaunchActivity.this, GuideActivity.class);
        } else {
            intent = new Intent(LaunchActivity.this, MainAct.class);
        }
        startActivity(intent);
        finish();
    }


    private void iniWidgets() {
        lead_TimeShow = (TextView) findViewById(R.id.lead_TimeShow);
        lead_TimeShow.setOnClickListener(this);
        if (isPermission().containsKey("false")) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PHONE);
        } else {
            isJumpFlag = true;
            jumpLogin.start();
        }
        isFirst = SharedPreferencesUtil.getBoolean(this, AppConfig.getInstance().IS_FIRST_LAUNCH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE:
                if (isPermission().containsKey("true")) {
                    isJumpFlag = true;
                    jumpLogin.start();
                } else {
                    DialogHelp.getInstance(this).showDialog(R.string.lead_message, R.string.lead_message_2, R.string.cancel,
                            R.string.lead_message_3, new CustomDialog.OnSureInterface() {
                                @Override
                                public void getOnSure() {
                                    goToAppSetting();
                                }

                                @Override
                                public void getOnDesmiss() {
                                    finish();
                                }
                            });
                }
                break;
        }
    }
}

