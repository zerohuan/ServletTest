package com.yjh.listener;

import com.yjh.servlets.multiterminal.LoginServlet;
import com.yjh.util.ContextConstantUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * Created by yjh on 16-1-17.
 */
public class MServletContextListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger();

    {
        logger.debug("MServletContextListener created");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("MServletContextListener contextDestroyed");
        for (Map.Entry<String, Thread> entry : LoginServlet.T_POOL.entrySet()) {
            Thread t;
            if (!(t = entry.getValue()).isInterrupted()) {
                t.interrupt();
                logger.debug(t + "interrupted");
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("MServletContextListener init");
        //获取ServletContext对象
        ServletContext servletContext = sce.getServletContext();
        //添加一个监听器
        servletContext.addListener(new SessionListener2());
        //初始化基于上下文的全局属性
        ContextConstantUtil.init(servletContext);
    }


}
