<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
%>      
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>申请贷款</title>
</head>
<body>
<form action="<%=path %>/plat/apply/credit/add" method="post">
<table>
<tr><td>产品：</td><td><select name="mBusinesssyncId">
<option value="1496745399264001">成都银行&nbsp;&nbsp;惠农贷</option>
</select></td></tr>
<tr><td>记录号：</td><td><input type="text" name="recordId"/></td></tr>
<tr><td>操作人编码：</td><td><input type="text" name="operatorCode"/></td></tr>
<tr><td>操作人名字：</td><td><input type="text" name="operatorName"/></td></tr>
<tr><td>操作说明：</td><td><textarea name="actionDescription"></textarea></td></tr>
</table>
<br>
<input type="submit" value="提交"/>
</form>
</body>
</html>