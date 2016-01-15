package com.yjh.servlets.async;

import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

/**
 * Created by yjh on 16-1-14.
 */
public class MonitorAsyncListener implements AsyncListener {
    private final Logger logger;

    public MonitorAsyncListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        logger.debug("onComplete");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.debug("onTimeout");
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        logger.debug("onError");
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        logger.debug("onStartAsync");
    }
}
