package com.jk.ndt.etl.dao.system;

import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.Role;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface AdminDao {

    public Admin getByName(String username);

    public Admin getAdmin(BigDecimal admin_id);

    public int updateAdmin(Admin admin);

    public int saveAdmin(Admin admin);

    public int deleteAdmin(BigDecimal admin_id);

    public int deleteAdminRoles(BigDecimal admin_id);

    public List<Admin> listAdmin(Admin admin);

    public List<Resource> listResource();

    public List<Role> listRole(BigDecimal admin_id);

    public BigDecimal getAdminSequence();

    public Admin getAdminByNameOrId(Admin admin);

    public int saveAdminRoles(Admin admin);

}
