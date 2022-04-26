<%--
  Created by IntelliJ IDEA.
  User: rcz
  Date: 2022/3/17
  Time: 下午7:30
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
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap-theme.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/css" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {
            $("#customerName").typeahead({
                source:function (jquery,process) {
                    $.ajax({
                        url:'workbench/transaction/queryAllCustomerName.do',
                        data:{
                            customerName:jquery
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            process(data);
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
