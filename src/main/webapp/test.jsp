<%--
  Created by IntelliJ IDEA.
  User: WH
  Date: 2021/10/4
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <title>Title</title>
    <base href="<%=basePath%>">
</head>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script>
    $.ajax({
        url: "",
        type:"post",
        dataType:"json",
        data:{

        },
        success:function(){

        }
    })
</script>
<body>

</body>
</html>
