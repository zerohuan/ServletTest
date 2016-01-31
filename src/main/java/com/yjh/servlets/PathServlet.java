package com.yjh.servlets;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yjh on 16-1-30.
 */
@WebServlet(name = "pathServlet", urlPatterns = "/path", loadOnStartup = 1)
public class PathServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(IOUtils.toString(this.getClass().getResource("/log4j2.xml")));
        out.println(this.getClass().getResource("/log4j2.xml").getPath());
    }
}
