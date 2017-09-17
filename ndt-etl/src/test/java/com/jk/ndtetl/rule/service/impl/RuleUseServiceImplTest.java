package com.jk.ndtetl.rule.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.dao.UserDao;

public class RuleUseServiceImplTest extends SpringTest {

    @Autowired
    private IRuleUseService ruleUseService;

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetRulesByFile() {
        try {
            ruleUseService.getRulesByFile(106, "clean");
        }
        catch (CustomException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testList(){
    	List<RuleUse> list = ruleUseService.listRuleUseByTables(106, 107);
    	for (RuleUse ruleUse : list) {
			System.out.println(ruleUse.getColumnName()+"-->"+ruleUse.getEtlCoulumnTargetName());
		}
    }

    @Test
    public void testUserList() {
        List<User> userList=userDao.listUsers(new User());
        System.out.println(userList.size());
    }

}
