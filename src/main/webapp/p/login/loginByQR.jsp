<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body style="text-align: center">
<h2>多端统一登录</h2>
<img style="width: 300px;height: 300px;" src="/s/qr/${uid}.png" />
</body>
<script type="application/javascript">
        function createStreamingClient(url, progress, finished) {
          var xhr = new XMLHttpRequest(),
                  received = 0;
          xhr.open("get", url, true);
          xhr.onreadystatechange = function() {
            var result;
            if (xhr.readyState == 3) {
              result = xhr.responseText.substring(received);
              received += result.length;

              progress(result);
            } else if (xhr.readyState == 4) {
              finished(xhr.responseText);
            }
          };
          xhr.send(null);
          return xhr;
        }
        //轮询后台登录状态
        var client = createStreamingClient("http://localhost:8080/s/login?type=pcCheck&uid=${uid}", function
                (msg) {
        }, function(msg) {
//            alert(msg);
          if (msg.trim() == 'checked') {
              window.location.href = "http://localhost:8080/s/p/login/index.jsp";
          }
        });
</script>
</html>
