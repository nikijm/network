<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<html>
<head>
<title>Insert title here</title>
<script type="text/javascript">
function analysis(id) {
	var columns=["A0","A1" ,"A2" ,"A3" ,"A4" ,"A5" ,"A6" ,"A7" ,"A8" ,"A9" ,"A10"];

	window.location.href = "${ctx}/analysis/analysis/" + id+"/"+columns;
}

</script>
</head>
<body>
	<table border="1" cellpadding="0" cellspacing="0">
		<tr>
			<th>ID</th>
			<th>FILENAME</th>
			<th>PATH</th>
			<th>SOURCE</th>
			<th>USER_ID</th>
			<th>STATUS</th>
			<th>SHEETS_NUM</th>
			<th>UPLOAD_DATE</th>
			<th>SHA1</th>
			<th>operation</th>
		</tr>
		<c:forEach items="${uploadInfoList}" var="uploadInfo">
			<tr>
				<td>${uploadInfo.id }</td>
				<td>${uploadInfo.fileName }</td>
				<td>${uploadInfo.path }</td>
				<td>${uploadInfo.source }</td>
				<td>${uploadInfo.userId }</td>
				<!--状态,-1:删除,0:未缓存,1:已缓存,2:处理中,3:清洗完毕,4:全部处理完毕; -->
				<td><c:if test="${uploadInfo.status==-1 }">删除</c:if> <c:if
						test="${uploadInfo.status==0 }">未缓存</c:if> <c:if
						test="${uploadInfo.status==1 }">已缓存</c:if> <c:if
						test="${uploadInfo.status==2 }">处理中</c:if> <c:if
						test="${uploadInfo.status==3 }">清洗完毕</c:if> <c:if
						test="${uploadInfo.status==4 }">全部处理完毕</c:if></td>
				<td>${uploadInfo.sheetsNum }</td>
				<td><fmt:formatDate value="${uploadInfo.uploadDate }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${uploadInfo.sha1 }</td>
				<td>
					<button onclick="analysis(${uploadInfo.id })">解析</button>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>