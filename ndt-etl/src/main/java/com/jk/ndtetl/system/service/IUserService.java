package com.jk.ndtetl.system.service;


import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.User;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface IUserService {

    public User getByName(String username);

    public User getByNameTest(String username);

    public List<User> listUsers(User user, BasePage basePage);

    public User getUser(Integer user_id);

    public int saveUser(User user);

    public int updateUser(User user,User userOld);

    public int deleteUser(Integer user_id);

    public Integer getUserSequence();

    public User getUserByNameOrId(User user);

}
