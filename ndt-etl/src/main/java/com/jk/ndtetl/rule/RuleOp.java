package com.jk.ndtetl.rule;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.jk.ndtetl.BaseEntity;

public class RuleOp extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "规则名不能为空")
	private String name;

	@NotBlank(message = "class name 不能为空")
	private String clz;

	// {'a':'va','b','v'}
	@NotBlank(message = "参数定义不能为空")
	private String params;
    //规则处理器的封装实体类
	private CleanParam cleanParam;
	private CleanParam[] cleanParams = null;
	
	private IClean iclean = null;
	
	@NotNull(message = "规则类型不能为空")
	private String etlop;//规则处理器的类型

	// 模版参数key
	private String paramKey;
	//模版参数值
	private String paramValue;

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public CleanParam getCleanParam() {
		return cleanParam;
	}
	public void setCleanParam(CleanParam cleanParam) {
		this.cleanParam = cleanParam;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClz() {
		return clz;
	}
	public void setClz(String clz) {
		this.clz = clz;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getEtlop() {
		return etlop;
	}
	public void setEtlop(String etlop) {
		this.etlop = etlop;
	}

	public IClean getIclean() {
		return iclean;
	}

	public void setIclean(IClean iclean) {
		this.iclean = iclean;
	}

	public CleanParam[] getCleanParams() {
		return cleanParams;
	}

	public void setCleanParams(CleanParam[] cleanParams) {
		this.cleanParams = cleanParams;
	}
}
