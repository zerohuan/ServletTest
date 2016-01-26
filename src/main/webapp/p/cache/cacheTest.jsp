<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body style="text-align: center">
<h2>缓存测试</h2>
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
            xhr.cacheable=true;
            xhr.setRequestHeader("Cache-Control", "private, max-stale=100");
            xhr.send(null);
            return xhr;
        }
        //轮询后台登录状态
        var client = createStreamingClient("http://localhost:8080/s/cache", function
                (msg) {
        }, function(msg) {
            alert(msg);
        });
</script>
</html>
