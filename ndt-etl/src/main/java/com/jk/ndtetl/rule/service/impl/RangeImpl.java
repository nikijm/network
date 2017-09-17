package com.jk.ndtetl.rule.service.impl;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.service.IVerifyRuleService;
public class RangeImpl implements IVerifyRuleService{

	@Override
	public boolean verify(String value,CleanParam[] param) {
		if(value == null || param == null){
			return false;
		}
		int start = 0;
		int end = 0;
		for (CleanParam cleanParam : param) {
			if(cleanParam.getParamKey().equals("start")){
				start = Integer.parseInt(cleanParam.getParamValue());
			}
			if(cleanParam.getParamKey().equals("end")){
				end = Integer.parseInt(cleanParam.getParamValue());
			}
		}
		if(start < Integer.parseInt(value) && Integer.parseInt(value)<end){
			return true;
		}
		return false;
	}
}
