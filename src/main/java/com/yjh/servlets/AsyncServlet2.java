package com.yjh.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异步servlet
 * Created by yjh on 16-1-6.
 */
@WebServlet(name = "Async2", urlPatterns = "/async2/*", asyncSupported = true)
public class AsyncServlet2 extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        logger.info("async2 servlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        logger.info(req.getDispatcherType());
        out.println("success async2");
        //非法调用，此时Async的状态是DISPATCHED，complete在startAsync调用之后，dispatch之前进行调用是合法的；否则抛出IllegalState
        //此时分派尚未返回到容器，在service返回后，容器将执行complete
//        asyncContext.complete();
        AsyncContext asyncContext = req.startAsync();
        asyncContext.start(() -> {
            System.out.println("Start!");
            asyncContext.complete(); //完成，进行响应
        });
    }

    @Override
    public void destroy() {
        logger.info("async servlet destroy");
    }
}
