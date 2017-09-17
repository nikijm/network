package com.jk.ndtetl.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.Role;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface IRoleService extends IBaseService<Role>{

    public Role getRole(Integer role_id);

    public Role getRoleByNameOrId(Role role);

    public List<Role> listRole(Role role, BasePage basePage);

    public List<Map<String,Object>> listRoleByParam(Map<String,Object> param);

    public List<Role> listRoleAll();

    public int saveRole(Role role);

    public int updateRole(Role role);

    public int deleteRole(Integer role_id);

    public Integer getRoleSequence();

    public int getUserRolesCount(Integer role_id);

}
