<%@ taglib prefix="c" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by 朱生（朱仁平）.
  User: 朱生
  Date: 2017/5/13
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>用户登录</title>
    <link rel="stylesheet" href="<%=basePath%>/resources/bootstrap/css/bootstrap.css"  type="text/css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/css/login/login.css">

    <script src="<%=basePath%>/resources/js/jquery-2.2.3.min.js"></script>
    <script src="<%=basePath%>/resources/bootstrap/js/bootstrap.min.js"></script>
    <style type="text/css">
        .form-bg{
            padding: 2em 0;
        }
        .form-horizontal{
            background: #fff;
            padding-bottom: 40px;
            border-radius: 15px;
            text-align: center;
        }
        .form-horizontal .heading{
            display: block;
            font-size: 35px;
            font-weight: 700;
            padding: 35px 0;
            border-bottom: 1px solid #f0f0f0;
            margin-bottom: 30px;
        }
        .form-horizontal .form-group{
            padding: 0 40px;
            margin: 0 0 25px 0;
            position: relative;
        }
        .form-horizontal .form-control{
            background: #f0f0f0;
            /*border: none;*/
            border-radius: 20px;
            box-shadow: none;
            padding: 0 20px 0 45px;
            height: 40px;
            transition: all 0.3s ease 0s;
        }
        .form-horizontal .form-control:focus{
            background: #e0e0e0;
            box-shadow: none;
            outline: 0 none;
        }
        .form-horizontal .form-group i{
            position: absolute;
            top: 12px;
            left: 60px;
            font-size: 17px;
            color: #c8c8c8;
            transition : all 0.5s ease 0s;
        }
        .form-horizontal .form-control:focus + i{
            color: #00b4ef;
        }
        .form-horizontal .glyphicon-question-sign{
            display: inline-block;
            position: absolute;
            top: 12px;
            right: 60px;
            font-size: 20px;
            color: #808080;
            transition: all 0.5s ease 0s;
        }
        .form-horizontal .glyphicon-question-sign:hover{
            color: #000;
        }
        .form-horizontal .main-checkbox{
            float: left;
            width: 20px;
            height: 20px;
            background: #11a3fc;
            border-radius: 50%;
            position: relative;
            margin: 5px 0 0 5px;
            border: 1px solid #11a3fc;
        }
        .form-horizontal .main-checkbox label{
            width: 20px;
            height: 20px;
            position: absolute;
            top: 0;
            left: 0;
            cursor: pointer;
        }
        .form-horizontal .main-checkbox label:after{
            content: "";
            width: 10px;
            height: 5px;
            position: absolute;
            top: 5px;
            left: 4px;
            border: 3px solid #fff;
            border-top: none;
            border-right: none;
            background: transparent;
            opacity: 0;
            -webkit-transform: rotate(-45deg);
            transform: rotate(-45deg);
        }
        .form-horizontal .main-checkbox input[type=checkbox]{
            visibility: hidden;
        }
        .form-horizontal .main-checkbox input[type=checkbox]:checked + label:after{
            opacity: 1;
        }
        .form-horizontal .text{
            float: left;
            margin-left: 7px;
            line-height: 20px;
            padding-top: 5px;
            text-transform: capitalize;
        }
        .form-horizontal .btn{
            float: right;
            font-size: 14px;
            color: #fff;
            background: #00b4ef;
            border-radius: 30px;
            padding: 10px 25px;
            border: none;
            text-transform: capitalize;
            transition: all 0.5s ease 0s;
        }
        @media only screen and (max-width: 479px){
            .form-horizontal .form-group{
                padding: 0 25px;
            }
            .form-horizontal .form-group i{
                left: 45px;
            }
            .form-horizontal .btn{
                padding: 10px 20px;
            }
        }
        .vertical-center{
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
    </style>
    <script type="text/javascript">

    </script>
</head>
<body>
<div class="htmleaf-container">
    <header class="htmleaf-header">
        <h1>四川省成都市农贷通运维管理系统</h1>
    </header>
    <div class="demo form-bg">
        <div class="container vertical-center">
            <div class="row">
                <div class="col-md-offset-3 col-md-6">
                    <c:form class="form-horizontal" action="/etl/system/user/doLogin">
                        <span class="heading">用户登录</span>
                        <div class="form-group">
                            <input type="text" class="form-control" id="name" name="username" placeholder="请输入用户名">
                            <i class="glyphicon glyphicon-user"></i>
                        </div>
                        <div class="form-group help">
                            <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
                            <i class="glyphicon glyphicon-lock"></i>
                            <a href="#" class="glyphicon glyphicon-question-sign"></a>
                        </div>
                        <div class="form-group">
                            <div class="main-checkbox">
                                <input type="checkbox" value="None" id="checkbox1" name="check"/>
                                <label for="checkbox1"></label>
                            </div>
                            <span class="text">记住我</span>
                            <button type="submit" class="btn btn-default">立刻登录</button>
                        </div>
                        <s:if test="${msg!='' && msg!=null}">
                            <div id="errorMsg" class="alert alert-danger hide" role="alert">${msg}</div>
                        </s:if>
                    </c:form>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
