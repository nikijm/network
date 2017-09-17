package com.jk.ndtetl.system.dao;/**
 * Created by 朱生 on 2017/6/9.
 */

import java.lang.Integer;
import java.util.List;
import java.util.Map;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.etl.Org;

/**
 * 朱生
 *
 * @create 2017-06-09 14:25
 **/
public interface OrganizationDao extends BaseDao<Org>{

    public Org getOrganization(Integer organization_id);

    public Org getOrganizationByCode(String code);

    public int updateOrganization(Org organization);

    public int saveOrganization(Org organization);

    public int deleteOrganization(Integer organization_id);

    public int countUserByOrgId(Integer organization_id);

    public List<Org> listOrganization(Org organization);

    public List<Map<String,Object>> listOrganizationByParam(Map<String, Object> param);

    public Integer getOrganizationSequence();

    public Org getOrganizationByNameOrId(Org organization);

    public Org getOrganizationByValue(Org organization);

    public int saveOrganizationRoles(Org organization);
}
