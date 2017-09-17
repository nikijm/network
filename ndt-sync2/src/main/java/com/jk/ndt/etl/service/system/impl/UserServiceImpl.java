package com.jk.ndt.etl.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.dao.system.UserDao;
import com.jk.ndt.etl.entity.system.User;
import com.jk.ndt.etl.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;


    @Override
    public User getByName(String user_name) {
        return userDao.getByName(user_name);
    }

    @Override
    public List<User> listUsers(User user, com.jk.ndt.etl.entity.Page basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return userDao.listUsers(user);
    }

    @Override
    public User getUser(BigDecimal user_id) {
        return userDao.getUser(user_id);
    }

    @Override
    public int saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(BigDecimal user_id) {
        return userDao.deleteUser(user_id);
    }

    @Override
    public BigDecimal getUserSequence() {
        return userDao.getUserSequence();
    }

    @Override
    public User getUserByNameOrId(User user) {
        return userDao.getUserByNameOrId(user);
    }


}
