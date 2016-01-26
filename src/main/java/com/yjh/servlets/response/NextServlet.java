package com.yjh.servlets.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yjh on 16-1-16.
 */
@WebServlet(name = "nextServletResponseInfo", urlPatterns = "/nextR", loadOnStartup = 1)
public class NextServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("NextServlet here!");

        out.flush();
    }
}
