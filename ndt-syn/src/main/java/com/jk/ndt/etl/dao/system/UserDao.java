package com.jk.ndt.etl.dao.system;

import com.jk.ndt.etl.entity.system.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface UserDao {

    public User getByName(String username);

    public List<User> listUsers(User user);

    public User getUser(BigDecimal user_id);

    public int saveUser(User user);

    public int updateUser(User user);

    public int deleteUser(BigDecimal user_id);

    public BigDecimal getUserSequence();

    public User getUserByNameOrId(User user);

}
