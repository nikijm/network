package com.jk.ndtetl.system;


import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jk.ndtetl.controller.system.UserController;
import com.jk.ndtetl.util.EHCacheUtil;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.StringUtils;
import com.jk.ndtetl.util.promission.ResourceMenusUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by 朱生 on 2017/5/18.
 * 权限校验
 */
public class EtlPermissionsAuthorizationFilter extends AccessControlFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证 jjTW token签名 权限控制
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return true 验证通过
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//// 获取token对于的用户信息
//        String compactJws = request.getHeader(UserController.LOGIN_USER_CLIENT_TOKEN);
//        String rr = request.getRequestURI();
//        System.out.println("拦截的url：" + rr);
//        if (StringUtils.isBlank(compactJws) || null == EHCacheUtil.get(compactJws)) {
//            ErrorUtil.getOverdueError(null, response);
//            return false;
//        }
//        Map<String, Object> loginSessionMap = (Map<String, Object>) EHCacheUtil.get(compactJws);
//        Object obj = loginSessionMap.get(UserController.LOGIN_USER_SESSION);
//        String name = "";
//        User user = (User) obj;
//        name = user.getName();
//
//////验证jjtw签名
//        if (!ResourceMenusUtil.jjtwValid(loginSessionMap, compactJws, name, response)) {
//            ErrorUtil.getOverdueError(null, response);
//            EHCacheUtil.remove(compactJws);
//            return false;
//        }
////验证权限
//
//        if (!ResourceMenusUtil.permissionValid(loginSessionMap, response, request)) {
//            ErrorUtil.getPermissionError(null, "无权限", response);
//            System.out.println("无权限！");
//            return false;
//        }
//        System.out.println("权限校验成功！");
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        return false;
    }

}
