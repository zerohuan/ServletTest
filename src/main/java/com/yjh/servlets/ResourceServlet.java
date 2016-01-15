package com.yjh.servlets;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * 通过
 * Created by yjh on 16-1-13.
 */
@WebServlet(name = "resourceServlet", urlPatterns = "/r", loadOnStartup = 1)
public class ResourceServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        String path = req.getParameter("path");
        PrintWriter out = resp.getWriter();
//        logger.debug(path);
        if (!StringUtils.isEmpty(path) && path.startsWith("/")) {
            URL url = servletContext.getResource(path);
            out.println(url);
        } else {
            out.println("请输入一个以“/”开头的路径");
        }
    }
}
