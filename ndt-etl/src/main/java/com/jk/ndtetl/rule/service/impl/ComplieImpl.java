package com.jk.ndtetl.rule.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.service.IVerifyRuleService;

public class ComplieImpl implements IVerifyRuleService {

	@Override
	public boolean verify(String value,CleanParam[] param) {
		if(value == null || param == null){
			return false;
		}
		for (CleanParam cleanParam : param) {
			String reg = cleanParam.getParamValue();
			Matcher m = Pattern.compile(reg).matcher(value);
			if(!m.matches()){
				return false;
			}
		}
		return true;
	}
}
