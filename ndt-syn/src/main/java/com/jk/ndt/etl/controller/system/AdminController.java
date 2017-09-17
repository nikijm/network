package com.jk.ndt.etl.controller.system;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Menu;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.service.system.AdminService;
import com.jk.ndt.etl.utility.*;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;
import com.jk.ndt.etl.utility.promission.ResourceMenusUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 运维登录、注销、运维人员列表管理等
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class AdminController extends BaseController {


    @Autowired
    private AdminService adminService;

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
     * @param admin 用户提交的用户名、密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/auth/session", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletResponse response, HttpServletRequest request, Admin admin) {

        admin = (Admin) TransforUtil.handleMapToObject(request, Admin.class);
        JSONObject errors = new JSONObject();
        String message = null;
        try {
            if (null == admin || StringUtils.isBlank(admin.getName())) {
                errors.put("name", "用户名不能为空");
                message = "用户名不能为空";
            }
            Admin queryAdmin = null;
            if (null == admin || StringUtils.isBlank(admin.getPassword())) {
                errors.put("password", "密码不能为空");
                if (null == message) {
                    message = "密码不能为空";
                }
            } else {
                queryAdmin = adminService.getByName(admin.getName());
                if (null == queryAdmin || !Md5SaltTool.validPassword(admin.getPassword(), queryAdmin.getPassword())) {
                    errors.put("name_password", "用户名或密码错误");
                    if (null == message) {
                        message = "用户名或密码错误";
                    }
                }
                if (null!=queryAdmin && (null == queryAdmin.getIs_active() || !queryAdmin.getIs_active())) {
                    errors.put("is_active", "用户未启用");
                    if (null == message) {
                        message = "用户未启用";
                    }
                }
            }

            if (!errors.isEmpty()) {
                return ErrorUtil.getRequestError(errors, message);
            }
            Subject currentAdmin = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(queryAdmin.getName(), queryAdmin.getPassword());
            currentAdmin.login(token);
            Key key = MacProvider.generateKey();
            String compactJws = Jwts.builder()
                    .setSubject(queryAdmin.getName())
                    .signWith(SignatureAlgorithm.HS512, key)
                    .setIssuedAt(DateTime.now().toDate())
                    .setExpiration(DateTime.now().plusMinutes(Constant.LOGIN_USER_CLIENT_TOKEN_EXPIRED).toDate())
                    .compact();

            Map<String, Object> loginSessionMap = new HashMap<String, Object>();
            Map<String, Object> menuMap = ResourceMenusUtil.setRoleAndPermission(queryAdmin, request, loginSessionMap);
            loginSessionMap.put(Constant.LOGIN_USER_SESSION, queryAdmin);
            loginSessionMap.put(Constant.LOGIN_USER_JJTW_KEY, key);

            EHCacheUtil.put(compactJws, loginSessionMap);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.LOGIN_USER_CLIENT_TOKEN_RESPONSE, compactJws);
            JSONObject promissObject = assembleReturnValues(queryAdmin);
            promissObject.put("menus", menuMap.get(Constant.LOGIN_USER_MENU_SESSION));
            jsonObject.put("user", promissObject);
            logger.debug(queryAdmin.getName()+"登陆成功！");
            logger.debug("登录获取的token："+compactJws);
            System.out.println(queryAdmin.getName() + "登录成功！");
            System.out.println("登录获取的ｔｏｋｅｎ："+compactJws);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("login", "登录失败！");
            return ErrorUtil.getRequestError(errors,"登录失败");
        }
    }

    /**
     * 按照文档返回数据的格式组织登录返回的数据
     * @param admin
     * @return
     */
    private JSONObject assembleReturnValues(Admin admin) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", admin.getId());
        jsonObject.put("name", admin.getName());

        List<Role> roleList = admin.getRoles();
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
     * admin列表
     * @param p_admin 过滤参数
     * @param basePage 分页信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    @ResponseBody
    public Object listAdmin(Admin p_admin, com.jk.ndt.etl.entity.Page basePage, HttpServletRequest request, HttpServletResponse response) {

        p_admin = (Admin) TransforUtil.handleMapToObject(request, Admin.class);

        List<Admin> listAdmin = adminService.listAdmin(p_admin, basePage);
        Page<Admin> pageAdmins = (Page<Admin>) listAdmin;
        basePage.setTotal(pageAdmins.getTotal());

        JSONArray adminArray = new JSONArray();
        if (null != listAdmin && !listAdmin.isEmpty()) {
            for (Admin admin : listAdmin) {
                JSONObject adminObject = new JSONObject();
                adminObject.put("id", admin.getId());
                adminObject.put("name", admin.getName());
                adminObject.put("email", admin.getEmail());
                adminObject.put("phone", admin.getPhone());
                adminObject.put("created_user", admin.getCreated_user());
                adminObject.put("updated_user", admin.getUpdated_user());
                adminObject.put("created_at", admin.getUpdated_user());
                adminObject.put("updated_at", admin.getUpdated_user());
                adminObject.put("is_active", admin.getIs_active());

                JSONArray permissionsArray = new JSONArray();
                List<Role> roleList = admin.getRoles();
                if (null != roleList && !roleList.isEmpty()) {
                    for (Role role : roleList) {
                        JSONObject roleJson = new JSONObject();
                        roleJson.put("id", role.getId());
                        roleJson.put("name", role.getName());
//                        roleJson.put("description", role.getDescription());
                        permissionsArray.add(roleJson);
                    }
                }
                adminObject.put("roles", permissionsArray);
                adminArray.add(adminObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.BASE_PAGE, basePage);
        jsonObject.put("admins", adminArray);
        jsonObject.put(Constant.PAGE_PERMISSIONS, ResourceMenusUtil.getPagePermission(request));
        return jsonObject;
    }

    /**
     * admin详情
     * @param admin_id 主键
     * @param response
     * @return
     */
    @RequestMapping(value = "/admins/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getAdmin(@PathVariable("id") BigDecimal admin_id, HttpServletResponse response) {
        Admin admin = adminService.getAdmin(admin_id);
        JSONObject errors=new JSONObject();
        if (null == admin) {
            errors.put("admin", "不存在该用户");
            return ErrorUtil.getRequestError(errors,"不要存在该用户");
        }
        JSONObject result = new JSONObject();
        JSONObject adminObject = new JSONObject();

        adminObject.put("id", admin.getId());
        adminObject.put("name", admin.getName());
        adminObject.put("email", admin.getEmail());
        adminObject.put("password", admin.getPassword());
        adminObject.put("phone", admin.getPhone());
        adminObject.put("is_active", admin.getIs_active());

        result.put("admin", adminObject);

        JSONArray jsonArray = new JSONArray();
        List<Role> roleList = admin.getRoles();
        if (null != roleList && !roleList.isEmpty()) {
            for (Role role : roleList) {
                JSONObject roleJson = new JSONObject();
                roleJson.put("id", role.getId());
                roleJson.put("name", role.getName());
//                roleJson.put("description", role.getDescription());
                jsonArray.add(roleJson);
            }
        }
        adminObject.put("roles", jsonArray);
        return result;
    }

    /**
     * 保存admin
     * @param response
     * @param request
     * @param admin_p 参数
     * @param result  hibernate validator
     * @return
     */
    @RequestMapping(value = "/admins", method = RequestMethod.POST)
    @ResponseBody
    public Object saveAdmin(HttpServletResponse response, HttpServletRequest request, @Valid @RequestBody  Admin admin_p, BindingResult result) {

//        admin_p =(Admin) TransforUtil.handleMapToObject(request, Admin.class);
        JSONObject errors = new JSONObject();
        String msg = null;
        Admin a_double = adminService.getAdminByNameOrId(admin_p);
        if (StringUtils.isBlank(admin_p.getPassword())) {
            errors.put("password", "密码不能为空");
            msg = "密码不能为空";
        }
        if (!StringUtils.isBlank(admin_p.getPassword()) && !Checker.checkPassword(admin_p.getPassword())) {
            errors.put("password", Constant.PWD_NOTICE);
            if (null == msg) {
                msg = Constant.PWD_NOTICE;
            }
        }
        if (null != a_double) {
            errors.put(admin_p.getName(), "用户名重复");
            if (null == msg) {
                msg = "用户名重复";
            }
        }

        if (!errors.isEmpty()) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        try {
            admin_p.setPassword(Md5SaltTool.getEncryptedPwd(admin_p.getPassword()));
        } catch (Exception e) {
            admin_p.setPassword(null);
        }
        admin_p.setCreated_by(LoginSessionUtil.getAdminId(request));
        admin_p.setCreated_at(new Date());

        admin_p.setId(adminService.getAdminSequence());
        adminService.saveAdmin(admin_p);
        return admin_p.getId();
    }

    /**
     * 更新admin
     * @param response
     * @param request
     * @param id 主键
     * @param admin_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/admins/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateAdmin(HttpServletResponse response, HttpServletRequest request, @PathVariable("id") BigDecimal id, @Valid  @RequestBody   Admin admin_p, BindingResult result) {

//        admin_p =(Admin) TransforUtil.handleMapToObject(request, Admin.class);
        JSONObject errors = new JSONObject();
        String msg = null;
        admin_p.setId(id);
        Admin a_double = adminService.getAdminByNameOrId(admin_p);
        Admin a_old = adminService.getAdmin(admin_p.getId());
        if (null != a_double) {
            errors.put(admin_p.getName(), "用户名重复");
            msg = "用户名重复";
        }
        if (!StringUtils.isBlank(admin_p.getPassword()) && !Checker.checkPassword(admin_p.getPassword())) {
            errors.put("password",Constant.PWD_NOTICE);
            if (null == msg) {
                msg = Constant.PWD_NOTICE;
            }
        }
        if (!errors.isEmpty()) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        try {
            if (!StringUtils.isBlank(admin_p.getPassword())) {
                admin_p.setPassword(Md5SaltTool.getEncryptedPwd(admin_p.getPassword()));
            } else {
                admin_p.setPassword(a_old.getPassword());
            }
        } catch (Exception e) {
            admin_p.setPassword(null);
        }
        admin_p.setUpdated_by(LoginSessionUtil.getAdminId(request));
        admin_p.setUpdated_at(new Date());
        adminService.updateAdmin(admin_p);
        return null;
    }

    /**
     * 删除admin
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/admins/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteAdmin(@PathVariable("id") BigDecimal id) {
        Admin admin = adminService.getAdmin(id);
        if (null!=admin && null!=admin.getNotdelete() && admin.getNotdelete()) {
           return ErrorUtil.getRequestError(null, "该用户为系统默认，不可删除");
        }
        adminService.deleteAdmin(id);
        return null;
    }

}
