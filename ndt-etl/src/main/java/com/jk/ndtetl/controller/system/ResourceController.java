package com.jk.ndtetl.controller.system;

import java.lang.Integer;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.util.Checker;
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
import com.jk.ndtetl.system.Resource;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IResourceService;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;

/**
 * 资源管理
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class ResourceController extends BaseController {


    @Autowired
    private IResourceService resourceService;

    /**
     * 资源列表
     * @param p_resources 参数
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    @ResponseBody
    public Object listResource(Resource p_resources, com.jk.ndtetl.controller.BasePage basePage,HttpServletRequest request) {

        List<Resource> listResource = resourceService.listResource(p_resources, basePage);
        Page<Resource> pageResource = (Page<Resource>) listResource;
        if (null !=pageResource) {
            basePage.setTotal(pageResource.getTotal());
        }
        JSONArray resourcesArray = new JSONArray();
        if (null != listResource && !listResource.isEmpty()) {
            for (Resource resource : listResource) {
                resourcesArray.add(setResultResource(resource));
            }
        }
        JSONObject jsonObject = new JSONObject();
        basePage.setTotal(pageResource.getTotal());
        jsonObject.put(BaseSystemEntity.BASE_PAGE, basePage);
        jsonObject.put("resources", resourcesArray);
//        jsonObject.put(User.PAGE_PERMISSIONS, ResourceMenusUtil.getPagePermission(request));
        return jsonObject;
    }

    /**
     * 资源详情
     * @param resources_id 资源id
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getResource(@PathVariable("id") Integer resources_id) {
        Resource resource= resourceService.getResource(resources_id);
        JSONObject errors=new JSONObject();
        if (null == resource) {
            errors.put("resource", "该资源不存在");
            return ErrorUtil.getRequestError(errors);
        }
        return setResultResource(resource);
    }

    /**
     * 设置返回结果集
     * @param resource
     * @return
     */
    private JSONObject setResultResource(Resource resource) {
        JSONObject resourcesObject = new JSONObject();
        resourcesObject.put("id", resource.getId());
        resourcesObject.put("description", resource.getDescription());
        resourcesObject.put("name", resource.getName());
        resourcesObject.put("key", resource.getKey());
        resourcesObject.put("availableOperations", resource.getAvailableOperations());
        resourcesObject.put("createUser", resource.getCreatedUser());
        resourcesObject.put("updatedUser", resource.getUpdatedUser());
        resourcesObject.put("created", resource.getCreated());
        resourcesObject.put("updated", resource.getUpdated());
        resourcesObject.put("isActive", resource.getIsActive());
        return resourcesObject;
    }

    /**
     * 保存资源
     *
     * @param response
     * @param request
     * @param resources_p 资源
     * @param result
     * @return
     */
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    @ResponseBody
    public Object saveResource(HttpServletResponse response, HttpServletRequest request, @Valid @RequestBody Resource resources_p, BindingResult result) {

        String msg=null;
        JSONObject errorsDetail = new JSONObject();
        Resource r_double_name = resourceService.getResourceByNameOrId(resources_p);
        int r_double_count = resourceService.getResourceByKey(resources_p);
        if (null != r_double_name) {
            if (null == msg) {
                msg = "资源名重复";
            }
            errorsDetail.put(resources_p.getName(), "资源名重复");
        }
        if (r_double_count > 0) {
            if (null == msg) {
                msg = "资源编码重复";
            }
            errorsDetail.put(resources_p.getKey(), "资源编码重复");
        }
        if (Checker.isNotNullOrEmpty(errorsDetail)) {
            return ErrorUtil.getRequestError(errorsDetail, msg);
        }
        resources_p.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        resources_p.setCreated(new Date());
        resources_p.setId(resourceService.getResourceSequence());
        resourceService.saveResource(resources_p);
        return resources_p.getId();
    }

    /**
     * 更新资源
     * @param request
     * @param response
     * @param resources_id 资源id
     * @param resources_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateResource(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") Integer resources_id,@Valid  @RequestBody   Resource resources_p,BindingResult result) {

        String msg=null;
        JSONObject errorsDetail = new JSONObject();
        resources_p.setId(resources_id);
        if (null == resources_p.getId()) {
            if (null == msg) {
                msg = "资源id不能为空";
            }
            errorsDetail.put("etl_resource_id", "资源id不能为空");
        }
        Resource r_double_name=resourceService.getResourceByNameOrId(resources_p);
        Resource r_exist=resourceService.getResource(resources_p.getId());
        int r_double_count = resourceService.getResourceByKey(resources_p);
        if (null== r_exist) {
            if (null == msg) {
                msg = "该资源不存在";
            }
            errorsDetail.put(resources_p.getId()+"", "该资源不存在");
        }
        if (null!= r_double_name) {
            if (null == msg) {
                msg = "资源名重复";
            }
            errorsDetail.put(resources_p.getName(), "资源名重复");
        }
        if (r_double_count > 0) {
            if (null == msg) {
                msg = "资源编码重复";
            }
            errorsDetail.put(resources_p.getKey(), "资源编码重复");
        }
        if (Checker.isNotNullOrEmpty(errorsDetail)) {
            return ErrorUtil.getRequestError(errorsDetail, msg);
        }
        resources_p.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        resources_p.setUpdated(new Date());
        resourceService.updateResource(resources_p);
        return null;
    }

    /**
     * 删除资源
     * @param resource_id 资源id
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteResource(@PathVariable("id") Integer resource_id) {

        resourceService.deleteResource(resource_id);
        return null;
    }

}
