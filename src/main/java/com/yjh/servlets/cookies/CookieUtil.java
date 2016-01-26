package com.yjh.servlets.cookies;

import javax.servlet.http.Cookie;

/**
 * Created by yjh on 16-1-18.
 */
public final class CookieUtil {
    private CookieUtil() {
    }

    public static String getCookieStr(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key))
                    return cookie.getValue();
            }
        }
        return null;
    }

    public static Cookie getCookie(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key))
                    return cookie;
            }
        }
        return null;
    }
}
