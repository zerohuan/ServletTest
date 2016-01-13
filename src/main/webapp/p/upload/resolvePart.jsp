<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 16-1-13
  Time: 上午11:35
  To change this template use File | Settings | File Templates.
--%>
<%@page import="org.apache.commons.io.IOUtils" %>
<html>
<head>
    <title>Multipart解析</title>
</head>
<body>
    <h1>Multipart解析</h1>
    <c:forEach items="${parts}" var="part">
      <p>
        <h3>Part: ${part.name}</h3>
        <span>Size: ${part.size}</span><br />
        <span>SubmittedFileName: ${part.submittedFileName}，Servlet 3.1新增方法</span><br />
        <span>ContentType: ${part.contentType}</span><br />
        <c:choose>
          <c:when test="${part.contentType == null || part.contentType eq 'text/plain'}">
            <p>
                ${IOUtils.toString(part.inputStream)}
            </p>
          </c:when>
          <c:when test="${part.contentType eq 'application/octet-stream'}">
            <p>
                ${part.getHeader("content-disposition")}
            </p>
          </c:when>
        </c:choose>
      </p>
    </c:forEach>
  <c:import url="upload.jsp" />
</body>
</html>
