package com.jk.ndtetl.controller.system;

import java.lang.Integer;
import java.security.Key;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.system.service.IRoleService;
import com.jk.ndtetl.util.*;
import com.jk.ndtetl.util.etl.OptionUtil;
import com.jk.ndtetl.util.promission.ResourceMenusUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IUserService;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import com.jk.ndtetl.util.promission.Md5SaltTool;

/**
 * user用户登录、注销、用户列表管理等
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class UserController extends BaseController {

    /**
     * 登录用户session Role key
     */
    public static final String LOGIN_USER_ROLE_SESSION = "login_user_role_session";
    /**
     * 登录用户session Menu key
     */
    public static final String LOGIN_USER_MENU_SESSION = "login_user_menu_session";
    /**
     * 登录用户session Resource key
     */
    public static final String LOGIN_USER_RESOURCE_SESSION = "login_user_resource_session";
    public static final String LOGIN_USER_JJTW_KEY = "login_user_jjtw_key";
    /**
     * 登录用户session key
     */
    public static final String LOGIN_USER_SESSION = "login_user_session";
    /**
     * 客户端传递的 key
     */
    public static final String LOGIN_USER_CLIENT_TOKEN = "X_ETL_TOKEN";
    /**
     * 返回给 客户端 key
     */
    public static final String LOGIN_USER_CLIENT_TOKEN_RESPONSE = "token";
    /**
     * token 失效时间
     */
    public static final int LOGIN_USER_CLIENT_TOKEN_EXPIRED = 30;
    public static final int NO_PERMISSON = 401;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IRoleService iRoleService;

    /**
     * 注销登录
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth/session", method = RequestMethod.DELETE)
    @ResponseBody
    public Object logout(HttpServletResponse response, HttpServletRequest request) {
        String compactJws = request.getHeader(LOGIN_USER_CLIENT_TOKEN);
        JSONObject errors = new JSONObject();
        if (null == EHCacheUtil.get(compactJws)) {
            response.setStatus(500);
            errors.put("error_message", "注销失败");
            return errors;
        }
        try {
            EHCacheUtil.remove(compactJws);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            errors.put("error_message", "注销失败");
            return errors;
        }
        return null;
    }

    /**
     * 用户登录
     *
     * @param user 用户提交的用户名、密码
     * @return
     */
    @RequestMapping(value = "/auth/session", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request, @RequestBody User user) {
        JSONObject errors = new JSONObject();
        String msg = null;
        try {
            User queryUser = userService.getByName(user.getName());
            if (StringUtils.isBlank(user.getName())) {
                errors.put("name", "用户名不能为空");
                msg = "用户名不能为空";
            }
            if (StringUtils.isBlank(user.getPassword())) {
                errors.put("password", "密码不能为空");
                if (null == msg) {
                    msg = "密码不能为空";
                }
            }
            if (null == queryUser || !Md5SaltTool.validPassword(user.getPassword(), queryUser.getPassword())) {
                errors.put("name_password", "用户名或密码错误");
                if (null == msg) {
                    msg = "用户名或密码错误";
                }
            }
            if (null != queryUser && StringUtils.equalsIgnoreCase("N", queryUser.getIsActive().toString())) {
                errors.put("isActive", "用户未启用");
                if (null == msg) {
                    msg = "用户未启用";
                }
            }
            if (Checker.isNotNullOrEmpty(errors)) {
                return ErrorUtil.getRequestError(errors, msg);
            }
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(queryUser.getName(), queryUser.getPassword());
            currentUser.login(token);
            Key key = MacProvider.generateKey();
            String compactJws = Jwts.builder()
                    .setSubject(queryUser.getName())
                    .signWith(SignatureAlgorithm.HS512, key)
                    .setIssuedAt(DateTime.now().toDate())
                    .setExpiration(DateTime.now().plusMinutes(LOGIN_USER_CLIENT_TOKEN_EXPIRED).toDate())
                    .compact();

            Map<String, Object> loginSessionMap = new HashMap<String, Object>();
            Map<String, Object> menuMap = ResourceMenusUtil.setRoleAndPermission(queryUser, request, loginSessionMap);
            loginSessionMap.put(LOGIN_USER_SESSION, queryUser);
            loginSessionMap.put(LOGIN_USER_JJTW_KEY, key);
            loginSessionMap.put("expiration", DateTime.now().plusMinutes(LOGIN_USER_CLIENT_TOKEN_EXPIRED));

            EHCacheUtil.put(compactJws, loginSessionMap);

            JSONObject rs = new JSONObject();
            rs.put(LOGIN_USER_CLIENT_TOKEN_RESPONSE, compactJws);
            JSONObject promissObject = assembleReturnValues(queryUser);
            promissObject.put("menus", menuMap.get(LOGIN_USER_MENU_SESSION));
            rs.put("user", promissObject);
            if (LoginSessionUtil.isOuterUser(queryUser)) {
                rs.put("userType", "user");
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("login", "登录失败！");
            return ErrorUtil.getRequestError(errors, "登录失败");
        }
    }

    /**
     * 按照文档返回数据的格式组织登录返回的数据
     *
     * @param login_user
     * @return
     */
    private JSONObject assembleReturnValues(User login_user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", login_user.getId());
        jsonObject.put("name", login_user.getName());
        jsonObject.put("orgName", login_user.getOrgName());
        List<Role> roleList = login_user.getRoles();
        JSONArray roleArray = new JSONArray();
        if (null != roleList && !roleList.isEmpty()) {

            for (Role role : roleList) {
                JSONObject roleJson = new JSONObject();
                roleJson.put("id", role.getId());
                roleJson.put("name", role.getName());

                JSONArray permissionsArray = JSONArray.parseArray(role.getPermissions());
                roleJson.put("permissions", permissionsArray);
                roleArray.add(roleJson);
            }
        }
        jsonObject.put("roles", roleArray);
        return jsonObject;
    }

    /**
     * user列表
     *
     * @param user_p
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Object listUsers(User user_p, com.jk.ndtetl.controller.BasePage basePage, HttpServletRequest request,HttpServletResponse response) {

        if (!StringUtils.isBlank(user_p.getSearchKey())) {
            user_p.setName(user_p.getSearchKey());
            user_p.setEmail(user_p.getSearchKey());
            user_p.setPhone(user_p.getSearchKey());
            user_p.setPhone2(user_p.getSearchKey());
        }
        List<User> userList = userService.listUsers(user_p, basePage);
        PageInfo pageInfo = new PageInfo(userList);
        basePage.setTotal(pageInfo.getTotal());
        JSONArray userArray = new JSONArray();
        if (Checker.isNotNullOrEmpty(userList)) {
            for (User user : userList) {
                userArray.add(setResultUser(user));
            }
        }
        JSONObject rs = new JSONObject();
        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        rs.put("users", userArray);
        if (Checker.isNullOrEmpty(user_p.getExternalz())) {//运维人员
            rs.put("roles", listRoleOptions(iRoleService, null));
        } else {
            rs.put("orgs", listOrgOptions(iOrganizationService,null));
        }
//        rs.put(User.PAGE_PERMISSIONS, ResourceMenusUtil.getPagePermission(request));
        return rs;
    }

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUser(@PathVariable("id") Integer id) {

        User user = userService.getUser(id);
        JSONObject errors = new JSONObject();
        if (null == user) {
            errors.put("user", "不存在该用户");
            return ErrorUtil.getRequestError(errors, "不存在该用户");
        }
        return setResultUser(user);
    }

    /**
     * 组装返回的值
     *
     * @param user
     * @return
     */
    private JSONObject setResultUser(User user) {
        JSONObject userObject = new JSONObject();
        userObject.put("id", user.getId());
        userObject.put("orgId", user.getOrgId());
        userObject.put("createUser", user.getCreatedUser());
        userObject.put("updatedUser", user.getUpdatedUser());
        userObject.put("created", user.getCreated());
        userObject.put("updated", user.getUpdated());
        userObject.put("isActive", user.getIsActive());
        userObject.put("email", user.getEmail());
        userObject.put("name", user.getName());
        userObject.put("phone", user.getPhone());
        userObject.put("phone2", user.getPhone2());
        userObject.put("description", user.getDescription());
        userObject.put("orgName", user.getOrgName());

        JSONArray permissionsArray = new JSONArray();
        List<Role> roleList = user.getRoles();
        if (Checker.isNotNullOrEmpty(roleList)) {
            for (Role role : roleList) {
                JSONObject roleJson = new JSONObject();
                roleJson.put("id", role.getId());
                roleJson.put("name", role.getName());
                permissionsArray.add(roleJson);
            }
        }
        userObject.put("roles", permissionsArray);
        return userObject;
    }

    /**
     * 保存用户
     *
     * @param response
     * @param request
     * @param user     参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public Object saveUser(HttpServletResponse response, HttpServletRequest request, @Valid @RequestBody User user, BindingResult result) {

        JSONObject errors = new JSONObject();
        String msg = null;
        User u_double = userService.getUserByNameOrId(user);
        if (null != u_double) {
            errors.put(user.getName(), "用户名重复");
            msg = "用户名重复";
        }
        if (StringUtils.isBlank(user.getPassword())) {
            errors.put("password", "密码不能为空");
            if (null == msg) {
                msg = "密码不能为空";
            }
        }
        if (!StringUtils.isBlank(user.getPassword()) && !Checker.checkPassword(user.getPassword())) {
            errors.put("password", User.PWD_NOTICE);
            if (null == msg) {
                msg = User.PWD_NOTICE;
            }
        }
        if (!StringUtils.isBlank(user.getExternalz())) {
            if (null == user.getOrgId()) {
                errors.put("org", "请选择所属机构");
                if (null == msg) {
                    msg = "请选择所属机构";
                }
            } else {
                Integer[] roleIds = {LoginSessionUtil.OUTER_USER};
                user.setRoleIds(roleIds);
            }
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        try {
            user.setPassword(Md5SaltTool.getEncryptedPwd(user.getPassword()));
        } catch (Exception e) {
            user.setPassword(null);
        }
        user.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        user.setId(userService.getUserSequence());
        userService.saveUser(user);
        return user.getId();
    }

    /**
     * 更新用户
     *
     * @param request
     * @param response
     * @param id       主键
     * @param user     参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer id, @Valid @RequestBody User user, BindingResult result) {

        JSONObject errors = new JSONObject();
        String msg = null;
        user.setId(id);
        User u_double = userService.getUserByNameOrId(user);
        User u_old = userService.getUser(user.getId());
        if (null == u_old) {
            errors.put("user", "不存在该用户");
            msg = "不存在该用户";
        }
        if (null != u_double) {
            errors.put(user.getName(), "用户名重复");
            if (null == msg) {
                msg = "用户名重复";
            }
        }
        if (!StringUtils.isBlank(user.getPassword()) && !Checker.checkPassword(user.getPassword())) {
            errors.put("password", User.PWD_NOTICE);
            if (null == msg) {
                msg = User.PWD_NOTICE;
            }
        }
        if (!StringUtils.isBlank(user.getExternalz())) {
            if (null == user.getOrgId()) {
                errors.put("org", "请选择所属机构");
                if (null == msg) {
                    msg = "请选择所属机构";
                }
            }
//            else {
//                Integer[] roleIds = {LoginSessionUtil.OUTER_USER};
//                user.setRoleIds(roleIds);
//            }
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        try {
            if (!StringUtils.isBlank(user.getPassword())) {
                user.setPassword(Md5SaltTool.getEncryptedPwd(user.getPassword()));
            } else {
                user.setPassword(u_old.getPassword());
            }
        } catch (Exception e) {
            user.setPassword(null);
        }
        user.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        user.setUpdated(new Date());
        userService.updateUser(user,u_old);
        return null;
    }

    /**
     * 删除user
     *
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteUser(@PathVariable("id") Integer id) {
        JSONObject errors = new JSONObject();
        String msg = null;
        User user = userService.getUser(id);
        if (null != user && !StringUtils.isBlank(user.getIsdelete())) {
            errors.put("user", "系统默认用户，不能删除");
            msg = "系统默认用户，不能删除";
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        userService.deleteUser(id);
        return null;
    }

}
