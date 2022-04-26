<%--
  Created by IntelliJ IDEA.
  User: rcz
  Date: 2022/4/5
  Time: 下午3:01
  To change this template use File | Settings | File Templates.
--%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>演示文件下载</title>
<script type="text/javascript">
    $(function () {
        $("#fileDownloadBtn").click(function () {
            //发送文件下载的请求
            window.location.href="workbench/activity/fileDownload.do"
        });
    });
</script>

</head>
<body>
<input type="button" value="下载文件" id="fileDownloadBtn">
</body>
</html>
