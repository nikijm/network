package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.util.Checker;

/**
 * 
 * @ClassName: PrependStringOperation 
 * @Description: 字符串为空时插入 
 * @author fangwei 
 * @date 2017年6月16日 下午2:55:11 
 *
 */
public class EmptyStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params) {
        
        String firstParam = params.getParamOne();
        if(Checker.isNullOrEmpty(firstParam) || Checker.isNotNullOrEmpty(value)){
            return null;
        }
        // 默认第一个参数为插入值
        String insertValue = firstParam.trim();
        return insertValue;
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
		if(params == null){
			return value;
		}
		for (CleanParam cleanParam : params) {
			value = cleanParam.getParamValue();
		}
		return value;
	}
}
