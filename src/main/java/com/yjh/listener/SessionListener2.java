package com.yjh.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by yjh on 16-1-14.
 */
public class SessionListener2 implements HttpSessionIdListener, HttpSessionListener {
    private static final Logger logger = LogManager.getLogger();
    {
        logger.debug("SessionListener2 created");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {

    }
}
