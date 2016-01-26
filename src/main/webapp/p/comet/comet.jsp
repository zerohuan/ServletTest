<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 16-1-14
  Time: 下午12:01
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>comet推送</title>
</head>
<body id="main">
  <script type="application/javascript">
//    function createStreamingClient(url, progress, finished) {
//      var xhr = new XMLHttpRequest(),
//              received = 0;
//      xhr.open("post", url, true);
//      xhr.onreadystatechange = function() {
//        var result;
//        if (xhr.readyState == 3) {
//          result = xhr.responseText.substring(received);
//          received += result.length;
//
//          progress(result);
//        } else if (xhr.readyState == 4) {
//          finished(xhr.responseText);
//        }
//      };
//      xhr.send(null);
//      return xhr;
//    }
//    var client = createStreamingClient("http://localhost:8080/s/async4", function (msg) {
//      var doc = document.getElementById("main");
//      doc.innerHTML = doc.innerHTML + "<p>" + msg + "</p>";
//    }, function(msg) {
//      alert("finished!" + msg);
//    });
  (function conn() {
    var source = new EventSource("http://localhost:8080/s/resp?type=locale");
    console.log(typeof(EventSource));
    source.onmessage = function (event) {
      var doc = document.getElementById("main");
      doc.innerHTML = doc.innerHTML + "<p>" + event.data + "</p>";
    };
    source.onopen = function (event) {
      console.log(2);
      console.log(event);
    };
    //source.addEventListener('message', function (e) {
    //    console.log(11);
    //    console.log(e.data);
    //}, false);
    source.addEventListener('error', function (e) {
      console.log(3);
      console.log(e);
    }, false);
  })()
  </script>
</body>
</html>
