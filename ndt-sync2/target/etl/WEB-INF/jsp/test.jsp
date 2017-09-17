<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="${ctx}/resources/js/jquery-2.2.3.min.js"></script> 
<script type="text/javascript">

$(function(){
	$.ajax({
		url:"${ctx}/serverHost/add",
		type:"post",
		data:{"username":"root","password":"root_123","tablename":"AddOneTabltWO"},
		dataType:'json',
		success:function(data){
			alert(data.data);
		}
	})
})

</script>

</head>
<body>
nihao
</body>
</html>