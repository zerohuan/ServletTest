<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body style="text-align: center">
<h2>多端统一登录</h2>
<img style="width: 300px;height: 300px;" src="/s/qr/${uid}.png" />
</body>
<script type="application/javascript">
    (function conn() {
        var source = new EventSource("http://localhost:8080/s/login?type=pcCheckPoll&uid=${uid}");
        console.log(typeof(EventSource));
        source.onmessage = function (event) {
            var msg = event.data;
            if (msg.trim() == "checked") {
                window.location.href = "http://localhost:8080/s/p/login/index.jsp";
            }
        };
        source.onopen = function (event) {
        };
        //source.addEventListener('message', function (e) {
        //    console.log(11);
        //    console.log(e.data);
        //}, false);
        source.addEventListener('error', function (e) {
            console.log("error");
            console.log(e);
        }, false);
    })()
</script>
</html>
