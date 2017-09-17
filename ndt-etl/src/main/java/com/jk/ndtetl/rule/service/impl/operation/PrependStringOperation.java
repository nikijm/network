package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.Checker;

/**
 * 
 * @ClassName: PrependStringOperation 
 * @Description: 在字符串开始位置插入字符串 
 * @author fangwei 
 * @date 2017年6月16日 下午2:55:11 
 *
 */
public class PrependStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params) {
       
        String firstParam = params.getParamOne();
        if(Checker.isNullOrEmpty(firstParam)||Checker.isNullOrEmpty(value)){
            return value;
        }
        // 默认第一个参数为插入值
//        String insertValue = firstParam.toString();
        StringBuffer sb = new StringBuffer(firstParam);
        sb.append(value);
        return sb.toString();
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
		if(Checker.isNullOrEmpty(params)||Checker.isNullOrEmpty(value)){
            return value;
        }
		String insertValue = null;
		for (CleanParam cleanParam : params) {
			insertValue = cleanParam.getParamValue();
		}
		return new String(ScheduleUtil.setValue(0, value.getBytes(), insertValue.getBytes()));
	}
}
