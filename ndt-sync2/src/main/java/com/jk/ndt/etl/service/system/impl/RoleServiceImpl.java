package com.jk.ndt.etl.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.dao.system.RoleDao;
import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role getRole(BigDecimal role_id) {
        return roleDao.getRole(role_id);
    }

    @Override
    public Role getRoleByNameOrId(Role role) {
        return roleDao.getRoleByNameOrId(role);
    }

    @Override
    public List<Role> listRole(Role role, com.jk.ndt.etl.entity.Page basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return roleDao.listRole(role);
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
    public int deleteRole(BigDecimal role_id) {
        return roleDao.deleteRole(role_id);
    }

    @Override
    public BigDecimal getRoleSequence() {
        return roleDao.getRoleSequence();
    }

    @Override
    public int getAdminRolesCount(BigDecimal role_id) {
        return roleDao.getAdminRolesCount(role_id);
    }

}
