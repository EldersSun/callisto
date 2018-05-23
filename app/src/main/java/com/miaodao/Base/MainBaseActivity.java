package com.miaodao.Base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;
import com.miaodao.Application.WheatFinanceApplication;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Remote.IRemoteResponse;
import com.miaodao.Sys.Utils.RequestTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 程序基类
 * Created by Home_Pc on 2017/3/8.
 */

public abstract class MainBaseActivity extends FragmentActivity implements IRemoteResponse {

    protected LinearLayout ac_base_layout;
    protected RelativeLayout title_layout;
    protected ImageView title_back;
    protected TextView title_tvShow;
    protected TextView title_menuNum;
    protected TextView tv_title_right;
    protected Button title_menu;
    protected LayoutInflater inflater;
    protected LinearLayout ac_base_title;
    protected FrameLayout ac_base_Content;
    protected View contentView;

    protected RequestTool mRequestTool;
    private List<Fragment> fragments;
    protected FragmentManager fmManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base_layout);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.appColor));
        inflater = LayoutInflater.from(this);
        initFindViews();
        initWidgets();
        initEvent();
    }

    protected abstract View initViews(LayoutInflater inflater);

    protected abstract void initWidgets();

    protected abstract void initEvent();

    private void initFindViews() {
        mRequestTool = new RequestTool(this);
        fragments = new ArrayList<>();
        title_layout = (RelativeLayout) inflater.inflate(R.layout.v_title_layout, null);
        title_back = (ImageView) title_layout.findViewById(R.id.title_back);
        title_tvShow = (TextView) title_layout.findViewById(R.id.title_tvShow);
        title_menuNum = (TextView) title_layout.findViewById(R.id.title_menuNum);
        tv_title_right = (TextView) title_layout.findViewById(R.id.tv_title_right);
        title_menu = (Button) title_layout.findViewById(R.id.title_menu);
        ac_base_layout = (LinearLayout) findViewById(R.id.ac_base_layout);
        ac_base_title = (LinearLayout) findViewById(R.id.ac_base_title);
        ac_base_Content = (FrameLayout) findViewById(R.id.ac_base_Content);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 0, 0, 0);
        title_layout.setLayoutParams(layoutParams);
        ac_base_title.addView(title_layout);
        contentView = initViews(inflater);
        if (contentView == null) {
            Log.i("initFindViews", "initFindViews为空");
            return;
        }
        ac_base_Content.addView(contentView);

        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title_back.getVisibility() == View.VISIBLE) {
                    finish();
                }
            }
        });
    }

    protected void pushFragmentController(int id, Fragment fragment, Bundle bundle, boolean isAddFlag) {

        fmManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fmManager.beginTransaction();

        for (Fragment fm : fragments) {
            fragmentTransaction.hide(fm);
        }
        if (fragments.contains(fragment)) {
            fragmentTransaction.show(fragment);
        } else {
            fragments.add(fragment);
            fragment.setArguments(bundle);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.add(id, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void setMenuNumTvShow(int number) {
        if (number == 0) {
            title_menuNum.setText("");
            title_menuNum.setVisibility(View.GONE);
        } else {
            title_menuNum.setVisibility(View.VISIBLE);
            title_menuNum.setText(String.valueOf(number));
        }
    }

    protected void setMenuOnlcik(View.OnClickListener onClick) {
        if (onClick != null) {
            title_menu.setOnClickListener(onClick);
        }
    }

    protected void pushFragmentController(Fragment fragment, Bundle bundle, boolean isAddFlag) {
        pushFragmentController(R.id.main_container, fragment, bundle, isAddFlag);
    }

    protected void pushFragmentController(Fragment fragment, boolean isAddFlag, int title, boolean showMessage, boolean showTitle) {

        if (showTitle) {
            title_layout.setVisibility(View.VISIBLE);
            title_tvShow.setText(title);
            if (showMessage) {
                title_menu.setVisibility(View.VISIBLE);
                title_menuNum.setVisibility(View.VISIBLE);
            } else {
                title_menu.setVisibility(View.GONE);
                title_menuNum.setVisibility(View.GONE);
            }
        } else {
            title_layout.setVisibility(View.GONE);
        }
        pushFragmentController(R.id.main_container, fragment, null, isAddFlag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // ---------------------------------消息---------------------------------


    protected void observeMessage(String msgKey) {
        WheatFinanceApplication.addMsgHandler(msgKey, this);
    }

    public void onReceiveMessage(String msgkey, Object msgObject) {

    }

    private String msgKey = "";

    private Handler activityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onReceiveMessage(msgKey, msg.obj);
        }
    };

    public void onActivityReceiveMessage(String msgkey, Object msgObject) {
        msgKey = msgkey;
        Message msg = new Message();
        msg.obj = msgObject;
        activityHandler.sendMessage(msg);
    }

    protected void sendMessage(String msgkey, Object msgObject) {
        WheatFinanceApplication.postMsg(msgkey, msgObject);
    }

    protected void requestForHttp(int tag, String url, Map<String, Object> map, Boolean isEtc) {
        if (mRequestTool == null) {
            mRequestTool = new RequestTool(this);
        }
        mRequestTool.requestForHttp(this, tag, url, map, isEtc);
    }

    protected void requestForHttp(int tag, String url, Map<String, Object> map) {
        requestForHttp(tag, url, map, false);
    }


    /**
     * 文件上传
     *
     * @param tag
     * @param url
     * @param fileMap
     * @param map
     * @param isEtc
     */
    protected void requestForHttpFile(int tag, String url, Map<String, File> fileMap, Map<String, String> map, Boolean isEtc) {
        if (mRequestTool == null) {
            mRequestTool = new RequestTool(this);
        }
        mRequestTool.requestForHttpFile(this, tag, url, fileMap, map, isEtc);
    }

    /**
     * @param tag
     * @param url
     * @param fileMap
     * @param map
     */
    protected void requestForHttpFile(int tag, String url, Map<String, File> fileMap, Map<String, String> map) {
        requestForHttpFile(tag, url, fileMap, map, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments.clear();
        fragments = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
