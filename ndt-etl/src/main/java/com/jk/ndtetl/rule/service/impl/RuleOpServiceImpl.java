package com.jk.ndtetl.rule.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.rule.dao.IRuleOpDao;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.util.QueryParam;

/**
 * 
 * 
 * @ClassName: RuleOpService
 * @Description: 
 * @author wangzhi
 * @date 2017年7月3日 下午2:05:39
 *
 */
@Service("iRuleOpService")
public class RuleOpServiceImpl extends BaseServiceImpl<RuleOp> implements IRuleOpService{
    @Autowired
	IRuleOpDao iRuleOpDao;
	
	@Override
	protected BaseDao<RuleOp> getDao() {
		return iRuleOpDao;
	}

	@Override
	public List<RuleOp> listRuleOpByOpType(String opType) {
		QueryParam queryParam=new QueryParam();
		if(opType!=null){
			Map<String, Object> param = queryParam.getParam();
			param.put("opType", opType);
		}
		List<RuleOp> list=iRuleOpDao.listRuleOpByOpType(queryParam);
		return list;
	}

}
