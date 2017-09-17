package com.jk.ndtetl.system.dao;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.system.Role;
import org.omg.CORBA.OBJ_ADAPTER;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface RoleDao extends BaseDao<Role>{

    public Role getRole(Integer role_id);

    public Role getRoleByNameOrId(Role Role);

    public List<Role> listRole(Role role);

    public List<Map<String,Object>> listRoleByParam(Map<String,Object> param);

    public List<Role> listRoleByUserId(Integer userId);

    public int saveRole(Role role);

    public int updateRole(Role role);

    public int deleteRole(Integer role_id);

    public int getUserRolesCount(Integer role_id);

    public Integer getRoleSequence();


}
