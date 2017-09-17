package com.jk.ndtetl.rule.service;

import com.jk.ndtetl.rule.CleanParam;

/**
 * 
 * @ClassName: IValidateRuleService
 * @Description: 数据验证接口
 * @author 王志
 * @date 2017年6月23日 下午2:48:05
 *
 */
public interface IValidateRuleService {
    /**
     * 
     * @Description: 验证电话号码
     * @author 王志
     * @date 2017年6月23日 下午2:48:28
     * @param field
     * @param params
     * @return
     */
    Boolean doValidate(String field,CleanParam regexExpression);
    
}
