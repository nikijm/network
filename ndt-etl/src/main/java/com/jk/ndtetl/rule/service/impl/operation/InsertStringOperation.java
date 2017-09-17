package com.jk.ndtetl.rule.service.impl.operation;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.Checker;

/**
 * 在固定串后插入字符串
 * @author liuquanxin
 *
 */
public class InsertStringOperation implements StringOperation {

    @Override
    public String doClean(String value, CleanParam params) {
    	
        String firstParam = params.getParamOne();
        String paramTwo = params.getParamTwo();
        if(Checker.isNullOrEmpty(firstParam)||Checker.isNullOrEmpty(value)||Checker.isNullOrEmpty(paramTwo)){
            return value;
        }
        StringBuilder sb=new StringBuilder();
        if(value.contains(firstParam)){
        	String[] split = value.split(firstParam);
        	for (String string : split) {
				string=string+firstParam+paramTwo;
				sb.append(string);
			}
        	
        }
        return sb.toString();
    }

	@Override
	public String doCleans(String value, CleanParam[] params) {
		if(Checker.isNotNullOrEmpty(value) ||Checker.isNullOrEmpty(params) ){
			return value;
		}
		String reg = null;
		String insertValue = null;
		for (CleanParam cleanParam : params) {
			if(cleanParam.getParamKey().equals("designated")){
				reg = cleanParam.getParamValue();
				continue;
			}
			if(cleanParam.getParamKey().equals("designatedValue")){
				insertValue = cleanParam.getParamValue();
			}
		}
		if(Checker.isNullOrEmpty(reg) || Checker.isNullOrEmpty(insertValue)){
			return value;
		}
		int index = 0;
		byte[] valueBytes = value.getBytes();
		byte[] insertValueBytes = insertValue.getBytes();
		byte[] targetBytes = reg.getBytes();
		while(true){
			index = ScheduleUtil.indexOf(valueBytes,targetBytes, index);
			if(index == -1){
				break;
			}
			index += targetBytes.length;
			valueBytes = ScheduleUtil.setValue(index, valueBytes, insertValueBytes);
			index += insertValueBytes.length;
		}
		return new String(valueBytes);
	}
}
