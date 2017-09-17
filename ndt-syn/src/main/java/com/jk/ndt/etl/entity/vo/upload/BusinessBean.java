package com.jk.ndt.etl.entity.vo.upload;

import com.alibaba.fastjson.annotation.JSONField;

public class BusinessBean {
	@JSONField(name = "BusinessCode")
	private String businessCode;

	@JSONField(name = "BusinessName")
	private String businessName;

	@JSONField(name = "Action")
	private String action;

	@JSONField(name = "IconUrl")
	private String iconUrl;

	@JSONField(name = "BusinessUrl")
	private String businessUrl;

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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getBusinessUrl() {
		return businessUrl;
	}

	public void setBusinessUrl(String businessUrl) {
		this.businessUrl = businessUrl;
	}

	public AttributeBean getAttribute() {
		return attribute;
	}

	public void setAttribute(AttributeBean attribute) {
		this.attribute = attribute;
	}
}
