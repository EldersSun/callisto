package com.miaodao.Fragment.Free;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fcloud.licai.miaodao.R;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Activity.CouponActivity;
import com.miaodao.Activity.LoginActivity;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.NetWorkUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.VerticalTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 免息
 * Created by Home_Pc on 2017/3/9.
 */

public class FreeMainFragment extends BaseFragment implements View.OnClickListener {

    private final int REQUEST_ROLL_TAG = 0x0001;
    private final int GET_COUPON = 0x0002;

    //复制版上面的名称字符串
    private final static String COPY_STR = "umeng_socialize_text_copy";


    private WebView FreeMainWebView;
    //友盟分享使用
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private RelativeLayout rlParent;
    private ImageView ivBack;
    private TextView title;
    private ProgressBar progress;
    private VerticalTextView tvFreeAd;
    private LinearLayout llScrollTextFree;

    private ArrayList<String> ad = new ArrayList<String>();
    private String invitedCode = "";
    private String shareUrl = "";
    private int couponNumber;
    private String tokenId = "";

    private static FreeMainFragment freeMainFragment = new FreeMainFragment();

    public FreeMainFragment() {
    }

    public static FreeMainFragment getInstance() {
        return freeMainFragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        tvFreeAd.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        tvFreeAd.stopAutoScroll();
    }

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_freemain_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        rlParent = (RelativeLayout) fgView.findViewById(R.id.rl_parent);
        llScrollTextFree = (LinearLayout) fgView.findViewById(R.id.ll_scroll_text_free);
        ivBack = (ImageView) fgView.findViewById(R.id.iv_back);
        title = (TextView) fgView.findViewById(R.id.title_tvShow);
        progress = (ProgressBar) fgView.findViewById(R.id.progress);
        tvFreeAd = (VerticalTextView) fgView.findViewById(R.id.tv_free_ad);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String show = bundle.getString("showTitle");
            String titleStr = bundle.getString("title");
            if ("showTitle".equals(show)) {
                rlParent.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(titleStr)) {
                    title.setText(titleStr);
                }
            } else {
                rlParent.setVisibility(View.GONE);
            }
        }
        FreeMainWebView = (WebView) fgView.findViewById(R.id.FreeMainWebView);

        tvFreeAd.setText(14, 5, getResources().getColor(R.color.loan_bank_text));//设置属性
        tvFreeAd.setTextStillTime(3000);//设置停留时长间隔
        tvFreeAd.setAnimTime(800);//设置进入和退出的时间间隔
        getRollStr();
    }


    private void getRollStr() {
        Map<String, Object> rollStr = new HashMap<>();
        rollStr.put("msgType", "A");
        OkHttpUtils.post().url(StringUtils.toUtl(AppConfig.getInstance().GET_ROLL_STR))
                .content(StringUtils.toJsonString(rollStr)).build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                llScrollTextFree.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                String data = StringUtils.getResponseData(response);
                if (TextUtils.isEmpty(data))
                    return;
                try {
                    JSONObject object = new JSONObject(data);
                    ArrayList<String> strings = (ArrayList<String>) JSON.parseArray(object.optString("scrollbarMsg"), String.class);
                    if (strings.isEmpty())
                        return;
                    llScrollTextFree.setVisibility(View.VISIBLE);
                    tvFreeAd.setTextList(strings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    /**
     * 获取免息券
     */
    private void getCoupon() {
        invitedCode = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_INVITEDCODE, "");
        shareUrl = "http://120.27.49.79/appStatic/view/appRegister.main.html" + "?" + invitedCode;
        tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
        if (TextUtils.isEmpty(tokenId)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("jumpFlag", true);
            startActivity(intent);
        }
        if (!TextUtils.isEmpty(tokenId)) {
            Map<String, Object> coupon = new HashMap<>();
            coupon.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
            coupon.put("tokenId", tokenId);
            requestForHttp(GET_COUPON, AppConfig.getInstance().GET_COUPON_NUMBER, coupon);
        } else {
            couponNumber = SharedPreferencesUtil.getInt(getActivity(), AppConfig.getInstance().RESULT_COUPON_NUMBER, 0);
            FreeMainWebView.loadUrl(AppConfig.getInstance().ACCOUNT_ACTIVITY_URL);
        }
    }


    @Override
    protected void initEvent() {
        WebSettings webSettings = FreeMainWebView.getSettings();
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
        FreeMainWebView.setWebViewClient(new LoadWebViewClient());
        FreeMainWebView.setWebChromeClient(new LoadingProgress());
        FreeMainWebView.addJavascriptInterface(FreeMainFragment.this, "invite");
        ivBack.setOnClickListener(this);

        //获取免息券数量
        getCoupon();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (!FreeMainWebView.getUrl().equals(AppConfig.getInstance().ACCOUNT_ACTIVITY_URL)) {
                    FreeMainWebView.goBack();
                } else {
                    getActivity().finish();
                }
                break;

            default:
                break;

        }
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
//            if (url_ != null && url_.contains("appStatic/view/null.html")) {
//                initShare();
//                mShareAction.open();
//            }


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
            FreeMainWebView.loadUrl("javascript:invite_code_show('" + invitedCode + "');");
            FreeMainWebView.loadUrl("javascript:coupon_show('已获得" + couponNumber + "张 >');");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }
    }


    /**
     * 初始化分享信息
     */
    private void initShare() {
        mShareListener = new CustomShareListener(getActivity());
           /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(getActivity()).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .addButton(COPY_STR, COPY_STR, "ic_share", "ic_share")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        if (share_media == null) {
                            if (snsPlatform.mKeyword.equals(COPY_STR)) {
                                copyInfo();
                            }
                        } else {
                            platShare(share_media);
                        }
                    }
                });
    }


    /**
     * 复制信息
     */
    private void copyInfo() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("key", shareUrl);
        clipboard.setPrimaryClip(clipData);
        ToastUtils.shortShow(getString(R.string.copy_success));
    }

    @JavascriptInterface
    public void copy() {
        copyInfo();
    }

    @JavascriptInterface
    public void invite() {
        initShare();
        mShareAction.open();
    }

    @JavascriptInterface
    public void coupon() {
        Intent intent = new Intent(getActivity(), CouponActivity.class);
        intent.putExtra("useCoupon", "account");
        startActivity(intent);
    }


    /**
     * 调用平台的分享
     *
     * @param share_media
     */
    private void platShare(SHARE_MEDIA share_media) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle("麦子秒到邀请码");
        web.setDescription("麦子秒到邀请码:" + invitedCode);
        web.setThumb(new UMImage(getActivity(), R.mipmap.ic_share));
        new ShareAction(getActivity()).withMedia(web)
                .setPlatform(share_media)
                .setCallback(mShareListener)
                .share();
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {


        Map<String, Object> resultMap = (Map<String, Object>) result;
        if (StringUtils.isResponseNull(resultMap)) return;
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        if (StringUtils.isResponseNull(dataMap)) return;
        switch (TAG) {
            case REQUEST_ROLL_TAG:
                ad = (ArrayList<String>) dataMap.get("scrollbarMsg");
                if (ad == null) return;
                llScrollTextFree.setVisibility(View.VISIBLE);
                tvFreeAd.setTextList(ad);
                break;

            case GET_COUPON:
                couponNumber = (int) dataMap.get("count");
                FreeMainWebView.loadUrl(AppConfig.getInstance().ACCOUNT_ACTIVITY_URL);
                break;

            default:
                break;
        }


    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        couponNumber = 0;
        FreeMainWebView.loadUrl(AppConfig.getInstance().ACCOUNT_ACTIVITY_URL);
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        couponNumber = 0;
        FreeMainWebView.loadUrl(AppConfig.getInstance().ACCOUNT_ACTIVITY_URL);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(String event) {
        if (AppConfig.getInstance().MESSAGE_GET_COUPON.equals(event)) {
            getCoupon();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
