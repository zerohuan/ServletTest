package com.yjh.config;

import com.yjh.servlets.UploadServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

/**
 * 通过ServletContextListener配置ServletContext
 * Created by yjh on 16-1-13.
 */
@WebListener("BootStrap Listener")
public class BootStrap implements ServletContextListener {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("ServletContextListener invoked");
        //获取ServletContext对象
        ServletContext servletContext = sce.getServletContext();
        //添加Servlet
        ServletRegistration.Dynamic uploadRegistration = servletContext.addServlet("uploadServlet", UploadServlet.class);
        uploadRegistration.addMapping("/upload/*");
        uploadRegistration.setLoadOnStartup(1);
        MultipartConfigElement configElement = new MultipartConfigElement(".", 52428800,
                52428800, 0);
        uploadRegistration.setMultipartConfig(configElement);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("ServletContextListener destroy");
    }
}
