<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
%>    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
<a href="<%=path %>/plat/apply/credit">申请贷款</a>

<a href="<%=path %>/plat/list">农贷通处理列表信息</a>

<a href="<%=path %>/bank/list">银行处理列表信息</a>
</body>
</html>