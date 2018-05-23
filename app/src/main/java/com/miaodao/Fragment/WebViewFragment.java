package com.miaodao.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.NetWorkUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Home_Pc on 2017/3/16.
 */

public class WebViewFragment extends ContentBaseFragment {
    private WebView fm_webView;
    private String Url, title;
    private LoadWebViewClient client = new LoadWebViewClient();
    private ProgressBar progress;
    private String webViewInternalUrl;

    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_webview_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        fm_webView = (WebView) fgView.findViewById(R.id.fm_webView);
        progress = (ProgressBar) fgView.findViewById(R.id.progress);
    }

    @Override
    protected void initEvent() {
        Bundle bundle = getArguments();

        if (!bundle.containsKey("url")
                || StringUtils.isBlank(bundle.getString("url"))) {
            ToastUtils.shortShow(R.string.SysErrMsg);
            return;
        }

        if (bundle.containsKey("url")) {
            Url = bundle.getString("url");
        }

        if (bundle.containsKey("title")) {
            title = bundle.getString("title");
            title_tvShow.setText(title);
        }

        WebSettings webSettings = fm_webView.getSettings();
        /**
         * 禁止缩放
         */
        webSettings.setSupportZoom(false);
        /**
         * 适应手机屏幕
         */
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        fm_webView.loadUrl(Url);
        fm_webView.setWebViewClient(client);
        fm_webView.setWebChromeClient(new LoadingProgress());
        fm_webView.addJavascriptInterface(new MyCallBack(), IMaiziActions.class.getSimpleName());

        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("芝麻信用授权".equals(title)) {
                    goMain();
                    return;
                }

                if (title_back.getVisibility() == View.VISIBLE) {
                    fm_webView.goBack();
                    if (fm_webView.getUrl().equals(Url)) {
                        getActivity().finish();
                    }
                }
            }
        });
    }


    /**
     * 显示加载进度条
     */
    private class LoadingProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progress.setVisibility(View.GONE);//加载完网页进度条消失
            } else {
                progress.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                progress.setProgress(newProgress);//设置进度值
            }
        }
    }


    private class LoadWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url_) {
            webViewInternalUrl = url_;
            view.loadUrl(url_);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    protected boolean OnkeyDownListener(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ("芝麻信用授权".equals(title)) {
                goMain();
                return true;
            }

            fm_webView.goBack();
            if (fm_webView.getUrl().equals(Url)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 是否返回主页
     */
    private void goMain() {

        if (webViewInternalUrl.contains("/vendor/zmxy/verifySuccess.html") || webViewInternalUrl.contains("/vendor/zmxy/verifyFail.html")) {
            getActivity().finish();
        } else {
            DialogHelp.getInstance(getActivity()).showDialog("提示", "确认要放弃授权操作吗?", "放弃", "继续", new CustomDialog.OnSureInterface() {
                @Override
                public void getOnSure() {

                }

                @Override
                public void getOnDesmiss() {
                    getActivity().finish();
                }
            });
        }
    }


    /**
     * webview 回调
     */
    public class MyCallBack extends IMaiziActions {

        @JavascriptInterface
        @Override
        public void back() {
            EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
            getActivity().finish();
        }
    }


}
