package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.util.Checker;

/**
 * 
 * @ClassName: PrependStringOperation 
 * @Description: 在字符串结束位置插入字符串 
 * @author fangwei 
 * @date 2017年6月16日 下午2:55:11 
 *
 */
public class AppendStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params)  {
        String firstParam = params.getParamOne();
        if(Checker.isNullOrEmpty(firstParam)){
            return value;
        }
        // 默认第一个参数为插入值
        String insertValue = firstParam.toString();
        StringBuffer sb = new StringBuffer(value);
        sb.append(insertValue);
        return sb.toString();
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
		if(value == null || params == null){
			return value;
		}
		StringBuilder sb = new StringBuilder(value);
		for (CleanParam cleanParam : params) {
			sb.append(cleanParam.getParamValue());
		}
		return sb.toString();
	}
    
   
}
