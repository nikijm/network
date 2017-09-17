<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path=request.getContextPath();
%>      
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>农贷通处理列表</title>
<style>
    table,table tr th, table tr td { border:1px solid #0094ff; }
    table { text-align: center; border-collapse: collapse;}   
</style>
</head>
<body>
<a href="<%=path %>/plat/apply/credit">申请贷款</a>
<table>
    <tr>
        <td>序号</td>
        <td>记录号</td>
        <td>产品</td>
        <td>操作人编码</td>
        <td>操作人名字</td>
        <td>操作说明</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${list}" var="obj" varStatus="idxStatus">
        <tr>
            <td>${idxStatus.index}</td>
            <td>${obj.recordId}</td>
            <td>成都银行惠农贷</td>
            <td>${obj.operatorCode}</td>
            <td>${obj.operatorName}</td>
            <td>${obj.actionDescription}</td>
            <td><a href="<%=path %>/plat/apply/detail?documentNo=${obj.documentNo}">查看</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>