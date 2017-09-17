package com.jk.ndtetl.controller.system;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.util.*;
import com.jk.ndtetl.util.etl.OptionUtil;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.system.Resource;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IResourceService;
import com.jk.ndtetl.system.service.IRoleService;
import com.jk.ndtetl.util.promission.LoginSessionUtil;

/**
 * 角色管理
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class RoleController extends BaseController {


    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResourceService resourceService;

    /**
     * 角色列表
     * @param p_role 参数
     * @param basePage 分页
     * @param request
     * @return
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public Object listRole(Role p_role, com.jk.ndtetl.controller.BasePage basePage, HttpServletRequest request) {

        p_role = (Role) TransforUtil.handleMapToObject(request, Role.class);

        List<Role> listRole = roleService.listRole(p_role, basePage);
        Page<Role> pageRole = (Page<Role>) listRole;
        if (null !=pageRole) {
            basePage.setTotal(pageRole.getTotal());
        }
        JSONArray roleArray = new JSONArray();
        if (null != listRole && !listRole.isEmpty()) {
            for (Role role : listRole) {
                roleArray.add(setResultRole(role));
            }
        }
        JSONObject jsonObject = new JSONObject();
        basePage.setTotal(pageRole.getTotal());
        jsonObject.put(BaseSystemEntity.BASE_PAGE, basePage);
        jsonObject.put("roles", roleArray);
        jsonObject.put("resources", resourceService.listAllResource());
//        jsonObject.put(User.PAGE_PERMISSIONS, ResourceMenusUtil.getPagePermission(request));
        return jsonObject;
    }

    /**
     * 角色详情
     * @param role_id 角色id
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getRole(@PathVariable("id") Integer role_id) {
        Role role = roleService.getRole(role_id);
        JSONObject errors=new JSONObject();
        if (null == role) {
            errors.put("role", "不存在该角色");
            return ErrorUtil.getRequestError(errors,"不存在该角色");
        }
        return setResultRole(role);
    }

    /**
     * 设置返回结果集
     * @param role
     * @return
     */
    private JSONObject setResultRole(Role role) {
        JSONObject roleObject = new JSONObject();

        roleObject.put("id", role.getId());
        roleObject.put("description", role.getDescription());
        roleObject.put("name", role.getName());
        roleObject.put("createUser", role.getCreatedUser());
        roleObject.put("updatedUser", role.getUpdatedUser());
        roleObject.put("created", role.getCreated());
        roleObject.put("updated", role.getUpdated());
        roleObject.put("isActive", role.getIsActive());

        List<Resource> resourceList = new ArrayList<>();
        String permissions = role.getPermissions();
        JSONArray proArray = new JSONArray();
        if (!StringUtils.isBlank(permissions)) {
            try {
                resourceList = JSONArray.parseArray(permissions, Resource.class);
                if (Checker.isNotNullOrEmpty(resourceList)) {
                    for (Resource resource : resourceList) {
                        JSONObject resourceObject = new JSONObject();
                        resourceObject.put("id", resource.getId());
                        resourceObject.put("name", resource.getName());
                        resourceObject.put("description", resource.getDescription());
                        resourceObject.put("key", resource.getKey());
                        resourceObject.put("availableOperations", resource.getAvailableOperations());
                        proArray.add(resourceObject);
                    }
                }
            } catch (Exception e) {
                logger.info("roleId:"+role.getId()+",资源解析失败permissions:"+role.getPermissions());
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
    public Object saveRole(HttpServletResponse response, HttpServletRequest request, @RequestBody  @Valid Role role_p, BindingResult result) throws IOException {

//        role_p=(Role)TransforUtil.handleMapToObject(request,Role.class);
        JSONObject errors = new JSONObject();
        String msg = null;
        role_p.setPermissions(JSON.toJSONString(role_p.getResources()));
        if (null != roleService.getRoleByNameOrId(role_p)) {
            errors.put(role_p.getName(), "角色名重复");
            msg = "角色名重复";
        }
//        if (Checker.isNullOrEmpty(role_p.getResources())) {
//            errors.put(role_p.getName(), "请选择资源");
//            if (null == msg) {
//                msg = "请选择资源";
//            }
//        }
        if (!errors.isEmpty()) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        role_p.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        role_p.setCreated(new Date());
        role_p.setId(roleService.getRoleSequence());
        roleService.saveRole(role_p);
        EHCacheUtil.remove(BaseController.OPTION_ROLE);
        return role_p.getId();
    }

    /**
     * 更新角色
     * @param request
     * @param response
     * @param role_id 角色id
     * @param role_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRole(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer role_id,@RequestBody  @Valid  Role role_p, BindingResult result) {

//        role_p = (Role) TransforUtil.handleMapToObject(request, Role.class);
        JSONObject errors = new JSONObject();
        String msg = null;
        role_p.setId(role_id);
        if (null == role_p.getId()) {
            errors.put("id", "角色id不能为空");
        }
        Role r_double = roleService.getRoleByNameOrId(role_p);
        if (null != r_double) {
            errors.put(role_p.getName(), "角色名重复");
            if (null == msg) {
                msg = "角色名重复";
            }
        }
//        if (Checker.isNullOrEmpty(role_p.getResources())) {
//            errors.put(role_p.getName(), "请选择资源");
//            if (null == msg) {
//                msg = "请选择资源";
//            }
//        }
        if (!errors.isEmpty()) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        role_p.setPermissions(JSON.toJSONString(role_p.getResources()));
        role_p.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        role_p.setUpdated(new Date());
        roleService.updateRole(role_p);
        EHCacheUtil.remove(BaseController.OPTION_ROLE);
        return null;
    }

    /**
     * 删除角色
     * @param role_id 角色id
     * @return
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteRole(@PathVariable("id") Integer role_id) {

        JSONObject errors = new JSONObject();
        String msg = null;
        Role role = roleService.getRole(role_id);
        if (null != role && !StringUtils.isBlank(role.getIsdelete())) {
            errors.put("role", "系统默认角色，不能删除");
            msg = "系统默认角色，不能删除";
        }
        if (roleService.getUserRolesCount(role_id) > 0) {
            errors.put("role", "该角色正在使用中，不能删除");
            if (null == msg) {
                msg = "该角色正在使用中，不能删除";
            }
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors,msg);
        }
        roleService.deleteRole(role_id);
        EHCacheUtil.remove(BaseController.OPTION_ROLE);
        return null;
    }
}
