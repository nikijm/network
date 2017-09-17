package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.util.Checker;

/**
 * 
 * @ClassName: PrependStringOperation
 * @Description: 字符串全部转换成大写
 * @author fangwei
 * @date 2017年6月16日 下午2:55:11
 *
 */
public class UpperStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params) {
       
        if (Checker.isNullOrEmpty(value)) {
            return value;
        }
        return value.toUpperCase();
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
        if (Checker.isNullOrEmpty(value)) {
            return value;
        }
		return value.toUpperCase();
	}

}
