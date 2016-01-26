package com.yjh.servlets.async;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 一个常用方法模型：耗时任务提交线程池异步执行
 * Created by yjh on 16-1-13.
 */
@WebServlet(name = "asyncServletSSE", urlPatterns = "/asyncSSE", loadOnStartup = 1, asyncSupported = true)
public class AsyncServletSSE extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0); //默认是Servlet的超时时间，默认30秒，这里设置为notimeout
        asyncContext.addListener(new MonitorAsyncListener(logger));
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = asyncContext.getResponse().getWriter();
                    while (true) {
                        String msg = queue.take();
                        out.println(msg);
                        out.flush();
                    }
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                asyncContext.complete(); //任务完成调用onComplete，通知，调用回调函数
            }
        });
        new Thread(new DataHandler(queue)).start();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0); //默认是Servlet的超时时间，默认30秒，这里设置为notimeout
        asyncContext.addListener(new MonitorAsyncListener(logger));
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String msg = queue.take();
                        PrintWriter out;
                        (out = asyncContext.getResponse().getWriter()).println(msg);
                        out.flush();
                    }
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                asyncContext.complete(); //任务完成调用onComplete，通知，调用回调函数
            }
        });
        new Thread(new DataHandler(queue)).start();
    }

    private static class DataHandler implements Runnable {
        private final BlockingQueue<String> queue;
        public DataHandler(BlockingQueue<String> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            int id = 1;
            while (true) {
                try {
                    queue.put(String.valueOf(id++));
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }

}
