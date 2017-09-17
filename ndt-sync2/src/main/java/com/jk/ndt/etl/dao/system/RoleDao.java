package com.jk.ndt.etl.dao.system;

import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.entity.system.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface RoleDao {

    public Role getRole(BigDecimal role_id);

    public Role getRoleByNameOrId(Role Role);

    public List<Role> listRole(Role role);

    public int saveRole(Role role);

    public int updateRole(Role role);

    public int deleteRole(BigDecimal role_id);

    public int getAdminRolesCount(BigDecimal role_id);

    public BigDecimal getRoleSequence();


}
