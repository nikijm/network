package com.jk.ndtetl.rule;

/**
 * 
 * @ClassName: CleanParam
 * @Description: 
 * @author wangzhi
 * @date 2017年7月4日 下午3:50:27
 *
 */
public class CleanParam {
	
	private String paramKey = null;
	private String name =null;
	private String paramValue = null;
	private String type = null;
	
	//清洗的第一个参数
	private String paramOne;
	//清洗的第二个参数
	private String paramTwo;
	public String getParamOne() {
		return paramOne;
	}
	public void setParamOne(String paramOne) {
		this.paramOne = paramOne;
	}
	public String getParamTwo() {
		return paramTwo;
	}
	public void setParamTwo(String paramTwo) {
		this.paramTwo = paramTwo;
	}
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
