package com.yjh.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by yjh on 16-1-13.
 */
@WebServlet (
        name = "uploadServlet2",
        urlPatterns = "/upload2",
        loadOnStartup = 1
)
@MultipartConfig (
        fileSizeThreshold = 5_242_880,
        maxFileSize = 20_971_520L,
        maxRequestSize = 41_943_040L
)
public class UploadServlet2 extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/p/upload/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        logger.debug("uploading...");
        req.setAttribute("parts", parts);
        req.getRequestDispatcher("/p/upload/resolvePart.jsp").forward(req, resp);
    }
}
