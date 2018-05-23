package com.miaodao.Application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.maizi.MyApplication;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.miaodao.Base.BaseActivity;
import com.miaodao.Base.BaseFragment;
import com.miaodao.Fragment.Account.gesturepd.LoginGestureActivity;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.service.baidu.LocationService;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * application
 * Created by Home_Pc on 2017/3/8.
 */

public class WheatFinanceApplication extends MyApplication {

    private final String TAG = WheatFinanceApplication.class.getSimpleName();

    private static WheatFinanceApplication instance;
    private Map<String, ArrayList<WeakReference<Object>>> msgHandlerMap = new HashMap();
    public LocationService locationService;
    //经纬度
    private String latitude = "", longitude = "";
    //地址信息
    private String address = "";
    private String province = "";
    private String city = "";

    //手势密码相关
    private long time = -2;
    private int count = 0;

    public static WheatFinanceApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        instance = this;
        initBaiduLBS();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());


        gestureRegister();
    }


    /**
     * 注册监听所有Activity的生命周期回调
     */
    private void gestureRegister() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    // 首先计算时间 网络请求判断是否进入手势密码验证界面
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle" + count);
                    if (-2 == time) {
                        //判断进入解锁手势密d
                        showGesture();
                    } else {
                        Date date2 = new Date();
                        long returnTime = date2.getTime();
                        double sub = new BigDecimal(returnTime).subtract(new BigDecimal(time)).doubleValue();
                        if (sub >= 3000d) {//此处是判断应用到后台多久时间以后需要开启手势密码
                            //判断进入解锁手势密码\
                            showGesture();
                        } else {
                            time = -1;
                        }
                    }

                } else {
                    if (-2 == time) {
                        //判断进入解锁手势密
                        showGesture();
                    }
                    time = -1;
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped()" + count);
                count--;
                if (count == 0) {
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle" + count);
                    Date date = new Date();
                    time = date.getTime();
                } else {
                    time = -1;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 跳转到手势密码页面
     */
    private void showGesture() {

        String gesturePd = SharedPreferencesUtil.getString(this, AppConfig.getInstance().GESTURE_PD, "");
        if (!TextUtils.isEmpty(gesturePd)) {
            Intent intent = new Intent(instance, LoginGestureActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    /**
     * 初始化百度地图
     */

    private void initBaiduLBS() {
        locationService = new LocationService(getApplicationContext());
    }

    public static void addMsgHandler(String msgKey, Object msgHandler) {
        instance.pushMsgHandler(msgKey, msgHandler);
    }

    private void pushMsgHandler(String msgKey, Object msgHandler) {
        if ((msgKey == null) || (msgHandler == null)) {
            return;
        }
        ArrayList<WeakReference<Object>> list = (ArrayList) this.msgHandlerMap
                .get(msgKey);
        if (list == null) {
            list = new ArrayList();
            this.msgHandlerMap.put(msgKey, list);
        }
        boolean exist = false;
        for (WeakReference<Object> item : list) {
            if (item.get() == msgHandler) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            list.add(new WeakReference(msgHandler));
        }
    }

    public static void postMsg(String msgKey, Object msg) {
        instance.pushMsg(msgKey, msg);
    }

    private void pushMsg(String msgKey, Object msg) {
        ArrayList<WeakReference<Object>> list = (ArrayList) this.msgHandlerMap.get(msgKey);
        if (list == null) {
            return;
        }
        Iterator localIterator = list.iterator();

        WeakReference<Object> item = (WeakReference) localIterator.next();
        Object handler = item.get();
        if ((handler instanceof BaseActivity)) {
            ((BaseActivity) handler).onActivityReceiveMessage(msgKey, msg);
        } else if ((handler instanceof BaseFragment)) {
            ((BaseFragment) handler).onFragmentReceiveMessage(msgKey, msg);
        } else {

        }
    }

    public void setLruCache(String key, Bitmap bitmap) {
        if (StringUtils.isBlank(key) || bitmap == null) {
            return;
        }
        lruCache.put(key, bitmap);
    }

    public Bitmap getLruCache(String key) {
        return lruCache.get(key);
    }

    public void clearLruCache() {
        lruCache.evictAll();
    }

    public LruCache<String, Bitmap> lruCache = new LruCache<>(1024 * 1024 * 10);


    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wx78d20f784cd62d4a", "7631a0d8608f6ca28f05aea05796a663");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("1106156819", "KEY8Q3K2CzJUakQ7IzH");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
