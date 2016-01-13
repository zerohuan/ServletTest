package com.yjh.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-1-6.
 */
@WebServlet(name = "Async3", urlPatterns = "/async3/*", asyncSupported = true)
public class AsyncServlet3 extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        logger.info("async3 servlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //在getParameter之前调用，否则getParameter将使用默认编码
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=UTF-8");

        PrintWriter out = resp.getWriter();
        AsyncContext asyncContext = req.startAsync();
//        asyncContext.complete(); //调用complete之后，Async的状态为MUST_COMPLETE，不能再进行分派
        asyncContext.setTimeout(1000); //不设置则是servlet默认的超时时间
        asyncContext.addListener(new AsyncListener() {
            //onComplete将延迟到分派返回到容器之后进行
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                logger.info("onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                logger.info("onTimeout");
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                logger.info("onError");
                logger.error(event.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                logger.info("onStartAsync");
            }
        });
//        asyncContext.complete(); //在dispatch之后调用complete， Async state = MUST_DISPATCH，IllegalStateException
        //异步任务，也是一次分派
        asyncContext.start(()->{
            try {
//                asyncContext.getResponse().getWriter().println("start async task"); //超时可能会IllegalStateException
                TimeUnit.SECONDS.sleep(5);
                System.out.println("already timeout!");
//                asyncContext.complete();
//                asyncContext.setTimeout(6); //可能会IllegalStateException，已经执行了complete方法
//                asyncContext.getResponse().getWriter().println("start async end"); //超时可能会IllegalStateException
            } catch (Exception e) {
                logger.error(e);
            }
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//            asyncContext.dispatch("/upload.jsp");
        });
        out.println("success");
    }

    @Override
    public void destroy() {
        logger.info("async servlet destroy");
    }
}
