package com.jk.ndtetl.rule.service.impl.operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.StringUtils;

/**
 * 
 * @ClassName: ReplaceStringOperation
 * @Description: 替换字符串操作，只替换第一次匹配的
 * @author fangwei
 * @date 2017年5月18日 上午9:20:37
 *
 */
public class ReplaceOnceStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params){
        
        // 默认第一个参数为查找参数值
        String regex = params.getParamOne();
        // 默认第二个参数作为替换参数值
        String replacement = params.getParamTwo();
        if(Checker.isNullOrEmpty(value)||Checker.isNullOrEmpty(regex)||Checker.isNullOrEmpty(replacement)){
        	return value;
        }
        Pattern p = Pattern.compile(StringUtils.escapeExprSpecialWord(regex));
        Matcher m = p.matcher(value);
        String newString = m.replaceFirst(replacement);
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
		Pattern p = Pattern.compile(StringUtils.escapeExprSpecialWord(reg));
        Matcher m = p.matcher(value);
        String newString = m.replaceFirst(replaceValue);
        return newString;
	}

}
