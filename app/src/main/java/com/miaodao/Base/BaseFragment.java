package com.miaodao.Base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.miaodao.Application.WheatFinanceApplication;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Remote.IRemoteResponse;
import com.miaodao.Sys.Communication.OnTouchEventClickListener;
import com.miaodao.Sys.Communication.OnkeyDownInterface;
import com.miaodao.Sys.Utils.RequestTool;
import com.miaodao.Sys.Widgets.loadingView.LoadingView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fragment基类
 * Created by Home_Pc on 2017/3/8.
 */

public abstract class BaseFragment extends Fragment implements IRemoteResponse {

    protected View fgView;
    protected RequestTool mRequestTool;
    private OnkeyDownInterface onkeyDownInterface;
    private OnTouchEventClickListener onTouchEventClickListener;
    private Map<String, Fragment> exisMap = new HashMap<>();
    private List<Fragment> fragments = new ArrayList<>();
//    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fgView = initWidgetsViews(inflater);
        initWidgets(fgView);
        initEvent();
        init();
        return fgView;
    }

    private void init() {
        mRequestTool = new RequestTool(this);
        if (getActivity() instanceof ServiceBaseActivity) {
            ((ServiceBaseActivity) getActivity()).setOnkeyDownInterface(new OnkeyDownInterface() {
                @Override
                public boolean OnkeyDown(int keyCode, KeyEvent event) {
                    return OnkeyDownListener(keyCode, event);
                }
            });
            ((ServiceBaseActivity) getActivity()).setOnTouchEventClickListener(new OnTouchEventClickListener() {
                @Override
                public Boolean setOnTouchEventClickListener(MotionEvent event) {
                    return OnTouchEventListener(event);
                }
            });
        } else if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setOnkeyDownInterface(new OnkeyDownInterface() {
                @Override
                public boolean OnkeyDown(int keyCode, KeyEvent event) {
                    return OnkeyDownListener(keyCode, event);
                }
            });
            ((BaseActivity) getActivity()).setOnTouchEventClickListener(new OnTouchEventClickListener() {
                @Override
                public Boolean setOnTouchEventClickListener(MotionEvent event) {
                    return OnTouchEventListener(event);
                }
            });
        }
        if (getActivity() == null) {
            return;
        }
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle(R.string.Prompt);
//        progressDialog.setMessage(getResources().getString(R.string.loadMessage));
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
//        progressDialog.create();
    }

    protected void showProgressDailog() {
        if (getActivity() == null) return;
        LoadingView.show(getActivity(), getActivity());
    }

    protected void dismissProressDialog() {
        if (getActivity() == null) return;
        LoadingView.dismiss();
    }

    protected abstract View initWidgetsViews(LayoutInflater inflater);

    protected abstract void initWidgets(View fgView);

    protected abstract void initEvent();

    protected void pushFragmentController(int id, Fragment fragment, Bundle bundle, boolean isAddflag) {
        if (getActivity() != null) {
            FragmentManager fmManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fmManager.beginTransaction();
            fragments = fmManager.getFragments();
            if (fragment != null) {
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                if (isAddflag) {
                    fmManager.popBackStack(fragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fragmentTransaction.replace(id, fragment);
                if (isAddflag) {
                    fragmentTransaction.isAddToBackStackAllowed();
                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                }
//                fragmentTransaction.commit();
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }


    protected void pushChildFragment(int id, Fragment fragment, Bundle bundle, boolean flag) {

        FragmentManager fmManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fmManager.beginTransaction();

        fragments = fmManager.getFragments();
        if (fragment != null) {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            fragmentTransaction.replace(id, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    protected void pushFragmentController(Fragment fragment, Bundle bundle) {
        pushFragmentController(R.id.main_container, fragment, bundle, true);
    }

    protected void pushFragmentController(Fragment fragment) {
        pushFragmentController(fragment, null);
    }

    protected boolean OnkeyDownListener(int keyCode, KeyEvent event) {
        return false;
    }

    protected boolean OnTouchEventListener(MotionEvent event) {
        return false;
    }

    // ---------------------------------消息---------------------------------

    protected void observeMessage(String msgKey) {
        WheatFinanceApplication.addMsgHandler(msgKey, this);
    }

    private String msgKey = "";

    private Handler fragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onReceiveMessage(msgKey, msg.obj);
        }
    };

    public void onReceiveMessage(String msgkey, Object msgObject) {

    }

    public void onFragmentReceiveMessage(String msgkey, Object msgObject) {
        msgKey = msgkey;
        Message msg = new Message();
        msg.obj = msgObject;
        fragmentHandler.sendMessage(msg);
    }

    protected void sendMessage(String msgkey, Object msgObject) {
        WheatFinanceApplication.postMsg(msgkey, msgObject);
    }

    /**
     * 请求
     *
     * @param tag
     * @param url
     * @param map
     * @param isEtc
     */
    protected void requestForHttp(int tag, String url, Map<String, Object> map, Boolean isEtc) {
        if (mRequestTool == null) {
            mRequestTool = new RequestTool(this);
        }
        mRequestTool.requestForHttp(getActivity(), tag, url, map, isEtc);
    }

    /**
     * @param tag
     * @param url
     * @param map
     */
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
        mRequestTool.requestForHttpFile(getActivity(), tag, url, fileMap, map, isEtc);
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

    protected void requestUrlForData(int tag, String url, Boolean isEtc) {
        if (mRequestTool == null) {
            mRequestTool = new RequestTool(this);
        }
        mRequestTool.requestUrlForData(getActivity(), tag, url, isEtc);
    }

    protected void requestUrlForData(int tag, String url) {
        requestUrlForData(tag, url, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}
