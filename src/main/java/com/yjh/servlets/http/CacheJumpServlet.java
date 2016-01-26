package com.yjh.servlets.http;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 缓存实例
 * Created by yjh on 16-1-20.
 */
@WebServlet(name = "cacheJ", urlPatterns = "/cacheJ", loadOnStartup = 1)
public class CacheJumpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/p/cache/cacheTest.jsp").forward(req, resp);
    }
}
