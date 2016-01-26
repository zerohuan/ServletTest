package com.yjh.servlets.response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;

/**
 * ResponseServlet Info
 * Created by yjh on 16-1-16.
 */
@WebServlet(name = "responseInfoServlet", urlPatterns = "/resp", loadOnStartup = 1)
public class ResponseServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String type = StringUtils.defaultIfEmpty(req.getParameter("type"), "");
        switch (type) {
            case "buffer" : showBuffer(req, resp); break;
            case "redirect" : showRedirect(req, resp); break;
            case "locale" : showLocale(req, resp); break;
            default : showEncode(req, resp);
        }
    }
    //http://localhost:8080/s/resp?type=redirect&type=2
    private void showRedirect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String respType = StringUtils.defaultIfEmpty(req.getParameter("respType"), "1");
        switch (respType) {
            case "1" : out.println("data1"); break; //有数据写入缓冲区，但未提交响应，调用sendError或setRedirect后，将重置缓冲区，舍弃原来的数据
            case "2" : out.println("data1"); out.flush(); break; //有数据写入缓冲区，并未提交响应
        }
        try {
            resp.sendRedirect("/s/nextR");
        } catch (IllegalStateException e) {
            out.println("不能在响应提交后，调用这两个方法");
        }
        out.println("data after redirect"); //sendRedirect和sendError后servlet中的输出将被忽略
        out.flush();
    }

    /**
     * 注意在{@link com.yjh.filters.LogFilter}调用setContentType设置了响应头
     * @see com.yjh.filters.LogFilter
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void showLocale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //浏览器支持的第一个locale
        Locale first= req.getLocale();
        //覆盖OS中的locale设置
//        Locale.setDefault(first);
        //设置响应的locale
//        resp.setLocale(first);
//        resp.setContentType("text/html; charset=utf8");
//        resp.addHeader("Content-Type", "text/html; charset=utf8");
//        resp.setCharacterEncoding("UTF8");

        //如果在 servlet 响应的 getWriter 方法已经调用之后或响应被提交之后,调用相关方法设置字符编码将没有任何作用。
        PrintWriter out = resp.getWriter();
        Enumeration locales = req.getLocales();
        while(locales.hasMoreElements())
        {
            Locale clientLocale=(Locale)locales.nextElement();

            out.println("客户端:"+clientLocale+"<br>");
            out.println("国别:"+clientLocale.getDisplayCountry()+"<br>");
            out.println("语言:"+clientLocale.getDisplayLanguage()+"<br>");
        }
    }
    private void showEncode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void showBuffer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        //缓冲
        out.println(String.format("默认缓冲区大小：%s", resp.getBufferSize())); //8192
        try {
            resp.setBufferSize(10000);
        } catch (IllegalStateException e) {
            out.println("不能在缓冲区写入内容后在修改大小");
        }
        resp.flushBuffer();
        out.println(String.format("是否刷新到客户端： %b", resp.isCommitted()));
        //当响应没有提交时，可以重置缓冲区，否则不可，重置会抛出IllegalStateException
        try {
            resp.resetBuffer();
        } catch (IllegalStateException e) {
            out.println("不能在缓冲区提交内容后在重置缓冲区");
        }
    }
}
