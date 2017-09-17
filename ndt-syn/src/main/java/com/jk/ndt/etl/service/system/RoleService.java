package com.jk.ndt.etl.service.system;

import com.jk.ndt.etl.entity.system.Role;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface RoleService {

    public Role getRole(BigDecimal role_id);

    public Role getRoleByNameOrId(Role role);

    public List<Role> listRole(Role role,  com.jk.ndt.etl.entity.Page basePage);

    public int saveRole(Role role);

    public int updateRole(Role role);

    public int deleteRole(BigDecimal role_id);

    public BigDecimal getRoleSequence();

    public int getAdminRolesCount(BigDecimal role_id);

}
