package com.miaodao.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Utils.alipay.PayOrderUtil;
import com.miaodao.Sys.Utils.alipay.PayResult;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by daixinglong on 2017/3/27.
 */

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class WXPayActivity extends Activity {

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCiaHuCp0FoUyTUgL2HygvltmAcRoRqSAi1PQ5n5UNF5XPAihgORpAphUIdFF6MY1ns0xcpgh16wdUZoFoYB7uyvdtqAeLj/FbvkMlndER0XHzhXdMY7h/P6/vO8/FYAtc2G6ZOYNMnfsRJym6yf+9e1XvbH7LBr7f4qNcCPTYeqQqa4rejw1cthTa6xMnC7s6w9gANVDuNuUtqEixcu9K48R/ALO2y7CCh5go/XzrHH3/ivgB9a+svKlyFxTLE5ZUyVJ3W9ewwc8t6nGZRTDTPmneMqlMnHJrvIef6zPeVbga5kArwKB/O3VO6tohaGu4ZfO/Tf8qOQxnRT1okNj57AgMBAAECggEAIOjLHk6GEn3GmMj9nuZyQroR/6jKbD7shM06fhMW7rLwIbPZ7Aqga8l0/C+EeS4oqrSbnO2gy587b1Xu66+leTK/o1t+fnRDqYfNSAlnVazoIULB4+tcxuUqwWTtIshwk6ZGrNsx7m7hl5JNnB4Qg0b/MgfaUSUqQlj0OJ/ut4d16scrKVopuzo+CKKxeuMlvDIlzo4avi78dxgLhcbBOcgycE/ISu1RvfniCXwelUp9xbCafknnktBUmGOjXW9kHAmqkrNMrfq3UWPiy/stOD286OzKfsgaHcULPYY+AklpxAcABL3wPfIFKsTZh2T09NxHIo2ic/cE7GFgXql4AQKBgQDtFnidkjxImfT4MyHC1Xn1XAH6R1c2qVaPfYQLUFEKGid6KUNaf/vvEyhKOBCbc1kcHKDCi/xQBwOwgImAJD7ybps4lmcPrXO0wOmO7KEUSitOoUgCL6aF1036NLdp4NwWO6kr9feSy+F6ob/K1OwPWzExnUJtZfcla+wqVzISoQKBgQCvXP26IrJ8lVZjfITp+z8rY0wyLJ8Tdcdi+lkHLpv/gtjVO5K9P3nRubKlcDXKygngr6E/gbK7TBXfq1H8oYXpe+bqkMq1n9m9qcCj4ypVMpAQHce5nGxZc8Hdr4o6Q4pN/x7tjkb4bQqXxX5eSWBKMo01SjIp7TQIr+gvLuiXmwKBgQC5GVglvSvvirNkq5bqI/zJgIHSwqvmcyKveFEE/Dmo5252w4xgNZeduZk1CLx+gPnLJtajzOK6IB/TRycSsjmq0IvDDDl+Ve8F6a7u9PN1gcp54xTzD/y0dbndZBbmRLXPbdjQB4K4SxDIy0uPsdhyzMGFeL+J5Zek0bNaAyWGwQKBgDlthJHuRvFRnobaAlT1uIwm6o8L2dRvn6okPf+CHggg+41pAvcC0IwD+iL/ij4sx+tS2MWJXdTEZ+1ll2XaJ5Z8wvRSEnBENvRLoRCKlhRsiI0ek4ePWFQ6+mP01pmaALLmN2SoUUmNvAhT3rxV3zitMV0gPSlJyt6FbM322Ki9AoGBALR/sVtMLuc/AVyjRoOpDci3i+pN+FPTEfn+KWS+OAbuJvdQNbFFdhMALFQ+adLznF+JD3cXzCf/s8uf++82F3JfZafTYRs7BM0gi+HbeEo7ApxJHK4dCy1AeYVAMNZ6nt3L1Iljn2kmI4f7VF9/T03LG2hEMmiHKxEE7YeT75tx";
    public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCiaHuCp0FoUyTUgL2HygvltmAcRoRqSAi1PQ5n5UNF5XPAihgORpAphUIdFF6MY1ns0xcpgh16wdUZoFoYB7uyvdtqAeLj/FbvkMlndER0XHzhXdMY7h/P6/vO8/FYAtc2G6ZOYNMnfsRJym6yf+9e1XvbH7LBr7f4qNcCPTYeqQqa4rejw1cthTa6xMnC7s6w9gANVDuNuUtqEixcu9K48R/ALO2y7CCh5go/XzrHH3/ivgB9a+svKlyFxTLE5ZUyVJ3W9ewwc8t6nGZRTDTPmneMqlMnHJrvIef6zPeVbga5kArwKB/O3VO6tohaGu4ZfO/Tf8qOQxnRT1okNj57AgMBAAECggEAIOjLHk6GEn3GmMj9nuZyQroR/6jKbD7shM06fhMW7rLwIbPZ7Aqga8l0/C+EeS4oqrSbnO2gy587b1Xu66+leTK/o1t+fnRDqYfNSAlnVazoIULB4+tcxuUqwWTtIshwk6ZGrNsx7m7hl5JNnB4Qg0b/MgfaUSUqQlj0OJ/ut4d16scrKVopuzo+CKKxeuMlvDIlzo4avi78dxgLhcbBOcgycE/ISu1RvfniCXwelUp9xbCafknnktBUmGOjXW9kHAmqkrNMrfq3UWPiy/stOD286OzKfsgaHcULPYY+AklpxAcABL3wPfIFKsTZh2T09NxHIo2ic/cE7GFgXql4AQKBgQDtFnidkjxImfT4MyHC1Xn1XAH6R1c2qVaPfYQLUFEKGid6KUNaf/vvEyhKOBCbc1kcHKDCi/xQBwOwgImAJD7ybps4lmcPrXO0wOmO7KEUSitOoUgCL6aF1036NLdp4NwWO6kr9feSy+F6ob/K1OwPWzExnUJtZfcla+wqVzISoQKBgQCvXP26IrJ8lVZjfITp+z8rY0wyLJ8Tdcdi+lkHLpv/gtjVO5K9P3nRubKlcDXKygngr6E/gbK7TBXfq1H8oYXpe+bqkMq1n9m9qcCj4ypVMpAQHce5nGxZc8Hdr4o6Q4pN/x7tjkb4bQqXxX5eSWBKMo01SjIp7TQIr+gvLuiXmwKBgQC5GVglvSvvirNkq5bqI/zJgIHSwqvmcyKveFEE/Dmo5252w4xgNZeduZk1CLx+gPnLJtajzOK6IB/TRycSsjmq0IvDDDl+Ve8F6a7u9PN1gcp54xTzD/y0dbndZBbmRLXPbdjQB4K4SxDIy0uPsdhyzMGFeL+J5Zek0bNaAyWGwQKBgDlthJHuRvFRnobaAlT1uIwm6o8L2dRvn6okPf+CHggg+41pAvcC0IwD+iL/ij4sx+tS2MWJXdTEZ+1ll2XaJ5Z8wvRSEnBENvRLoRCKlhRsiI0ek4ePWFQ6+mP01pmaALLmN2SoUUmNvAhT3rxV3zitMV0gPSlJyt6FbM322Ki9AoGBALR/sVtMLuc/AVyjRoOpDci3i+pN+FPTEfn+KWS+OAbuJvdQNbFFdhMALFQ+adLznF+JD3cXzCf/s8uf++82F3JfZafTYRs7BM0gi+HbeEo7ApxJHK4dCy1AeYVAMNZ6nt3L1Iljn2kmI4f7VF9/T03LG2hEMmiHKxEE7YeT75tx";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private IWXAPI api;
    private AppConfig appConfig;
    private MyHandler myHandler;


    private static class MyHandler extends Handler {

        private WeakReference<WXPayActivity> mActivity;

        public MyHandler(WXPayActivity mActivity) {
            this.mActivity = new WeakReference<WXPayActivity>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SDK_PAY_FLAG:

                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity.get(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity.get(), "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    break;

            }


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_wx_pay);
        initParams();
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        appConfig = AppConfig.getInstance();
        myHandler = new MyHandler(this);
    }


    /**
     * 微信pay demo 示例
     *
     * @param view
     */
    public void wxpay(View view) {
        api = WXAPIFactory.createWXAPI(this, appConfig.WX_APP_ID);
        api.registerApp(appConfig.WX_APP_ID);

        // TODO: 2017/3/27 我们服务器请求uri
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        ToastUtils.shortShow(R.string.wx_pay_tip);

        try {

            // TODO: 2017/3/27 中间网络请求，返回的参数进行信息组装
            byte[] buf = null;
            if (buf != null && buf.length > 0) {
                String content = new String(buf);
                JSONObject json = new JSONObject(content);
                if (null != json && !json.has("retcode")) {
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
                    ToastUtils.shortShow(R.string.wx_pay_success);
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    Toast.makeText(WXPayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("PAY_GET", "服务器请求错误");
                Toast.makeText(WXPayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(WXPayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 支付宝pay demo 示例
     *
     * @param view
     */
    public void alipay(View view) {

        //配置沙箱模式
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        //ali 入参检查
        if (TextUtils.isEmpty(appConfig.ALI_APP_ID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            return;
        }
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = PayOrderUtil.buildOrderParamMap(appConfig.ALI_APP_ID, rsa2, PayOrderUtil.getOutTradeNo(), "0.01", "1");
        String orderParam = PayOrderUtil.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = PayOrderUtil.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;
        final String orderInfo = "app_id=2016080400162441&biz_content=%7B%22out_trade_no%22%3A%222017041315350800000000100017%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E7%8E%B0%E9%87%91%E8%B4%B7%22%2C%22total_amount%22%3A%221026.00%22%7D&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.xx.com%2Freceive_notify.htm%2FALIPAYHZ0301%2F01%2F&sign=CJp9L79IIegVYT647edTPN%2BrUL2%2FmTwg51DXgzTj%2FTiL5%2B7ic4QLxv8fQoITJB6cONUJZbK6Yv7uUoI4jKWF42li8jd4TlYEO78MtvS3S4vHdKvpCuleY%2FucvH8vwpBmwlRizuWjLox1id4hcTu0PH9EFuLnoUOPFYgxJY2c6xSYdaJSz891o07hEcjaxmIVl4ExUgqUlQFJPiUDjRqsxiJekaXfNoya19SAluES8GxrkfBkA4StbdA23wIuwU8uLBga4hu5s6jTIhn1GQn%2BK6c1%2BRmaUVM5nf75%2FSeiPt%2BtQPmwhukM7qZTBP%2F90iyhRykz0Bayakz2P1FBL%2FnQkw%3D%3D&sign_type=RSA2&timestamp=2017-04-13+15%3A35%3A08&version=1.0";
//        final String orderInfo = "app_id=2016080400162441&biz_content=%7B%22out_trade_no%22%3A%22201704060001001%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%BF%98%E6%AC%BE%22%2C%22total_amount%22%3A%22100%22%7D&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.xx.com%2Freceive_notify.htm%2FALIPAYHZ0301%2F01%2F&sign=nGwk36EaqmOxsiZhh4rLD2k3IEJMHn5azgnxLrW1b%2BZ1nife9CPT5FcDa%2BLkPoMoIYtUo0BvEHkLEgUUgtF%2F56gVZdpMM%2FUFqernCxHx3tVLvLzeYBlLVtw6hMljJa63dOYredA1cv7vgDZIHrKzwHXTq%2BqGoI5HDz71Btigrfcmgl8ySmKfpKAJAueHFifJE0USjWkraX6nlpQE8csehg4huJuaQ8WLrgs8%2FaAlNSoPQ5D5RS4qCWtqfz%2BoqiJR3pznLbsQwLjJlvYgs9UhWLKRumc87NkMiVXXm7NwWRTBTpGe%2FE2KMampwXZtnTQhCiR3Z%2FNx7Dh30qFf5T9o9g%3D%3D&sign_type=RSA2&timestamp=2017-04-07+10%3A30%3A10&version=1.0";

        //实际支付中,调用后台接口得到
        //支付宝demo中是为了能支付，直接在demo中生成
        //这里直接写死
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(WXPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA2\"";
    }

}
