package com.yjh.servlets.cookies;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yjh on 16-1-17.
 */
@WebServlet(name = "cookieServlet", urlPatterns = "/cookie", loadOnStartup = 1)
public class CookieBaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = StringUtils.defaultIfEmpty(req.getParameter("type"), "");

        switch (type) {
            case "1": testBase(req, resp); break;
            case "2": testRule(req, resp); break;
        }
    }

    private void testRule(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        Cookie k1 = CookieUtil.getCookie(cookies, "k1");
        if (k1 == null) {
            k1 = new Cookie("k1", "13,123");
            k1.setPath("/");
            resp.addCookie(k1);
        }
    }

    private void testBase(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        Cookie usernameCookie = CookieUtil.getCookie(cookies, "username");
        if (usernameCookie == null ) {
            usernameCookie = new Cookie("username", "yjh");
            usernameCookie.setMaxAge(5000);
            usernameCookie.setPath("/");
            resp.addCookie(usernameCookie);
        }
    }
}
