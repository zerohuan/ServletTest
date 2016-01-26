package com.yjh.servlets.multiterminal;

import com.yjh.util.QRCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一登录接口
 * Created by yjh on 16-1-18.
 */
@WebServlet(name = "loginServlet", urlPatterns = "/login", loadOnStartup = 1, asyncSupported = true)
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    //PC端轮询标识
    private static final Map<String, MUserStatus> PC_STATUS = new ConcurrentHashMap<>();
    //如果存在异步检查状态的任务线程中存在阻塞状态的（ms.wait()）在ServletContextListener中进行中断
    //使用WeakHashMap不影响GC
    public static final Map<String, Thread> T_POOL = new WeakHashMap<>();
    private static final long TIMEOUT = 10000;

    //模拟的验证数据库
    private static final Map<String, String> VERIFY_DATABASE = new HashMap<>();
    @Override
    public void init() throws ServletException {
        super.init();
        VERIFY_DATABASE.put("username", "pwd123");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //请求类型
        String requestType = StringUtils.defaultIfEmpty(req.getParameter("type"), "");
        //扫码页面检查服务器登录状态的方式
        String checkType = StringUtils.defaultIfEmpty(req.getParameter("checkType"), "poll");
        switch (requestType) {
            case "mobile":
                if (!isLogin(req))
                    req.getRequestDispatcher("/p/login/login.jsp").forward(req, resp); //跳转到登录界面
                else
                    req.getRequestDispatcher("/p/login/index.jsp").forward(req, resp); //跳转到首页
                break;
            case "pc": //http://localhost:8080/s/login?type=pc&uid=jwre13123
                //获取PC端轮询标识
                String uid = StringUtils.defaultString(req.getParameter("uid"), "123123");
                //保存二维码图片
                try {
                    QRCodeUtil.saveQR("http://192.168.1.110:8080/s/login?type=mobileCheck&uid=" + uid, uid);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                MUserStatus ms;
                PC_STATUS.put(uid, new MUserStatus(false, uid));
                req.setAttribute("uid", uid);
                //跳转到扫码界面
                req.getRequestDispatcher("/p/login/loginByQR" + (checkType.equals("poll") ? "Poll" : "") + ".jsp")
                        .forward(req,
                                resp);
                break;
            case "mobileCheck":
                uid = StringUtils.defaultString(req.getParameter("uid"), "123123");
                ms = PC_STATUS.get(uid);
                if (ms != null) {
                    synchronized (ms) {
                        ms.setIsLogin(true);
                        ms.notifyAll();
                    }
                }
                break;
            case "pcCheck":
                //用异步实现，避免占用连接资源
                uid = StringUtils.defaultString(req.getParameter("uid"), "123123");
                resp.setContentType("text/html; charset=utf-8");
                AsyncContext asyncContext = req.startAsync();
                asyncContext.setTimeout(TIMEOUT);
                asyncContext.start(new StatusCheck(PC_STATUS.get(uid), uid, asyncContext));
                break;
            case "pcCheckPoll": //客户端轮询检查接口
                resp.setContentType("text/event-stream; charset=UTF-8");
                resp.addHeader("Cache-Control", "no-cache; must-revalidate");
                PrintWriter out = resp.getWriter();
                uid = StringUtils.defaultString(req.getParameter("uid"), "123123");
                ms = PC_STATUS.get(uid);
                if (ms != null && ms.isLogin)
                    out.println("data: checked\n\n");
                else
                    out.println("data: unchecked\n\n");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //手机端登录逻辑
        String username = StringUtils.defaultString(req.getParameter("username"), "");
        String password = StringUtils.defaultString(req.getParameter("password"), "");
        if (verifyUser(username, password)) {
            //这里简单的放在session中标记已登录
            req.getSession().setAttribute("isLogin", "true");
            req.getRequestDispatcher("/p/login/index.jsp").forward(req, resp); //跳转到首页
        } else {
            PrintWriter out = resp.getWriter();
            out.println("fail");
        }
    }
    private static boolean isLogin(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String isLogin;
        return (isLogin = (String)session.getAttribute("isLogin")) != null && isLogin.equals("true");
    }
    private static boolean verifyUser(String username, String password) {
        String temp;
        return (temp = VERIFY_DATABASE.get(username)) != null && temp.equals(password);
    }

    private static class StatusCheck implements Runnable {
        private final String uid;
        private final MUserStatus ms;
        private final AsyncContext asyncContext;

        public StatusCheck(MUserStatus ms, String uid, AsyncContext asyncContext) {
            this.ms = ms;
            this.uid = uid;
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            if (ms != null) {
                synchronized (ms) {
                    try {
                        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
                        response.addHeader("Cache-Control", "no-store");
                        PrintWriter out = asyncContext.getResponse().getWriter();
                        try {
                            T_POOL.put(uid, Thread.currentThread());
                            logger.debug(Thread.currentThread());
                            int count = 0;
                            //用超时时间防止PC端放弃登录而是该线程一直阻塞
                            while (!ms.isLogin() && count++ == 0) {
                                ms.wait(TIMEOUT - 10);
                            }
                            if (count == 1)
                                out.print("checked");
                            else
                                out.print("timeout");
                        } catch (InterruptedException e) {
                            out.print("fail");
                            Thread.currentThread().interrupt();
                        } finally {
                            asyncContext.complete();
                            PC_STATUS.remove(uid); //清除登录标志
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static class MUserStatus {
        private String uid;
        private boolean isLogin;

        public MUserStatus(boolean isLogin, String uid) {
            this.isLogin = isLogin;
            this.uid = uid;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public void setIsLogin(boolean isLogin) {
            this.isLogin = isLogin;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
