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
@WebServlet(name = "nextAsync", urlPatterns = "/nextAsync", loadOnStartup = 1, asyncSupported = true)
public class NextAsyncServlet extends HttpServlet {
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
            //这里也不能调用complete和dispatch，因为处于分派过程中，但是可以重新startAsyncContext
//            AsyncContext asyncContext = req.getAsyncContext();
//            asyncContext.dispatch("/normal");
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.debug(this.name + " service leaving");
    }

    @Override
    public void destroy() {
        logger.debug(name + " destroy");
    }
}
