package com.yjh.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yjh on 16-1-13.
 */
public class LogFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    {
        logger.debug("LogFilter created");
    }

    @Override
    public void destroy() {
        logger.debug("LogFilter destroy");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("LogFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF8");
//        HttpServletResponse resp = (HttpServletResponse)response;
//        HttpServletRequest req = (HttpServletRequest)request;
//        String host = request.getServerName();
//        if (host.startsWith("localhost")) {
//            logger.debug(req.getRequestURL());
//            resp.setStatus(301);
//            resp.setHeader( "Location", "http://www.baidu.com/");
//            resp.setHeader( "Connection", "close");
//        } else
            chain.doFilter(request, response);
//        response.getWriter().println("After ");
    }
}
