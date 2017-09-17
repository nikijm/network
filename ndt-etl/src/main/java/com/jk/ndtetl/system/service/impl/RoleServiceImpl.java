package com.jk.ndtetl.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.Role;
import com.jk.ndtetl.system.dao.RoleDao;
import com.jk.ndtetl.system.service.IRoleService;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    protected BaseDao<Role> getDao() {
        return roleDao;
    }

    @Override
    public Role getRole(Integer role_id) {
        return roleDao.getRole(role_id);
    }

    @Override
    public Role getRoleByNameOrId(Role role) {
        return roleDao.getRoleByNameOrId(role);
    }

    @Override
    public List<Role> listRole(Role role, BasePage basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return roleDao.listRole(role);
    }

    @Override
    public List<Map<String,Object>> listRoleByParam(Map<String, Object> param) {
        return roleDao.listRoleByParam(param);
    }

    @Override
    public List<Role> listRoleAll() {
        return roleDao.listRole(null);
    }

    @Override
    public int saveRole(Role role) {
        return roleDao.saveRole(role);
    }

    @Override
    public int updateRole(Role role) {
        return roleDao.updateRole(role);
    }

    @Override
    public int deleteRole(Integer role_id) {
        return roleDao.deleteRole(role_id);
    }

    @Override
    public Integer getRoleSequence() {
        return roleDao.getRoleSequence();
    }

    @Override
    public int getUserRolesCount(Integer role_id) {
        return roleDao.getUserRolesCount(role_id);
    }

}
