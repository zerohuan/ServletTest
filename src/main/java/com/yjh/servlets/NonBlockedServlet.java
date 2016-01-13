package com.yjh.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用异步非阻塞I/O
 * Created by yjh on 16-1-7.
 */
@WebServlet(name = "nonBlockedServlet", urlPatterns = "/nonBlocked/*", asyncSupported = true)
public class NonBlockedServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        ServletInputStream inputStream = req.getInputStream();
        inputStream.setReadListener(new MReadListener(asyncContext, inputStream));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private static class MReadListener implements ReadListener {
        private final ServletInputStream inputStream;
        private final AsyncContext context;

        public MReadListener(AsyncContext context, ServletInputStream inputStream) {
            this.context = context;
            this.inputStream = inputStream;
        }

        @Override
        public void onDataAvailable() throws IOException {
            byte[] buffer = new byte[2048];
            int len;
            StringBuilder sb = new StringBuilder();
            while (inputStream.isReady() && (len = inputStream.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, len));
            }
            /*处理请求数据*/
            logger.debug(sb);
        }

        @Override
        public void onAllDataRead() throws IOException {
            //异步处理完成
            context.complete();
        }

        @Override
        public void onError(Throwable t) {
            logger.error(t);
        }
    }
}
