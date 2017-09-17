<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<html>
<head>
    <title></title>
</head>
<body>
	<form action="${ctx}/upload/uploadFile" enctype="multipart/form-data" method="post">
		<input type="file" name="file" />
		<input type="submit" value="提交" />
	</form>
</body>
</html>