package com.miaodao.Sys.Utils.cookie;

import com.miaodao.Application.WheatFinanceApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by simondai on 2017/3/13.
 * <p>
 * 管理cookie
 */

public class CookieManger implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(WheatFinanceApplication.getInstance());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        Cookie cookie = new Cookie.Builder()
                .domain(url.host())
                .name("Cookie")
                // TODO: 2017/3/13 将sessionId保存到cookie中
                .value("MTPSESSIONID=" + "bfdabfdabfdabfdab").build();
        cookies.add(cookie);

//        List<Cookie> cookies = cookieStore.get(url);


        return cookies;
    }
}
