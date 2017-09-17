package com.jk.ndtetl.system.service.impl;
/**
 * Created by 朱生 on 2017/6/9.
 */

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.dao.OrganizationDao;
import com.jk.ndtetl.system.dao.UserDao;
import com.jk.ndtetl.system.service.IOrganizationService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 朱生
 *
 * @create 2017-06-09 14:40
 **/
@Service("organizationService")
public class OrganizationServiceImpl extends BaseServiceImpl<Org> implements IOrganizationService {

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserDao userDao;

    @Override
    protected BaseDao<Org> getDao() {
        return organizationDao;
    }
    @Override
    public Org getOrganization(Integer organization_id) {
        return organizationDao.getOrganization(organization_id);
    }

    @Override
    public Org getOrganizationByCode(String code) {
        return organizationDao.getOrganizationByCode(code);
    }

    @Transactional
    @Override
    public int updateOrganization(Org organization) {

        return organizationDao.updateOrganization(organization);
    }

    @Transactional
    @Override
    public int saveOrganization(Org organization, User default_user) {
        if (null != default_user.getRoleIds() && default_user.getRoleIds().length > 0) {
            userDao.saveUserRoles(default_user);
        }
        userDao.saveUser(default_user);
        return organizationDao.saveOrganization(organization);
    }

    @Override
    public int deleteOrganization(Integer organization_id) {
        return organizationDao.deleteOrganization(organization_id);
    }

    @Override
    public int countUserByOrgId(Integer organization_id) {
        return organizationDao.countUserByOrgId(organization_id);
    }


    /**
     * 获取所有的机构列表
     * @return
     */
    @Override
    public List<Org> listOrganizationAll(Org org) {
        return organizationDao.listOrganization(org);
    }

    @Override
    public List<Map<String,Object>> listOrganizationByParam(Map<String, Object> param) {
        return organizationDao.listOrganizationByParam(param);
    }

    @Override
    public List<Org> listOrganization(Org organization, BasePage basePage) {
        PageHelper.startPage(basePage.getCurrent(), basePage.getPage_size(), true);
        return organizationDao.listOrganization(organization);
    }

    @Override
    public Integer getOrganizationSequence() {
        return organizationDao.getOrganizationSequence();
    }

    @Override
    public Org getOrganizationByNameOrId(Org organization) {
        return organizationDao.getOrganizationByNameOrId(organization);
    }

    @Override
    public Org getOrganizationByValue(Org organization) {
        return organizationDao.getOrganizationByValue(organization);
    }

    @Override
    public int saveOrganizationRoles(Org organization) {
        return organizationDao.saveOrganization(organization);
    }

}
