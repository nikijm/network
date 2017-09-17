package com.jk.ndtetl.controller;

import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.system.service.IRoleService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.EHCacheUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fangwei
 * @ClassName: BaseController
 * @Description: 基础控制器，对控制器公共逻辑进行封装
 * @date 2017年5月15日 下午4:04:22
 */
public class BaseController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String PARAM = "param";
    protected static final String MESSAGE = "传入的参数不正确";


    public static final String OPTION_ROLE = "listRoleOptions";
    public static final String OPTION_ORG = "listOrgOptions";
    public static final String OPTION_DATA_FILE_TYPE= "listDataFileTypeOptions";
    public static final String OPTION_BUSINESS_TYPE= "listBusinessTypeOptions";

    /**
     * 获取缓存的角色选项
     *
     * @return
     */
    protected Object listRoleOptions(IRoleService iRoleService, Map<String, Object> param) {
        Object o = EHCacheUtil.get(OPTION_ROLE);
        if (null==o || ((List<Role>)o).size()<1) {
            if (null == param) {
                param = new HashMap<>();
                param.put("isActive", "Y");
                param.put("roleId", LoginSessionUtil.OUTER_USER);
            }
            List<Map<String,Object>> roles = iRoleService.listRoleByParam(param);
            EHCacheUtil.put(OPTION_ROLE, roles);
            return roles;
        }
        return EHCacheUtil.get(OPTION_ROLE);
    }

    /**
     * 获取缓存的机构选项
     *
     * @return
     */
    protected Object listOrgOptions(IOrganizationService iOrganizationService, Map<String, Object> param) {
        Object o = EHCacheUtil.get(OPTION_ORG);
        if (null==o || ((List<Org>)o).size()<1) {
            if (null == param) {
                param = new HashMap<>();
                param.put("isActive", "Y");
            }
            List<Map<String,Object>> orgs = iOrganizationService.listOrganizationByParam(param);
            EHCacheUtil.put(OPTION_ORG, orgs);
            return orgs;
        }
        return EHCacheUtil.get(OPTION_ORG);
    }
    /**
     * 获取文件类型选项列表
     * @return
     */
    protected Object listDataFileTypeOptions(IDataFileTypeService iDataFileTypeService, Map<String, Object> param) {
        Object o = EHCacheUtil.get(OPTION_DATA_FILE_TYPE);
        if (null==o || ((List<DataFileType>)o).size()<1) {
            if (null == param) {
                param = new HashMap<>();
                param.put("isActive", "Y");
            }
            List<Map<String,Object>> dataFileTypes = iDataFileTypeService.listOptionByParam(param);
            EHCacheUtil.put(OPTION_DATA_FILE_TYPE, dataFileTypes);
            return dataFileTypes;
        }
        return EHCacheUtil.get(OPTION_DATA_FILE_TYPE);
    }
    /**
     * 获取业务类型选项列表
     * @return
     */
    protected Object listBusinessTypeOptions(IBusinessTypeService iBusinessTypeService, Map<String, Object> param) {
        Object o = EHCacheUtil.get(OPTION_BUSINESS_TYPE);
        if (null==o || ((List<BusinessType>)o).size()<1) {
            if (null == param) {
                param = new HashMap<>();
                param.put("isActive", "Y");
            }
            List<Map<String,Object>> dataFileTypes = iBusinessTypeService.listOptionByParam(param);
            EHCacheUtil.put(OPTION_BUSINESS_TYPE, dataFileTypes);
            return dataFileTypes;
        }
        return EHCacheUtil.get(OPTION_BUSINESS_TYPE);
    }

}
