package com.miaodao.Fragment.Account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.ToastUtils;

import static com.fcloud.licai.miaodao.R.id.fm_webView;

/**
 * Created by daixinglong on 2017/4/10.
 */

public class FollowUsFragment extends ContentBaseFragment {

    private final String url = "file:///android_asset/followUs.html";
    private WebView webView;
    private LoadWebViewClient client = new LoadWebViewClient();
    private ProgressBar progress;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_webview_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {

        webView = (WebView) fgView.findViewById(fm_webView);
        progress = (ProgressBar) fgView.findViewById(R.id.progress);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title_tvShow.setText(bundle.getString("title"));
        }


        WebSettings webSettings = webView.getSettings();
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


        webView.loadUrl(url);
        webView.addJavascriptInterface(new JSHook(), "follow");
        webView.setWebViewClient(client);
        webView.setWebChromeClient(new LoadingProgress());
    }


    public class JSHook{

        @JavascriptInterface
        public void saveImg() {

            showProgressDailog();
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.follow_wechat_ma);
                ImgUtils.saveImageToGallery(getActivity(), bitmap, "follow");
                ToastUtils.shortShow("图片保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.shortShow("图片保存失败");
            }
            dismissProressDialog();
        }
    }







    @Override
    protected void initEvent() {

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
            view.loadUrl(url_);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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

}
