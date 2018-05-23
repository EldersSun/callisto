package com.miaodao.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;


/**
 * Created by daixinglong on 2017/4/10.
 */

public class SetNetFragment extends ContentBaseFragment implements View.OnClickListener {

    private final String url = "file:///android_asset/netSetA.html";

    private WebView wvShowSet;
    private TextView tvSetNet;
    private ProgressBar progress;


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.view_net_set, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        wvShowSet = (WebView) fgView.findViewById(R.id.wv_show_set);
        tvSetNet = (TextView) fgView.findViewById(R.id.tv_set_net);
        progress = (ProgressBar) fgView.findViewById(R.id.progress);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title_tvShow.setText(bundle.getString("title"));
        }

        WebSettings webSettings = wvShowSet.getSettings();
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
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvShowSet.loadUrl(url);
        wvShowSet.setWebChromeClient(new LoadingProgress());
    }

    @Override
    protected void initEvent() {
        tvSetNet.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        // 隐式意图
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(0x10200000);
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
        startActivity(intent);
    }
}
