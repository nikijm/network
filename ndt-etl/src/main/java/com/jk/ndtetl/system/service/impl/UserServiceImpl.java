package com.jk.ndtetl.system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.jk.ndtetl.system.dao.RoleDao;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.StringUtils;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.dao.UserDao;
import com.jk.ndtetl.system.service.IUserService;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;


    @Override
    public User getByName(String user_name) {
        return userDao.getByName(user_name);
    }

    @Override
    public User getByNameTest(String username) {
        return userDao.getByNameTest(username);
    }

    @Override
    public List<User> listUsers(User user, com.jk.ndtetl.controller.BasePage basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        List<User> userList=userDao.listUsers(user);
        if (Checker.isNotNullOrEmpty(userList)) {
            for (User queryUser:userList) {
                queryUser.setRoles(roleDao.listRoleByUserId(queryUser.getId()));
            }
        }
        return userList;
    }

    @Override
    public User getUser(Integer user_id) {
        return userDao.getUser(user_id);
    }

    @Override
    public int saveUser(User user) {
        if (null != user.getRoleIds() && user.getRoleIds().length > 0) {
            userDao.saveUserRoles(user);
        }
        return userDao.saveUser(user);
    }

    @Override
    public int updateUser(User user,User userOld) {
        if (!LoginSessionUtil.isOuterUser(userOld)) {
            if (null != user.getRoleIds() && user.getRoleIds().length > 0) {
                userDao.delUserRoles(user.getId());
                userDao.saveUserRoles(user);
            }else {
                userDao.delUserRoles(user.getId());
            }
        }
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(Integer user_id) {
        return userDao.deleteUser(user_id);
    }

    @Override
    public Integer getUserSequence() {
        return userDao.getUserSequence();
    }

    @Override
    public User getUserByNameOrId(User user) {
        return userDao.getUserByNameOrId(user);
    }


}
