package com.jk.ndt.etl.controller.system;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.github.pagehelper.Page;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.service.system.AdminService;
import com.jk.ndt.etl.service.system.RoleService;
import com.jk.ndt.etl.utility.*;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 角色管理
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class RoleController extends BaseController {


    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;

    /**
     * 角色列表
     * @param p_role 参数
     * @param basePage 分页信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public Object listRole(Role p_role, com.jk.ndt.etl.entity.Page basePage, HttpServletRequest request) {

        p_role = (Role) TransforUtil.handleMapToObject(request, Role.class);

        List<Role> listRole = roleService.listRole(p_role, basePage);
        Page<Role> pageRole = (Page<Role>) listRole;

        JSONArray roleArray = new JSONArray();
        if (null != listRole && !listRole.isEmpty()) {
            for (Role role : listRole) {
                JSONObject roleObject = new JSONObject();
                roleObject.put("id", role.getId());
                roleObject.put("description", role.getDescription());
                roleObject.put("name", role.getName());

                roleObject.put("create_user", role.getCreated_user());
                roleObject.put("updated_user", role.getUpdated_user());
                roleObject.put("created_at", role.getCreated_at());
                roleObject.put("updated_at", role.getUpdated_at());
                roleObject.put("is_active", role.getIs_active());

                List<Resource> resourceList = new ArrayList<>();
                String permissions = role.getPermissions();
                JSONArray proArray = new JSONArray();
                if (!StringUtils.isBlank(permissions)) {
                    resourceList = JSONArray.parseArray(permissions, Resource.class);
                    if (Checker.isNotNullOrEmpty(resourceList)) {
                        for (Resource resource : resourceList) {
                            JSONObject resourceObject = new JSONObject();
                            resourceObject.put("name", resource.getName());
                            resourceObject.put("key", resource.getKey());
                            resourceObject.put("available_operations", resource.getAvailable_operations());
                            proArray.add(resourceObject);
                        }
                    }
                }

                roleObject.put("permissions", proArray);
                roleArray.add(roleObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        basePage.setTotal(pageRole.getTotal());
        jsonObject.put(Constant.BASE_PAGE, basePage);
        jsonObject.put("roles", roleArray);
        return jsonObject;
    }

    /**
     * 角色详情
     * @param role_id 主键
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getRole(@PathVariable("id") BigDecimal role_id) {
        Role role = roleService.getRole(role_id);
        JSONObject errors=new JSONObject();
        if (null == role) {
            errors.put("role", "不存在该角色");
            return ErrorUtil.getRequestError(errors,"不存在该角色");
        }
        JSONObject roleObject = new JSONObject();
        roleObject.put("id", role.getId());
        roleObject.put("description", role.getDescription());
        roleObject.put("name", role.getName());

        roleObject.put("create_user", role.getCreated_user());
        roleObject.put("updated_user", role.getUpdated_user());
        roleObject.put("created_at", role.getCreated_at());
        roleObject.put("updated_at", role.getUpdated_at());
        roleObject.put("is_active", role.getIs_active());

        String permissions = role.getPermissions();
        List<Resource> resourceList = new ArrayList<>();
        JSONArray proArray = new JSONArray();
        if (!StringUtils.isBlank(permissions)) {
            resourceList = JSONArray.parseArray(permissions, Resource.class);
            if (Checker.isNotNullOrEmpty(resourceList)) {
                for (Resource resource : resourceList) {
                    JSONObject resourceObject = new JSONObject();

                    resourceObject.put("id", resource.getId());
                    resourceObject.put("name", resource.getName());
                    resourceObject.put("description", resource.getDescription());
                    resourceObject.put("key", resource.getKey());
                    resourceObject.put("available_operations", resource.getAvailable_operations());
                    proArray.add(resourceObject);
                }
            }
        }
        roleObject.put("permissions", proArray);
        return roleObject;
    }

    /**
     * 保存角色
     * @param response
     * @param request
     * @param role_p 参数
     * @param result
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    @ResponseBody
    public Object saveRole(HttpServletResponse response, HttpServletRequest request, @Valid   Role role_p, BindingResult result) throws IOException {

        role_p=(Role)TransforUtil.handleMapToObject(request,Role.class);
        JSONObject errorsDetail = new JSONObject();
        String msg = null;
        if (null != role_p.getPermissions()) {
            try {
                JSONArray.parseArray(JSON.toJSON(role_p.getPermissions()).toString());
            } catch (Exception e) {
                e.printStackTrace();
                errorsDetail.put("permissions", "权限格式非JSON数组");
            }
        }
        if (null != roleService.getRoleByNameOrId(role_p)) {
            errorsDetail.put(role_p.getName(), "角色名重复");
            msg = "角色名重复";
        }
        if (!errorsDetail.isEmpty()) {
            return ErrorUtil.getRequestError(errorsDetail,msg);
        }
        role_p.setCreated_by(LoginSessionUtil.getAdminId(request));
        role_p.setCreated_at(new Date());
        role_p.setId(roleService.getRoleSequence());
        roleService.saveRole(role_p);
        return role_p.getId();
    }

    /**
     * 更新角色
     * @param request
     * @param response
     * @param role_id 主键
     * @param role_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRole(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") BigDecimal role_id, @Valid Role role_p, BindingResult result) {

        role_p = (Role) TransforUtil.handleMapToObject(request, Role.class);
        JSONObject errorsDetail = new JSONObject();
        String msg = null;
        role_p.setId(role_id);
        if (null == role_p.getId()) {
            errorsDetail.put("id", "角色id为空");
        }
        if (StringUtils.isBlank(role_p.getName())) {
            errorsDetail.put("name", "角色名为空");
            msg = "角色名不能为空";
        }
        if (null != role_p.getPermissions()) {
            try {
                JSONArray.parseArray(JSON.toJSON(role_p.getPermissions()).toString());
            } catch (Exception e) {
                e.printStackTrace();
                errorsDetail.put("permissions", "权限格式非JSON数组");
            }
        }
        Role r_double = roleService.getRoleByNameOrId(role_p);
        if (null != r_double) {
            errorsDetail.put(role_p.getName(), "角色名重复");
            if (null == msg) {
                msg = "角色名重复";
            }
        }
        if (!errorsDetail.isEmpty()) {
            return ErrorUtil.getRequestError(errorsDetail,msg);
        }
        role_p.setUpdated_by(LoginSessionUtil.getAdminId(request));
        role_p.setUpdated_at(new Date());
        roleService.updateRole(role_p);
        return null;
    }

    /**
     * 删除空闲角色
     * @param role_id 主键
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteRole(@PathVariable("id") BigDecimal role_id) {

        JSONObject errors = new JSONObject();
        String msg = null;
        Role role = roleService.getRole(role_id);
        if (null != role && null != role.getNotdelete() && role.getNotdelete()) {
            errors.put("role", "该角色正在使用中,不能删除");
            msg = "该角色为系统超级管理员默认角色,不能删除";
        }
        if (roleService.getAdminRolesCount(role_id) > 0) {
            errors.put("role", "该角色正在使用中,不能删除");
            if (null == msg) {
                msg = "该角色正在使用中,不能删除";
            }
        }
        if (!errors.isEmpty()) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        roleService.deleteRole(role_id);
        return null;
    }
}
