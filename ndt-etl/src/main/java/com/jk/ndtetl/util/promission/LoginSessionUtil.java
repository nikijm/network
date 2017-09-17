package com.jk.ndtetl.util.promission;/**
 * Created by 朱生 on 2017/5/31.
 */

import java.lang.Integer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.system.UserController;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.EHCacheUtil;
import com.jk.ndtetl.util.StringUtils;

/**
 * 朱生
 * 获取登录用户
 * @create  2017-05-31 14:10
 **/
public class LoginSessionUtil {

    //系统管理员
    public static final int SYSTEM_USER = 100;

    //数据管理员
    public static final int DATA_USER = 101;

    //运维管理员
    public static final int MAINTAIN_USER= 102;

    //外部用户
    public static final int OUTER_USER= 999;


    /**
     * 获取user对象，如果不是user对象或者user对象为空，返回空
     * @param request
     * @return  user对象
     */
    public static User getLoginUser(HttpServletRequest request) {
        String clientToken = request.getHeader(UserController.LOGIN_USER_CLIENT_TOKEN);
        // 测试代码
        if (StringUtils.isBlank(clientToken)) {
            User user=new User();
            user.setId(1);
            user.setName("user");
            user.setOrgId(1);
            return  user;
        }
        // 测试代码
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        try {
            return  (User) sessionLoginMap.get(UserController.LOGIN_USER_SESSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取登录名
     * @param request
     * @return
     */
    public static String getLoginName(HttpServletRequest request) {
        String clientToken = request.getHeader(UserController.LOGIN_USER_CLIENT_TOKEN);
        // 测试代码
        if (StringUtils.isBlank(clientToken)) {
            User user=new User();
            user.setId(1);
            user.setName("user");
            return  user.getName();
        }
        // 测试代码
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        User user=(User) sessionLoginMap.get(UserController.LOGIN_USER_SESSION);
        return user.getName();
    }
    /**
     * 获取登录用户id
     * @param request
     * @return
     */
    public static Integer getLoginUserId(HttpServletRequest request) {
        String clientToken = request.getHeader(UserController.LOGIN_USER_CLIENT_TOKEN);
        // 测试代码
        if (StringUtils.isBlank(clientToken)) {
            User user=new User();
            user.setId(1);
            user.setName("user");
            return  user.getId();
        }
        // 测试代码
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        User user=(User) sessionLoginMap.get(UserController.LOGIN_USER_SESSION);
        return user.getId();
    }
    /**
     * 是否是外部用户
     * @param user
     * @return
     */
    public static boolean isOuterUser(User user) {
        List<Role> roles=user.getRoles();
        if (Checker.isNotNullOrEmpty(roles)) {
            for (Role role:roles) {
                if (role.getId()!=null && role.getId()== OUTER_USER){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是外部用户
     * @param request
     * @return
     */
    public static boolean isOuterUser(HttpServletRequest request) {
        User user = getLoginUser(request);
        if (null == user) {
            return true;
        }
        Integer[] roleIds=user.getRoleIds();
        if (Checker.isNotNullOrEmpty(roleIds)) {
            for (Integer roleId:roleIds) {
                if (roleId== OUTER_USER){
                    return true;
                }
            }
        }
        return false;
    }
}
