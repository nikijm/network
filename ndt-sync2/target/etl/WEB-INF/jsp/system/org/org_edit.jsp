<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: 朱生
  Date: 2017/5/13
  Time: 15:50
  主页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>农贷通</title>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <jsp:include page="../../header.jsp"/>
    <jsp:include page="../../left.jsp"/>

<div class="box box-primary " style="float: right;">
    <div class="box-header with-border">
        <h3 class="box-title">组</h3>
    </div>
    <form role="form" action="/etl/system/org/saveOrUpdate">
        <div class="box-body">
            <input type="text" class="form-control hide" id="adOrgId" name="adOrgId" value="${org.adOrgId}" >
            <div class="form-group">
                <label for="name">信用金额限制</label>
                <input type="text" class="form-control" id="name" name="name"  value="${org.name}">
            </div>
            <div class="form-group">
                <label for="adOrgtypeId">组织类型</label>
                <select name="adOrgtypeId">
                    <option value="1" <c:choose><c:when test="${org.adOrgtypeId==1}">selected='selected'</c:when></c:choose> >建行</option>
                    <option value="2" <c:choose><c:when test="${org.adOrgtypeId==2}">selected='selected'</c:when></c:choose>  >成都市地税局</option>
                </select>
            </div>
            <div class="form-group">
                <label for="soCreditlimit">信用金额限制</label>
                <input type="number" class="form-control" id="soCreditlimit" name="soCreditlimit" value="${org.soCreditlimit}" >
            </div>
            <div class="form-group">
                <label for="soCreditused">已用金额限制</label>
                <input type="number" class="form-control" id="soCreditused" name="soCreditused"   value="${org.soCreditused}" >
            </div>
            <div class="form-group">
                <label for="cLocationId">地址位置</label>
                <select name="cLocationId">
                    <option value="1" <c:choose><c:when test="${org.cLocationId==1}">selected='selected'</c:when></c:choose>  >成都</option>
                    <option value="2" <c:choose><c:when test="${org.cLocationId==2}">selected='selected'</c:when></c:choose>  >深圳</option>
                </select>
            </div>
            <div class="form-group">
                <label for="adAdmindivisionId">归属行政区域</label>
                <select name="adAdmindivisionId">
                    <option value="1" <c:choose><c:when test="${org.adAdmindivisionId==1}">selected='selected'</c:when></c:choose> >成都</option>
                    <option value="2" <c:choose><c:when test="${org.adAdmindivisionId==2}">selected='selected'</c:when></c:choose> >深圳</option>
                </select>
            </div>
            <div class="form-group">
                <label for="isactive">是否活跃</label>
                <select name="isactive">
                    <option value="1" <c:choose><c:when test="${org.isactive==1}">selected='selected'</c:when></c:choose>  >是</option>
                    <option value="2" <<c:choose><c:when test="${org.isactive==2}">selected='selected'</c:when></c:choose>  >否</option>
                </select>
            </div>
            <div class="form-group">
                <label for="issummary">是否节点</label>
                <select name="issummary">
                    <option value="1" <c:choose><c:when test="${org.issummary==1}">selected='selected'</c:when></c:choose>  >是</option>
                    <option value="0" <c:choose><c:when test="${org.issummary==0}">selected='selected'</c:when></c:choose> >否</option>
                </select>
            </div>
            <div class="form-group">
                <label for="isvalid">是否有效</label>
                <select name="isvalid">
                    <option value="1" <c:choose><c:when test="${org.isvalid==1}">selected='selected'</c:when></c:choose> >有</option>
                    <option value="0" <c:choose><c:when test="${org.isvalid==0}">selected='selected'</c:when></c:choose>  >无</option>
                </select>
            </div>
        </div>
        <div class="box-footer">
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>
</div>


<aside class="control-sidebar control-sidebar-dark">
    <!-- Create the tabs -->
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
        <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
        <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <!-- Home tab content -->
        <div class="tab-pane" id="control-sidebar-home-tab">
            <h3 class="control-sidebar-heading">Recent Activity</h3>
            <ul class="control-sidebar-menu">
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-birthday-cake bg-red"></i>

                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                            <p>Will be 23 on April 24th</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-user bg-yellow"></i>

                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>

                            <p>New phone +1(800)555-1234</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>

                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>

                            <p>nora@example.com</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-file-code-o bg-green"></i>

                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>

                            <p>Execution time 5 seconds</p>
                        </div>
                    </a>
                </li>
            </ul>
            <!-- /.control-sidebar-menu -->

            <h3 class="control-sidebar-heading">Tasks Progress</h3>
            <ul class="control-sidebar-menu">
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Custom Template Design
                            <span class="label label-danger pull-right">70%</span>
                        </h4>

                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Update Resume
                            <span class="label label-success pull-right">95%</span>
                        </h4>

                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-success" style="width: 95%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Laravel Integration
                            <span class="label label-warning pull-right">50%</span>
                        </h4>

                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Back End Framework
                            <span class="label label-primary pull-right">68%</span>
                        </h4>

                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
                        </div>
                    </a>
                </li>
            </ul>
            <!-- /.control-sidebar-menu -->

        </div>
        <!-- /.tab-pane -->
        <!-- Stats tab content -->
        <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
        <!-- /.tab-pane -->
        <!-- Settings tab content -->
        <div class="tab-pane" id="control-sidebar-settings-tab">
            <form method="post">
                <h3 class="control-sidebar-heading">General Settings</h3>

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Report panel usage
                        <input type="checkbox" class="pull-right" checked>
                    </label>

                    <p>
                        Some information about this general settings option
                    </p>
                </div>
                <!-- /.form-group -->

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Allow mail redirect
                        <input type="checkbox" class="pull-right" checked>
                    </label>

                    <p>
                        Other sets of options are available
                    </p>
                </div>
                <!-- /.form-group -->

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Expose author name in posts
                        <input type="checkbox" class="pull-right" checked>
                    </label>

                    <p>
                        Allow the user to show his name in blog posts
                    </p>
                </div>
                <!-- /.form-group -->

                <h3 class="control-sidebar-heading">Chat Settings</h3>

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Show me as online
                        <input type="checkbox" class="pull-right" checked>
                    </label>
                </div>
                <!-- /.form-group -->

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Turn off notifications
                        <input type="checkbox" class="pull-right">
                    </label>
                </div>
                <!-- /.form-group -->

                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Delete chat history
                        <a href="javascript:void(0)" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
                    </label>
                </div>
                <!-- /.form-group -->
            </form>
        </div>
        <!-- /.tab-pane -->
    </div>
</aside>
<!-- /.control-sidebar -->
<!-- Add the sidebar's background. This div must be placed
immediately after the control sidebar -->
<div class="control-sidebar-bg"></div>
