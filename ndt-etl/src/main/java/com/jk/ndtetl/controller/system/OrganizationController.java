package com.jk.ndtetl.controller.system;
/**
 * Created by 朱生 on 2017/6/9.
 */

import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.util.EHCacheUtil;
import org.aspectj.weaver.ast.Or;
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
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.system.service.IUserService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.StringUtils;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import com.jk.ndtetl.util.promission.Md5SaltTool;

/**
 * 朱生
 * 
 * @create 2017-06-09 14:42
 **/

@Controller
@RequestMapping("/api")
public class OrganizationController extends BaseController {

    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IUserService userService;

    /**
     * organization列表
     * @param p_organization 过滤参数
     * @param basePage       分页信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    @ResponseBody
    public Object listOrganization(Org p_organization, com.jk.ndtetl.controller.BasePage basePage,
                                   HttpServletRequest request) {
        if (!StringUtils.isBlank(p_organization.getSearchKey())) {
            p_organization.setName(p_organization.getSearchKey());
            p_organization.setCode(p_organization.getSearchKey());
        }
        List<Org> listOrganization = organizationService.listOrganization(p_organization, basePage);
        PageInfo pageInfo=new PageInfo(listOrganization);
        basePage.setTotal(pageInfo.getTotal());

        JSONObject rs = new JSONObject();
        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        rs.put("organizations", listOrganization);
//        jsonObject.put("organizations", assembleListResult(listOrganization));
//        jsonObject.put(User.PAGE_PERMISSIONS, ResourceMenusUtil.getPagePermission(request));
        return rs;
    }
    /**
     * organization详情
     * @param organization_id
     *            主键
     * @return
     */
    @RequestMapping(value = "/organizations/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrganization(@PathVariable("id") Integer organization_id,
            HttpServletRequest request) {
        Org organization = organizationService.getOrganization(organization_id);
        JSONObject errors = new JSONObject();
        if (null == organization) {
            errors.put("organization", "不存在该机构");
            return ErrorUtil.getRequestError(errors, "不要存在该机构");
        }
        JSONObject result = new JSONObject();
        result.put("organization", organization);
        return result;
    }
    /**
     * 保存organization
     * @param response
     * @param request
     * @param organization_p
     *            参数
     * @param result
     *            hibernate validator
     * @return
     */
    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    @ResponseBody
    public Object saveOrganization(HttpServletResponse response, HttpServletRequest request,
                                   @RequestBody  @Valid Org organization_p, BindingResult result) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        JSONObject errors = new JSONObject();
        String msg = null;
        Org a_double = organizationService.getOrganizationByNameOrId(organization_p);
        if (null != a_double) {
            errors.put(organization_p.getName(), "机构名重复");
            if (null == msg) {
                msg = "机构名重复";
            }
        }
        Org a_value_double = organizationService.getOrganizationByValue(organization_p);
        if (null != a_value_double) {
            errors.put(organization_p.getCode(), "机构代码重复");
            if (null == msg) {
                msg = "机构代码重复";
            }
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }

        organization_p.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        organization_p.setCreated(new Date());
        organization_p.setOrgId(organizationService.getOrganizationSequence());

        // 创建默认帐号
        User default_user=new User();
        default_user.setId(userService.getUserSequence());
        StringBuffer ss = new StringBuffer(StringUtils.randomString(10));
        ss.insert(3, default_user.getId());
        default_user.setName(ss.toString());
        default_user.setPassword(Md5SaltTool.getEncryptedPwd(StringUtils.randomString(10)));
        default_user.setOrgId(organization_p.getOrgId());
        Integer[] roleIds={LoginSessionUtil.OUTER_USER};
        default_user.setRoleIds(roleIds);
        organizationService.saveOrganization(organization_p,default_user);
        EHCacheUtil.remove(BaseController.OPTION_ORG);
        return organization_p.getOrgId();
    }

    /**
     * 更新organization
     * @param response
     * @param request
     * @param organization_id
     *            主键
     * @param organization_p
     *            参数
     * @param result
     * @return
     */
    @RequestMapping(value = "/organizations/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateOrganization(HttpServletResponse response, HttpServletRequest request,
            @PathVariable("id") Integer organization_id, @Valid @RequestBody Org organization_p,
            BindingResult result) {

        JSONObject errors = new JSONObject();
        String msg = null;
        organization_p.setOrgId(organization_id);
        Org a_double = organizationService.getOrganizationByNameOrId(organization_p);
        if (null != a_double) {
            errors.put(organization_p.getName(), "机构名重复");
            msg = "机构名重复";
        }
//        Org a_value_double = organizationService.getOrganizationByValue(organization_p);
//        if (null != a_value_double) {
//            errors.put(organization_p.getCode(), "机构代码重复");
//            if (null == msg) {
//                msg = "机构代码重复";
//            }
//        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        organization_p.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        organization_p.setUpdated(new Date());
        organizationService.updateOrganization(organization_p);
        EHCacheUtil.remove(BaseController.OPTION_ORG);
        return null;
    }

    /**
     * 删除organization
     * 
     * @param id
     *            主键
     * @return
     */
    @RequestMapping(value = "/organizations/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteOrganization(@PathVariable("id") Integer id) {
        JSONObject errors = new JSONObject();
        String msg=null;
        if (organizationService.countUserByOrgId(id) > 0) {
            errors.put("using", "该机构下存在用户，不能删除");
            msg = "该机构下存在用户，不能删除";
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        organizationService.deleteOrganization(id);
        EHCacheUtil.remove(BaseController.OPTION_ORG);
        return null;
    }

}
