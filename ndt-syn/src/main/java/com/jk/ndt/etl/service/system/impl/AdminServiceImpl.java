package com.jk.ndt.etl.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.dao.system.AdminDao;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.service.system.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin getByName(String name) {
        return adminDao.getByName(name);
    }

    @Override
    public Admin getAdmin(BigDecimal id) {
        return adminDao.getAdmin(id);
    }

    /**
     * 1.保存所选角色
     * 2.保存admin
     * @param admin
     * @return
     */
    @Transactional
    @Override
    public int updateAdmin(Admin admin) {
        if (null != admin.getRoleIds() && admin.getRoleIds().length > 0) {
            adminDao.deleteAdminRoles(admin.getId());
            adminDao.saveAdminRoles(admin);
        }else {
            adminDao.deleteAdminRoles(admin.getId());
        }
        return adminDao.updateAdmin(admin);
    }

    /**
     * 1.保存所选角色
     * 2.保存admin
     * @param admin
     * @return
     */
    @Transactional
    @Override
    public int saveAdmin(Admin admin) {
        if (null != admin.getRoleIds() && admin.getRoleIds().length > 0) {
            adminDao.saveAdminRoles(admin);
        }
        return adminDao.saveAdmin(admin);
    }

    /**
     * 1.删除admin相关角色中间表
     * 2.删除admin
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int deleteAdmin(BigDecimal id) {
        adminDao.deleteAdminRoles(id);
        return adminDao.deleteAdmin(id);
    }

    @Override
    public int deleteAdminRoles(BigDecimal id) {
        return adminDao.deleteAdmin(id);
    }

    @Override
    public List<Admin> listAdmin(Admin admin, com.jk.ndt.etl.entity.Page basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return adminDao.listAdmin(admin);
    }

    /**
     * 资源列表
     * @return
     */
    @Override
    public List<Resource> listResource() {
        return adminDao.listResource();
    }

    /**
     * 角色列表
     * @param id
     * @return
     */
    @Override
    public List<Role> listRole(BigDecimal id) {
        return adminDao.listRole(id);
    }

    /**
     * 判断用户名是否重复
     * @param admin
     * @return
     */
    @Override
    public Admin getAdminByNameOrId(Admin admin) {
        return adminDao.getAdminByNameOrId(admin);
    }

    /**
     * 获取admin序列
     * @return
     */
    @Override
    public BigDecimal getAdminSequence() {
        return adminDao.getAdminSequence();
    }

}
