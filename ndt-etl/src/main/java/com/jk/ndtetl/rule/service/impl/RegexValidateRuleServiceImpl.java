package com.jk.ndtetl.rule.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.service.IValidateRuleService;

/**
 * 
 * @ClassName: ValidateRuleServiceImpl
 * @Description: 
 * @author wangzhi
 * @date 2017年7月4日 上午9:23:37
 *
 */
@Service("iValidateRuleService")
public class RegexValidateRuleServiceImpl implements IValidateRuleService{

	@Override
	public Boolean doValidate(String field, CleanParam regexExpression) {
		 boolean flag=false;
		 if(regexExpression.getParamOne()==null){
			 return true;
		 }
		 Pattern pattern = Pattern.compile(regexExpression.getParamOne());
         Matcher matcher = pattern.matcher(field);
         if (matcher.matches()) {
             flag = true;
         }
		return flag;
	}
}
