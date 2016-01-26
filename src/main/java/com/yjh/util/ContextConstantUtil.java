package com.yjh.util;

import javax.servlet.ServletContext;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yjh on 16-1-18.
 */
public final class ContextConstantUtil {
    private ContextConstantUtil() {
    }
    private static AtomicBoolean IS_INITIALIZED = new AtomicBoolean(false);

    //基于容器上下文的全局属性
    private static String CONTEXT_REAL_PATH;
    private static String QR_PIC_PATH;
    public static final String CONTEXT_URL = "http://localhost:8080/s/";

    //保证只初始化一次
    public static void init(ServletContext servletContext) {
        if (IS_INITIALIZED.compareAndSet(false, true)) {
            CONTEXT_REAL_PATH = servletContext.getRealPath("");
            QR_PIC_PATH = CONTEXT_REAL_PATH + "/qr/";
        }
    }

    public static String getContextRealPath() {
        return CONTEXT_REAL_PATH;
    }

    public static String getQrPicPath() {
        return QR_PIC_PATH;
    }
}
