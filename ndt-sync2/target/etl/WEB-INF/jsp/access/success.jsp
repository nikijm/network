<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
测试
success
<c:forEach items="${list}" var="table">
<br/>
<table border="1" cellpadding="0" cellspacing="0">
	<tr>
		<c:forEach items="${table.header}" var="headCell">
		<th>
			${headCell}
		</th>
		</c:forEach>
	</tr>
	<c:forEach items="${table.content}" var="row">
	<tr>
		<c:forEach items="${row}" var="cell">
			<td>${cell}</td>
		</c:forEach>
	</tr>
	</c:forEach>
</table>
</c:forEach>
</body>
</html>