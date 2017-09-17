package com.jk.ndtetl.rule.service;

import java.util.List;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.rule.RuleOp;

/**
 * 
 * 
 * @ClassName: IRuleOpService
 * @Description: 
 * @author wangzhi
 * @date 2017年7月3日 下午2:11:37
 *
 */
public interface IRuleOpService extends IBaseService<RuleOp>{
	
	public List<RuleOp> listRuleOpByOpType(String opType);

}
