<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>多端统一登录</h2>
<p>
    <form action="/s/login" method="post" enctype="application/x-www-form-urlencoded">
        <fieldset>
            <legend>登录</legend>
            <p><label for="username">用户名： </label><input id="username" type="text" name="username" /></p>
            <p><label for="password">密码： </label><input id="password" type="password" name="password" /></p>
            <input id="type" type="text" name="mobile" hidden />
            <input type="submit" />
        </fieldset>
    </form>
</p>
</body>
</html>
