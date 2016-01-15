package com.yjh.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yjh on 16-1-14.
 */

public class AsyncFilter1 implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private String name;

    @Override
    public void destroy() {
        logger.debug("AsyncFilter1 destroy");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("AsyncFilter1 init");
        this.name = filterConfig.getFilterName();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("Entering " + this.name + ".doFilter()");
        chain.doFilter(request, response);
        if (request.isAsyncSupported() && request.isAsyncStarted()) {
            AsyncContext asyncContext = request.getAsyncContext();
            logger.debug("asyncContext state: " + asyncContext.hasOriginalRequestAndResponse());
        } else {
            logger.debug("leaving " + this.name + ".doFilter()");
        }
    }
}
