package com.miaodao.Sys.Utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Sys.Config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的工具类
 * Created by Home_Pc on 2016/7/26.
 */
public class StringUtils {

    public static final String EMPTY = "";

    /**
     * 判断字符串不是空白字符串
     *
     * @return
     */
    public static boolean isNotBlank(String str) {
        if (null == str || "".equals(str)) {
            return false;
        } else if ("".equals(str.trim())) {
            return false;
        } else if ("".equals(replaceBlank(str))) {
            return false;
        }
        return true;
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String stringTrimAll(String str) {
        return stringFilter(ToDBC(replaceSqlBlank(str)));
    }

    public static String replaceSqlBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取控件的值
     *
     * @param tview
     * @return
     */
    public static String getTextViewContent(TextView tview) {
        if (null == tview || null == tview.getText()) {
            return null;
        } else if (StringUtils.isBlank(tview.getText().toString())) {
            return "";
        } else {
            return tview.getText().toString().trim();
        }
    }


    public static boolean isBlank(String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        } else if ("".equals(replaceBlank(str))) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\\\\r|\\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 在字符串判断是否存在匹配的字符
     *
     * @param paramStr 要查找的字符串
     * @param subStr   待匹配的字符
     * @return
     */
    public static boolean isExistSubString(String paramStr, String subStr) {
        if (null == paramStr || null == subStr) {
            return false;
        }
        if (paramStr.indexOf(subStr) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 在字符串判断是否存在匹配的字符
     *
     * @param paramStr 要查找的字符串
     * @param subStr   待匹配的字符
     * @return
     */
    public static boolean isExistSubStringOrBlank(String paramStr, String subStr) {
        if (null == paramStr || null == subStr) {
            return true;
        }
        if (paramStr.indexOf(subStr) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是空字符串
     *
     * @param str 带匹配的字符串
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEquals(String s1, String s2) {
        if (null == s1 && null == s2) {
            return true;
        }
        if (null != s1 && null != s2) {
            if (s1.equals(s2)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static boolean isNumberNotZero(String str) {
        if (isNotBlank(str)) {
            try {
                Double i = Double.parseDouble(str);
                if (i != 0) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static String getTwoDecimal(String str) {
        if (isBlank(str)) {
            return "0";
        }
        String reuslt = "";
        try {
            BigDecimal bg = new BigDecimal(str);
            BigDecimal resValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (resValue.doubleValue() == 0) {
                reuslt = "0";
            } else {
                reuslt = resValue.toString();
            }
        } catch (Exception e) {
            reuslt = "";
        }
        return reuslt;
    }

    public static String trimToEmpty(String str) {
        if (str == null || str.equals("")) {
            return EMPTY;
        }
        return str.trim();
    }

    public static String Stringof(Object obj) {
        if (obj == null)
            return EMPTY;
        else
            return String.valueOf(obj);
    }

    //判断是否为数字
    public static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return b;
    }

    //判断是否为汉字
    public static boolean isChineseStr(String str) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{0,}$");
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return b;
    }


    /**
     * 判断网络请求返回的信息是不是空
     *
     * @param resultMap
     * @return
     */
    public static boolean isResponseNull(Map<String, Object> resultMap) {
        if (resultMap == null) return true;
        if (resultMap.equals(null)) return true;
        if (resultMap.isEmpty()) return true;
        return false;
    }


    /**
     * 将请求转换成json string
     *
     * @param requestMap
     * @return
     */
    public static String toJsonString(Map<String, Object> requestMap) {
        requestMap.put("source", "Android");
        requestMap.put("deviceId", AdrToolkit.getIMEI(WheatFinanceApplication.getInstance()));
        requestMap.put("mac", StringUtils.getWifiMac(WheatFinanceApplication.getInstance()));
        requestMap.put("uuid", UUID.randomUUID().toString());
        requestMap.put("geogX", WheatFinanceApplication.getInstance().getLatitude());
        requestMap.put("geogY", WheatFinanceApplication.getInstance().getLongitude());
        return JSON.toJSONString(requestMap);
    }


    /**
     * 获取返回数据的data
     * @param response
     * @return
     */
    public static String getResponseData(String response) {

        JSONObject object = null;
        try {
            object = new JSONObject(response);

            String data = object.optString("data");
            if (TextUtils.isEmpty(data)) {
                return "";
            }
            if (data.equals(null)) {
                return "";
            }
            return data;


        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }


    /**
     * 返回请求的url
     *
     * @param uri
     * @return
     */
    public static String toUtl(String uri) {
        return AppConfig.getInstance().appUrl + uri;
    }


    /**
     * 获取手机的WIFI MAC 地址
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * 密码正则
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
//        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[A-Z]{1,}[0-9]{1,}[0-9A-Za-z]{4,18}$";
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,16}$";
        return password.matches(regex);
    }


    /**
     * 手机正则
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumer(String phoneNumber) {
        String regex = "^1[3|4|5|8|7][0-9]\\d{8}$";
        return phoneNumber.matches(regex);
    }


    /**
     * 身份证正则
     * @param idCard
     * @return
     */
    public static boolean isCard(String idCard){
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        return idCard.matches(regex);
    }


    /**
     * 身份证正则
     * @param idCard
     * @return
     */
    public static boolean isCard1(String idCard){
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X)$)";
        return idCard.matches(regex);
    }



}
