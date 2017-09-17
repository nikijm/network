package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.StringUtils;

/**
 * 
 * @ClassName: ReplaceStringOperation
 * @Description: 替换字符串操作
 * @author fangwei
 * @date 2017年5月18日 上午9:20:37
 *
 */
public class ReplaceStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params) {
        // 默认第一个参数为查找参数值
        String paramOne = params.getParamOne();
        String paramTwo = params.getParamTwo();
        if(Checker.isNullOrEmpty(value)||Checker.isNullOrEmpty(paramOne)||Checker.isNullOrEmpty(paramTwo)){
        	return value;
        }
        String newString = value.replaceAll(StringUtils.escapeExprSpecialWord(paramOne), paramTwo);
        return newString;
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
		if(Checker.isNullOrEmpty(value)||Checker.isNullOrEmpty(params)){
        	return value;
        }
		String reg = null;
		String replaceValue = null;
		for (CleanParam cleanParam : params) {
			if(cleanParam.getParamKey().equals("designated")){
				reg = cleanParam.getParamValue();
				continue;
			}
			if(cleanParam.getParamKey().equals("replaceValue")){
				replaceValue = cleanParam.getParamValue();
			}
		}
		String newString = value.replaceAll(StringUtils.escapeExprSpecialWord(reg), replaceValue);
        return newString;
	}

  

}
