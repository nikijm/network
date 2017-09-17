package com.jk.ndt.etl.controller.system;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.system.Menu;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.User;
import com.jk.ndt.etl.service.system.UserService;
import com.jk.ndt.etl.utility.*;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * user用户登录、注销、用户列表管理等
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    // user 的菜单
    private String menus = "[{\"icon\":\"fafa-cloud-upload\",\"name\":\"数据上传\",\"type\":\"tree\",\"items\":[{\"route\":\"/upload\",\"icon\":\"fafa-upload\",\"name\":\"文档上传\",\"type\":\"item\",\"url\":\"/api/uploads\",\"key\":\"uploads_uploads\"}]}]";

    /**
     * 注销登录
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/session", method = RequestMethod.DELETE)
    @ResponseBody
    public Object logout(HttpServletResponse response, HttpServletRequest request) {
        String compactJws = request.getHeader(Constant.LOGIN_USER_CLIENT_TOKEN);
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
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/user/session", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {

        JSONObject rsObject = new JSONObject();
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
            if (null!=queryUser && (null == queryUser.getIs_active() || !queryUser.getIs_active())) {
                errors.put("is_active", "用户未启用");
                if (null == msg) {
                    msg = "用户未启用";
                }
            }
            if (!errors.isEmpty()) {
                return ErrorUtil.getRequestError(errors, msg);
            }

            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            session.setAttribute("user_login", "user_login");
            UsernamePasswordToken token = new UsernamePasswordToken(queryUser.getName(), queryUser.getPassword());
            currentUser.login(token);
            Key key = MacProvider.generateKey();
            String compactJws = Jwts.builder()
                    .setSubject(queryUser.getName())
                    .signWith(SignatureAlgorithm.HS512, key)
                    .setIssuedAt(DateTime.now().toDate())
                    .setExpiration(DateTime.now().plusMinutes(Constant.LOGIN_USER_CLIENT_TOKEN_EXPIRED).toDate())
                    .compact();

            Map<String, Object> loginSessionMap = new HashMap<String, Object>();
            loginSessionMap.put(Constant.LOGIN_USER_SESSION, queryUser);
            loginSessionMap.put(Constant.LOGIN_USER_JJTW_KEY, key);

            Map<String, String> resourceMap = new HashMap<>();
            List<Menu> menuResult = new ArrayList<>();
            Menu menu=new Menu();
            menu.setUrl("/api/uploads");
            menu.setKey("uploads_uploads");
            resourceMap.put("uploads_uploads", "c");
            menuResult.add(menu);
            loginSessionMap.put(Constant.LOGIN_USER_RESOURCE_SESSION, resourceMap);
            loginSessionMap.put(Constant.LOGIN_USER_MENU_SESSION, menuResult);

            EHCacheUtil.put(compactJws, loginSessionMap);

            JSONObject userObject = new JSONObject();
            rsObject.put(Constant.LOGIN_USER_CLIENT_TOKEN_RESPONSE, compactJws);

            userObject.put("id", queryUser.getId());
            userObject.put("name", queryUser.getName());
            userObject.put("menus", JSON.parseArray(menus));
            rsObject.put("user", userObject);

            return rsObject;
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("login", "登录失败！");
            return ErrorUtil.getRequestError(errors,"登录失败");
        }
    }

    /**
     * user列表
     * @param user_p
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Object listUsers(User user_p, com.jk.ndt.etl.entity.Page basePage) {
        List<User> userList = userService.listUsers(user_p, basePage);
        Page<User> userPage = (Page<User>) userList;

        JSONArray userArray = new JSONArray();
        if (null != userList && !userList.isEmpty()) {
            for (User user : userList) {
                JSONObject userObject = new JSONObject();
                userObject.put("id", user.getId());
                userObject.put("create_user", user.getCreated_user());
                userObject.put("updated_user", user.getUpdated_user());
                userObject.put("created_at", user.getCreated_at());
                userObject.put("updated_at", user.getUpdated_at());
                userObject.put("is_active", user.getIs_active());
                userObject.put("email", user.getEmail());
                userObject.put("name", user.getName());
                userObject.put("phone", user.getPhone());
                userObject.put("description", user.getDescription());
                userArray.add(userObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        basePage.setTotal(userPage.getTotal());
        jsonObject.put(Constant.BASE_PAGE, basePage);
        jsonObject.put("users", userArray);
        return jsonObject;
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUser(@PathVariable("id") BigDecimal id) {

        User user = userService.getUser(id);

        JSONObject errors = new JSONObject();
        if (null == user) {
            errors.put("user", "不存在该用户");
            return ErrorUtil.getRequestError(errors, "不存在该用户");
        }
        JSONObject userObject = new JSONObject();
        userObject.put("id", user.getId());
        userObject.put("create_user", user.getCreated_user());
        userObject.put("updated_user", user.getUpdated_user());
        userObject.put("created_at", user.getCreated_at());
        userObject.put("updated_at", user.getUpdated_at());
        userObject.put("is_active", user.getIs_active());
        userObject.put("email", user.getEmail());
        userObject.put("name", user.getName());
        userObject.put("phone", user.getPhone());
        userObject.put("password", user.getPassword());
        userObject.put("description", user.getDescription());
        return userObject;
    }

    /**
     * 保存用户
     * @param response
     * @param request
     * @param user 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public Object saveUser(HttpServletResponse response, HttpServletRequest request, @Valid @RequestBody User user, BindingResult result) {

        JSONObject errorsDetail = new JSONObject();
        String msg = null;
        User u_double = userService.getUserByNameOrId(user);
        if (null != u_double) {
            errorsDetail.put(user.getName(), "用户名重复");
            msg = "用户名重复";
        }
        if (StringUtils.isBlank(user.getPassword())) {
            errorsDetail.put("password", "密码不能为空");
            if (null == msg) {
                msg = "密码不能为空";
            }
        }
        if (!StringUtils.isBlank(user.getPassword()) && !Checker.checkPassword(user.getPassword())) {
            errorsDetail.put("password", "密码必须同时包含大小写字母数字，长度>8");
            if (null == msg) {
                msg = "密码必须同时包含大小写字母数字，长度>8";
            }
        }
        if (!errorsDetail.isEmpty()) {
            return ErrorUtil.getRequestError(errorsDetail,msg);
        }
        try {
            user.setPassword(Md5SaltTool.getEncryptedPwd(user.getPassword()));
        } catch (Exception e) {
            user.setPassword(null);
        }
        user.setCreated_by(LoginSessionUtil.getAdminId(request));

        user.setId(userService.getUserSequence());
        userService.saveUser(user);
        return user.getId();
    }

    /**
     * 更新用户
     * @param request
     * @param response
     * @param id 主键
     * @param user 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") BigDecimal id, @Valid @RequestBody User user, BindingResult result) {

        JSONObject errorsDetail = new JSONObject();
        String msg = null;
//        List<String> errorsDetail = new JSONObject();
        user.setId(id);
        User u_double = userService.getUserByNameOrId(user);
        User u_old = userService.getUser(user.getId());
        if (null == u_old) {
            errorsDetail.put("user", "修改的对象不存在");
            msg = "不存在该用户";
        }
        if (null != u_double) {
            errorsDetail.put(user.getName(), "用户名重复");
            if (null == msg) {
                msg = "用户名重复";
            }
        }
        if (null == user.getId()) {
            errorsDetail.put("id", "用户id为空");
        }
        if (!StringUtils.isBlank(user.getPassword()) && !Checker.checkPassword(user.getPassword())) {
            errorsDetail.put("password", "密码必须同时包含大小写字母数字，长度>8");
            if (null == msg) {
                msg = "密码必须同时包含大小写字母数字，长度>8";
            }
        }
        if (!errorsDetail.isEmpty()) {
            return ErrorUtil.getRequestError(errorsDetail,msg);
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
        user.setUpdated_by(LoginSessionUtil.getAdminId(request));
        user.setUpdated_at(new Date());
        userService.updateUser(user);
        return null;
    }

    /**
     * 删除user
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteUser(@PathVariable("id") BigDecimal id) {

        userService.deleteUser(id);
        return null;
    }

}
