package com.jk.ndt.etl.entity.vo.businessoperate;

import com.alibaba.fastjson.annotation.JSONField;

public class BusinessOperateBean {
	@JSONField(name = "RequestType")
	private String requestType;

	@JSONField(name = "TransactionID")
	private String transactionID;

	@JSONField(name = "OrgCode")
	private String orgCode;

	@JSONField(name = "OrgName")
	private String orgName;

	@JSONField(name = "BusinessCode")
	private String businessCode;

	@JSONField(name = "BusinessName")
	private String businessName;

	@JSONField(name = "DocumentNo")
	private String documentNo;

	@JSONField(name = "Workflow")
	private WorkflowBean workflow;

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

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public WorkflowBean getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowBean workflow) {
		this.workflow = workflow;
	}

}
