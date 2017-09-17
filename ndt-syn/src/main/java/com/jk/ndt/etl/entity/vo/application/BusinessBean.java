package com.jk.ndt.etl.entity.vo.application;

import com.alibaba.fastjson.annotation.JSONField;

public class BusinessBean {
	@JSONField(name = "BusinessCode")
	private String businessCode;

	@JSONField(name = "BusinessName")
	private String businessName;

	@JSONField(name = "Attribute")
	private AttributeBean attribute;

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public AttributeBean getAttribute() {
		return attribute;
	}

	public void setAttribute(AttributeBean attribute) {
		this.attribute = attribute;
	}

}
