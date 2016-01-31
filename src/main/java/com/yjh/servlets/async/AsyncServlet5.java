package com.yjh.servlets.async;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 一个常用方法模型：异步分派：
 * （1）分派到支持异步的Servlet；
 * （2）分派到jsp和不支持异步Servlet；
 * （3）同步Servlet分派到异步Servlet是非法的，因为前者不具备在请求完成前返回的能力；
 * Created by yjh on 16-1-14.
 */
@WebServlet(name = "asyncServlet5", urlPatterns = "/async5", loadOnStartup = 1, asyncSupported = true)
public class AsyncServlet5 extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private String name;

    {
        logger.debug("AsyncServlet5 created");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        name = config.getServletName();
        logger.debug(name + "init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isDispatchAsync = StringUtils.defaultIfEmpty(req.getParameter("isAsync"), "");
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(60000);
        if (isDispatchAsync.equals("true")) {
            asyncContext.dispatch("/nextAsync");
        } else {
            asyncContext.dispatch("/normal"); //分派到不支持异步，会在返回容器后由容器调用complete方法
        }
        resp.getWriter().println(this.name + " leaving");
        resp.flushBuffer();
        logger.debug(this.name + " leaving"); //可以在分派返回容器之前返回
    }

    @Override
    public void destroy() {
        logger.debug(name + "destroy");
    }
}
