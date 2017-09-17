package com.jk.ndt.etl.controller;

import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.User;
import com.jk.ndt.etl.service.system.AdminService;
import com.jk.ndt.etl.service.system.UserService;
import com.jk.ndt.etl.utility.EHCacheUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 朱生 on 2017/5/18.
 */
public class MyRealm extends AuthorizingRealm {


    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    /**
     * 为当前登陆成功的用户授予权限和角色，已经登陆成功了
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal(); //获取用户名
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

//        Subject currentUser = SecurityUtils.getSubject();
//        Session session=currentUser.getSession();

//        User login_user = userService.getByName(username);
//        Role role = login_user.getRole();
//        session.setAttribute(Const.LOGIN_USER_ROLE_SESSION,role.getRole_name());
//
//        Set<String> roleSet = new TreeSet<>();
//        roleSet.add(role.getRole_name());
//        authorizationInfo.setRoles(roleSet);
//
//        List<Menu> menuParentList = menuService.selectParentMenuByRole(role.getRole_id());
//        if (!menuParentList.isEmpty()) {
//            Set<String> menuSet = new TreeSet<>();
//            for (Menu menu : menuParentList) {
//                menuSet.add(menu.getUrl());
//            }
//            for (Menu menu : menuParentList) {
//                Map<String, BigDecimal> params = new HashMap<>();
//                params.put("menu_id", menu.getMenu_id());
//                params.put("role_id", role.getRole_id());
//                List<Menu> menuList = menuService.selectMenuByRole(params);
//                if (!menuList.isEmpty()) {
//                    for (Menu menuChild : menuList) {
//                        menuSet.add(menuChild.getUrl());
//                    }
//                }
//            }
//            List<String> menuAll=new ArrayList(menuSet);
//            session.setAttribute(Const.LOGIN_USER_MENU_SESSION,menuAll);
//            authorizationInfo.setStringPermissions(menuSet);
            return authorizationInfo;
//        }
//        return null;
    }


    /**
     * 验证当前登录的用户，获取认证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        // 用户表不参与权限，此处硬编码!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // 用户表不参与权限，此处硬编码!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // 用户表不参与权限，此处硬编码!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (null != session.getAttribute("user_login")) {
            String user_name = (String) authenticationToken.getPrincipal(); // 获取用户名
            User user= userService.getByName(user_name);
            if (user != null) {
                AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getName(), user.getPassword(), "myRealm");
                return authcInfo;
            } else {
                return null;
            }
        }else {

            String admin_name = (String) authenticationToken.getPrincipal(); // 获取用户名
            Admin admin= adminService.getByName(admin_name);
            if (admin != null) {
                AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(admin.getName(), admin.getPassword(), "myRealm");
                return authcInfo;
            } else {
                return null;
            }
        }
    }
}
