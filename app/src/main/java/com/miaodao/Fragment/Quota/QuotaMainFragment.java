package com.miaodao.Fragment.Quota;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.NetWorkUtil;

/**
 * 提额
 * Created by Home_Pc on 2017/3/9.
 */

public class QuotaMainFragment extends BaseFragment {
    private ProgressBar progress;
    private WebView quotaMainWebView;

    private static QuotaMainFragment quotaMainFragment = new QuotaMainFragment();


    public static QuotaMainFragment getInstance() {
        return quotaMainFragment;
    }



    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_quotamain_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        progress = (ProgressBar) fgView.findViewById(R.id.progress);
        quotaMainWebView = (WebView) fgView.findViewById(R.id.quotaMainWebView);
    }

    @Override
    protected void initEvent() {
        WebSettings webSettings = quotaMainWebView.getSettings();
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
        quotaMainWebView.loadUrl(AppConfig.getInstance().QUOTA_MAIN_URL);
        quotaMainWebView.setWebChromeClient(new LoadingProgress());
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


    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    private class LoadWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url_) {
            view.loadUrl(url_);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
