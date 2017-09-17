package com.jk.ndt.etl.entity.vo.application;

import com.alibaba.fastjson.annotation.JSONField;

public class ApplicationBean {
	@JSONField(name = "RequestType")
	private String requestType;

	@JSONField(name = "TransactionID")
	private String transactionID;

	@JSONField(name = "OrgCode")
	private String orgCode;

	@JSONField(name = "OrgName")
	private String orgName;

	@JSONField(name = "BPartner")
	private BPartnerBean bPartner;

	@JSONField(name = "Business")
	private BusinessBean business;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public BPartnerBean getbPartner() {
		return bPartner;
	}

	public void setbPartner(BPartnerBean bPartner) {
		this.bPartner = bPartner;
	}

	public BusinessBean getBusiness() {
		return business;
	}

	public void setBusiness(BusinessBean business) {
		this.business = business;
	}

}
