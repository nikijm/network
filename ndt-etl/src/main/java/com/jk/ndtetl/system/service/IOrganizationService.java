package com.jk.ndtetl.system.service;

import java.lang.Integer;
import java.util.List;
import java.util.Map;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.User;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface IOrganizationService extends IBaseService<Org>{


    public Org getOrganization(Integer organization_id);

    public Org getOrganizationByCode(String code);

    public int updateOrganization(Org organization);

    public int saveOrganization(Org organization, User default_user);

    public int deleteOrganization(Integer organization_id);

    public int countUserByOrgId(Integer organization_id);

    public List<Org> listOrganizationAll(Org org);

    public List<Map<String,Object>> listOrganizationByParam(Map<String, Object> param);

    public List<Org> listOrganization(Org organization, com.jk.ndtetl.controller.BasePage basePage);

    public Integer getOrganizationSequence();

    public Org getOrganizationByNameOrId(Org organization);

    public Org getOrganizationByValue(Org organization);

    public int saveOrganizationRoles(Org organization);
}
