<%@ page import="java.io.PrintWriter" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>你好，中国！</h2>
<p>
    <%= request.getDispatcherType() %>
    <%
        PrintWriter o = response.getWriter();
        try {
            for (int i = 0; i <= 3; ++i) {
                o.println("sfsf");
                Thread.sleep(5000);
                o.flush();
            }
        } catch (Exception e) {
            e.printStackTrace(o);
        }
    %>
</p>
</body>
</html>
