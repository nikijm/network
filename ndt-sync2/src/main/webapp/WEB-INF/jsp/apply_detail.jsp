<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path=request.getContextPath();
%>      
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列表</title>
<style>
    table,table tr th, table tr td { border:1px solid #0094ff; }
    table { text-align: center; border-collapse: collapse;}   
</style>
</head>
<body>
<table>
    <tr>
        <td>NODECODE</td>
        <td>NODENAME</td>    
        <td>备注</td>
    </tr>
    <c:forEach items="${list}" var="obj" varStatus="idxStatus">
        <tr>
            <td>${obj.nodeCode}</td>
            <td>${obj.nodeName}</td>
            <td>完成</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>