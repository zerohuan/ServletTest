<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>你好，中国！</h2>
<p>
    <form action="/s/upload/1" method="post" enctype="multipart/form-data">
        <fieldset>
            <legend>测试1：单文件上传</legend>
            <p><label for="name">名称 </label><input id="name" type="text" name="name" /></p>
            <p><label for="files">文件 </label><input id="files" type="file" name="files" /></p>
            <p><label for="location">地区 </label><input id="location" type="text" name="location" /></p>
            <input type="submit" />
        </fieldset>
    </form>

    <form action="/s/upload/1" method="post" enctype="multipart/form-data">
        <fieldset>
            <legend>测试2：多文件上传</legend>
            <p><label for="name">名称 </label><input id="name1" type="text" name="name" /></p>
            <p><label for="files1">文件 </label><input id="files1" type="file" name="files1" multiple="multiple" /></p>
            <p><label for="files2">文件 </label><input id="files2" type="file" name="files2" multiple="multiple" /></p>
            <p><label for="location">地区 </label><input id="location1" type="text" name="location" /></p>
            <input type="submit" />
        </fieldset>
    </form>
</p>
</body>
</html>
