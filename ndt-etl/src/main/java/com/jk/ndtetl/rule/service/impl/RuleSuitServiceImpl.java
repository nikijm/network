package com.jk.ndtetl.rule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.rule.RuleSuit;
import com.jk.ndtetl.rule.dao.IRuleSuitDao;
import com.jk.ndtetl.rule.service.IRuleSuitService;

/**
 * 
 * 
 * @ClassName: RuleSuitService
 * @Description: 
 * @author wangzhi
 * @date 2017年7月3日 下午2:09:26
 *
 */
@Service("iRuleSuitService")
public class RuleSuitServiceImpl extends BaseServiceImpl<RuleSuit> implements IRuleSuitService{

	@Autowired
	IRuleSuitDao iRuleSuitDao;
	
	@Override
	protected BaseDao<RuleSuit> getDao() {
		return iRuleSuitDao;
	}
	
	

}
