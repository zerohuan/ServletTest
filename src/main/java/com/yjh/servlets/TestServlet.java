package com.yjh.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Created by yjh on 16-1-6.
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        //显示每次请求的线程ID
        out.println(Thread.currentThread() + " " + this.toString());
    }
}

// 请求和响应，与客户端进行交互的是servlet容器，容器将请求dispatch到对应的servlet（委托），取得response后返回到客户端；（通信基于HTTP等协议）
// # Servlet：
// ## Servlet的数量
// servlet默认是线程不安全的，一个容器中只有每个servlet一个实例，但是如果实现了SingleThreadModule接口，容器将实现多个servlet实例
// SingleThreadModule也能保证线程安全，它只能保证任意两个线程不会使用同一个Servlet实例（可能由一个对象池来维护），servlet 2.4已经将这个接口弃用了；
//
// ## servlet的生命周期
// 加载和实例化：servlet容器负责加载和实例化Servlet，在容器启动时根据设置决定是在启动时初始化，还是延迟初始化致第一次请求前；
//
// 初始化：``init()``，执行一些一次性的动作，可以通过ServletConfig配置对象，获取初始化参数，访问ServletContext上下文环境；
// 初始化时可能发生错误，UnavailableException和ServletException，那么servlet不应放置活动服务中，未成功初始化，destroy方法也应被调用
//
// 请求处理：servlet容器封装Request和Response对象传给对应的servlet的service方法，对于HttpServlet，就是HttpServletRequest和HttpServletResponse；
// HttpServlet中使用模板方法模式，service方法根据HTTP请求方法进一步分派到doGet，doPost等不同的方法来进行处理；
// servlet中默认线程不安全，单例多线程，因此对于共享的数据（静态变量，堆中的对象实例等）自己维护进行同步控制，不要在service方法或doGet等由service分派出去的方法，直接使用synchronized
// 方法，很显然要根据业务控制同步控制块的大小进行细粒度的控制，将不影响线程安全的耗时操作移出同步控制块；
// 请求处理时同样可能抛出异常，UnavailableException和ServletException；UnavailableException表示不可用，永久不可用状态返回404；暂时不可用返回503（服务不可用），RETRY
// -After；
//
// 异步处理：
// Servlet 3.0后引入了异步处理请求的能力；异步处理中servlet的service在任务执行（另一个线程中）完成前可以可以不产生响应返回，任务处理完成后可以通过AsyncListener回调进行相应的处理；
// 关键方法：
// 启用：让servlet支持异步支持:asyncSupported = true；
// 启动：AsyncContext asyncContext = req.startAsyncContext();/startAsyncContext(req, resp);
// 完成：asyncContext.complete(); 必须在startAsync调用之后，分派进行之前调用；同一个AsyncContext不能同时调用dispatch和complete
// 分派：asyncContext.dispatch();dispatch(String path);dispatch(ServletContext context, String path);不能在complete之后调用；
// 从一个同步servlet分派到异步servlet是非法的；
// 超时：asyncContext.setTimeout(millis);
// 超时之后，将不能通过asyncContext进行操作，但是可以执行其他耗时操作；
// 在异步周期开始后，容器启动的分派已经返回后，调用该方法抛出IllegalStateException；
// 事件监听：addListener(new AsyncListener {...})；
// onComplete：完成时回调，如果进行了分派，onComplete方法将延迟到分派返回容器后进行调用；
// onError：可以通过AsyncEvent.getThrowable获取异常；
// onTimeout：超时进行回调；
// onStartAsync：在该AsyncContext中启动一个新的异步周期（调用startAsyncContext）时，进行回调；
//
// 超时和异常处理，步骤：
// （1）调用所有注册的AsyncListener实例的onTimeout/onError；
// （2）如果没有任何AsyncListener调用AsyncContext.complete()或AsyncContext.dispatch()，执行一个状态码为HttpServletResponse
// .SC_INTERNAL_SERVER_ERROR 出错分派；
// （3）如果没有找到错误页面或者错误页面没有调用AsyncContext.complete()/dispatch()，容器要调用complete方法；
//
// 终止：
// servlet容器确定从服务中移除servlet时，可以通过调用``destroy()
// ``方法将释放servlet占用的任何资源和保存的持久化状态等。调用destroy方法之前必须保证当前所有正在执行service方法的线程执行完成或者超时；
// 之后servlet实例可以被垃圾回收，当然什么时候回收并不确定，因此destroy方法是是否必要的。