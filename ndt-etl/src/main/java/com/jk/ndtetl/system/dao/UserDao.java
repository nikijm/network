package com.jk.ndtetl.system.dao;

import java.math.BigDecimal;
import java.util.List;

import com.jk.ndtetl.system.User;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface UserDao {

    public User getByName(String username);

    public User getByNameTest(String username);

    public List<User> listUsers(User user);

    public User getUser(Integer user_id);

    public int saveUser(User user);

    public int updateUser(User user);

    public int deleteUser(Integer user_id);

    public Integer getUserSequence();

    public User getUserByNameOrId(User user);

    public int delUserRoles(Integer etl_user_id);

    public int saveUserRoles(User user);

}
