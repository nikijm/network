<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path=request.getContextPath();
%>      
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>银行处理列表</title>
<style>
    table,table tr th, table tr td { border:1px solid #0094ff; }
    table { text-align: center; border-collapse: collapse;}   
</style>
</head>
<body>
<table>
    <tr>
        <td>序号</td>
        <td>产品</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${list}" var="obj" varStatus="idxStatus">
        <tr>
            <td>${idxStatus.index}</td>
            <td>成都银行惠农贷</td>
            <td>
                <a href="<%=path %>/bank/apply/save?documentNo=${obj.documentNo}&nodeCode=9200">申请受理</a>
                <a href="<%=path %>/bank/apply/save?documentNo=${obj.documentNo}&nodeCode=9300">贷前尽职调查</a>
                <a href="<%=path %>/bank/apply/save?documentNo=${obj.documentNo}&nodeCode=9400">银行初审</a>
                <a href="<%=path %>/bank/apply/save?documentNo=${obj.documentNo}&nodeCode=9500">银行复审</a>
                <a href="<%=path %>/bank/apply/save?documentNo=${obj.documentNo}&nodeCode=9600">合同签订</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>