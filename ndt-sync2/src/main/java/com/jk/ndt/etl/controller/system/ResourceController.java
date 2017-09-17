package com.jk.ndt.etl.controller.system;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.service.system.ResourceService;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 资源管理
 * Created by 朱生 on 2017/5/13.
 */
@Controller
@RequestMapping("/api")
public class ResourceController extends BaseController {


    @Autowired
    private ResourceService resourceService;

    /**
     * 资源列表
     *
     * @param p_resources 参数
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    @ResponseBody
    public Object listResource(Resource p_resources, com.jk.ndt.etl.entity.Page basePage) {

        List<Resource> listResource = resourceService.listResource(p_resources, basePage);
        Page<Resource> pageResource = (Page<Resource>) listResource;

        JSONArray resourcesArray = new JSONArray();
        if (null != listResource && !listResource.isEmpty()) {
            for (Resource resources : listResource) {
                JSONObject resourcesObject = new JSONObject();
                resourcesObject.put("id", resources.getId());
                resourcesObject.put("description", resources.getDescription());
                resourcesObject.put("name", resources.getName());
                resourcesObject.put("key", resources.getKey());
                resourcesObject.put("available_operations", resources.getAvailable_operations());

                resourcesObject.put("create_user", resources.getCreated_user());
                resourcesObject.put("updated_user", resources.getUpdated_user());
                resourcesObject.put("created_at", resources.getCreated_at());
                resourcesObject.put("updated_at", resources.getUpdated_at());
                resourcesObject.put("is_active", resources.getIs_active());
                resourcesArray.add(resourcesObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        basePage.setTotal(pageResource.getTotal());
        jsonObject.put(Constant.BASE_PAGE, basePage);
        jsonObject.put("resources", resourcesArray);
        return jsonObject;
    }

    /**
     * 资源详情
     * @param resources_id 主键
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getResource(@PathVariable("id") BigDecimal resources_id) {
        Resource resource= resourceService.getResource(resources_id);

        JSONObject errors=new JSONObject();
        if (null == resource) {
            errors.put("resource", "不存在该权限");
            return ErrorUtil.getRequestError(errors);
        }
        JSONObject resourcesObject = new JSONObject();
        resourcesObject.put("id", resource.getId());
        resourcesObject.put("description", resource.getDescription());
        resourcesObject.put("name", resource.getName());
        resourcesObject.put("key", resource.getKey());

        resourcesObject.put("create_user", resource.getCreated_user());
        resourcesObject.put("updated_user", resource.getUpdated_user());
        resourcesObject.put("created_at", resource.getCreated_at());
        resourcesObject.put("updated_at", resource.getUpdated_at());
        resourcesObject.put("is_active", resource.getIs_active());

        return resourcesObject;
    }

    /**
     * 保存资源
     * @param response
     * @param request
     * @param resources_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    @ResponseBody
    public Object saveResource(HttpServletResponse response, HttpServletRequest request, @Valid  @RequestBody Resource resources_p, BindingResult result) {

        JSONObject message = new JSONObject();
        JSONObject errors = new JSONObject();
        JSONObject errorsDetail = new JSONObject();
        Resource r_double_name=resourceService.getResourceByNameOrId(resources_p);
        int r_double_count = resourceService.getResourceByKey(resources_p);
        if (null!= r_double_name) {
            errorsDetail.put(resources_p.getName(), "权限名重复");
        }
        if (r_double_count > 0) {
            errorsDetail.put(resources_p.getKey(), "权限key重复");
        }
        if (null==resources_p.getIs_active()) {
            errorsDetail.put("is_active", "是否激活不能为空");
        }
        if(null!=r_double_name || r_double_count>0 || null==resources_p.getIs_active()){

            errors.put("resource", errorsDetail);
            message.put("message", "客户端输入有误，请确保输入正确");
            message.put(Constant.REQ_ERRORS, errors);
            response.setStatus(Constant.PARAMS_ERROR);
            return message;
        }

        resources_p.setCreated_by(LoginSessionUtil.getAdminId(request));
        resources_p.setCreated_at(new Date());
        resources_p.setId(resourceService.getResourceSequence());
        resourceService.saveResource(resources_p);
        return resources_p.getId();
    }

    /**
     * 更新资源
     * @param request
     * @param response
     * @param resources_id 主键
     * @param resources_p 参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateResource(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") BigDecimal resources_id,@Valid  @RequestBody   Resource resources_p,BindingResult result) {

        JSONObject message = new JSONObject();
        JSONObject errors = new JSONObject();
        JSONObject errorsDetail = new JSONObject();
        resources_p.setId(resources_id);
        if (null == resources_p.getId()) {
            errorsDetail.put("id", "角色id为空");
        }
        Resource r_double_name=resourceService.getResourceByNameOrId(resources_p);
        Resource r_exist=resourceService.getResource(resources_p.getId());
        int r_double_count = resourceService.getResourceByKey(resources_p);
        if (null== r_exist) {
            errorsDetail.put(resources_p.getId()+"", "修改的权限不存在");
        }
        if (null!= r_double_name) {
            errorsDetail.put(resources_p.getName(), "权限名重复");
        }
        if (r_double_count > 0) {
            errorsDetail.put(resources_p.getKey(), "权限key重复");
        }
        if (null==resources_p.getIs_active()) {
            errorsDetail.put("is_active", "是否激活不能为空");
        }
        if(null!=r_double_name || r_double_count>0 || null== r_exist ||  null==resources_p.getIs_active()){

            errors.put("resource", errorsDetail);
            message.put("message", "客户端输入有误，请确保输入正确");
            message.put(Constant.REQ_ERRORS, errors);
            response.setStatus(Constant.PARAMS_ERROR);
            return message;
        }
        resources_p.setUpdated_by(LoginSessionUtil.getAdminId(request));
        resources_p.setUpdated_at(new Date());
        resourceService.updateResource(resources_p);
        return null;
    }

    /**
     * 删除资源
     * @param resource_id 主键
     * @return
     */
    @RequestMapping(value = "/resources/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteResource(@PathVariable("id") BigDecimal resource_id) {

        resourceService.deleteResource(resource_id);
        return null;
    }

}
