package com.jk.ndt.etl.entity.vo.upload;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class UploadBean {
	@JSONField(name = "RequestType")
	private String requestType;

	@JSONField(name = "TransactionID")
	private String transactionID;

	@JSONField(name = "OrgCode")
	private String orgCode;

	@JSONField(name = "OrgName")
	private String orgName;

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

	public BusinessBean getBusiness() {
		return business;
	}

	public void setBusiness(BusinessBean business) {
		this.business = business;
	}

	public static void main(String[] args) {
		UploadBean uploadBean = new UploadBean();
		uploadBean.setRequestType("Upload");
		uploadBean.setTransactionID("消息交换唯一编号");
		
		BusinessBean businessBean=new BusinessBean();
		uploadBean.setBusiness(businessBean);
		businessBean.setAction("upload");
		AttributeBean attributeBean=new AttributeBean();
		businessBean.setAttribute(attributeBean);
		attributeBean.setMaxAmt(1000L);
		System.out.println(JSONObject.toJSONString(uploadBean));
	}
}
