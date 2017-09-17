package com.jk.ndtetl.rule;

import com.alibaba.fastjson.JSON;
import com.jk.ndtetl.rule.service.IValidateRuleService;

/**
 * 
 * @ClassName: ValidateUtil
 * @Description: 
 * @author wangzhi
 * @date 2017年7月4日 上午10:18:18
 *
 */
public class RuleUtil {
	
	/**
	 * 
	 * @Description: 解析json到实体
	 * @author wangzhi
	 * @date  2017年7月4日 上午10:23:19
	 * @param params
	 * @return
	 * @return ValidateEntity
	 */
	public static ValidateEntity jsonParseToValidateEntity(String params){
		ValidateEntity parseObject = JSON.parseObject(params, ValidateEntity.class);
		return parseObject;
	}
	/**
	 * 
	 * @Description: 解析清洗的参数到清洗实体
	 * @author wangzhi
	 * @date  2017年7月15日 下午4:24:46
	 * @param params
	 * @return
	 * @return CleanEntity
	 */
	public static CleanEntity jsonParseToCleanEntity(String params){
		CleanEntity parseObject = JSON.parseObject(params, CleanEntity.class);
		return parseObject;
	}
	/**
	 * 
	 * @Description: 获得验证器
	 * @author wangzhi
	 * @date  2017年7月4日 上午10:34:04
	 * @param clz
	 * @return
	 * @throws Exception
	 * @return IValidateRuleService
	 */
	public static IValidateRuleService getValidator(String clz) {
		Class<?> forName=null;
		try {
			forName = Class.forName(clz);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IValidateRuleService newInstance=null;
		try {
			newInstance = (IValidateRuleService) forName.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newInstance;
	}
	/**
	 * 
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月4日 下午3:56:12
	 * @param clz
	 * @return
	 * @throws Exception
	 * @return ICleanRuleService
	 */
	public static StringOperation getCleaner(String clz) throws Exception{
		Class<?> forName = Class.forName(clz);
		StringOperation newInstance = (StringOperation) forName.newInstance();
		return newInstance;
	}
	
	public static String getvalidatorName(String clz){
		Class<?> forName=null;
		try {
			forName = Class.forName(clz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return forName.getName();
	}
	public static String getCleanerName(String clz){
		Class<?> forName=null;
		try {
			forName = Class.forName(clz);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forName.getName();
	}

}
