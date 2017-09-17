package com.jk.ndtetl.rule.dao;

import java.util.List;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.util.QueryParam;

/**
 * 
 * @ClassName: IRuleSuitDao
 * @Description: 
 * @author wangzhi
 * @date 2017��6��27�� ����4:53:18
 *
 */
public interface IRuleOpDao extends BaseDao<RuleOp>{

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月10日 下午5:39:25
	 * @param queryParam
	 * @return
	 * @return List<RuleOp>
	 */
	List<RuleOp> listRuleOpByOpType(QueryParam queryParam);

}
