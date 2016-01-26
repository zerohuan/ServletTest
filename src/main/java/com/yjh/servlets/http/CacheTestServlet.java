package com.yjh.servlets.http;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * 缓存实例
 * Created by yjh on 16-1-20.
 */
@WebServlet(name = "cache", urlPatterns = "/cache", loadOnStartup = 1)
public class CacheTestServlet extends HttpServlet {
    private Random rnd = new Random();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String maxAge = StringUtils.defaultString(req.getParameter("maxAge"));
        String noCache = StringUtils.defaultString(req.getParameter("noCache"));
        String noStore = StringUtils.defaultString(req.getParameter("noStore"));

        String cacheControlResp = "public";
        if (!maxAge.equals(""))
            cacheControlResp += ", max-age=" + maxAge;
        if (noCache.equals("y"))
            cacheControlResp += ", no-cache";
        if (noStore.equals("y"))
            cacheControlResp += ", no-store";
//        resp.addHeader("Cache-Control", cacheControlResp);
        out.println("test content " + rnd.nextInt());
    }

}
