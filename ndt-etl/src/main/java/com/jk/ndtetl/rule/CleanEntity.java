package com.jk.ndtetl.rule;

/**
 * 
 * @ClassName: CleanEntity
 * @Description:
 * @author wangzhi
 * @date 2017年7月4日 下午3:48:56
 *
 */
public class CleanEntity {
    //清洗规则的名字
	private String name;
    //清洗规则的参数封装
	private CleanParam cleanParams;
    //处理类的名字
	private String operationName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CleanParam getCleanParams() {
		return cleanParams;
	}

	public void setCleanParams(CleanParam cleanParams) {
		this.cleanParams = cleanParams;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
}
