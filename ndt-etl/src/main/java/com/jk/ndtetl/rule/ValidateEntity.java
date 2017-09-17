package com.jk.ndtetl.rule;

/**
 * 
 * @ClassName: ValidateEntity
 * @Description: 验证的实体
 * @author wangzhi
 * @date 2017年7月4日 上午10:08:13
 *
 */
public class ValidateEntity {

	//验证的名称
	private String name;
	//验证所需的参数
	private CleanParam cleanParam;
	//操作的名字
	private String operationName;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CleanParam getCleanParam() {
		return cleanParam;
	}

	public void setCleanParam(CleanParam cleanParam) {
		this.cleanParam = cleanParam;
	}
	
	
}
