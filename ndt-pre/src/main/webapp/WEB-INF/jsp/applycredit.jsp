<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<c:forEach items="${businessList}" var="obj">
    <option value="${obj.businesssyncId}">${obj.orgName}&nbsp;&nbsp;${obj.businessName}</option>
</c:forEach>
</select></td></tr>
<tr><td>记录号：</td><td><input type="text" name="recordId"/></td></tr>
<tr><td>操作人编码：</td><td><input type="text" name="operatorCode"/></td></tr>
<tr><td>操作人名字：</td><td><input type="text" name="operatorName"/></td></tr>
<tr><td>BPCode：</td><td><input type="text" name="bPCode"/></td></tr>
<tr><td>BPName：</td><td><input type="text" name="bPName"/></td></tr>
<tr><td>Address：</td><td><input type="text" name="address"/></td></tr>
<tr><td>申请人身份证号：</td><td><input type="text" name="applyUserID"/></td></tr>
<tr><td>申请人姓名：</td><td><input type="text" name="applyUserName"/></td></tr>
<tr><td>申请人移动号码：</td><td><input type="text" name="applyUserPhone"/></td></tr>
<tr><td>融资方式：</td><td><input type="text" name="financingMode"/></td></tr>
<tr><td>贷款年利率：</td><td><input type="text" name="annualRate"/></td></tr>
<tr><td>投资行业：</td><td><input type="text" name="investmentIndustry"/></td></tr>
<tr><td>项目类型：</td><td><input type="text" name="projectType"/></td></tr>
<tr><td>申请贷款金额(格式为两位小数)：</td><td><input type="text" name="amount"/></td></tr>
<tr><td>贷款期限(单位月)：</td><td><input type="text" name="termMonth"/></td></tr>
<tr><td>还款方式：</td><td><input type="text" name="corpusPayMethod"/></td></tr>
<tr><td>多种抵质押资产编码：</td><td><input type="text" name="assetCodes"/></td></tr>
<tr><td>抵质押资产名称：</td><td><input type="text" name="assetNames"/></td></tr>
<tr><td>项目材料：</td><td><input type="text" name="projectDoc"/></td></tr>
<tr><td>操作说明：</td><td><textarea name="actionDescription"></textarea></td></tr>
</table>
<br>
<input type="submit" value="提交"/>
</form>
</body>
</html>