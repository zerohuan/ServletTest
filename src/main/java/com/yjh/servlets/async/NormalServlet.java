package com.yjh.servlets.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 不支持异步的Servlet
 * Created by yjh on 16-1-14.
 */
@WebServlet(name = "nonAsync", urlPatterns = "/normal", loadOnStartup = 1)
public class NormalServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private String name;

    @Override
    public void init(ServletConfig config) throws ServletException {
        name = config.getServletName();
        logger.debug(name + "init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TimeUnit.SECONDS.sleep(5);
            //不能在不支持异步的Servlet中操作AsyncContext，返回后由容器调用complete
//            AsyncContext asyncContext = req.getAsyncContext();
//            asyncContext.complete();
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.debug(this.name + " service leaving");
    }

    @Override
    public void destroy() {
        logger.debug(name + "destroy");
    }
}
