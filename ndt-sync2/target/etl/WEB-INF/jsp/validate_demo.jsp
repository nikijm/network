<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<html>
<head>
    <title></title>
</head>
<body>
	<form action="${ctx}/validate/demo" method="post">
		id:<input type="text" name="id" /><br/>
		姓名：<input type="text" name="name" /><br/>
		<!-- 手机号：<input type="text" name="phone" /><br/>-->
		邮箱：<input type="text" name="email" /><br/> 
		<input type="submit" value="提交" />
	</form>
</body>
</html>