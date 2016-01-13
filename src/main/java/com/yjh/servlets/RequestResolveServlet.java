package com.yjh.servlets;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by yjh on 16-1-7.
 */
@WebServlet(name = "requestResolve", urlPatterns = "/request/*")
public class RequestResolveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        //参数，从getRequestURI和getPathInfo返回的字符串中解析，这时就开始编码了，因此要在调用getParameter前设置正确的编码方式
        out.println(req.getParameter("s"));
        out.println(req.getParameterMap());
        out.println(req.getParameterNames());
        out.println(Arrays.toString(req.getParameterValues("s")));

        //文件上传

        /*
        路径转换:
        http://localhost:8080/s/request/pathinfo, 在我的机器上返回“/home/yjh/wks/workspace/ServletTest/target/servletTest/pathinfo”
         */
        out.println(req.getPathTranslated());
        out.println(req.getServletContext().getRealPath(req.getPathInfo()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(req.getParameter("s"));
        out.println(req.getParameterMap());
        out.println(req.getParameterNames());
        out.println(Arrays.toString(req.getParameterValues("s")));

        out.println(IOUtils.toString(req.getInputStream()));
    }

}
